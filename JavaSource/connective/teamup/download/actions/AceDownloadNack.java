package connective.teamup.download.actions;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.services.AceDownloadService;

/**
 * @author haneym
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class AceDownloadNack implements Action 
{
	private static final Logger LOGGER = Logger.getLogger(AceDownloadNack.class);
	
	protected SimpleDateFormat df = null;

	/**
	 * Constructor for AceDownloadNack.
	 */
	public AceDownloadNack() 
	{
		super();
		
		df = (SimpleDateFormat) SimpleDateFormat.getInstance();
		df.applyPattern("MM/dd/yyyy HH:mm:ss.SSS");
	}

	/**
	 * @see connective.teamup.download.Action#perform(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException 
	{
		try
		{
			AgentInfo agentInfo = serverInfo.getAgentInfo(req.getSession(), op);
			int batchNum = Integer.parseInt(req.getParameter("batchnum"));
			String msg = req.getParameter("error_msg");

			// find the batch in question
			AceDownloadService service = new AceDownloadService();
			service.downloadFailed(op, serverInfo, agentInfo, batchNum, msg, null);
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
			throw new ActionException("Error in ACE download NACK", e);
		}
		
		return null;
	}

}
