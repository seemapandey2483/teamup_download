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
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.services.EmailService;

/**
 * @author Kyle McCreary
 *
 * Action bean to save edited agency vendor system settings from the Agency Admin pages.
 */
public class SaveAgencyAmsConfig implements Action
{
	private static final Logger LOGGER = Logger.getLogger(SaveAgencyAmsConfig.class);
	/**
	 * Constructor for SaveAgencyAmsConfig.
	 */
	public SaveAgencyAmsConfig()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.Action#perform(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException
	{
		String nextPage = "testdownload";
		
		try
		{
			// Load the agent info bean and database connection info
			AgentInfo agent = serverInfo.getAgentInfo(req.getSession(), op);
			
			// Parse updated agency vendor info from the request
			agent.setAmsVer(req.getParameter("amsVersion"));
			agent.setRemoteDir(req.getParameter("remoteDirectory"));
			String filename = req.getParameter("filename");
			if (filename != null && !filename.trim().equals("") &&
									 !filename.equals(agent.getDefaultFilename()))
			{
				filename = filename.trim();
				int n = filename.indexOf('.');
				if (n < 0)
					filename += ".001";
				else if (n == filename.length() - 1)
					filename += "001";
				agent.setDefaultFilename(filename);
				
				// Update the filename for all archived files for this agent
				agent.updateFilenameToDb();
			}
			
			// Save changes back to the database
			agent.save();
			
			// Save updated agent info back to the HTTP session
			serverInfo.setAgentInfo(req.getSession(), agent);
			
			// Check to see if agency vendor system settings have been changed
			if (serverInfo.getCarrierInfo().isNotifyOnAgentChange())
			{
				String vendorChanged = req.getParameter("vendor_changed");
				if (vendorChanged != null && vendorChanged.equals("Y"))
				{
					// Send an email to the carrier notifying that the agent has
					// completed the registration process
					CustomTextFactory factory = new CustomTextFactory(
						CustomTextFactory.TEXT_AGENT_VENDOR_CHANGE, 
						CustomTextFactory.TYPE_EMAIL,
						serverInfo, agent, op);
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
			
			// Import the test file for the download test
			ImportTestFile uploadAction = new ImportTestFile();
			uploadAction.perform(req, resp, serverInfo, op, items);
		}
		catch (ActionException e)
		{
			LOGGER.error("Error sending email notification of agency vendor change", e);
			throw new ActionException("Error sending email notification of agency vendor change", e);
		}
		catch (Exception e)
		{
			LOGGER.error("Error updating agency settings", e);
			throw new ActionException("Error updating agency settings", e);
		}
		
		return nextPage;
	}

}
