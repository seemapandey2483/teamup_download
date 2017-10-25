/*
 * 05/06/2005 - Created
 */
package connective.teamup.download.ws.objects;

import java.io.Serializable;


/**
 * @author mccrearyk
 */
public class DownloadFileInfo implements Serializable
{
	protected String filename = null;
	protected String originalFilename = null;
	protected String status = null;
	protected String participantCode = null;
	protected String errorMsg = null;
	protected boolean error = false;
	protected boolean testFile = false;
	protected long createdDate = 0;
	protected long importedDate = 0;
	protected long lastDownloadedDate = 0;
	protected int batchNumber = 0;
	protected int msgSeq = 0;
	protected int transactionCount = 0;
	protected int fileSize = -1;
	protected String fileContents = null;
	
	protected DownloadTransactionInfo[] transactions = null;


	/**
	 * Constructor for DownloadFileInfo.
	 */
	public DownloadFileInfo()
	{
		super();
	}


	/**
	 * Returns the batch number associated with this file (if applicable).
	 */
	public int getBatchNumber()
	{
		return batchNumber;
	}

	/**
	 * Returns the file created date.
	 * @return long
	 */
	public long getCreatedDate()
	{
		return createdDate;
	}

	/**
	 * Returns true if this file is in error; otherwise false.
	 * @return boolean
	 */
	public boolean isError()
	{
		return error;
	}

	/**
	 * Returns the error text or the system-generated exception message.
	 * @return String
	 */
	public String getErrorMsg()
	{
		return errorMsg;
	}

	/**
	 * Returns the file contents as a byte array.
	 * @return byte[]
	 */
	public String getFileContentsEncoded()
	{
		return fileContents;
	}

	/**
	 * Returns the file contents as a byte array.
	 * @return byte[]
	 */
	public void setFileContentsEncoded(String str)
	{
		fileContents = str;
	}

	/**
	 * Returns the name the file should be saved as on the agent's system.
	 * @return String
	 */
	public String getFilename()
	{
		return filename;
	}

	/**
	 * Returns the file size, to be used to confirm the entire file contents
	 * were received correctly.  (Returns -1 if error occurred during file
	 * retrieval.)
	 * @return int
	 */
	public int getFileSize()
	{
		return fileSize;
	}

	/**
	 * Returns the name of the file as originally imported (used for file
	 * retrieval from the TEAM-UP database/archive).
	 * @return String
	 */
	public String getOriginalFilename()
	{
		return originalFilename;
	}

	/**
	 * Returns the array of transaction objects contained within this file, or null.
	 */
	public DownloadTransactionInfo[] getTransactions()
	{
		return transactions;
	}
	
	public DownloadTransactionInfo getTransactions(int index)
	{
		return transactions[index];
	}
	
	public void setTransactions(int index, DownloadTransactionInfo value)
	{
		transactions[index] = value;
	}
	
	/**
	 * Sets the batch number associated with this file (if applicable).
	 */
	public void setBatchNumber(int i)
	{
		batchNumber = i;
	}

	/**
	 * Sets the file created date.
	 */
	public void setCreatedDate(long l)
	{
		createdDate = l;
	}

	/**
	 * Sets the error flag.
	 * @param isError - True if an error occurred (default = false)
	 */
	public void setError(boolean isError)
	{
		error = isError;
	}

	/**
	 * Sets the error message text.
	 */
	public void setErrorMsg(String string)
	{
		errorMsg = string;
	}

	/**
	 * Sets the filename to be used when saving the file to the agent's system.
	 */
	public void setFilename(String string)
	{
		filename = string;
	}

	/**
	 * Sets the file size.
	 * @param fileSize - The number of bytes in the file contents; -1 if an error occurred during file retrieval
	 */
	public void setFileSize(int fileSize)
	{
		this.fileSize = fileSize;
	}

	/**
	 * Sets the original (imported) name of the file.
	 */
	public void setOriginalFilename(String string)
	{
		originalFilename = string;
	}

	/**
	 * Sets the array of transaction objects contained within the file, if applicable.
	 */
	public void setTransactions(DownloadTransactionInfo[] infos)
	{
		transactions = infos;
		
		/*if (infos != null)
			transactionCount = infos.length;*/
	}

	/**
	 * Returns the download status of the file.
	 */
	public String getStatus()
	{
		return status;
	}

	/**
	 * Returns the file imported date.
	 */
	public long getImportedDate()
	{
		return importedDate;
	}

	/**
	 * Returns the last date the file was downloaded.
	 */
	public long getLastDownloadedDate()
	{
		return lastDownloadedDate;
	}

	/**
	 * Returns the ACORD message sequence.
	 */
	public int getMsgSeq()
	{
		return msgSeq;
	}

	/**
	 * Returns the agent/participant code.
	 */
	public String getParticipantCode()
	{
		return participantCode;
	}

	/**
	 * Returns true if file is a test file, otherwise false.
	 */
	public boolean isTestFile()
	{
		return testFile;
	}

	/**
	 * Returns the number of transactions in the file.
	 */
	public int getTransactionCount()
	{
		return transactionCount;
	}

	/**
	 * Sets the download status of the file.
	 */
	public void setStatus(String string)
	{
		status = string;
	}

	/**
	 * Sets the date the file was imported.
	 */
	public void setImportedDate(long l)
	{
		importedDate = l;
	}

	/**
	 * Sets the date the file was last downloaded.
	 */
	public void setLastDownloadedDate(long l)
	{
		lastDownloadedDate = l;
	}

	/**
	 * Sets the ACORD message sequence.
	 */
	public void setMsgSeq(int i)
	{
		msgSeq = i;
	}

	/**
	 * Sets the agent/participant code.
	 */
	public void setParticipantCode(String string)
	{
		participantCode = string;
	}

	/**
	 * Sets the test file flag.
	 */
	public void setTestFile(boolean b)
	{
		testFile = b;
	}

	/**
	 * Sets the number of transactions included in the file.
	 */
	public void setTransactionCount(int i)
	{
		transactionCount = i;
	}

}
