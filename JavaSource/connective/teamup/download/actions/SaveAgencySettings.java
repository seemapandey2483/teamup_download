package connective.teamup.download.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.CustomTextFactory;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.AmsInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.services.EmailService;

/**
 * @author Kyle McCreary
 *
 * Action bean to save edited agency information from the Agency Admin pages.
 */
public class SaveAgencySettings implements Action
{
	private static final Logger LOGGER = Logger.getLogger(SaveAgencySettings.class);
	/**
	 * Constructor for SaveAgencySettings.
	 */
	public SaveAgencySettings()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.Action#perform(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException
	{
		String nextPage = "menu.settings";
		
		try
		{
			// Load the agent info bean and database connection info
			AgentInfo agent = serverInfo.getAgentInfo(req.getSession(), op);
			boolean changeLoginKey = false;
			boolean previouslyLive = agent.isLive();
			
			// Parse updated agency info from the request
			String agencyName = req.getParameter("agencyname");
			if (serverInfo.getCarrierInfo().isAgentInfoChangeAllowed() &&
				agencyName != null && !agencyName.equals("") && 
				!agencyName.equals(agent.getName()))
			{
				agent.setName(agencyName);
				//if (agent.isRegistered())
				//	changeLoginKey = true;
			}
			
			String amsID = req.getParameter("system");
			if (amsID == null || agent.getAms().getId() == null || 
				!agent.getAms().getId().equals(amsID))
			{
				AmsInfo ams = op.getAmsInfo(amsID);
				agent.setAms(ams);
			}
			agent.setRemoteDir(req.getParameter("dldir"));
			
			// Only get/save updated contact info if agent is allowed to change that info
			if (serverInfo.getCarrierInfo().isAgentInfoChangeAllowed())
			{
				agent.setContactName(req.getParameter("contact"));
				agent.setContactEmail(req.getParameter("email"));
				agent.setLocationState(req.getParameter("locstate"));
				
				String phoneArea = req.getParameter("phone_area");
				String phonePrefix = req.getParameter("phone_prefix");
				String phoneSuffix = req.getParameter("phone_suffix");
				String phoneExt = req.getParameter("phone_ext");
				String contactPhone = phoneArea + phonePrefix + phoneSuffix;
				if (phoneExt != null)
					contactPhone += phoneExt.trim();
				agent.setContactPhone(contactPhone);
			}
						
			if (agent.getPassword().equals(""))
			{
				String pword = req.getParameter("pword");
				if (pword != null && serverInfo.getCarrierInfo().isAgentPasswordChangeAllowed())
					agent.setPassword(pword);
			}
			else
			{
				String changePwd = req.getParameter("change_pword");
				if (changePwd != null && changePwd.equals("Y"))
				{
					String newPassword = req.getParameter("pword");
					if (newPassword == null)
						newPassword = "";
					
					// If agent has previously registered and is now changing the
					// password, display a message that the download link has been
					// changed.
					if (agent.isRegistered() && !newPassword.equals(agent.getPassword()))
						changeLoginKey = true;
					
					agent.setPassword(newPassword);
				}
			}
			
			// If agent has previously registered and is now changing either the
			// password or the agency name, display a message that the download
			// link has been changed -- 10/08/2003, kwm
			if (changeLoginKey && agent.isRegistered())
				nextPage = "menu.settings.keychanged";
			
			if (agent.isRegistered())
			{
				String liveFlag = req.getParameter("golive");
				if (liveFlag == null || !liveFlag.equals("Y"))
					liveFlag = "N";
				if (liveFlag.equals("Y"))
					agent.setStatus(AgentInfo.STATUS_LIVE);
				else
					agent.setStatus(AgentInfo.STATUS_REGISTERED);
			}
			
			String interactiveFlag = req.getParameter("interactive");
			if (interactiveFlag == null || !interactiveFlag.equals("Y"))
				interactiveFlag = "N";
			agent.setInteractive(interactiveFlag);
			
			
			// Save changes back to the database
			agent.save();
			
			// If the AMS system was changed, update the agent's default filename
			// to match the new system
			AmsInfo ams = agent.getAms();
			if (ams != null && ams.getCompanyFilename() != null && 
				(agent.getDefaultFilename() == null || !ams.getCompanyFilename().equals(agent.getDefaultFilename())))
			{
				agent.setDefaultFilename(ams.getCompanyFilename());
				agent.updateFilenameToDb();
			}
			
			// Save updated agent info back to the HTTP session
			serverInfo.setAgentInfo(req.getSession(), agent);
	
			
			if (!agent.isRegistered())
			{
				// Navigate to the next page of the agency registration process
				nextPage = "config.registration.ams";
			}
			else if (previouslyLive != agent.isLive() &&
					  serverInfo.getCarrierInfo().isNotifyOnStatusChange())
			{
				// Send an email to the carrier notifying that the agent has
				// changed their download status
				CustomTextFactory factory = new CustomTextFactory(
					CustomTextFactory.TEXT_STATUS_CHANGE, CustomTextFactory.TYPE_EMAIL,
					serverInfo, agent, op);
				factory.setPriorStatus((previouslyLive ? "live" : "active (registered)"));
				String message = factory.getText();
				String subject = factory.getEmailSubject();
				
				String htmlMsg = null;
				if (serverInfo.getCarrierInfo().isEmailAsHtml())
					htmlMsg = factory.getHtml();
				
				String to = serverInfo.getCarrierInfo().getReportsEmail();
				if (to == null || to.equals(""))
					to = serverInfo.getCarrierInfo().getContactEmail();
				EmailService.getInstance().sendEMail(to, subject, message, htmlMsg);
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error updating agency config settings", e);
			throw new ActionException("Error updating agency config settings", e);
		}
		
		return nextPage;
	}

}
