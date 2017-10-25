/*
 * 05/12/2005 - Created
 */
package connective.teamup.download.services;

import java.sql.SQLException;

import connective.teamup.download.db.ClaimFileTransactionInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.DirectBillTransactionInfo;
import connective.teamup.download.db.DownloadStatus;
import connective.teamup.download.db.FileInfo;
import connective.teamup.download.db.TransactionInfo;
import connective.teamup.download.ws.objects.ClaimFileOutput;
import connective.teamup.download.ws.objects.DirectBillFileOutput;
import connective.teamup.download.ws.objects.DownloadClaimFileInfo;
import connective.teamup.download.ws.objects.DownloadDirectBillTransInfo;
import connective.teamup.download.ws.objects.DownloadFileInfo;
import connective.teamup.download.ws.objects.DownloadFileInfoInternal;
import connective.teamup.download.ws.objects.DownloadTransactionInfo;

/**
 * @author mccrearyk
 */
public class ArchiveService
{

	/**
	 * Constructor for ArchiveService.
	 */
	public ArchiveService()
	{
		super();
	}

	public DownloadFileInfoInternal[] getArchiveFiles(DatabaseOperation op, String agentId) throws SQLException
	{
		return getArchiveFiles(op, agentId, false);
	}
	public DirectBillFileOutput getDirectBillInfo(DatabaseOperation op, String agentId,String fileName,Long createdDate) throws SQLException{
		
		DownloadDirectBillTransInfo[] dbtrans = null;
		DirectBillFileOutput DBfile  = null;
		DirectBillTransactionInfo[] files = op.loadDirectBillsTransactionFromDb(agentId, fileName, createdDate);
		if(files != null && files.length>0) {
			dbtrans = new DownloadDirectBillTransInfo[files.length];
			for (int i=0; i < files.length; i++) {
				dbtrans[i] = ServiceHelper.getInstance().getDownloadDirectBillTransInfo(files[i]);
			}
			DBfile = new DirectBillFileOutput();
			DBfile.setDirectBillTransactionCount(files.length);
			DBfile.setDirectBillTransactions(dbtrans);
		}
		return DBfile;
	}
	public DownloadFileInfoInternal[] getArchiveFiles(DatabaseOperation op, String agentId, boolean loadTransactionInfo) throws SQLException
	{
		DownloadFileInfoInternal[] ret = null;
		FileInfo[] files = op.getAgentArchive(agentId, loadTransactionInfo);
		if (files != null && files.length > 0)
		{
			ret = new DownloadFileInfoInternal[files.length];
			for (int i=0; i < files.length; i++)
			{
				ret[i] = ServiceHelper.getInstance().getDownloadFileInfo(files[i]);
				
				if (loadTransactionInfo && files[i].getTransactionCount() > 0)
				{
					DownloadTransactionInfo[] trans = new DownloadTransactionInfo[files[i].getTransactionCount()];
					for (int j=0; j < files[i].getTransactionCount(); j++)
						trans[j] = ServiceHelper.getInstance().getDownloadTransInfo(files[i].getTransaction(j));
					ret[i].setTransactions(trans);
				}
			}
		}
		
		return ret;
	}
	
	public DownloadFileInfoInternal[] getArchiveFilesSelected(DatabaseOperation op, String agentId) throws SQLException
	{
		DownloadFileInfoInternal[] archiveFileCount = null;
		FileInfo[] files = op.getAgentArchiveForPaging(agentId);
		archiveFileCount = new DownloadFileInfoInternal[files.length];
		return archiveFileCount;
	}
	
	
	public DownloadFileInfoInternal[] getArchiveFilesSelected(DatabaseOperation op, String agentId, int pageNo, int rowsPerPage, boolean loadTransactionInfo) throws SQLException
	{
		DownloadFileInfoInternal[] returnArchiveFilesWithTrans = null;
		int transCount =0;
		FileInfo[] files = op.getAgentArchiveForPaging(agentId, pageNo, rowsPerPage, loadTransactionInfo);
		if (files != null && files.length > 0)
		{
			returnArchiveFilesWithTrans = new DownloadFileInfoInternal[files.length];
			for (int i=0; i < files.length; i++)
			{
				returnArchiveFilesWithTrans[i] = ServiceHelper.getInstance().getDownloadFileInfo(files[i]);
				transCount =  files[i].getTransactionCount();
				if(files[i].isDirectBill()) {
					transCount =1;
				}
				if (loadTransactionInfo && transCount > 0)
				{
					DownloadTransactionInfo[] trans = new DownloadTransactionInfo[transCount];
					for (int j=0; j < transCount; j++){
						if(files[i].getTransaction(j) != null)
							trans[j] = ServiceHelper.getInstance().getDownloadTransInfo(files[i].getTransaction(j));
					}
						
					returnArchiveFilesWithTrans[i].setTransactions(trans);
				}
			}
		}
		
		return returnArchiveFilesWithTrans;
	}
	
