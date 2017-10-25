/*
 * 05/07/2005 - Created
 */
package connective.teamup.download.services;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import connective.teamup.download.CarrierInfo;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.DistributedFileInfo;
import connective.teamup.download.db.DistributedFileMappingInfo;
import connective.teamup.download.db.DownloadStatus;
import connective.teamup.download.db.FileDownloadStatus;
import connective.teamup.download.ws.objects.DownloadFileInfo;
import connective.teamup.download.ws.objects.DownloadFileInfoInternal;


/**
 * Download service used for handling Applied Custom Edits files.
 * 
 * @author mccrearyk
 */
public class AceDownloadService
{
	private static final Logger LOGGER = Logger.getLogger(AceDownloadService.class);
	/**
	 * Constructor for AceDownloadWebService
	 */
	public AceDownloadService()
	{
		super();
	}

	public DownloadFileInfoInternal[] getAppliedEditFiles(DatabaseOperation op, String agentId, String dlStatus)
	{
		DownloadFileInfoInternal[] ret = null;
		try
		{
			ArrayList fileList = new ArrayList();
			
			DownloadStatus status = null;
			if (dlStatus != null && !dlStatus.equals(""))
				status = DownloadStatus.getStatusForCode(dlStatus);
			
			DistributedFileMappingInfo[] fileMappings = op.getAgentDistributedBatchesByStatus(agentId, status);
			if (fileMappings != null)
			{
				for (int i=0; i < fileMappings.length; i++)
				{
					DistributedFileInfo[] files = fileMappings[i].getFiles();
					if (files == null)
					{
						fileMappings[i].delete();
						System.out.println("No files attached to this mapping -- mapping deleted");
					}
					else
					{
						AgentInfo agent = op.getAgentInfo(agentId);
						for (int j=0; j < files.length; j++)
						{
							DownloadFileInfoInternal file = new DownloadFileInfoInternal();
							String filename = agent.getRemoteDir();
							String ext = getExtension(files[j].getFilename());
							if (ext != null && !ext.equalsIgnoreCase("dbf") && !ext.equalsIgnoreCase("ndx"))
								filename += "forms\\";
							filename += (files[j].getFilename());
							file.setFilename(filename);
							file.setOriginalFilename(files[j].getFilename());
							file.setBatchNumber(files[j].getBatchNumber());
							file.setCreatedDate(files[j].getCreatedDate());
					
							try
							{
								byte[] fileContents = files[j].getFileContents();
								file.setFileContents(fileContents);
								if (fileContents == null || fileContents.length == 0)
								{
									file.setError(true);
									file.setErrorMsg("Error retrieving file contents");
								}
							}
							catch (Exception e)
							{
								LOGGER.error(e);
								file.setError(true);
								file.setErrorMsg("Error retrieving file contents: " + e.getMessage());
								file.setFileSize(-1);
							}
							
							// Add it to the array
							fileList.add(file);
						}
						break;
					}
				}
			}
			
			if (fileList.size() > 0)
			{
				ret = new DownloadFileInfoInternal[fileList.size()];
				fileList.toArray(ret);
			}
		}
		catch (SQLException e)
		{
			LOGGER.error(e);
			ret = new DownloadFileInfoInternal[1];
			ret[0] = new DownloadFileInfoInternal();
			ret[0].setError(true);
			ret[0].setErrorMsg("Error retrieving Applied Company Edit files: " + e.getMessage());
		}
		catch (IOException e)
		{
			LOGGER.error(e);
			ret = new DownloadFileInfoInternal[1];
			ret[0] = new DownloadFileInfoInternal();
			ret[0].setError(true);
			ret[0].setErrorMsg("Error retrieving Applied Company Edit files: " + e.getMessage());
		}
		
		if (ret != null && ret.length == 0)
			ret = null;
		
		return ret;
	}

	protected String getExtension(String filename)
	{
		String ret = null;
		if (filename != null)
		{
			int isep = filename.lastIndexOf('.');
			if (isep != -1)
				ret = filename.substring(isep + 1);
		}
		return ret;
	}

