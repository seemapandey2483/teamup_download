package connective.teamup.download.actions;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.services.ExportReportService;
import connective.teamup.download.services.LicenceService;

public class ReportExportList implements Action{

	public static Logger log =Logger.getLogger(ReportExportList.class);

	/**
	 * Constructor for TradingPartnerListView.
	 */
	public ReportExportList()
	{
		super();
	}

	/* (non-Javadoc)
	 * @see connective.teamup.download.Action#perform(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, connective.teamup.download.ServerInfo, connective.teamup.download.db.DatabaseOperation, org.apache.commons.fileupload.FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items)
		throws ActionException
	{

	
		
		String jsonArray ="";
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");

	
		Long fromMillSeconds = null;
		Long toMillSeconds = null;
		InputStream in = null;
		ServletOutputStream out = null;
	
		String method = req.getParameter("method");
		try {

			
			
		if ("exportDailyReport".equals(method)) {
		
				String fileName = req.getParameter("fileName");
				String fileDisplay = "";
				String exportfrmClaimFileDate = req.getParameter("exportfrmClaimFileDate");
				String exporttoClaimFileDate = req.getParameter("exporttoClaimFileDate");
				String exportStatus = req.getParameter("exportStatus");
				byte[] fileBytes = null;
				if(exportfrmClaimFileDate != null  && exportfrmClaimFileDate.length()>=10) {
					Date datefrom  = sdf.parse(exportfrmClaimFileDate +" 00:00:00");
					fromMillSeconds = datefrom.getTime();	
				}

				if(exporttoClaimFileDate != null && exporttoClaimFileDate.length()>=10) {
					if(exporttoClaimFileDate.equals(exportfrmClaimFileDate)){
						exporttoClaimFileDate = exporttoClaimFileDate +" 23:59:00";
					}else{
						exporttoClaimFileDate = exporttoClaimFileDate +" 00:00:00";
					}
					Date dateTo = sdf.parse(exporttoClaimFileDate);
					toMillSeconds = dateTo.getTime();
				}
				try{
					
				}catch(Exception e) {
					log.error(e);
				}
				
				if("P".equals(exportStatus)){
					fileDisplay = "dailyTransactionReport" +".pdf";
				}
				else if("X".equals(exportStatus)){
					fileDisplay = "dailyTransactionReport" +".xls";
				}
				else{
					fileDisplay = "dailyTransactionReport" +".csv";
				}
				resp.setContentType("application/download");
				resp.setHeader("Content-Disposition","attachment;filename="+fileDisplay);
				out = resp.getOutputStream();
				
				ExportReportService.isClaimAccessAllowed = LicenceService.isClaimActivated(op);
				
				if("P".equals(exportStatus)) {
					fileBytes =ExportReportService.exportReportToPdf(op, fromMillSeconds, toMillSeconds);

				}else if("X".equals(exportStatus) ){
					fileBytes =ExportReportService.exportReportToExcel(op, fromMillSeconds, toMillSeconds);

				}else if("C".equals(exportStatus)) {
					fileBytes =ExportReportService.exportReportToCsv(op, fromMillSeconds, toMillSeconds);
				}
				
					in =  new ByteArrayInputStream(fileBytes);
					byte[] buf = new byte[32000];
					int read; 
					while ((read = in.read(buf, 0, 32000)) != -1)
					{
						out.write(buf, 0, read);
					}
				
			}

		}catch(SQLException se) {
			log.error(se.getMessage());			
			throw new ActionException("Error retrieving exportDailyReport ", se);

		}catch(Exception se) {
			log.error(se.getMessage());
			throw new ActionException("Error retrieving exportDailyReport", se);
		}
		
		
		return jsonArray;
	}

	

}
