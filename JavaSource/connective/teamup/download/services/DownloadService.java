/*
 * 05/06/2005 - Created
 */
package connective.teamup.download.services;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import connective.teamup.download.ActionException;
import connective.teamup.download.CarrierInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.DownloadStatus;
import connective.teamup.download.db.FileDownloadStatus;
import connective.teamup.download.db.FileInfo;
import connective.teamup.download.db.LogTransInfo;
import connective.teamup.download.db.TransactionInfo;
import connective.teamup.download.ws.objects.DownloadFileInfo;
import connective.teamup.download.ws.objects.DownloadFileInfoInternal;
import connective.teamup.download.ws.objects.DownloadTransactionInfo;

/**
 * @author mccrearyk
 */
public class DownloadService
{
	private static final Logger LOGGER = Logger.getLogger(DownloadService.class);
	
	protected boolean debugFlag = false;
	/**
	 * Constructor for DownloadService
	 */
	public DownloadService()
	{
		super();
	}

	public DownloadFileInfoInternal[] getDownloadFiles(DatabaseOperation op, String agentId, String dlStatus)
	{
		return getDownloadFiles(op, agentId, dlStatus, false);
	}

	public DownloadFileInfoInternal[] getDownloadFiles(DatabaseOperation op, String agentId, String dlStatus, boolean loadTransactionInfo)
	{
		DownloadFileInfoInternal[] ret = null;
		try
		{
			DownloadStatus status = null;
			if (dlStatus != null && !dlStatus.equals(""))
				status = DownloadStatus.getStatusForCode(dlStatus);
				
			//	03/11/2014 -- Limit the number of policies downloaded to as close to 500 as possible
			//		as we do not want to split up a file
			int policyCounterLimit = 500;
			int newPolicyCounter = 0;	// used to count number of policies and if reached, identifies last file processed
			boolean policyLimitReached = false;
			FileInfo[] files = op.getAgentFilesByStatus(agentId, status);
			if (files != null && files.length > 0)
			{
				ret = new DownloadFileInfoInternal[files.length];
				for (int i=0; i < files.length; i++)
				{
					ret[i] = ServiceHelper.getInstance().getDownloadFileInfo(files[i]);

					ArrayList transList = null;
					if (loadTransactionInfo)
					{
						transList = new ArrayList();
						if (debugFlag)
							System.out.println("File: " + (i+1));
						int transCount = ret[i].getTransactionCount();
						newPolicyCounter+= transCount;
						if (transCount > 0)
						{
							if (debugFlag)
								System.out.println("transaction count: " + (transCount) + ", counter: " + newPolicyCounter);
							if (newPolicyCounter > policyCounterLimit)
							{
								//	Evaluate overage to determine if we keep processing this file or discard for next batch
								if ((policyCounterLimit - newPolicyCounter) >= 50 && i > 0)
								{
									//	Stop, do not process this file unless it is the first file
									policyLimitReached = true;
									newPolicyCounter = i - 1;
									if (debugFlag)
										System.out.println("BREAK");
									break;
								}
							}
						}
					}
					
					try
					{
						byte[] fileContents = ServiceHelper.getInstance().getAL3Contents(files[i], transList);
						ret[i].setFileContents(fileContents); 
						if (fileContents == null || fileContents.length == 0)
						{
							ret[i].setError(true);
							ret[i].setErrorMsg("Error retrieving file contents");
						}
						else if (loadTransactionInfo && transList != null && transList.size() > 0)
						{
							// Load the list of transactions included in the file
							DownloadTransactionInfo[] transactions = new DownloadTransactionInfo[transList.size()];
							for (int j=0; j < transList.size(); j++)
							{
								transactions[j] = ServiceHelper.getInstance().getDownloadTransInfo((TransactionInfo)transList.get(j));
							}
							ret[i].setTransactions(transactions);
							ret[i].setTransactionCount(transactions.length);
						}
					}
					catch (Exception e)
					{
						LOGGER.error(e);
						ret[i].setError(true);
						ret[i].setErrorMsg("Error retrieving file contents: " + e.getMessage());
						ret[i].setFileSize(-1);
					}
					if (newPolicyCounter > policyCounterLimit)
					{
						policyLimitReached = true;
						newPolicyCounter = i;
						if (debugFlag)
							System.out.println("BREAK");
						break;
					}
				}
				if (policyLimitReached)
				{
					if (debugFlag)
						System.out.println("resizing array");
					//	Resize ret and populate
					DownloadFileInfoInternal[] newRet = new DownloadFileInfoInternal[newPolicyCounter];
					for (int i = 0; i < newPolicyCounter; i++)
					{
						newRet[i] = ret[i];
						ret[i] = null;
					}
					ret = newRet;
				}
			}
		}
		catch (SQLException e)
		{
			LOGGER.error(e);
			ret = new DownloadFileInfoInternal[1];
			ret[0] = new DownloadFileInfoInternal();
			ret[0].setError(true);
			ret[0].setErrorMsg("Error retrieving download files: " + e.getMessage());
		}
		
		return ret;
	}
	
