/*
 * 03/21/2005 - Created
 */
package connective.teamup.download.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.CustomTextFactory;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.BatchInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.DownloadStatus;
import connective.teamup.download.db.FileImportStatus;
import connective.teamup.download.services.EmailService;

/**
 * @author Kyle McCreary
 *
 * Action bean to complete the upload process for Applied edits file distribution.
 */
public class CompleteFileDistributionUpload implements Action
{
	private static final Logger LOGGER = Logger.getLogger(CompleteFileDistributionUpload.class);
	/* (non-Javadoc)
	 * @see connective.teamup.download.Action#perform(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, connective.teamup.download.ServerInfo, connective.teamup.download.db.DatabaseOperation, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException
	{
		try
		{
			// Parse the batch info from the request
			String distBatchNumber = req.getParameter("distBatchNumber");
			int batchNumber = Integer.parseInt(distBatchNumber);
			BatchInfo batchInfo = op.getBatchInfo(batchNumber);
			
			// Reset the download status to "Current" for all file mappings
			op.updateDistributionBatchStatus(batchInfo.getBatchNumber(), DownloadStatus.CURRENT);
			
			// Add an entry to the log
			FileImportStatus status = new FileImportStatus(FileImportStatus.IMPORTED_LEAVE_FILE);
			status.setText("Applied edits files imported successfully.");
			op.logDistributionImport(batchInfo, status, "Applied edits distribution");
			
			// Set status message
			serverInfo.setStatusMessage(req.getSession(), "Applied edits are ready for download");
			
			String notify = req.getParameter("notify");
			if (notify != null && notify.equals("Y"))
			{
				// Send email notification to all recipients
				AgentInfo[] agents = op.getDistributionAgentList(batchInfo);
				for (int i=0; i < agents.length; i++)
				{
					String address = agents[i].getContactEmail();
					if (address != null && !address.trim().equals(""))
					{
						CustomTextFactory factory = new CustomTextFactory(
							CustomTextFactory.TEXT_APPLIED_EDITS, 
							CustomTextFactory.TYPE_EMAIL,
							serverInfo, agents[i], op);
						factory.setLoginUrl(serverInfo.getRequestUrl(req, "/agency"));
						String htmlMsg = null;
						if (serverInfo.getCarrierInfo().isEmailAsHtml())
							htmlMsg = factory.getHtml();
						EmailService.getInstance().sendEMail(address, factory.getEmailSubject(), 
															 factory.getText(), htmlMsg);
					}
				}
			}
		}
		catch (ActionException ae)
		{
			LOGGER.error("Error sending agent notification of Applied edits files", ae);
			throw new ActionException("Error sending agent notification of Applied edits files", ae);
		}
		catch (Exception e)
		{
			LOGGER.error("Error completing the Applied edits file distribution process", e);
			throw new ActionException("Error completing the Applied edits file distribution process", e);
		}
		
		return "splash";
	}

}
