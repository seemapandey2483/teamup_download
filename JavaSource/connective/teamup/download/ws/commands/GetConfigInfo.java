/*
 * Created on Aug 30, 2006
 */
package connective.teamup.download.ws.commands;

import java.util.ArrayList;
import java.util.Vector;

import connective.teamup.download.CarrierInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.ParticipantInfo;
import connective.teamup.download.ws.objects.GetAgentConfigurationInput;
import connective.teamup.download.ws.objects.GetConfigInfoOutput;
import connective.teamup.download.ws.objects.OptionBean;
import connective.teamup.ws.ICommand;
import connective.teamup.ws.SecurityStatus;

/**
 * Web service command to retrieve the current agent/company configuration info by the agent
 * workstation (java client), for display purposes only.
 * 
 * @author mccrearyk
 */
public class GetConfigInfo implements ICommand
{

	/* (non-Javadoc)
	 * @see connective.teamup.ws.ICommand#perform(java.lang.Object)
	 */
	public Object perform(Object inputData, SecurityStatus secInfo) throws Exception
	{
		GetAgentConfigurationInput input = (GetAgentConfigurationInput) inputData;
		GetConfigInfoOutput config = null;
		DatabaseOperation op = null;
		
		try
		{
			op = DatabaseFactory.getInstance().startOperation();
			AgentInfo agent = op.getAgentInfo(secInfo.getAgentId());
			if (agent == null)
			{
				throw new Exception("Invalid agent ID.");
			}
			else if (!agent.isActive())
			{
				throw new Exception("This agent ID is not currently active.  Please contact your carrier representative to continue registration for TEAM-UP Download.");
			}
			else
			{
				// build the list of company info to be displayed
				ArrayList cList = new ArrayList();
				cList.add(new OptionBean("Company Name", CarrierInfo.getInstance().getName()));
				cList.add(new OptionBean("Company ID", CarrierInfo.getInstance().getCarrierId()));
				cList.add(new OptionBean("Contact Email", CarrierInfo.getInstance().getContactEmail()));
				cList.add(new OptionBean("Application Version", op.getPropertyValue(DatabaseFactory.PROP_VERSION)));
				OptionBean[] cInfo = new OptionBean[cList.size()];
				cList.toArray(cInfo);
				
				// build the list of agency info to be displayed
				ArrayList aList = new ArrayList();
				aList.add(new OptionBean("Agency Name", agent.getName()));
				aList.add(new OptionBean("Agency ID", agent.getAgentId()));
				
				agent.loadParticipantsFromDb();
				Vector partList = agent.getParticipants();
				if (partList != null && partList.size() > 0)
				{
					String participants = "";
					for (int i=0; i < partList.size(); i++)
					{
						if (i > 0)
							participants += ", ";
						ParticipantInfo part = (ParticipantInfo) partList.elementAt(i);
						participants += part.getParticipantCode();
					}
					aList.add(new OptionBean("Participant Codes", participants));
				}
				
//				String flag = op.getPropertyValue(DatabaseFactory.PROP_AGENT_INFO_CHANGE);
//				if (flag != null && flag.equals("N"))
//					aList.add(new OptionBean("Contact Info", "(contact help desk to update)"));
				
				aList.add(new OptionBean("Contact Name", agent.getContactName()));
				aList.add(new OptionBean("Contact Email", agent.getContactEmail()));
				aList.add(new OptionBean("Contact Phone", getContactPhone(agent.getContactPhone())));
				aList.add(new OptionBean("Agency Location", agent.getLocationState()));
				
				if (agent.getAms() == null)
					aList.add(new OptionBean("Agency Management System", "(not yet configured)"));
				else
				{
					aList.add(new OptionBean("Agency Management System", "NULL"));
					aList.add(new OptionBean("Vendor System", agent.getAms().getDisplayName()));
					aList.add(new OptionBean("Software Version", agent.getAmsVer()));
					aList.add(new OptionBean("AL3 Download to Directory", agent.getRemoteDir()));
					if(agent.getAms()!= null && agent.getAms().isClaimSupported())
						aList.add(new OptionBean("Claims Download to Directory", agent.getRemoteClaimDir()));
					if(agent.getAms()!= null && agent.getAms().isPolicyXMLSupported())
						aList.add(new OptionBean("Policy XML Download to Directory", agent.getRemoteClaimDir()));
				}
				
				aList.add(new OptionBean("Agency Download Status", "NULL"));
				aList.add(new OptionBean("Last Download Date", agent.getLastDownloadDateStrLong()));
				aList.add(new OptionBean("Registration Status", agent.getStatusDisplay()));
				if (agent.isTestAgent())
					aList.add(new OptionBean("Agency Type", "Test agent"));
				OptionBean[] aInfo = new OptionBean[aList.size()];
				aList.toArray(aInfo);
				
				// save config info to the output object
				config = new GetConfigInfoOutput();
				config.setAgentInfo(aInfo);
				config.setCompanyInfo(cInfo);
			}
		}
		finally
		{
			if (op != null)
				op.close();
		}
		
		return config;
	}

	/**
	 * Returns the agency contact's phone number (formatted).
	 * @return String
	 */
	public String getContactPhone(String contactPhone)
	{
		if (contactPhone == null || contactPhone.trim().length() < 10)
			return "";
		
		String phone = "(" + contactPhone.substring(0, 3) + ") " +
						contactPhone.substring(3, 6) + "-";
		if (contactPhone.length() == 10)
			phone += contactPhone.substring(6);
		else
			phone += contactPhone.substring(6, 10) + " x." + contactPhone.substring(10);
		
		return phone;
	}

}