	public DownloadFileInfoInternal[] getDownloadFiles(DatabaseOperation op, String agentId, String dlStatus, int pageNo, int rowsPerSelected, boolean loadTransactionInfo)
	{
		DownloadFileInfoInternal[] ret = null;
		try
		{
			DownloadStatus status = null;
			if (dlStatus != null && !dlStatus.equals(""))
				status = DownloadStatus.getStatusForCode(dlStatus);
				
			//	03/11/2014 -- Limit the number of policies downloaded to as close to 500 as possible
			//		as we do not want to split up a file
			int policyCounterLimit = 500;
			int newPolicyCounter = 0;	// used to count number of policies and if reached, identifies last file processed
			boolean policyLimitReached = false;
			FileInfo[] files = op.getAgentFilesByStatus(agentId, status, pageNo, rowsPerSelected);
			if (files != null && files.length > 0)
			{
				ret = new DownloadFileInfoInternal[files.length];
				for (int i=0; i < files.length; i++)
				{
					ret[i] = ServiceHelper.getInstance().getDownloadFileInfo(files[i]);

					ArrayList transList = null;
					if (loadTransactionInfo)
					{
						transList = new ArrayList();
						if (debugFlag)
							System.out.println("File: " + (i+1));
						int transCount = ret[i].getTransactionCount();
						if(files[i].isDirectBill()) {
							transCount =1;
						}
						newPolicyCounter+= transCount;
						if (transCount > 0)
						{
							if (debugFlag)
								System.out.println("transaction count: " + (transCount) + ", counter: " + newPolicyCounter);
							if (newPolicyCounter > policyCounterLimit)
							{
								//	Evaluate overage to determine if we keep processing this file or discard for next batch
								if ((policyCounterLimit - newPolicyCounter) >= 50 && i > 0)
								{
									//	Stop, do not process this file unless it is the first file
									policyLimitReached = true;
									newPolicyCounter = i - 1;
									if (debugFlag)
										System.out.println("BREAK");
									break;
								}
							}
						}
					}
					
					try
					{
						byte[] fileContents = ServiceHelper.getInstance().getAL3Contents(files[i], transList);
						ret[i].setFileContents(fileContents); 
						if (fileContents == null || fileContents.length == 0)
						{
							ret[i].setError(true);
							ret[i].setErrorMsg("Error retrieving file contents");
						}
						else if (loadTransactionInfo && transList != null && transList.size() > 0)
						{
							// Load the list of transactions included in the file
							DownloadTransactionInfo[] transactions = new DownloadTransactionInfo[transList.size()];
							for (int j=0; j < transList.size(); j++)
							{
								transactions[j] = ServiceHelper.getInstance().getDownloadTransInfo((TransactionInfo)transList.get(j));
							}
							ret[i].setTransactions(transactions);
							ret[i].setTransactionCount(transactions.length);
						}
					}
					catch (Exception e)
					{
						LOGGER.error(e);
						ret[i].setError(true);
						ret[i].setErrorMsg("Error retrieving file contents: " + e.getMessage());
						ret[i].setFileSize(-1);
					}
					if (newPolicyCounter > policyCounterLimit)
					{
						policyLimitReached = true;
						newPolicyCounter = i;
						if (debugFlag)
							System.out.println("BREAK");
						break;
					}
				}
				if (policyLimitReached)
				{
					if (debugFlag)
						System.out.println("resizing array");
					//	Resize ret and populate
					DownloadFileInfoInternal[] newRet = new DownloadFileInfoInternal[newPolicyCounter];
					for (int i = 0; i < newPolicyCounter; i++)
					{
						newRet[i] = ret[i];
						ret[i] = null;
					}
					ret = newRet;
				}
			}
		}
		catch (SQLException e)
		{
			LOGGER.error(e);
			ret = new DownloadFileInfoInternal[1];
			ret[0] = new DownloadFileInfoInternal();
			ret[0].setError(true);
			ret[0].setErrorMsg("Error retrieving download files: " + e.getMessage());
		}
		
		return ret;
	}
	
