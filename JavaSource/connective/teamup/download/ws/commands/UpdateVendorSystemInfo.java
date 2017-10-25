/*
 * Created on Jan 23, 2006
 */
package connective.teamup.download.ws.commands;

import connective.teamup.download.CarrierInfo;
import connective.teamup.download.CustomTextFactory;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.AmsInfo;
import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.services.EmailService;
import connective.teamup.download.ws.objects.AgentConfiguration;
import connective.teamup.ws.ICommand;
import connective.teamup.ws.SecurityStatus;

/**
 * @author haneym
 */
public class UpdateVendorSystemInfo implements ICommand 
{
	/* (non-Javadoc)
	 * @see connective.teamup.ws.ICommand#perform(java.lang.Object)
	 */
	public Object perform(Object inputData, SecurityStatus secInfo) throws Exception 
	{
		AgentConfiguration config = (AgentConfiguration) inputData;
				
		DatabaseOperation op = null;
		try
		{
			op = DatabaseFactory.getInstance().startOperation();
			
			AgentInfo agent = op.getAgentInfo(secInfo.getAgentId());
			if (agent == null) {
				throw new Exception("Invalid agent ID.");
			}
			else if (!agent.isActive()) {
				throw new Exception("This agent ID is not currently active.  Please contact your carrier representative to continue registration for TEAM-UP Download.");
			}
			else
			{
				// Load original settings for later comparison
				String oldVendor = "";
				if (agent.getAms() != null)
				{
					oldVendor = agent.getAms().getId();
					if (oldVendor == null)
						oldVendor = "";
				}
				String oldVersion = agent.getAmsVer();
				if (oldVersion == null)
					oldVersion = "";
				
				// Save agency vendor system settings
				agent.setAmsVer(config.getAmsVersion());
				agent.setDefaultFilename(config.getDefaultFilename());
				agent.setRemoteDir(config.getRemoteDirectory());
				
				AmsInfo ams = null;
				if (config.getAmsId() != null && !config.getAmsId().equals(""))
					ams = op.getAmsInfo(config.getAmsId());
				agent.setAms(ams);
				agent.save();
				
				// Check to see if agency vendor system settings have been changed
				String notifyFlag = op.getPropertyValue(DatabaseFactory.PROP_NOTIFY_ON_AGENCY_VENDOR_CHANGE);
				if (notifyFlag != null && notifyFlag.trim().equals("Y"));
				{
					if ((config.getAmsId() != null && !config.getAmsId().equals(oldVendor)) ||
						(config.getAmsVersion() != null && !config.getAmsVersion().equals(oldVersion)))
					{
						// Send an email to the carrier notifying that the agent has
						// completed the registration process
						CustomTextFactory factory = new CustomTextFactory(
							CustomTextFactory.TEXT_AGENT_VENDOR_CHANGE, 
							CustomTextFactory.TYPE_EMAIL,
							null, agent, op);
						String message = factory.getText(op);
						String subject = factory.getEmailSubject(op);
						String htmlMsg = null;
						if (CarrierInfo.getInstance().isEmailAsHtml())
							htmlMsg = factory.getHtml(op);
						
						String to = op.getPropertyValue(DatabaseFactory.PROP_EMAIL_REPORTS);
						if (to == null || to.equals(""))
							to = op.getPropertyValue(DatabaseFactory.PROP_EMAIL_CONTACT);
						EmailService.getInstance().sendEMail(to, subject, message, htmlMsg);
					}
				}
			}
		}
		finally
		{
			if (op != null)
				op.close();
		}
		
		return null;
	}
}
