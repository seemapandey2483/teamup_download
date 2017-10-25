package connective.teamup.download.actions;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;

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
import connective.teamup.download.ws.objects.DownloadFileInfoInternal;

/**
 * @author haneym
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class AceDownloadFile implements Action
{
	private static final Logger LOGGER = Logger.getLogger(AceDownloadFile.class);
	/**
	 * Constructor for DownloadFile.
	 */
	public AceDownloadFile()
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
			AgentInfo info = serverInfo.getAgentInfo(req.getSession(), op);
			int batchNumber = Integer.parseInt(req.getParameter("batchnum"));
			String filename = req.getParameter("filename");
			long date = Long.parseLong(req.getParameter("filedate"));
			
			AceDownloadService service = new AceDownloadService();
			DownloadFileInfoInternal file = service.getAppliedEditFile(op, info.getAgentId(), batchNumber, filename, date);
			byte[] fileBytes = file.getFileContents();
			
			byte[] buf = new byte[256];
			int read; 
	
			// download the file		
			resp.setContentLength(fileBytes.length);
			resp.setContentType("text/plain");		
			OutputStream os = resp.getOutputStream();
			ByteArrayInputStream is = new ByteArrayInputStream(fileBytes);
			while ((read = is.read(buf, 0, 256)) != -1)
				os.write(buf, 0, read);
				
			os.flush();
			os.close();			
		}
		catch (Exception e)
		{
			LOGGER.error("Error retrieving Applied company edit file", e);
			throw new ActionException("Error retrieving Applied company edit file", e);
		}
		
		return null;
	}
	
}
