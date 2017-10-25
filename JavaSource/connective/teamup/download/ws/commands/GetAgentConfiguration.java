/*
 * Created on Jan 23, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package connective.teamup.download.ws.commands;

import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.ws.objects.AgentConfiguration;
import connective.teamup.download.ws.objects.GetAgentConfigurationInput;
import connective.teamup.ws.ICommand;
import connective.teamup.ws.SecurityStatus;

/**
 * @author haneym
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class GetAgentConfiguration implements ICommand 
{
	/* (non-Javadoc)
	 * @see connective.teamup.ws.ICommand#perform(java.lang.Object)
	 */
	public Object perform(Object inputData, SecurityStatus secInfo) throws Exception 
	{
		GetAgentConfigurationInput input = (GetAgentConfigurationInput) inputData;
		AgentConfiguration config = null;
		DatabaseOperation op = null;
		try
		{
			op = DatabaseFactory.getInstance().startOperation();
			AgentInfo agent = op.getAgentInfo(secInfo.getAgentId());
			if (agent == null)
			{
				config = new AgentConfiguration();
				config.setSvcMessage("Invalid agent ID.");
			}
			else
			{
				config = new AgentConfiguration();
				if (!agent.isActive())
				{
					config.setAgentId(agent.getAgentId());
					config.setDisabled(true);
					config.setSvcMessage("This agent ID is not currently active.  Please contact your carrier representative to continue registration for TEAM-UP Download.");
				}
				else
				{
					config.setAgentId(agent.getAgentId());
					if (agent.getAms() != null)
						config.setAmsId(agent.getAms().getId());
					config.setAmsVersion(agent.getAmsVer());
					config.setContactEmail(agent.getContactEmail());
					config.setContactName(agent.getContactName());
					config.setContactPhone(agent.getContactPhone());
					config.setDefaultFilename(agent.getDefaultFilename());
					config.setDisabled(!agent.isActive());
					config.setLastDownloadDate(agent.getLastDownloadDate());
					config.setLastLoginDate(agent.getLastLoginDate());
					config.setLive(agent.isLive());
					config.setLocationState(agent.getLocationState());
					config.setName(agent.getName());
					config.setRemoteDirectory(agent.getRemoteDir());
					config.setScheduled(!agent.isInteractive());
					config.setTestAgent(agent.isTestAgent());
					
					// See if agent is allowed to change contact info
					String flag = op.getPropertyValue(DatabaseFactory.PROP_AGENT_INFO_CHANGE);
					config.setContactChangeAllowed(flag == null || flag.equals("Y"));
				}
			}
		}
		finally
		{
			if (op != null)
				op.close();
		}
		
		return config;
	}
}
