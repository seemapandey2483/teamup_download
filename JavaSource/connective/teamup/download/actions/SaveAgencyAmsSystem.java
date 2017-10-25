package connective.teamup.download.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.AmsInfo;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author mccrearyk
 *
 * Action bean to save updated agency vendor system settings from the Agency Admin pages.
 */
public class SaveAgencyAmsSystem implements Action
{
	private static final Logger LOGGER = Logger.getLogger(SaveAgencyAmsSystem.class);
	/**
	 * Constructor for SaveAgencyAmsSystem.
	 */
	public SaveAgencyAmsSystem()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.Action#perform(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException
	{
		String nextPage = "config.registration.ams";

		try
		{
			// Load the agent info bean and database connection info
			AgentInfo agent = serverInfo.getAgentInfo(req.getSession(), op);
			
			// Parse updated agency system info from the request.  Only save
			// if vendor system has been changed.
			String amsID = req.getParameter("system");
			if (amsID == null || agent.getAms().getId() == null || 
				!agent.getAms().getId().equals(amsID))
			{
				AmsInfo ams = op.getAmsInfo(amsID);
				agent.setAms(ams);
				agent.setRemoteDir(req.getParameter("dldir"));
				agent.save();
				
				agent.setDefaultFilename(ams.getCompanyFilename());
				agent.updateFilenameToDb();
				
				// Save updated agent info back to the HTTP session
				serverInfo.setAgentInfo(req.getSession(), agent);
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
