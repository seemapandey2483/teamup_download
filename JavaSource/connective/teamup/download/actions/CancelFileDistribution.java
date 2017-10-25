/*
 * 03/04/2005 - Created
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

/**
 * Action bean to cancel an incomplete batch file or email distribution.
 * 
 * @author Kyle McCreary
 */
public class CancelFileDistribution implements Action
{
	private static final Logger LOGGER = Logger.getLogger(CancelFileDistribution.class);
	/* (non-Javadoc)
	 * @see connective.teamup.download.Action#perform(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, connective.teamup.download.ServerInfo, connective.teamup.download.db.DatabaseOperation, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException
	{
		try
		{
			String batchnum = req.getParameter("distBatchNumber");
			if (batchnum != null && !batchnum.equals(""))
			{
				// Remove all agent mappings for the batch
				op.removeDistributedFileBatch(Integer.parseInt(batchnum));
				
				serverInfo.setStatusMessage(req.getSession(), "Cancelled file distribution process");
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error removing imported files", e);
			throw new ActionException("Error removing imported files", e);
		}
		
		return "splash";
	}

}
