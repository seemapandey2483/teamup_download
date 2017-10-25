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
import connective.teamup.download.db.FileInfo;

/**
 * @author Kyle McCreary
 *
 * Action bean to log the user out of the agency admin app and forward to a
 * generic page.
 */
public class ScheduledDownload implements Action
{
	private static final Logger LOGGER = Logger.getLogger(ScheduledDownload.class);
	/**
	 * Constructor for ScheduledDownloadComplete.
	 */
	public ScheduledDownload()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.Action#perform(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException 
	{
		String nextPage = "scheduled.download";
		
		try
		{
			AgentInfo info = serverInfo.getAgentInfo(req.getSession(), op);
			
			// get a list of files to download
			FileInfo[] filelist = op.getAgentFilesByStatus(info.getAgentId(), null);
			if (filelist.length == 0)
			{
				// Log the user out and display the "no files to download" page
				req.getSession().invalidate();
				nextPage = "scheduled.download.nofiles";
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error starting scheduled download process", e);
			throw new ActionException("Error starting scheduled download process", e);
		}

		return nextPage;
	}

}
