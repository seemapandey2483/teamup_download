package com.ebix.quartz.job;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.ebix.utility.date.DateUtility;

import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.services.ExportFileDailyReportService;
import connective.teamup.download.services.ExportReportService;

public class ScheduleDailyReport implements Job {
	Logger log =Logger.getLogger(ScheduleDailyReport.class);
	@Override
	public void execute(JobExecutionContext jeContext) throws JobExecutionException {
		// TODO Auto-generated method stub
		DatabaseOperation op = null;
		 
		
		try{
			log.info("Daily Report Email Job");
			System.out.println("Daily Report Email Job");
			op = DatabaseFactory.getInstance().startOperation();
			Long [] dates = DateUtility.getDateRange("D");
			 Calendar cal = Calendar.getInstance();
			 int hour = cal.get(Calendar.HOUR);
			 int ampm = cal.get(Calendar.AM_PM);
			JobDataMap jdMap = jeContext.getJobDetail().getJobDataMap();
			String email = jdMap.get("email").toString();
			String dlActivityType = jdMap.get("dlActivityType").toString();
			String dlFileType = jdMap.get("dlFileType").toString();
			String dlAttachType = jdMap.get("dlAttachType").toString();
			String detailReportFlag = jdMap.get("dtllReptFlag").toString();
			
			if("P".equals(dlAttachType) ){
				if("Y".equals(detailReportFlag)){
					ExportFileDailyReportService.exportReportToPdfAndSendEmail(op,dates[0],dates[1],email,dlActivityType,dlFileType);
					ExportReportService.exportReportToPdfAndSendEmail(op,dates[0],dates[1],email,dlActivityType,dlFileType);
				}else {
					ExportFileDailyReportService.exportReportToPdfAndSendEmail(op,dates[0],dates[1],email,dlActivityType,dlFileType);
				}
			}
				
			else if("X".equals(dlAttachType)) {
				if("Y".equals(detailReportFlag)){
					ExportFileDailyReportService.exportReportToExcelAndSendEmail(op,dates[0],dates[1],email,dlActivityType,dlFileType);
					ExportReportService.exportReportToExcelAndSendEmail(op,dates[0],dates[1],email,dlActivityType,dlFileType);
				}else {
					ExportFileDailyReportService.exportReportToExcelAndSendEmail(op,dates[0],dates[1],email,dlActivityType,dlFileType);
				}
			}
			else if("C".equals(dlAttachType)) {
				if("Y".equals(detailReportFlag)){
					ExportFileDailyReportService.exportReportToCSVAndSendEmail(op,dates[0],dates[1],email,dlActivityType,dlFileType);
					ExportReportService.exportReportToCSVAndSendEmail(op,dates[0],dates[1],email,dlActivityType,dlFileType);
				}else {
					ExportFileDailyReportService.exportReportToCSVAndSendEmail(op,dates[0],dates[1],email,dlActivityType,dlFileType);
				}
			}
			
		}catch(JobExecutionException je){
			log.error("Daily Report Email Job" +je.getMessage());
			System.out.println("Daily Report Email Job"  +je.getMessage());
			throw je;
		}
		catch(Exception e){
			log.error("Daily Report Email Job" +e.getMessage());
			System.out.println("Daily Report Email Job"  +e.getMessage());
		}finally {
			op.close();
		}
	}
	

	
}