	/**
	 * Returns the specified Applied Company Edits file.
	 */
	public DownloadFileInfoInternal getAppliedEditFile(DatabaseOperation op, String agentId, int batchNumber, String originalFilename, long createdDate)
	{
		DownloadFileInfoInternal file = new DownloadFileInfoInternal();
		try
		{
			AgentInfo agent = op.getAgentInfo(agentId);
			
			// Get the file record to download
			DistributedFileInfo fileInfo = op.getDistributedFile(batchNumber, originalFilename, createdDate);
			String filename = agent.getRemoteDir();
			String ext = getExtension(fileInfo.getFilename());
			if (ext != null && !ext.equalsIgnoreCase("dbf") && !ext.equalsIgnoreCase("ntx"))
				filename += "forms\\";
			filename += (fileInfo.getFilename());
			file.setFilename(filename);
			file.setOriginalFilename(fileInfo.getFilename());
			file.setBatchNumber(fileInfo.getBatchNumber());
			file.setCreatedDate(fileInfo.getCreatedDate());
			
			try
			{
				byte[] fileContents = fileInfo.getFileContents();
				file.setFileContents(fileContents);
				if (fileContents == null || fileContents.length == 0)
				{
					file.setError(true);
					file.setErrorMsg("Error retrieving file contents");
				}
			}
			catch (Exception e)
			{
				LOGGER.error(e);
				file.setError(true);
				file.setErrorMsg("Error retrieving file contents: " + e.getMessage());
				file.setFileSize(-1);
			}
		}
		catch (SQLException e)
		{
			LOGGER.error(e);
			file.setError(true);
			file.setErrorMsg("Error retrieving Applied Company Edits file: " + e.getMessage());
			file.setFileSize(-1);
		}
		
		return file;
	}

	/**
	 * Updates the status on the successfully downloaded batch.
	 * @return Number of distribution file batches still waiting to be downloaded for agent
	 */
	public int downloadSuccessful(DatabaseOperation op, AgentInfo agentInfo, int batchNumber, DownloadFileInfo[] files) throws SQLException
	{
		int batchesRemaining = -1;
		long downloadDate = System.currentTimeMillis();
		String desc = "Distribution batch downloaded successfully";
		
		// find the file in question
		DistributedFileMappingInfo[] maps = op.getAgentDistributedBatches(agentInfo.getAgentId());
		if (maps != null)
		{
			for (int i=0; i < maps.length; i++)
			{
				if (maps[i].getBatchNumber() == batchNumber)
				{
					// change the status
					maps[i].setDownloadStatus(DownloadStatus.ARCHIVED);
					maps[i].setLastDownloadedDate(downloadDate);
					maps[i].save();
					
					// build the XML list of downloaded files
					String xml = ServiceHelper.getInstance().writeFilesToXML(files);
					
					// add to the log
					FileDownloadStatus status = new FileDownloadStatus(FileDownloadStatus.DOWNLOADED_DISTRIBUTION_FILE);
					status.setText(desc);
					op.logDistributionDownload(maps[i], status, agentInfo.getName(), xml);
					
					// write the response message
					DistributedFileMappingInfo[] fileMappings = 
						op.getAgentDistributedBatchesByStatus(agentInfo.getAgentId(), DownloadStatus.getStatusForCode("C"));
					if (fileMappings != null)
						batchesRemaining = fileMappings.length;

					break;					
				}
			}
		}
		
		return batchesRemaining;
	}

	/**
	 * Updates the status for the failed batch download attempt.
	 */
	public void downloadFailed(DatabaseOperation op, ServerInfo serverInfo, AgentInfo agentInfo, int batchNumber, String errorMsg, String stackTrace) throws Exception
	{
		long downloadDate = System.currentTimeMillis();
		if (errorMsg == null)
			errorMsg = "";
		
		// find the file in question
		DistributedFileMappingInfo[] maps = op.getAgentDistributedBatches(agentInfo.getAgentId());
		if (maps != null)
		{
			for (int i=0; i < maps.length; i++)
			{
				if (maps[i].getBatchNumber() == batchNumber)
				{
					// add to the log
					String msg = errorMsg;
					if (msg.length() > 255)
						msg = msg.substring(255);
					FileDownloadStatus status = new FileDownloadStatus(FileDownloadStatus.FAILED_OTHER);
					status.setText(msg);
					op.logDistributionDownload(maps[i], status, agentInfo.getName(), stackTrace);
					
					// send email
					if (serverInfo != null && serverInfo.getCarrierInfo().isNotifyOnDownloadError())
					{
						AgentInfo agent = op.getAgentInfo(agentInfo.getAgentId());
						CarrierInfo carrier = serverInfo.getCarrierInfo();
						String to = carrier.getErrorsEmail();
						
						String subject = carrier.getShortName() + " Download Error";
						
						StringBuffer body = new StringBuffer("TEAM-UP Download trading partner ");
						body.append(agentInfo.getAgentId());
						body.append(" (");
						body.append(agent.getName());
						body.append(") received an error during the distributed file download process.\n\n");
						body.append("Batch number:  ");
						body.append(batchNumber);
						body.append("\nApplication:  ");
						if (serverInfo == null)
							body.append("Agency Java Client App");
						else
							body.append(serverInfo.getAppName());
						body.append("\n\n");
						
						if (errorMsg != null && !errorMsg.equals(""))
						{
							body.append("ERROR MESSAGE:\n\n");
							body.append(errorMsg);
						}
						
						EmailService.getInstance().sendEMail(to, subject, body.toString());
					}
					
					break;
				}
			}
		}
	}

}
