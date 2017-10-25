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
import connective.teamup.download.db.FileInfo;
import connective.teamup.download.services.DownloadService;
import connective.teamup.download.services.ServiceHelper;
import connective.teamup.download.ws.objects.DownloadFileInfo;

/**
 * @author haneym
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class DownloadAck implements Action
{
	private static final Logger LOGGER = Logger.getLogger(DownloadAck.class);
	/**
	 * Constructor for DownloadAck.
	 */
	public DownloadAck()
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
			
			// Retrieve the downloaded file info
			String filename = req.getParameter("filename");
			long createdDate = Long.parseLong(req.getParameter("filedate"));
			FileInfo fileInfo = op.getDownloadFile(agentInfo.getAgentId(), filename, createdDate);
			DownloadFileInfo file[] = new DownloadFileInfo[1];
			file[0] = ServiceHelper.getInstance().getDownloadFileInfo(fileInfo);
			
			// Acknowledge successful file download
			DownloadService service = new DownloadService();
			service.downloadAcknowledge(op, agentInfo, file);
		}
		catch (Exception e)
		{
			LOGGER.error("Error in download acknowledgment", e);
			throw new ActionException("Error in download acknowledgment", e);
		}
		
		return null;
	}

}
