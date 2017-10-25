package connective.teamup.download.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author Kyle W. McCreary
 *
 * Action bean to save updated agency password.
 */
public class SaveAgencyPassword implements Action
{
	private static final Logger LOGGER = Logger.getLogger(SaveAgencyPassword.class);
	/**
	 * Constructor for SaveAgencyPassword.
	 */
	public SaveAgencyPassword()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.Action#perform(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException
	{
		String nextPage = "splash";

		try
		{
			// Load the agent info bean
			AgentInfo agent = serverInfo.getAgentInfo(req.getSession(), op);
			
			// Parse new password info from the page
			String newPassword = req.getParameter("pword");
			if (newPassword == null)
				newPassword = "";
			
			// If agent has previously registered and is now changing the password,
			// forward to the desktop icons and/or scheduler config page, as appropriate
			if (agent.isRegistered() && !newPassword.equals(agent.getPassword()))
			{
				String desktopDownload = req.getParameter("desktopDownload");
				String desktopConfig = req.getParameter("desktopConfig");
				String scheduleTask = req.getParameter("scheduleTask");
		
				if ((desktopDownload != null && desktopDownload.equals("Y")) || (desktopConfig != null && desktopConfig.equals("Y")))
					nextPage = "config.desktop";
				else if (scheduleTask != null && scheduleTask.equals("Y"))
					nextPage = "config.scheduler";
			}
			
			// Save changes
			agent.setPassword(newPassword);
			agent.save();
			
			// Save updated agent info back to the HTTP session
			serverInfo.setAgentInfo(req.getSession(), agent);
		}
		catch (Exception e)
		{
			LOGGER.error("Error updating agent password", e);
			throw new ActionException("Error updating agent password", e);
		}
		
		return nextPage;
	}

}