	public int getCurrentFilesCount(DatabaseOperation op, String agentId) throws SQLException{
		int currentFileCount = 0;
		currentFileCount = op.getCurrentFilesCount(agentId);
		return currentFileCount;
	}
	
	public int getArchiveFilesCount(DatabaseOperation op, String agentId) throws SQLException{
		int currentFileCount = 0;
		currentFileCount = op.getArchiveFilesCount(agentId);
		return currentFileCount;
	}
	
	public DownloadFileInfoInternal[] getCurrentFiles(DatabaseOperation op, String agentId) throws SQLException
	{
		DownloadFileInfoInternal[] currentFileCount = null;
		FileInfo[] files = op.getAgentCurrentForPaging(agentId);
		currentFileCount = new DownloadFileInfoInternal[files.length];
		return currentFileCount;
	}
	
	public DownloadFileInfoInternal[] getCurrentFiles(DatabaseOperation op, String agentId, int pageNo, int rowsPerPage, boolean loadTransactionInfo) throws SQLException
	{
		DownloadFileInfoInternal[] returnCurrentFilesWithTrans = null;
		int transCounter =0;
		FileInfo[] files = op.getAgentCurrentForPaging(agentId, pageNo, rowsPerPage, loadTransactionInfo);
		if (files != null && files.length > 0)
		{
			returnCurrentFilesWithTrans = new DownloadFileInfoInternal[files.length];
			for (int i=0; i < files.length; i++)
			{
				returnCurrentFilesWithTrans[i] = ServiceHelper.getInstance().getDownloadFileInfo(files[i]);
				
				if (loadTransactionInfo && files[i].getTransactionCount() > 0)
				{
					DownloadTransactionInfo[] trans = new DownloadTransactionInfo[files[i].getTransactionCount()];
					if(files[i].isDirectBill()) {
						transCounter = 1;
					}else {
						
						transCounter = files[i].getTransactionCount();
					}
					for (int j=0; j < transCounter; j++){
						if(files[i].getTransaction(j) != null)
							trans[j] = ServiceHelper.getInstance().getDownloadTransInfo(files[i].getTransaction(j));						
					}

					returnCurrentFilesWithTrans[i].setTransactions(trans);
				}
			}
		}
		
		return returnCurrentFilesWithTrans;
	}
	
