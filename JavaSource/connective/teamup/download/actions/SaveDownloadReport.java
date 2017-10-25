package connective.teamup.download.actions;

import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;

import com.ebix.quartz.job.RegisterSchedJobs;
import com.ebix.quartz.job.ScheduleDailyReport;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.SchedJob;

/**
 * @author Kyle McCreary
 *
 * Parses the Download Alert! properties from the configuration wizard page and saves to the database.
 */
public class SaveDownloadReport implements Action
{
	private static final Logger LOGGER = Logger.getLogger(SaveDownloadReport.class);
	
	public static final String DOWNLOAD_REPORT_JOB_ID ="DJOB0002";
	public static final String DOWNLOAD_REPORT_JOB_NAME ="DOWNLOAD_REPORT";
	public static final String DOWNLOAD_REPORT_JOB_GROUP_NAME ="DOWNLOAD_REPORT";
	public static final String DOWNLOAD_REPORT_JOB_DESC ="Scheduled Task to Run Download Report";
	public static final String DOWNLOAD_REPORT_TRIG_NAME ="DOWNLOAD_REPORT_TRIG";
	public static final Class DOWNLOAD_REPORT_JOB_CLASS_NAME =ScheduleDailyReport.class;
	/**
	 * Constructor for SaveDLAlertProps.
	 */
	public SaveDownloadReport()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.Action#perform(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException 
	{
		String nextPage = null;

		try
		{
			Hashtable props = new Hashtable();
			
			// Parse the configuration settings from the request
			
			String enabledFlag = req.getParameter("flag_enable");
			
			props.put(DatabaseFactory.PROP_DOWNLOAD_REPORT_CARRIER_FLAG, enabledFlag);
			if (enabledFlag != null && enabledFlag.equals("Y"))
			{
				String dlhour = req.getParameter("dlhour");
				String dlminute = req.getParameter("dlminute");
				String dlampm = req.getParameter("dlampm");
				String dlActivityType = req.getParameter("dlActivityType");
				String dlFileType = req.getParameter("dlFileType");
				String dlAttachType = req.getParameter("dlAttachType");
				String detailReportFlag = req.getParameter("flag_sendDetail");
				
				props.put(DatabaseFactory.PROP_DOWNLOAD_REPORT_SCHED_HOUR, dlhour);
				props.put(DatabaseFactory.PROP_DOWNLOAD_REPORT_SCHED_MINUTE, dlminute);
				props.put(DatabaseFactory.PROP_DOWNLOAD_REPORT_SCHED_AMPM, dlampm);
				props.put(DatabaseFactory.PROP_DOWNLOAD_REPORT_ACTTYPE, dlActivityType);
				props.put(DatabaseFactory.PROP_DOWNLOAD_REPORT_FILETYPE, dlFileType);
				props.put(DatabaseFactory.PROP_DOWNLOAD_REPORT_ATTACH_TYPE, dlAttachType);
				props.put(DatabaseFactory.PROP_DOWNLOAD_REPORT_DETAIL_FLAG, detailReportFlag);
				
				int schedHour = Integer.parseInt(dlhour);
				
				if (dlampm.equalsIgnoreCase("12"))
					schedHour += 12;
				if (schedHour > 23 && dlampm.equalsIgnoreCase("12")){
					schedHour = 12;
				}else if(schedHour > 23 && dlampm.equalsIgnoreCase("0")){
					schedHour = 0;
				}
					
				int schedMinute = Integer.parseInt(dlminute);
				
				String cornStr ="";
				String jobTime = schedHour +":" +schedMinute +":00";
			
				
			
				
				
				cornStr="0 " +schedMinute + " " +schedHour+" * * ?";
				JobDetail jd = new JobDetail(DOWNLOAD_REPORT_JOB_NAME,DOWNLOAD_REPORT_JOB_GROUP_NAME,DOWNLOAD_REPORT_JOB_CLASS_NAME);
				String to = serverInfo.getCarrierInfo().getMiscreportsEmail();
				jd.getJobDataMap().put("email", to);
				jd.getJobDataMap().put("dlActivityType", dlActivityType);
				jd.getJobDataMap().put("dlFileType", dlFileType);
				jd.getJobDataMap().put("dlAttachType", dlAttachType);
				jd.getJobDataMap().put("dtllReptFlag", detailReportFlag);
				CronTrigger ct = new CronTrigger(DOWNLOAD_REPORT_TRIG_NAME,DOWNLOAD_REPORT_JOB_GROUP_NAME,cornStr);
				
				
				SchedJob job = new SchedJob();
				job.setActive("Y");
				job.setJobID(DOWNLOAD_REPORT_JOB_ID);
				job.setJobName(DOWNLOAD_REPORT_JOB_NAME);
				job.setJobDescription(DOWNLOAD_REPORT_JOB_DESC);
				job.setJobGroupName(DOWNLOAD_REPORT_JOB_GROUP_NAME);
				job.setSchedTime(jobTime);
				job.setTriggerName(DOWNLOAD_REPORT_TRIG_NAME);
				job.setTriggerStr(cornStr);
				job.setJobClass(DOWNLOAD_REPORT_JOB_CLASS_NAME.getName());
				job.setProp1("email");
				job.setValue1(to);
				
				job.setProp2("dlActivityType");
				job.setValue2(dlActivityType);
				
				job.setProp3("dlFileType");
				job.setValue3(dlFileType);
				
				job.setProp4("dlAttachType");
				job.setValue4(dlAttachType);
				
				job.setProp5("dtllReptFlag");
				job.setValue5(detailReportFlag);
				
				RegisterSchedJobs.registerJob(jd, ct, DOWNLOAD_REPORT_JOB_ID,op,job);
				
			}
			else
			{
				props.put(DatabaseFactory.PROP_DOWNLOAD_REPORT_CARRIER_FLAG, "N");
				SchedJob job = op.getJobById(DOWNLOAD_REPORT_JOB_ID);
				if(job != null) {
					op.updateJobStatus(DOWNLOAD_REPORT_JOB_ID, "N");
					RegisterSchedJobs.removeJob(DOWNLOAD_REPORT_JOB_ID);
				}
			}
			// save the properties
			op.setProperties(props);

			String configWizard = req.getParameter("config_wizard");
			if (configWizard != null && configWizard.equals("Y"))
			{
	

				// Navigate to the next page of the configuration wizard
				nextPage = "config_end";
			}
			else
			{
				// Navigate back to the splash screen
				nextPage = "splash";
				serverInfo.setStatusMessage(req.getSession(), "Advanced option configuration changes saved successfully");
			}

		}
		catch (Exception e)
		{
			LOGGER.error("Error saving Download Report! settings", e);
			throw new ActionException("Error saving Download Report! settings", e);
		}
				
		return nextPage;
	}
}
