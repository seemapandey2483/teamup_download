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
 * @author Kyle McCreary
 *
 * Action bean to set the agency to scheduled (non-interactive) mode.
 */
public class SaveSchedulerConfig implements Action
{
	private static final Logger LOGGER = Logger.getLogger(SaveSchedulerConfig.class);
	/**
	 * Constructor for SaveAgencyAmsConfig.
	 */
	public SaveSchedulerConfig()
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
			// update the interactive flag
			AgentInfo agent = serverInfo.getAgentInfo(req.getSession(), op);
			agent.setInteractive(false);
			agent.save();
			
			// Save updated agent info back to the HTTP session
			serverInfo.setAgentInfo(req.getSession(), agent);			
		}
		catch (Exception e)
		{
			LOGGER.error("Error updating agenct interactive/scheduled setting", e);
			throw new ActionException("Error updating agenct interactive/scheduled setting", e);
		}

		return nextPage;
	}

}
