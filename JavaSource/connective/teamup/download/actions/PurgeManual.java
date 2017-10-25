/*
 * Created on Nov 4, 2007
 */
package connective.teamup.download.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.DatabaseOperation;

/**
 * Runs the PurgeFiles action from the command line app.
 * 
 * @author kmccreary
 */
public class PurgeManual extends PurgeFiles
{
	private static final Logger LOGGER = Logger.getLogger(PurgeManual.class);
	/**
	 * Constructor for PurgeManual.
	 */
	public PurgeManual()
	{
		super();
	}

	/* (non-Javadoc)
	 * @see connective.teamup.download.actions.PurgeFiles#perform(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, connective.teamup.download.ServerInfo, connective.teamup.download.db.DatabaseOperation, org.apache.commons.fileupload.FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items)
		throws ActionException
	{
		String responseMsg = "";
		
		try
		{
			purgeFiles(req, resp, serverInfo, op);
			responseMsg = serverInfo.getStatusMessage(req.getSession());
		}
		catch (ActionException e)
		{
			LOGGER.error(e);
			responseMsg = e.getMessage();
		}
		
		if (responseMsg == null || responseMsg.equals(""))
			responseMsg = "Error purging archived files";
		try {
			resp.getOutputStream().print(responseMsg);
		}
		catch (Exception e)
		{
			LOGGER.error("Error writing purge results to the HttpServletResponse", e);
			throw new ActionException("Error writing purge results to the HttpServletResponse", e);
		}
		
		return null;
	}

}
