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
import connective.teamup.download.ws.objects.UpdateContactInfoOutput;
import connective.teamup.ws.ICommand;
import connective.teamup.ws.SecurityStatus;

/**
 * @author haneym
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class UpdateContactInfo implements ICommand 
{
	/* (non-Javadoc)
	 * @see connective.teamup.ws.ICommand#perform(java.lang.Object)
	 */
	public Object perform(Object inputData, SecurityStatus secInfo) throws Exception 
	{
		boolean successful = false;
		AgentConfiguration config = (AgentConfiguration) inputData;
				
		DatabaseOperation op = null;
		try
		{
			op = DatabaseFactory.getInstance().startOperation();
			
			// See if agent is allowed to change contact info
			String flag = op.getPropertyValue(DatabaseFactory.PROP_AGENT_INFO_CHANGE);
			boolean changeAllowed = (flag == null || flag.equals("Y"));
			if (changeAllowed)
			{
				AgentInfo agent = op.getAgentInfo(secInfo.getAgentId());
				if (agent != null && agent.isActive())
				{
					agent.setName(config.getName());
					agent.setLocationState(config.getLocationState());
					agent.setContactName(config.getContactName());
					agent.setContactEmail(config.getContactEmail());
					agent.setContactPhone(config.getContactPhone());
					agent.save();
					
					successful = true;
				}
			}
		}
		finally
		{
			if (op != null)
				op.close();
		}
		
		UpdateContactInfoOutput output = new UpdateContactInfoOutput();
		output.setSuccessful(successful);
		return output;
	}
}