	public DownloadFileInfoInternal[] getDownloadFiles(DatabaseOperation op, String agentId, String fileName, long createdDate, boolean loadTransactionInfo)
	{
		DownloadFileInfoInternal[] ret = null;
		try
		{
			//DownloadStatus status = null;
			/*if (dlStatus != null && !dlStatus.equals(""))
				status = DownloadStatus.getStatusForCode(dlStatus);*/
				
			//	03/11/2014 -- Limit the number of policies downloaded to as close to 500 as possible
			//		as we do not want to split up a file
			int policyCounterLimit = 500;
			int newPolicyCounter = 0;	// used to count number of policies and if reached, identifies last file processed
			boolean policyLimitReached = false;
			FileInfo[] files = op.getAgentFilesForDwnl(agentId, fileName, createdDate);
			if (files != null && files.length > 0)
			{
				ret = new DownloadFileInfoInternal[files.length];
				for (int i=0; i < files.length; i++)
				{
					ret[i] = ServiceHelper.getInstance().getDownloadFileInfo(files[i]);

					ArrayList transList = null;
					if (loadTransactionInfo)
					{
						transList = new ArrayList();
						if (debugFlag)
							System.out.println("File: " + (i+1));
						int transCount = ret[i].getTransactionCount();
						newPolicyCounter+= transCount;
						if (transCount > 0)
						{
							if (debugFlag)
								System.out.println("transaction count: " + (transCount) + ", counter: " + newPolicyCounter);
							if (newPolicyCounter > policyCounterLimit)
							{
								//	Evaluate overage to determine if we keep processing this file or discard for next batch
								if ((policyCounterLimit - newPolicyCounter) >= 50 && i > 0)
								{
									//	Stop, do not process this file unless it is the first file
									policyLimitReached = true;
									newPolicyCounter = i - 1;
									if (debugFlag)
										System.out.println("BREAK");
									break;
								}
							}
						}
					}
					
					try
					{
						byte[] fileContents = ServiceHelper.getInstance().getAL3Contents(files[i], transList);
						ret[i].setFileContents(fileContents); 
						if (fileContents == null || fileContents.length == 0)
						{
							ret[i].setError(true);
							ret[i].setErrorMsg("Error retrieving file contents");
						}
						else if (loadTransactionInfo && transList != null && transList.size() > 0)
						{
							// Load the list of transactions included in the file
							DownloadTransactionInfo[] transactions = new DownloadTransactionInfo[transList.size()];
							for (int j=0; j < transList.size(); j++)
							{
								transactions[j] = ServiceHelper.getInstance().getDownloadTransInfo((TransactionInfo)transList.get(j));
							}
							ret[i].setTransactions(transactions);
							ret[i].setTransactionCount(transactions.length);
						}
					}
					catch (Exception e)
					{
						LOGGER.error(e);
						ret[i].setError(true);
						ret[i].setErrorMsg("Error retrieving file contents: " + e.getMessage());
						ret[i].setFileSize(-1);
					}
					if (newPolicyCounter > policyCounterLimit)
					{
						policyLimitReached = true;
						newPolicyCounter = i;
						if (debugFlag)
							System.out.println("BREAK");
						break;
					}
				}
				if (policyLimitReached)
				{
					if (debugFlag)
						System.out.println("resizing array");
					//	Resize ret and populate
					DownloadFileInfoInternal[] newRet = new DownloadFileInfoInternal[newPolicyCounter];
					for (int i = 0; i < newPolicyCounter; i++)
					{
						newRet[i] = ret[i];
						ret[i] = null;
					}
					ret = newRet;
				}
			}
		}
		catch (SQLException e)
		{
			LOGGER.error(e);
			ret = new DownloadFileInfoInternal[1];
			ret[0] = new DownloadFileInfoInternal();
			ret[0].setError(true);
			ret[0].setErrorMsg("Error retrieving download files: " + e.getMessage());
		}
		
		return ret;
	}
	

