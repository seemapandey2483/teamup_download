package connective.teamup.download.pages;

import java.io.ByteArrayInputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

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
import connective.teamup.download.db.DatabaseObject;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.LogInfo;
import connective.teamup.download.services.ServiceHelper;
import connective.teamup.download.ws.objects.DownloadFileInfo;
import connective.teamup.download.ws.objects.DownloadTransactionInfo;

/**
 * @author Kyle McCreary
 *
 * Overrides the generic page bean to build the Agency Transaction Log with
 * parameters to only show agent download transactions.
 */
public class TransLogByDateRangePage extends GenericPage
{
	private static final Logger LOGGER = Logger.getLogger(TransLogByDateRangePage.class);
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
				sortOrder = "ASC";
			bean.setSortOrder(sortOrder);
			if (sortOrder == "ASC")
				sortOrder = DatabaseObject.colLogDate;
			else
				sortOrder = DatabaseObject.colLogDate + " " + sortOrder;
			
			String startDate = req.getParameter("startdt");
			bean.setStartDate(startDate);
			String startDateFormatted = startDate.substring(5, 7) + "/" + startDate.substring(8) + "/" + startDate.substring(0, 4);
			int startYear = Integer.parseInt(startDate.substring(0, 4));
			int startMonth =  Integer.parseInt(startDate.substring(5, 7));
			int startDay = Integer.parseInt(startDate.substring(8)); 
			GregorianCalendar calStart = new GregorianCalendar(startYear, startMonth - 1, startDay, 0, 0, 0);
			
			String endDate = req.getParameter("enddt");
			bean.setEndDate(endDate);
			String endDateFormatted = endDate.substring(5, 7) + "/" + endDate.substring(8) + "/" + endDate.substring(0, 4);
			int endYear = Integer.parseInt(endDate.substring(0, 4));
			int endMonth =  Integer.parseInt(endDate.substring(5, 7));
			int endDay = Integer.parseInt(endDate.substring(8)); 
			GregorianCalendar calEnd = new GregorianCalendar(endYear, endMonth - 1, endDay, 23, 59, 59);
			
			bean.setDateRange(startDateFormatted + " - " + endDateFormatted);
			
			// Get the transaction log entries for the specified date range
			LogInfo[] logs = op.getLogsByEventType(agentId, calStart.getTime().getTime(), 
												   calEnd.getTime().getTime(), 
												   sortOrder, "D");
			if (logs != null)
			{
				ArrayList fileList = new ArrayList();
				for (int i=0; i < logs.length; i++)
				{
					try
					{
						logs[i].loadTextData();
						ByteArrayInputStream bis = new ByteArrayInputStream(logs[i].getTextData().getBytes());
						DownloadFileInfo[] files = ServiceHelper.getInstance().getFilesFromXML(bis);
						if (files != null)
						{
							for (int j=0; j < files.length; j++)
							{
								// Fix the file downloaded date to match the DL log entry -- 11/23/2005, kwm
								files[j].setLastDownloadedDate(logs[i].getLog_date());
								if(isClaimFile(files[j])){
									loadClaimNumberinFile(files[j],agentId,op);
								}
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
						// Error occurred retrieving file detail data
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
	private boolean isClaimFile(DownloadFileInfo file) {
		boolean claimFile = false;
		
		if("E".equals(file.getStatus()) 
				|| "P".equals(file.getStatus())
				|| "Q".equals(file.getStatus())) {
			claimFile = true;
		}
		return claimFile;
	}
	private void loadClaimNumberinFile(DownloadFileInfo file, String agentId, DatabaseOperation op) throws SQLException{
		DownloadTransactionInfo [] trans =  file.getTransactions();
		for(int i=0;i<trans.length;i++) {
			trans[i].setPolicyNumber(op.getClaimNumber(file.getOriginalFilename(),agentId, file.getCreatedDate(),trans[i].getSequence()));
		}
	}

}
