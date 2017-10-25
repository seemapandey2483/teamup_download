package connective.teamup.download.actions;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author haneym
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class PurgeFiles implements Action 
{
	private static final Logger LOGGER = Logger.getLogger(PurgeFiles.class);
	
	protected SimpleDateFormat df = null;
	

	/**
	 * Constructor for PurgeFiles.
	 */
	public PurgeFiles()
	{
		super();
		
		// Create the date format helpers
		df = (SimpleDateFormat) SimpleDateFormat.getInstance();
		df.applyPattern("MM/dd/yyyy");
	}

	/**
	 * @see connective.teamup.download.Action#perform(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException 
	{
		purgeFiles(req, resp, serverInfo, op);
		
		return "splash";
	}
	
	/**
	 * Purges archived files older than the set archive period.
	 */
	protected void purgeFiles(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op) throws ActionException 
	{
		int numDays = 30;		// Use as default
		
		try
		{
			String archivePeriod = op.getPropertyValue(DatabaseFactory.PROP_ARCHIVE_PERIOD);
			if (archivePeriod != null)
				numDays = Integer.parseInt(archivePeriod);
		}
		catch (Exception e)
		{
			LOGGER.error(e);
			System.out.println(e.getMessage());
		}

		Calendar archiveDate = Calendar.getInstance();
		archiveDate.add(Calendar.DATE, (-1 * numDays));
		
		// purge the files
		try
		{
			long ts = archiveDate.getTime().getTime();
			op.purge(ts);
		}
		catch (SQLException e)
		{
			LOGGER.error("Error purging archived files", e);
			throw new ActionException("Error purging archived files", e);
		}

		serverInfo.setStatusMessage(req.getSession(), "Purge completed successfully");
		
		// Update the last purge date
		Hashtable props = new Hashtable();
		props.put(DatabaseFactory.PROP_LAST_PURGE_DATE, df.format(new Date(System.currentTimeMillis())));
	}

}
