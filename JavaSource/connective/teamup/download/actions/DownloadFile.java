package connective.teamup.download.actions;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.al3.AcordFactory;
import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.services.DownloadService;
import connective.teamup.download.ws.objects.DownloadFileInfoInternal;

/**
 * @author haneym
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class DownloadFile implements Action
{
	private static final Logger LOGGER = Logger.getLogger(DownloadFile.class);
	
	protected AcordFactory factory = null;


	/**
	 * Constructor for DownloadFile.
	 */
	public DownloadFile()
	{
		super();

		// instantiate the ACORD factory and load the group definitions
		try
		{
			factory = new AcordFactory();
			InputStream instr = getClass().getResourceAsStream("/al3.xml");
			factory.loadGroupDefinitions(instr);
		}
		catch (Exception e)
		{
			LOGGER.error(e);
			System.out.println(e.getMessage());
		}
	}

	/**
	 * @see connective.teamup.download.Action#perform(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException 
	{
		try
		{
			AgentInfo info = serverInfo.getAgentInfo(req.getSession(), op);
			String filename = req.getParameter("filename");
			long date = Long.parseLong(req.getParameter("filedate"));
			
			DownloadService download = new DownloadService();
			DownloadFileInfoInternal file = download.getDownloadFile(op, info.getAgentId(), filename, date);
			if (file.isError() || file.getFileSize() <= 0)
				throw new Exception(file.getErrorMsg());
			
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
			LOGGER.error("Error retrieving download file", e);
			throw new ActionException("Error retrieving download file", e);
		}
		
		return null;
	}

}
