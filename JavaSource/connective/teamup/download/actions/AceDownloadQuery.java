package connective.teamup.download.actions;

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
import connective.teamup.download.services.AceDownloadService;
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
public class AceDownloadQuery implements Action
{
	private static final Logger LOGGER = Logger.getLogger(AceDownloadQuery.class);
	/**
	 * Constructor for DownloadQuery.
	 */
	public AceDownloadQuery()
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
			AceDownloadService service = new AceDownloadService();
			DownloadFileInfo[] files = service.getAppliedEditFiles(op, info.getAgentId(), dlStatus);
			
			// build the return string
			int fileCount = 0;
			int batchNum = 0;
			StringBuffer output = new StringBuffer("");
			output.append("\n");

			if (files != null && files.length > 0)
			{
				batchNum = files[0].getBatchNumber();
				for (int i=0; i < files.length; i++)
				{
					if (!files[i].isError() && files[i].getFileSize() > 0)
					{
						fileCount++;
						output.append(files[i].getFilename());	// includes path
						output.append(",");
						output.append(String.valueOf(files[i].getFileSize()));
						output.append(",");
						output.append(String.valueOf(files[i].getCreatedDate()));
						output.append(",");
						output.append(files[i].getOriginalFilename());	// filename only
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
										 " Download: Error retrieving AL3 distributed file transaction";
						String message = "Application:  ";
						if (serverInfo == null)
							message += "Agency Java Client App";
						else
							message += serverInfo.getAppName();
						message += "\nAgent ID:  " + info.getAgentId() +
								   "\nFilename:  " + files[i].getOriginalFilename() +
								   "\nFile import date:  " + df.format(new Date(files[i].getCreatedDate())) +
								   "\nError timestamp:  " + df.format(new Date(timestamp));
						EmailService.getInstance().sendTechSupportEmail(subject, message, exception);
						
						if (exception == null)
							exception = new Exception("Error occurred retrieving Applied Company Edits file");
						throw exception;
					}
				}
			}
			
			resp.getWriter().print(String.valueOf(batchNum));
			resp.getWriter().print(",");			
			resp.getWriter().print(String.valueOf(fileCount));
			resp.getWriter().print(output.toString());
			resp.getWriter().flush();
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
			throw new ActionException("Error retrieving Applied company edit files", e);
		}
		
		return null;
	}

}
