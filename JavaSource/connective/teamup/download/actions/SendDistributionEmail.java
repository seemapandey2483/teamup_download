/*
 * 03/20/2005 - Created
 */
package connective.teamup.download.actions;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.BatchInfo;
import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.DistributedEmailHelper;
import connective.teamup.download.db.DistributedFileInfo;
import connective.teamup.download.db.DistributedFileMappingInfo;
import connective.teamup.download.services.EmailService;

/**
 * @author Kyle McCreary
 *
 * Action bean to send the agent email distribution.
 */
public class SendDistributionEmail implements Action
{
	private static final Logger LOGGER = Logger.getLogger(SendDistributionEmail.class);
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
			
			// Send the email(s)
			sendEmail(batchInfo, req.getSession(), serverInfo, op);
		}
		catch (Exception e)
		{
			LOGGER.error("Error sending distribution email to selected agents", e);
			throw new ActionException("Error sending distribution email to selected agents", e);
		}
		
		return "splash";
	}

	protected void sendEmail(BatchInfo batchInfo, HttpSession session, ServerInfo serverInfo, DatabaseOperation op) throws Exception
	{
		long emailTimestamp = System.currentTimeMillis();
		AgentInfo[] agents = op.getDistributionAgentList(batchInfo);
		if (agents == null || agents.length == 0)
		{
			String errMsg = "Email could not be sent -- no agents were selected";
			serverInfo.setStatusMessage(session, errMsg);
			op.logGroupEvent(null, "Email Distribution", DatabaseFactory.EVENT_EMAIL, errMsg, emailTimestamp, false, null);
		}
		else
		{
			String subject = "";
			String body = "";
			String emailAddress = "";
			
			DistributedFileInfo[] files = op.getDistributedFilesForBatch(batchInfo.getBatchNumber());
			if (files != null)
			{
				InputStream[] attachments = null;
				String[] fileNames = null;
				int fileCount = 0;
				for (int i=0; i < files.length; i++)
				{
					if (files[i].getFilename().equals(DistributedEmailHelper.EMAIL_BODY))
					{
						// Parse the email subject, body, etc. from the file contents
						DistributedEmailHelper email = new DistributedEmailHelper(files[i]);
						subject = email.getSubject();
						body = email.getBody();
					}
					else
						fileCount++;
				}
				
				// Create the list of file attachments
				if (fileCount > 0)
				{
					attachments = new InputStream[fileCount];
					fileNames = new String[fileCount];
					fileCount = -1;
					for (int i=0; i < files.length; i++)
					{
						if (!files[i].getFilename().equals(DistributedEmailHelper.EMAIL_BODY))
						{
							fileCount++;
							attachments[fileCount] = new ByteArrayInputStream(files[i].getFileContents());
							fileNames[fileCount] = files[i].getFilename();
						}
					}
				}

				for (int i=0; i < agents.length; i++)
				{
					emailAddress = agents[i].getContactEmail();
					if (emailAddress != null && !emailAddress.equals(""))
					{
						DistributedFileMappingInfo mapping = 
								op.getDistributedFileBatch(agents[i].getAgentId(), batchInfo.getBatchNumber());
						
						if (mapping != null)
						{
							// Send email with any saved attachments
							EmailService.getInstance().sendEMail(emailAddress, subject, body, null, attachments, fileNames);
					
							// Delete the agent mapping once the email has been sent
							mapping.delete();
						}
					}
				}
			
				// Also send a copy to the sender
				emailAddress = serverInfo.getCarrierInfo().getReportsEmail();
				EmailService.getInstance().sendEMail(emailAddress, subject, body, null, attachments, fileNames);
			
				// Set status message
				serverInfo.setStatusMessage(session, "Email was sent to all selected agents");
			
				// Add an entry to the log
				op.logGroupEvent(null, "Email Distribution", DatabaseFactory.EVENT_EMAIL, "SUBJECT: " + subject, emailTimestamp, true, null);
			}
			else
			{
				String errMsg = "Email could not be sent -- no email text was defined";
				serverInfo.setStatusMessage(session, errMsg);
				op.logGroupEvent(null, "Email Distribution", DatabaseFactory.EVENT_EMAIL, "SUBJECT: " + subject + "; ****" + errMsg, emailTimestamp, false, null);
			}
			
			// Delete the batch and all attached files
			if (files != null && files.length > 0)
			{
				for (int i=0; i < files.length; i++)
					files[i].delete();
			}
			batchInfo.delete();
		}
	}

}
