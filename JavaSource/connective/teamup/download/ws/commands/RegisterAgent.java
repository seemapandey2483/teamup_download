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
public class RegisterAgent implements ICommand 
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
				boolean previouslyLive = agent.isLive();
				boolean previouslyRegistered = (previouslyLive || agent.isRegistered());
				boolean previouslyRegWithClient = agent.isClientAppRegistered();
				String oldFilename = agent.getDefaultFilename();
				if (oldFilename == null)
					oldFilename = "";
				String oldVendor = "";
				if (agent.getAms() != null)
				{
					oldVendor = agent.getAms().getId();
					if (oldVendor == null)
						oldVendor = "";
				}
				
				// See if agent is allowed to change contact info
				String flag = op.getPropertyValue(DatabaseFactory.PROP_AGENT_INFO_CHANGE);
				boolean changeContact = (flag == null || flag.equals("Y"));
				
				agent.setAmsVer(maxLength(config.getAmsVersion(), 10));
				if (changeContact || agent.getContactName() == null || agent.getContactName().equals(""))
					agent.setContactName(maxLength(config.getContactName(), 30));
				if (changeContact || agent.getContactEmail() == null || agent.getContactEmail().equals(""))
					agent.setContactEmail(maxLength(config.getContactEmail(), 50));
				if (changeContact || agent.getContactPhone() == null || agent.getContactPhone().equals(""))
					agent.setContactPhone(maxLength(config.getContactPhone(), 20));
				agent.setDefaultFilename(maxLength(config.getDefaultFilename(), 50));
				agent.setInteractive(!config.isScheduled());
				if (changeContact || agent.getLocationState() == null || agent.getLocationState().equals(""))
					agent.setLocationState(config.getLocationState());
				if (changeContact || agent.getName() == null || agent.getName().equals(""))
					agent.setName(maxLength(config.getName(), 50));
				agent.setRemoteDir(maxLength(config.getRemoteDirectory(), 255));
				if (config.isLive())
					agent.setStatus(AgentInfo.STATUS_LIVE);
				else
					agent.setStatus(AgentInfo.STATUS_REGISTERED);
				agent.setClientAppRegistered(true);
				
				AmsInfo ams = null;
				if (config.getAmsId() != null && !config.getAmsId().equals(""))
					ams = op.getAmsInfo(config.getAmsId());
				agent.setAms(ams);
				agent.save();
				
				if (config.getDefaultFilename() == null || !config.getDefaultFilename().equals(oldFilename))
					agent.updateFilenameToDb();
				
				
				// Email notifications -- added 03/31/2006, kwm
				String to = op.getPropertyValue(DatabaseFactory.PROP_EMAIL_REPORTS);
				if (to == null || to.equals(""))
					to = op.getPropertyValue(DatabaseFactory.PROP_EMAIL_CONTACT);
				
				// Is agent completing the migration process (previously used browser version of app)?
				if (previouslyRegistered && !previouslyRegWithClient)
				{
					String notifyFlag = op.getPropertyValue(DatabaseFactory.PROP_NOTIFY_ON_AGENCY_MIGRATION);
					if (notifyFlag != null && notifyFlag.trim().equals("Y"))
					{
						// Send an email to the carrier notifying that the agent has completed migration
						// to use the new client app
						CustomTextFactory factory = new CustomTextFactory(
							CustomTextFactory.TEXT_MIGRATED, CustomTextFactory.TYPE_EMAIL,
							null, agent, op);
						String message = factory.getText(op);
						String subject = factory.getEmailSubject(op);
						String htmlMsg = null;
						if (CarrierInfo.getInstance().isEmailAsHtml())
							htmlMsg = factory.getHtml(op);
						EmailService.getInstance().sendEMail(to, subject, message, htmlMsg);
					}
				}
				
				// Else is agent completing registration for the first time?
				else if (!previouslyRegistered)
				{
					String notifyFlag = op.getPropertyValue(DatabaseFactory.PROP_NOTIFY_ON_AGENCY_REGISTRATION);
					if (notifyFlag != null && notifyFlag.trim().equals("Y"))
					{
						// Send an email to the carrier notifying that the agent has completed 
						// the registration process
						CustomTextFactory factory = new CustomTextFactory(
							CustomTextFactory.TEXT_REGISTERED, CustomTextFactory.TYPE_EMAIL,
							null, agent, op);
						String message = factory.getText(op);
						String subject = factory.getEmailSubject(op);
						String htmlMsg = null;
						if (CarrierInfo.getInstance().isEmailAsHtml())
							htmlMsg = factory.getHtml(op);
						EmailService.getInstance().sendEMail(to, subject, message, htmlMsg);
					}
				}
				
				else
				{
					// Else has agent's download status changed?
					if (previouslyLive != agent.isLive())
					{
						String notifyFlag = op.getPropertyValue(DatabaseFactory.PROP_NOTIFY_ON_AGENCY_STATUS_CHANGE);
						if (notifyFlag != null && notifyFlag.trim().equals("Y"))
						{
							// Send an email to the carrier notifying that the agent has
							// changed their download status
							CustomTextFactory factory = new CustomTextFactory(
								CustomTextFactory.TEXT_STATUS_CHANGE, CustomTextFactory.TYPE_EMAIL,
								null, agent, op);
							factory.setPriorStatus((previouslyLive ? "live" : "active (registered)"));
							String message = factory.getText(op);
							String subject = factory.getEmailSubject(op);
							String htmlMsg = null;
							if (CarrierInfo.getInstance().isEmailAsHtml())
								htmlMsg = factory.getHtml(op);
							
							if (to == null)
							{
								to = op.getPropertyValue(DatabaseFactory.PROP_EMAIL_REPORTS);
								if (to == null || to.equals(""))
									to = op.getPropertyValue(DatabaseFactory.PROP_EMAIL_CONTACT);
							}
							EmailService.getInstance().sendEMail(to, subject, message, htmlMsg);
						}
					}
					
					// Has agent's vendor system settings been changed?
					if (config.getAmsId() != null && !config.getAmsId().equals(oldVendor))
					{
						String notifyFlag = op.getPropertyValue(DatabaseFactory.PROP_NOTIFY_ON_AGENCY_VENDOR_CHANGE);
						if (notifyFlag != null && notifyFlag.trim().equals("Y"))
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
							EmailService.getInstance().sendEMail(to, subject, message, htmlMsg);
						}
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

	/**
	 * Checks the string and truncates if longer than the specified maximum size.
	 * @param data - The text to check
	 * @param len - The maximum string size allowed for this data
	 * @return java.lang.String
	 */
	private String maxLength(String data, int len)
	{
		String ret = data;
		if (data != null && data.length() > len)
			ret = data.substring(0, len);
		return ret;
	}

}
