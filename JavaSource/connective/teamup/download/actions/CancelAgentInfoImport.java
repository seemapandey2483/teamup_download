/*
 * 04/11/2005 - Created
 */
package connective.teamup.download.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.DistributedFileInfo;

/**
 * Action bean to cancel an incomplete import of comma-delimited agent info.
 * 
 * @author Kyle McCreary
 */
public class CancelAgentInfoImport implements Action
{
	private static final Logger LOGGER = Logger.getLogger(CancelAgentInfoImport.class);
	/* (non-Javadoc)
	 * @see connective.teamup.download.Action#perform(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, connective.teamup.download.ServerInfo, connective.teamup.download.db.DatabaseOperation, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException
	{
		try
		{
			String batchnum = req.getParameter("batchnum");
			if (batchnum != null && !batchnum.equals(""))
			{
				// Remove the imported agent info file(s)
				DistributedFileInfo[] files = op.getDistributedFilesForBatch(Integer.parseInt(batchnum));
				if (files != null)
				{
					for (int i=0; i < files.length; i++)
						files[i].delete();
				}
				
				serverInfo.setStatusMessage(req.getSession(), "Cancelled agent info import process");
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error removing imported comma-delimited files", e);
			throw new ActionException("Error removing imported comma-delimited files", e);
		}
		
		return "splash";
	}

}