	/**
	 * Returns the specified download file.  Performs any updates to the ACORD
	 * data that may be required for either a partial file download OR a
	 * resend/synchronization download.
	 */
	public DownloadFileInfoInternal getDownloadFile(DatabaseOperation op, String agentId, String originalFilename, long createdDate)
	{
		return getDownloadFile(op, agentId, originalFilename, createdDate, false);
	}

	public DownloadFileInfoInternal getDownloadFile(DatabaseOperation op, String fileName, boolean loadTransactionInfo){
		DownloadFileInfoInternal file = new DownloadFileInfoInternal();
		try
		{
			// Get the file record to download
			//FileInfo fileInfo = op.getDownloadFile(agentId, originalFilename, createdDate);
			FileInfo fileInfo = op.getDownloadFile(fileName);
			file = ServiceHelper.getInstance().getDownloadFileInfo(fileInfo);
					
			ArrayList transList = null;
			if (loadTransactionInfo)
				transList = new ArrayList();
			
			byte[] fileContents = ServiceHelper.getInstance().getAL3Contents(fileInfo, transList);
			file.setFileContents(fileContents);
			if (fileContents == null || fileContents.length == 0)
			{
				file.setError(true);
				file.setErrorMsg("Error retrieving file contents");
			}
			else if (loadTransactionInfo && transList != null && transList.size() > 0)
			{
				// Load the list of transactions included in the file
				DownloadTransactionInfo[] transactions = new DownloadTransactionInfo[transList.size()];
				for (int j=0; j < transList.size(); j++)
				{
					transactions[j] = ServiceHelper.getInstance().getDownloadTransInfo((TransactionInfo)transList.get(j));
				}
				file.setTransactions(transactions);
				file.setTransactionCount(transactions.length);
			}
		}
		catch (SQLException se)
		{
			LOGGER.error(se);
			file.setError(true);
			file.setErrorMsg("Error retrieving download file: " + se.getMessage());
			file.setFileSize(-1);
		}
		catch (Exception e)
		{
			LOGGER.error(e);
			file.setError(true);
			file.setErrorMsg("Error retrieving file contents: " + e.getMessage());
		}
		
		return file;
	}
	
	/**
	 * Returns the specified download file.  Performs any updates to the ACORD
	 * data that may be required for either a partial file download OR a
	 * resend/synchronization download.
	 */
	public DownloadFileInfoInternal getDownloadFile(DatabaseOperation op, String agentId, String originalFilename, long createdDate, boolean loadTransactionInfo)
	{
		DownloadFileInfoInternal file = new DownloadFileInfoInternal();
		try
		{
			// Get the file record to download
			FileInfo fileInfo = op.getDownloadFile(agentId, originalFilename, createdDate);
			file = ServiceHelper.getInstance().getDownloadFileInfo(fileInfo);
					
			ArrayList transList = null;
			if (loadTransactionInfo)
				transList = new ArrayList();
			
			byte[] fileContents = ServiceHelper.getInstance().getAL3Contents(fileInfo, transList);
			file.setFileContents(fileContents);
			if (fileContents == null || fileContents.length == 0)
			{
				file.setError(true);
				file.setErrorMsg("Error retrieving file contents");
			}
			else if (loadTransactionInfo && transList != null && transList.size() > 0)
			{
				// Load the list of transactions included in the file
				DownloadTransactionInfo[] transactions = new DownloadTransactionInfo[transList.size()];
				for (int j=0; j < transList.size(); j++)
				{
					transactions[j] = ServiceHelper.getInstance().getDownloadTransInfo((TransactionInfo)transList.get(j));
				}
				file.setTransactions(transactions);
				file.setTransactionCount(transactions.length);
			}
		}
		catch (SQLException se)
		{
			LOGGER.error(se);
			file.setError(true);
			file.setErrorMsg("Error retrieving download file: " + se.getMessage());
			file.setFileSize(-1);
		}
		catch (Exception e)
		{
			LOGGER.error(e);
			file.setError(true);
			file.setErrorMsg("Error retrieving file contents: " + e.getMessage());
		}
		
		return file;
	}

