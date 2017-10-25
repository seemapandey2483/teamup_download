package connective.teamup.download.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.FileInfo;

/**
 * Action bean to delete the specified file from the agent's download archive,
 * then re-display the archive page.  Also saves any changes to the download 
 * flags for other archived files.
 * 
 * @author Kyle McCreary
 */
public class ArchiveDeleteFile extends UpdateArchiveFiles
{
	private static final Logger LOGGER = Logger.getLogger(ArchiveDeleteFile.class);
	/**
	 * Constructor for ArchiveHideTransactions.
	 */
	public ArchiveDeleteFile()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.Action#perform(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException
	{
		// Save any changes to the download flags
		saveArchiveChanges(req, serverInfo, op);
		
		// Delete the specified file
		try
		{
			String agentID = req.getParameter("agentID");
			String fileType = req.getParameter("action_filetype");
			String fileName = req.getParameter("action_filename");
			String fileDate = req.getParameter("action_filedate");
			if (fileType != null && fileType.equals("D"))			// Download data files
			{
				if (fileName != null && !fileName.equals("") && fileDate != null && !fileDate.equals(""))
				{
					long date = Long.parseLong(fileDate);
					FileInfo file = op.getDownloadFile(agentID, fileName, date);
					if (file != null)
						file.delete();
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error deleting download file from archive", e);
			throw new ActionException("Error deleting download file from archive", e);
		}
		
		// Re-display the archive page without changing the "show transactions" status
		String nextPage = "archive";
		String transShown = req.getParameter("trans_shown");
		if (transShown != null && transShown.equals("Y"))
			nextPage = "archive.transactions";
		
		return nextPage;
	}

}
