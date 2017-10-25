package connective.teamup.download.actions;

import java.util.Date;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.BatchInfo;
import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;

//import com.ibm.jvm.Dump;

/**
 * @author Kyle McCreary
 *
 * Action bean to get a batch number for a new import process.
 */
public class ImportBatch extends PurgeFiles implements Action
{
	private static final Logger LOGGER = Logger.getLogger(ImportBatch.class);
	/**
	 * Constructor for ImportBatch.
	 */
	public ImportBatch()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.Action#perform(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException
	{
		try
		{
			// Check to see if carrier is configured for "auto-purge"
			String autoPurge = op.getPropertyValue(DatabaseFactory.PROP_AUTO_PURGE);
			if (autoPurge != null && autoPurge.equals("Y"))
			{
				// Check to see if a purge has been done today
				String lastPurgeDate = op.getPropertyValue(DatabaseFactory.PROP_LAST_PURGE_DATE);
				String today = df.format(new Date(System.currentTimeMillis()));
				if (lastPurgeDate == null || !lastPurgeDate.equals(today))
					purgeFiles(req, resp, serverInfo, op);
			}
			
			// Get the next batch number (if any), increment and save
			int batchNumber = 1;
			String str = op.getPropertyValue(DatabaseFactory.PROP_BATCHNUM);
			if (str != null && !str.equals(""))
				batchNumber = Integer.parseInt(str);
			Hashtable props = new Hashtable();
			props.put(DatabaseFactory.PROP_BATCHNUM, String.valueOf(batchNumber + 1));
			op.setProperties(props);
			
			// Create the batch import info object
			if (batchNumber > 0)
			{
				BatchInfo batchInfo = op.createBatchInfo(batchNumber);
				batchInfo.save();
			}
			
			// Get the maximum import block size
			int importSize = 0;
			try
			{
				String size = op.getPropertyValue(DatabaseFactory.PROP_IMPORT_BLOCK_SIZE);
				if (size != null)
					importSize = Integer.parseInt(size);
			}
			catch (Exception e) {
				LOGGER.error(e);
			}
			
			// Send the batch number and import block size to the import control
			String responseStr = String.valueOf(batchNumber) + "," +
								 String.valueOf(importSize);
			resp.getWriter().print(responseStr);
			resp.getWriter().flush();
		}
		catch (Exception e)
		{
			// MDH 8/17/07 - do a java core dump
			//Dump.JavaDump();
			LOGGER.error("Error initiating import process - Java Core Dump initiated", e);
			throw new ActionException("Error initiating import process - Java Core Dump initiated", e);
		}
		
		return null;
	}

}