	public void downloadAcknowledge(DatabaseOperation op, AgentInfo agentInfo, DownloadFileInfo[] files) throws ActionException
	{
		long downloadDate = System.currentTimeMillis();
		for (int i=0; i < files.length; i++)
		{
			if (files[i].isError())
				downloadFailed(op, agentInfo, files[i], downloadDate);
			else
				downloadSuccessful(op, agentInfo, files[i], downloadDate);
		}
	}

	protected void downloadSuccessful(DatabaseOperation op, AgentInfo agentInfo, DownloadFileInfo file, long downloadDate) throws ActionException
	{
		try
		{
			// build the XML info on the downloaded file
			file.setLastDownloadedDate(downloadDate);
			String xml = ServiceHelper.getInstance().writeFileToXML(file);
			int transCount =0;
			List<LogTransInfo> logList = null;
			LogTransInfo logInfo = null;
			
			// find the file in question
			FileInfo fileInfo = op.getDownloadFile(agentInfo.getAgentId(), file.getOriginalFilename(), file.getCreatedDate());
			boolean currentFile = (fileInfo.getDownloadStatus().equals(DownloadStatus.CURRENT) ||
								   fileInfo.getDownloadStatus().equals(DownloadStatus.CLAIM_CURRENT) ||
								   fileInfo.getDownloadStatus().equals(DownloadStatus.DB_CURRENT) || 
								   fileInfo.getDownloadStatus().equals(DownloadStatus.POLICYXML_CURRENT));
			String desc = "";
			
			FileDownloadStatus status = new FileDownloadStatus();
			if (currentFile)
				status.setStatus(FileDownloadStatus.DOWNLOADED_CURRENT_FILE);
			else if (fileInfo.getDownloadStatus().equals(DownloadStatus.RESEND))
				status.setStatus(FileDownloadStatus.DOWNLOADED_RESEND_FILE);
			else
				status.setStatus(FileDownloadStatus.DOWNLOADED_ARCHIVED_FILE);
			
			// delete file if it is marked as a test file, otherwise mark it as downloaded
			if (!fileInfo.isTestFile())
			{
				// mark file and trans as downloaded
				DownloadStatus newStatus = DownloadStatus.ARCHIVED;
				transCount = fileInfo.getTransactionCount();
				if (fileInfo.isDirectBill()){
					newStatus = DownloadStatus.DB_ARCHIVED;		
					transCount = 1;
				}
				if (fileInfo.isClaimFile()){
					newStatus = DownloadStatus.CLAIM_ARCHIVED;		
				}
				if (fileInfo.isPolicyXmlFile()){
					newStatus = DownloadStatus.POLICYXML_ARCHIVED;		
				}
				fileInfo.setDownloadStatus(newStatus);
				fileInfo.setLastDownloadDate(downloadDate);
				fileInfo.save();
				fileInfo.loadTransFromDb();
				logList = new ArrayList<LogTransInfo>();
				for (int i=0; i < transCount; i++)
				{
					TransactionInfo trans = (TransactionInfo) fileInfo.getTransaction(i);
					if (fileInfo.isClaimFile()){
						trans.loadClaimFromDb();						
					}

					if (trans.getDownloadStatus().equals(newStatus))
						status.setPartialFile();
					trans.setDownloadStatus(newStatus);
					trans.setLastDownloadDate(downloadDate);
					trans.save();
					logInfo = new LogTransInfo(trans,fileInfo);
					logInfo.setBatchnum(0);
					logInfo.setEvent_type(DatabaseFactory.EVENT_DOWNLOAD);
					logInfo.setFileName(fileInfo.getFilename());
					logInfo.setOrigFileName(fileInfo.getOriginalFilename());
					logInfo.setCreated_date(fileInfo.getCreatedDate());
					logList.add(logInfo);
				}
						
				desc = "File downloaded successfully";
			}
			else
			{
				fileInfo.delete();
				desc = "Test file download";
			}
				
			// add to the log
			status.setText(desc);
			//op.logDownload(fileInfo, status, agentInfo.getName(), xml, downloadDate);
			op.logImportWithDetails(fileInfo, status, 0, agentInfo.getAgentId(),logList,DatabaseFactory.EVENT_DOWNLOAD,xml);
			
			// If this was a "current" file, update the agent's last download date
			if (currentFile && !fileInfo.isTestFile())
			{
				op.updateAgentLastDLDate(agentInfo.getAgentId(), System.currentTimeMillis());
			}
		}
		catch (SQLException e)
		{
			LOGGER.error("Error occurred during the download acknowledgment", e);
			throw new ActionException("Error occurred during the download acknowledgment", e);
		}
		catch (Exception e)
		{
			LOGGER.error("Error occurred during the download acknowledgment", e);
			throw new ActionException("Error occurred during the download acknowledgment", e);
		}
	}

