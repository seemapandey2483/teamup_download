package connective.teamup.download.actions;

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
public class AceDownloadAck implements Action
{
	private static final Logger LOGGER = Logger.getLogger(AceDownloadAck.class);
	/**
	 * Constructor for DownloadAck.
	 */
	public AceDownloadAck()
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
			AgentInfo agentInfo = serverInfo.getAgentInfo(req.getSession(), op);
			long downloadDate = System.currentTimeMillis();
			
			// find the batch in question
			int batchNum = Integer.parseInt(req.getParameter("batchnum"));
			AceDownloadService service = new AceDownloadService();
			int batchesRemaining = service.downloadSuccessful(op, agentInfo, batchNum, null);
			
			String msg = "Applied Edits downloaded successfully.";
			if (batchesRemaining > 0)
				msg += "\n\nNOTE - More edit files are available for download.";
			resp.getWriter().print(msg);
			resp.getWriter().flush();
		}
		catch (Exception e)
		{
			LOGGER.error("Error in ACE download acknowledgment", e);
			throw new ActionException("Error in ACE download acknowledgment", e);
		}
		
		return null;
	}

}
