package connective.teamup.download.actions;

import java.sql.SQLException;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.DownloadStatus;
import connective.teamup.download.db.FileInfo;
import connective.teamup.download.db.TransactionInfo;

/**
 * @author Kyle McCreary
 *
 * Action bean to save any changes to the download flags on the archive list page.
 */
public class UpdateArchiveFiles implements Action
{
	private static final Logger LOGGER = Logger.getLogger(UpdateArchiveFiles.class);
	/**
	 * Constructor for UpdateArchiveFiles.
	 */
	public UpdateArchiveFiles()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.Action#perform(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException
	{
		saveArchiveChanges(req, serverInfo, op);
		
		String nextPage = "archive";
		String transShown = req.getParameter("trans_shown");
		if (transShown != null && transShown.equals("Y"))
			nextPage = "archive.transactions";
		
		return nextPage;
	}
	
	/**
	 * Save changes to the file download flags on the download archive page
	 * 
	 * @param request Object that encapsulates the request to the servlet
	 * @param serverInfo The server info bean
	 */
	protected void saveArchiveChanges(HttpServletRequest req, ServerInfo serverInfo, DatabaseOperation op) throws ActionException
	{
		String agentID = req.getParameter("agentID");
		String transFlag = req.getParameter("trans_shown");
		boolean transactionsShown = (transFlag != null && transFlag.equals("Y"));
		String pageVersion = req.getParameter("pagever");
		boolean companyPage = (pageVersion != null && pageVersion.equalsIgnoreCase("C"));
		
		try
		{
			// Parse the updated download flags from the page
			Hashtable files = new Hashtable();
			int changedCount = 0;
			int fileCount = Integer.parseInt(req.getParameter("filecount"));
			for (int i=0; i < fileCount; i++)
			{
				String file = "file" + i;
				String fileChanged = req.getParameter(file + "changed");
				if (fileChanged != null && fileChanged.equals("Y"))
				{
					changedCount++;
					
					// Build a hashtable of info about this file
					Hashtable fileHash = new Hashtable();
					DownloadStatus dlStatus = DownloadStatus.getStatusForCode(req.getParameter(file + "type"));
					if (dlStatus == null)
					{
						String dlDirBill = req.getParameter(file + "dirbill");
						if (dlDirBill != null && dlDirBill.equals("Y"))
							dlStatus = DownloadStatus.DB_ARCHIVED;
						else
							dlStatus = DownloadStatus.ARCHIVED;
					}
					fileHash.put("status", dlStatus);
					
					if (transactionsShown)
					{
						// Parse the status flags for each transaction
						int transCount = Integer.parseInt(req.getParameter(file + "transcount"));
						for (int t=0; t < transCount; t++)
						{
							String seq = req.getParameter(file + "trans" + t);
							if (seq != null && !seq.equals(""))
								fileHash.put(new Integer(seq), dlStatus);
						}
					}
					
					String key = req.getParameter(file);
					if (key != null && !key.equals(""))
						files.put(key, fileHash);
				}
			}
			
			if (changedCount > 0)
			{
				// Compare list of archived files to the files hashtable and build the
				// flag update statements
				FileInfo[] fileinfos = op.getAgentFiles(agentID);
				for (int i=0; i < fileinfos.length; i++)
				{
					if (companyPage || (!fileinfos[i].getDownloadStatus().equals(DownloadStatus.CURRENT) &&
										!fileinfos[i].getDownloadStatus().equals(DownloadStatus.DB_CURRENT)))
					{
						String filename = fileinfos[i].getOriginalFilename();
						String createdDate = String.valueOf(fileinfos[i].getCreatedDate());
						String key = filename + "_" + createdDate;
						
						// Check to see if this file was changed (ie, it is in the hashtable)
						if (files.get(key) != null)
						{
							Hashtable fileHash = (Hashtable) files.get(key);
							DownloadStatus status = (DownloadStatus) fileHash.get("status");
							if (status != null && !fileinfos[i].getDownloadStatus().equals(status))
							{
								fileinfos[i].setDownloadStatus(status);
								fileinfos[i].save();
								
								if (!transactionsShown)
								{
									// Update the status of all transactions in this file
									fileinfos[i].loadTransFromDb();
									for (int t=0; t < fileinfos[i].getTransactionCount(); t++)
									{
										TransactionInfo trans = fileinfos[i].getTransaction(t);
										trans.setDownloadStatus(status);
										trans.save();
									}
								}
							}
							
							if (transactionsShown)
							{
								// Loop through all transactions in this file. If the transaction
								// is in the hashtable, see if the status has changed. If not in
								// the hashtable, assume the status is "archived."  If the status
								// has changed, update and save to the database.
								fileinfos[i].loadTransFromDb();
								for (int t=0; t < fileinfos[i].getTransactionCount(); t++)
								{
									TransactionInfo trans = fileinfos[i].getTransaction(t);
									DownloadStatus tranStatus = (DownloadStatus) fileHash.get(new Integer(trans.getSequence()));
									if (tranStatus == null)
									{
										if (status != null && (status.equals(DownloadStatus.DB_ARCHIVED) ||
															   status.equals(DownloadStatus.DB_CURRENT)))
											tranStatus = DownloadStatus.DB_ARCHIVED;
										else
											tranStatus = DownloadStatus.ARCHIVED;
									}
									if (!trans.getDownloadStatus().equals(tranStatus))
									{
										trans.setDownloadStatus(tranStatus);
										trans.save();
									}
								}
							}
						}
					}
				}
			}
		}
		catch (SQLException e)
		{
			LOGGER.error("Error updating archive file status", e);
			throw new ActionException("Error updating archive file status", e);
		}
	}

}