	protected void downloadFailed(DatabaseOperation op, AgentInfo agentInfo, DownloadFileInfo file, long downloadDate) throws ActionException
	{
		try
		{
			// find the file in question
			FileInfo fileInfo = op.getDownloadFile(agentInfo.getAgentId(), file.getOriginalFilename(), file.getCreatedDate());
			
			FileDownloadStatus status = new FileDownloadStatus(FileDownloadStatus.FAILED_OTHER);
			String errorMsg = file.getErrorMsg();
			if (errorMsg == null)
				errorMsg = "";
			String msg = errorMsg;
			if (msg.length() > 255)
				msg = msg.substring(255);
			status.setText(msg);
			op.logDownload(fileInfo, status, agentInfo.getName(), downloadDate);
			
			// send email
			if (CarrierInfo.getInstance().isNotifyOnDownloadError())
			{
				String to = CarrierInfo.getInstance().getErrorsEmail();
				
				String subject = CarrierInfo.getInstance().getShortName() + " Download Error";
				
				SimpleDateFormat df = (SimpleDateFormat) SimpleDateFormat.getInstance();
				df.applyPattern("MM/dd/yyyy HH:mm:ss.SSS");
				
				StringBuffer body = new StringBuffer("TEAM-UP Download trading partner ");
				body.append(agentInfo.getAgentId());
				body.append(" (");
				body.append(agentInfo.getName());
				body.append(") received an error during the download process.\n\n");
				body.append("Application:  Agency Java Client App\n");
				body.append("File name:  ");
				body.append(fileInfo.getFilename());
				body.append("\n");
				if (!fileInfo.getFilename().equals(fileInfo.getOriginalFilename()))
				{
					body.append("Original file name:  ");
					body.append(fileInfo.getOriginalFilename());
					body.append("\n");
				}
				body.append("File created:  ");
				body.append(df.format(new Date(fileInfo.getCreatedDate())));
				body.append("\n");
				body.append("File imported:  ");
				body.append(df.format(new Date(fileInfo.getImportedDate())));
				body.append("\n\n");
				
				if (errorMsg != null && !errorMsg.equals(""))
				{
					body.append("ERROR MESSAGE:\n\n");
					body.append(errorMsg);
				}
				
				EmailService.getInstance().sendEMail(to, subject, body.toString());
			}
		}
		catch (SQLException e)
		{
			LOGGER.error("Error occurred during the download acknowledgment", e);
			throw new ActionException("Error occurred during the download acknowledgment", e);
		}
	}

}
