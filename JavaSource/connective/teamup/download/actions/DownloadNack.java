package connective.teamup.download.actions;

import java.text.SimpleDateFormat;
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
import connective.teamup.download.db.FileInfo;
import connective.teamup.download.services.DownloadService;
import connective.teamup.download.services.EmailService;
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
public class DownloadNack implements Action 
{
	private static final Logger LOGGER = Logger.getLogger(DownloadNack.class);
	
	protected SimpleDateFormat df = null;

	/**
	 * Constructor for DownloadNack.
	 */
	public DownloadNack() 
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
			
			// Retrieve the downloaded file info
			String filename = req.getParameter("filename");
			String msg = req.getParameter("error_msg");
			long createdDate = Long.parseLong(req.getParameter("filedate"));
			FileInfo fileInfo = op.getDownloadFile(agentInfo.getAgentId(), filename, createdDate);
			DownloadFileInfo file[] = new DownloadFileInfo[1];
			file[0] = ServiceHelper.getInstance().getDownloadFileInfo(fileInfo);
			file[0].setError(true);
			file[0].setErrorMsg(msg);
			
			// Report download error
			DownloadService service = new DownloadService();
			service.downloadAcknowledge(op, agentInfo, file);
			
			// send email
			CarrierInfo carrier = serverInfo.getCarrierInfo();
			if (carrier.isNotifyOnDownloadError())
			{
				String to = carrier.getErrorsEmail();
				
				String subject = carrier.getShortName() + " Download Error";
				
				StringBuffer body = new StringBuffer("TEAM-UP Download trading partner ");
				body.append(agentInfo.getAgentId());
				body.append(" (");
				body.append(agentInfo.getName());
				body.append(") received an error during the download process.\n\n");
				body.append("Application:  ");
				if (serverInfo == null)
					body.append("Agency Java Client App");
				else
					body.append(serverInfo.getAppName());
				body.append("\nFile name:  ");
				body.append(fileInfo.getFilename());
				body.append("\n");
				if (!fileInfo.getFilename().equals(fileInfo.getOriginalFilename()))
				{
					body.append("Original file name:  ");
					body.append(fileInfo.getOriginalFilename());
					body.append("\n");
				}
				body.append("File created:  ");
				body.append(df.format(new Date(fileInfo.getCreatedDate())));
				body.append("\n");
				body.append("File imported:  ");
				body.append(df.format(new Date(fileInfo.getImportedDate())));
				body.append("\n\n");
				
				msg = req.getParameter("error_msg");
				if (msg != null)
				{
					body.append("ERROR MESSAGE:\n\n");
					body.append(msg);
				}
				
				EmailService.getInstance().sendEMail(to, subject, body.toString());
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error in download NACK", e);
			throw new ActionException("Error in download NACK", e);
		}
		
		return null;
	}

}