	public void updateFiles(DatabaseOperation op, String agentId, DownloadFileInfo[] files) throws SQLException
	{
		if (files != null && files.length > 0)
		{
			for (int i=0; i < files.length; i++)
			{
				FileInfo fileInfo = op.getDownloadFile(agentId, files[i].getOriginalFilename(), files[i].getCreatedDate());
				DownloadStatus status = null;
				if (files[i].getStatus() == null || files[i].getStatus().equals(""))
					if(fileInfo.isClaimFile()){
						status = DownloadStatus.CLAIM_ARCHIVED;	
					}else{
						status = DownloadStatus.ARCHIVED;
					}
					
				else
					status = DownloadStatus.getStatusForCode(files[i].getStatus());
				
				// Check for change in file status
				if (!status.equals(fileInfo.getDownloadStatus()))
				{
					// Update the file status
					fileInfo.setDownloadStatus(status);
					fileInfo.save();
				}
				
				// Check for change in transactions status
				if (files[i].getTransactions() != null)
				{
					fileInfo.loadTransFromDb();
					DownloadTransactionInfo[] transactions = files[i].getTransactions();
					for (int j=0; j < transactions.length; j++)
					{
						if (j >= fileInfo.getTransactionCount())
							break;
						
						if (transactions[j].getDlStatus() == null || transactions[j].getDlStatus().equals(""))
							if(fileInfo.isClaimFile()){
								status = DownloadStatus.CLAIM_ARCHIVED;	
							}else{
								status = DownloadStatus.ARCHIVED;
							}
						else
							status = DownloadStatus.getStatusForCode(transactions[j].getDlStatus());
						
						TransactionInfo trans = fileInfo.getTransaction(j);
						if (!status.equals(trans.getDownloadStatus()))
						{
							// Update the transaction status
							trans.setDownloadStatus(status);
							trans.save();
						}
					}
				}
			}
		}
	}

public ClaimFileOutput getClaimFileInfo(DatabaseOperation op, String agentId,String fileName,Long createdDate) throws SQLException{
		
		DownloadClaimFileInfo[] dbtrans = null;
		ClaimFileOutput DBfile  = null;
		ClaimFileTransactionInfo[] files = op.loadClaimFilesTransactionFromDb(agentId, fileName, createdDate);
		if(files != null && files.length>0) {
			dbtrans = new DownloadClaimFileInfo[files.length];
			for (int i=0; i < files.length; i++) {
				dbtrans[i] = ServiceHelper.getInstance().getDownloadClaimTransInfo(files[i]);
			}
			DBfile = new ClaimFileOutput();
			DBfile.setClaimTransactionsCount(files.length);
			DBfile.setClaimTransactions(dbtrans);
		}
		return DBfile;
	}

public int getCurrentsearchFileCount(DatabaseOperation op, String agentId, String searchString, String searchFor) throws SQLException{
	int currentFileCount = 0;
	currentFileCount = op.getCurrentSearchFileCount(agentId, searchString, searchFor);
	return currentFileCount;
}

public int getArchivesearchFileCount(DatabaseOperation op, String agentId, String searchString, String searchFor) throws SQLException{
	int archiveFileCount = 0;
	archiveFileCount = op.getArchiveSearchFileCount(agentId, searchString, searchFor);
	return archiveFileCount;
}

public DownloadFileInfoInternal[] getCurrentSearchFiles(DatabaseOperation op, String agentId, int pageNo, int rowsPerPage, boolean loadTransactionInfo, String searchvalue, String searchFor) throws SQLException
{
	DownloadFileInfoInternal[] returnCurrentFilesWithTrans = null;
	int transCounter =0;
	FileInfo[] files = op.getAgentCurrentSearchForPaging(agentId, pageNo, rowsPerPage, loadTransactionInfo, searchvalue, searchFor);
	if (files != null && files.length > 0)
	{
		returnCurrentFilesWithTrans = new DownloadFileInfoInternal[files.length];
		for (int i=0; i < files.length; i++)
		{
			returnCurrentFilesWithTrans[i] = ServiceHelper.getInstance().getDownloadFileInfo(files[i]);
			
			if (loadTransactionInfo && files[i].getTransactionCount() > 0)
			{
				DownloadTransactionInfo[] trans = new DownloadTransactionInfo[files[i].getTransactionCount()];
				if(files[i].isDirectBill()) {
					transCounter = 1;
				}else {
					
					transCounter = files[i].getTransactionCount();
				}
				for (int j=0; j < transCounter; j++){
					if(files[i].getTransaction(j) != null)
						trans[j] = ServiceHelper.getInstance().getDownloadTransInfo(files[i].getTransaction(j));						
				}

				returnCurrentFilesWithTrans[i].setTransactions(trans);
			}
		}
	}
	
	return returnCurrentFilesWithTrans;
}

public DownloadFileInfoInternal[] getArchiveSearchFiles(DatabaseOperation op, String agentId, int pageNo, int rowsPerPage, boolean loadTransactionInfo, String searchvalue, String searchFor) throws SQLException
{
	DownloadFileInfoInternal[] returnArchiveFilesWithTrans = null;
	int transCounter =0;
	FileInfo[] files = op.getAgentArchiveSearchForPaging(agentId, pageNo, rowsPerPage, loadTransactionInfo, searchvalue, searchFor);
	if (files != null && files.length > 0)
	{
		returnArchiveFilesWithTrans = new DownloadFileInfoInternal[files.length];
		for (int i=0; i < files.length; i++)
		{
			returnArchiveFilesWithTrans[i] = ServiceHelper.getInstance().getDownloadFileInfo(files[i]);
			
			if (loadTransactionInfo && files[i].getTransactionCount() > 0)
			{
				DownloadTransactionInfo[] trans = new DownloadTransactionInfo[files[i].getTransactionCount()];
				if(files[i].isDirectBill()) {
					transCounter = 1;
				}else {
					
					transCounter = files[i].getTransactionCount();
				}
				for (int j=0; j < transCounter; j++){
					if(files[i].getTransaction(j) != null)
						trans[j] = ServiceHelper.getInstance().getDownloadTransInfo(files[i].getTransaction(j));						
				}

				returnArchiveFilesWithTrans[i].setTransactions(trans);
			}
		}
	}
	
	return returnArchiveFilesWithTrans;
}

}
