package connective.teamup.download.beans;

import java.io.Serializable;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.CarrierInfo;
import connective.teamup.download.DisplayBean;
import connective.teamup.download.DisplayBeanException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.BatchInfo;
import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseObject;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.LogInfo;

/**
 * @author Kyle McCreary
 *
 * Display bean used for building the Batch Import Log page.
 */
public class BatchLogDisplayBean implements Serializable, DisplayBean
{
	private static final Logger LOGGER = Logger.getLogger(BatchLogDisplayBean.class);
	
	private LogInfo[] logs = null;
	
	private CarrierInfo carrierInfo = null;
	
	private int batchNumber;
	private int importCount;
	private String importDate;
	private String orderBy;
	

	/**
	 * Constructor for BatchLogDisplayBean.
	 */
	public BatchLogDisplayBean()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.DisplayBean#init(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public void init(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws DisplayBeanException
	{
		try
		{
			// Load the company info
			carrierInfo = serverInfo.getCarrierInfo();
			
			// Parse the batch number from the request
			try
			{
				batchNumber = Integer.parseInt(req.getParameter("batch_num"));
			}
			catch (Exception e)
			{
				LOGGER.error("Required parameter 'batch_num' is null or non-numeric", e);
				throw new DisplayBeanException("Required parameter 'batch_num' is null or non-numeric", e);
			}
			
			// Load the import batch info
			BatchInfo batchInfo = op.getBatchInfo(batchNumber);
			importCount = batchInfo.getImportCount();
			importDate = batchInfo.getImportDateStrLong();
			
			orderBy = req.getParameter("orderby");
			if (orderBy == null || orderBy.equals(""))
				orderBy = DatabaseObject.colLogOrigName;
			
			// Load the transaction log entries for this import batch
			logs = op.getLogsByBatchNumber(batchNumber, DatabaseFactory.EVENT_IMPORT, orderBy);
		}
		catch (SQLException e)
		{
			LOGGER.error("Error occurred building import log report", e);
			throw new DisplayBeanException("Error occurred building import log report", e);
		}
	}

	/**
	 * Returns the import batch number.
	 * @return int
	 */
	public int getBatchNumber()
	{
		return batchNumber;
	}

	/**
	 * Returns the carrier info bean.
	 * @return CarrierInfo
	 */
	public CarrierInfo getCarrierInfo()
	 {
		return carrierInfo;
	}

	/**
	 * Returns the total number of files successfully imported.
	 * @return int
	 */
	public int getImportCount()
	{
		return importCount;
	}

	/**
	 * Returns the import date.
	 * @return String
	 */
	public String getImportDate()
	{
		return importDate;
	}

	/**
	 * Returns the specified log entry.
	 * @return LogInfo
	 */
	public LogInfo getLog(int index)
	{
		if (index > logs.length)
			return null;
		
		return logs[index];
	}
	
	/**
	 * Returns the total number of log entries for this import batch.
	 * @return int
	 */
	public int getLogCount()
	{
		return logs.length;
	}

}
