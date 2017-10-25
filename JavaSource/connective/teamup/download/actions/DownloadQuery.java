package connective.teamup.download.actions;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.CarrierInfo;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.services.DownloadService;
import connective.teamup.download.services.EmailService;
import connective.teamup.download.ws.objects.DownloadFileInfo;

/**
 * @author haneym
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class DownloadQuery implements Action
{
	private static final Logger LOGGER = Logger.getLogger(DownloadQuery.class);
	/**
	 * Constructor for DownloadQuery.
	 */
	public DownloadQuery()
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
			
			// get a list of files to download
			String dlStatus = req.getParameter("file_status");
			DownloadService service = new DownloadService();
			DownloadFileInfo[] files = service.getDownloadFiles(op, info.getAgentId(), dlStatus);
			
			// build the return string
			int fileCount = 0;
			StringBuffer output = new StringBuffer("");
			output.append("\n");
			if (files != null)
			{
				for (int i=0; i < files.length; i++)
				{
					if (!files[i].isError() && files[i].getFileSize() > 0)
					{
						fileCount++;
						output.append(files[i].getFilename());
						output.append(",");
						output.append(String.valueOf(files[i].getFileSize()));
						output.append(",");
						output.append(String.valueOf(files[i].getCreatedDate()));
						output.append(",");
						output.append(files[i].getOriginalFilename());
						output.append("\n");
					}
					else
					{
						Exception exception = null;
						if (files[i].isError())
							exception = new Exception(files[i].getErrorMsg());
						
						// Send a tech support email
						DateFormat df = DateFormat.getDateTimeInstance();
						long timestamp = System.currentTimeMillis();
						String subject = CarrierInfo.getInstance().getShortName() + 
										 " Download: Error retrieving AL3 file transaction";
						String message = "Application:  ";
						if (serverInfo == null)
							message += "Agency Java Client App";
						else
							message += serverInfo.getAppName();
						message += "\nAgent ID:  " + info.getAgentId() +
								   "\nFilename:  " + files[i].getOriginalFilename() +
								   "\nFile creation date:  " + df.format(new Date(files[i].getCreatedDate())) +
								   "\nError timestamp:  " + df.format(new Date(timestamp));
						EmailService.getInstance().sendTechSupportEmail(subject, message, exception);
						
						if (exception == null)
							exception = new Exception("Error occurred retrieving AL3 file transaction");
						throw new ActionException(null, exception);
					}
				}
			}
						
			resp.getWriter().print(String.valueOf(fileCount));
			resp.getWriter().print(output.toString());
			resp.getWriter().flush();
			
			
//			FileInfo[] files = op.getAgentFilesByStatus(info.getAgentId(), status);
//			
//			// build the return string
//			int fileCount = 0;
//			//StringBuffer output = new StringBuffer(String.valueOf(files.length));
//			StringBuffer output = new StringBuffer("");
//			output.append("\n");
//			for (int i=0; i < files.length; i++)
//			{
//				int fileLength = -1;
//				Exception exception = null;
//				try
//				{
//					byte[] fileBytes = files[i].getFileContents();
//					fileLength = fileBytes.length;
//				}
//				catch (Exception e)
//				{
//					// Send a tech support email
//					long timestamp = System.currentTimeMillis();
//					String subject = "Error occurred retrieving AL3 file transaction";
//					String message = "Agent ID:  " + files[i].getAgentId() +
//								   "\nFilename:  " + files[i].getOriginalFilename() +
//								   "\nFile creation date:  " + String.valueOf(files[i].getCreatedDate()) +
//								   "\nError timestamp:  " + String.valueOf(timestamp);
//					serverInfo.sendTechSupportEmail(subject, message, e);
//					
//					// Throw the exception
//					throw e;
//				}
//
//				fileCount++;
//				output.append(files[i].getFilename());
//				output.append(",");
//				output.append(String.valueOf(fileLength));
//				output.append(",");
//				output.append(String.valueOf(files[i].getCreatedDate()));
//				output.append(",");
//				output.append(files[i].getOriginalFilename());
//				output.append("\n");
//			}
//			
//			resp.getWriter().print(String.valueOf(fileCount));
//			resp.getWriter().print(output.toString());
//			resp.getWriter().flush();
		}
		catch (ActionException ae)
		{
			LOGGER.error(ae);
			throw ae;
		}
		catch (IOException ie)
		{
			LOGGER.error("Error sending download data", ie);
			throw new ActionException("Error sending download data", ie);
		}
		catch (Exception e)
		{
			LOGGER.error("Error retrieving agent info", e);
			throw new ActionException("Error retrieving agent info", e);
		}
		
		return null;
	}
}
