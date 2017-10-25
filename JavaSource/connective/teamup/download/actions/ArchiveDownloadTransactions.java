package connective.teamup.download.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author Kyle McCreary
 *
 * Action bean to save any changes to the download flags on the archive list page,
 * then navigate to the download control page.
 */
public class ArchiveDownloadTransactions extends UpdateArchiveFiles
{

	/**
	 * Constructor for ArchiveHideTransactions.
	 */
	public ArchiveDownloadTransactions()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.Action#perform(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException
	{
		saveArchiveChanges(req, serverInfo, op);
		
		return "download.archived.files";
	}

}
