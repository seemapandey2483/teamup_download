package connective.teamup.download.pages;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.DisplayBean;
import connective.teamup.download.DisplayBeanException;
import connective.teamup.download.GenericPage;
import connective.teamup.download.PageException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.beans.TransLogDisplayBean;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.LogInfo;
import connective.teamup.download.services.ServiceHelper;
import connective.teamup.download.ws.objects.DownloadFileInfo;

/**
 * @author Kyle McCreary
 *
 * Overrides the generic page bean to build the Agency Transaction Log for the last download only.
 */
public class TransLogLastDownloadPage extends GenericPage
{
	private static final Logger LOGGER = Logger.getLogger(TransLogLastDownloadPage.class);
	/**
	 * @see connective.teamup.download.GenericPage#createDisplayBean(HttpServletRequest, HttpServletResponse, ServerInfo)
	 */
	protected DisplayBean createDisplayBean(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws PageException
	{
		// Create the display bean
		TransLogDisplayBean bean = new TransLogDisplayBean();
		
		try
		{
			bean.init(req, resp, serverInfo, op, items);
			
			// Parse the date range parameters from the request
			String agentId = req.getParameter("agentId");
			String sortOrder = req.getParameter("sortOrder");
			if (sortOrder == null || sortOrder.equals(""))
				sortOrder = "";
			bean.setSortOrder(sortOrder);
			
			// Get the transaction log entries for the agent's last download
			LogInfo[] logs = op.getLogsLastDownload(agentId);
			if (logs != null)
			{
				ArrayList fileList = new ArrayList();
				for (int i=0; i < logs.length; i++)
				{
					if (i == 0)
					{
						String dlDate = bean.formatDate(logs[i].getImported_date());
						bean.setDateRange(dlDate);
					}
					
					logs[i].loadTextData();
					if (logs[i].getTextData() != null)
					{
						try
						{
							ByteArrayInputStream bis = new ByteArrayInputStream(logs[i].getTextData().getBytes());
							DownloadFileInfo[] files = ServiceHelper.getInstance().getFilesFromXML(bis);
							if (files != null)
							{
								for (int j=0; j < files.length; j++)
								{
									// Fix the file downloaded date to match the DL log entry -- 11/23/2005, kwm
									files[j].setLastDownloadedDate(logs[i].getLog_date());
								
									String partCode = files[j].getParticipantCode();
									if (partCode != null && !partCode.equals(agentId))
										bean.setParticipantUsed(true);
									fileList.add(files[j]);
								}
							}
						}
						catch (Exception e)
						{
							LOGGER.error(e);
							// Error occurred retrieving log detail data from blob
						}
					}
				}
				
				DownloadFileInfo[] files = new DownloadFileInfo[fileList.size()];
				fileList.toArray(files);
				bean.setFiles(files);
			}
		}
		catch (DisplayBeanException e)
		{
			LOGGER.error(e);
			throw new PageException(e);
		}
		catch (Exception e)
		{
			LOGGER.error("Error occurred building transaction log", e);
			throw new PageException("Error occurred building transaction log", e);
		}
		
		return bean;
	}

}
