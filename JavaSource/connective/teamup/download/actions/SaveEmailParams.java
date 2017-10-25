/*
 * 03/20/2005 - Created
 */
package connective.teamup.download.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.BatchInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.DistributedEmailHelper;
import connective.teamup.download.db.DistributedFileInfo;

/**
 * @author Kyle McCreary
 *
 * Action bean to save the email subject and body for agent email distribution.
 */
public class SaveEmailParams extends SendDistributionEmail
{
	private static final Logger LOGGER = Logger.getLogger(SaveEmailParams.class);
	/* (non-Javadoc)
	 * @see connective.teamup.download.Action#perform(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, connective.teamup.download.ServerInfo, connective.teamup.download.db.DatabaseOperation, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException
	{
		String nextPage = "splash";
		
		try
		{
			// Parse the batch info from the request
			String distBatchNumber = req.getParameter("distBatchNumber");
			int batchNumber = Integer.parseInt(distBatchNumber);
			BatchInfo batchInfo = op.getBatchInfo(batchNumber);
			
			// Parse the email params from the request and save
			DistributedEmailHelper email = new DistributedEmailHelper();
			email.setSubject(req.getParameter("subject"));
			email.setBody(req.getParameter("body"));
			email.setSender(req.getParameter("sender"));

			DistributedFileInfo[] files = op.getDistributedFilesForBatch(batchInfo.getBatchNumber());
			DistributedFileInfo fileInfo = null;
			if (files != null && files.length > 0)
			{
				for (int i=0; i < files.length; i++)
				{
					if (files[i].getFilename().equals(DistributedEmailHelper.EMAIL_BODY))
					{
						fileInfo = files[i];
						break;
					}
				}
			}
			if (fileInfo == null)
			{
				long datestamp = System.currentTimeMillis();
				fileInfo = op.createDistributedFile(batchInfo);
				fileInfo.setCreatedDate(datestamp);
				fileInfo.setFilename(DistributedEmailHelper.EMAIL_BODY);
				fileInfo.setFileType("E");
				fileInfo.setImportedDate(datestamp);
			}
			email.saveToFile(fileInfo);
			fileInfo.save();
			
			// Determine the next page
			String action = req.getParameter("addAttachments");
			if (action != null && action.equals("Y"))
				nextPage = "distribute.email.select.files";
			else
			{
				// Send the email now
				sendEmail(batchInfo, req.getSession(), serverInfo, op);
				nextPage = "splash";
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error saving email distribution parameters", e);
			throw new ActionException("Error saving email distribution parameters", e);
		}
		
		return nextPage;
	}

}
