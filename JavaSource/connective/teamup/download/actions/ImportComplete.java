package connective.teamup.download.actions;

import java.util.GregorianCalendar;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.BatchInfo;
import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.FileImportStatus;
import connective.teamup.download.db.FileInfo;

/**
 * @author Kyle McCreary
 *
 * Action bean to complete a batch import process.
 */
public class ImportComplete implements Action
{
	private static final Logger LOGGER = Logger.getLogger(ImportComplete.class);
	/**
	 * Constructor for ImportComplete.
	 */
	public ImportComplete()
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
			// Parse the batch number from the request
			int batchNumber = 0;
			int importCount = 0;
			try
			{
				batchNumber = Integer.parseInt(req.getParameter("batch_num"));
			}
			catch (Exception e) {
				LOGGER.error(e);
			}
			
			// Get the batch import info object
			if (batchNumber > 0)
			{
				// Check for incomplete files
				FileInfo[] files = op.getIncompleteFilesForBatch(batchNumber);
				for (int i=0; i < files.length; i++)
				{
					AgentInfo agent = op.getAgentInfo(files[i].getAgentId());
					String agentName = "";
					if (agent != null)
						agentName = agent.getName();
					
					FileImportStatus status = new FileImportStatus(FileImportStatus.FAILED_OTHER);
					status.setText("File import was incomplete");
					op.logImport(files[i], status, batchNumber, agentName);
					files[i].delete();
				}
				
				// Get count of complete imported files, update batch info
				importCount = op.getFileCountForBatchNumber(batchNumber);
				
				BatchInfo batchInfo = op.getBatchInfo(batchNumber);
				batchInfo.setImportCount(importCount);
				batchInfo.save();
			}
			
			// Send the number of files successfully imported to the import control
			resp.getWriter().print(String.valueOf(importCount) + "\n");
			resp.getWriter().flush();
			
			// Update import stats for the month, reset next month's stats to zero
			String isStr = DatabaseFactory.PROP_IMPORT_STATS;
			GregorianCalendar today = new GregorianCalendar();
			int month = today.get(GregorianCalendar.MONTH);
			int n = 0;
			try {
				n = Integer.parseInt(op.getPropertyValue(isStr + String.valueOf(month)));
			} catch (Exception e) {}
			Hashtable props = new Hashtable();
			props.put(isStr + String.valueOf(month), String.valueOf(n + importCount));
			if (month == GregorianCalendar.DECEMBER)
				month = GregorianCalendar.JANUARY;
			else
				month++;
			props.put(isStr + String.valueOf(month), "0");
			op.setProperties(props);
		}
		catch (Exception e)
		{
			LOGGER.error("Error completing batch import", e);
			throw new ActionException("Error completing batch import", e);
		}
		
		return null;
	}

}
