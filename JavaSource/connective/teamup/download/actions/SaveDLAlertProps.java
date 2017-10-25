package connective.teamup.download.actions;

import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;

import com.ebix.quartz.job.QuartzTestJOB;
import com.ebix.quartz.job.RegisterSchedJobs;

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
public class SaveDLAlertProps implements Action
{
	private static final Logger LOGGER = Logger.getLogger(SaveDLAlertProps.class);

	public static final String DOWNLOAD_ALERT_JOB_ID ="DJOB0001";
	public static final String DOWNLOAD_ALERT_JOB_NAME ="DOWNLOAD_ALERT";
	public static final String DOWNLOAD_ALERT_JOB_GROUP_NAME ="DOWNLOAD_REPORT";
	public static final String DOWNLOAD_ALERT_JOB_DESC ="Scheduled Task to Run Download Alert";
	public static final String DOWNLOAD_ALERT_TRIG_NAME ="DOWNLOAD_ALERT_TRIG";
	public static final Class DOWNLOAD_JOB_CLASS_NAME =QuartzTestJOB.class;
	/**
	 * Constructor for SaveDLAlertProps.
	 */
	public SaveDLAlertProps()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.Action#perform(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException 
	{
		boolean invalidTestfile = false;
		String nextPage = null;

		try
		{
			Hashtable props = new Hashtable();
			
			// Parse the configuration settings from the request
			
			String enabledFlag = req.getParameter("flag_enable");
			
			props.put(DatabaseFactory.PROP_DL_ALERT_FLAG, enabledFlag);
			if (enabledFlag != null && enabledFlag.equals("Y"))
			{
				String carrierFlag = req.getParameter("carrier_enable");
				String dlhour = req.getParameter("dlhour");
				String dlminute = req.getParameter("dlminute");
				String dlampm = req.getParameter("dlampm");
				props.put(DatabaseFactory.PROP_DL_SCHED_HOUR, dlhour);
				props.put(DatabaseFactory.PROP_DL_SCHED_MINUTE, dlminute);
				props.put(DatabaseFactory.PROP_DL_SCHED_AMPM, dlampm);
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
				if (carrierFlag == null || !carrierFlag.equals("Y"))
					carrierFlag = "N";
				
				props.put(DatabaseFactory.PROP_DL_ALERT_CARRIER_FLAG, carrierFlag);
				String schedDays = req.getParameter("inactive_sched_days");				
				props.put(DatabaseFactory.PROP_DL_FAILED_DAYS, schedDays);
				String nonschedDays = req.getParameter("inactive_nonsched_days");				
				props.put(DatabaseFactory.PROP_DL_STALE_DAYS, nonschedDays);
				
				cornStr="0 " +schedMinute + " " +schedHour+" * * ?";
				JobDetail jd = new JobDetail(DOWNLOAD_ALERT_JOB_NAME,DOWNLOAD_ALERT_JOB_GROUP_NAME,DOWNLOAD_JOB_CLASS_NAME);
				String url = serverInfo.getDownLoadAlertURL();
				if(url== null || "".equals(url)) {
					url = serverInfo.getRequestUrl(req, "/import");
					url += "?action=download_report";
				}
				 
				jd.getJobDataMap().put("jobURL", url);
				CronTrigger ct = new CronTrigger(DOWNLOAD_ALERT_TRIG_NAME,DOWNLOAD_ALERT_JOB_GROUP_NAME,cornStr);
				
				
				SchedJob job = new SchedJob();
				job.setActive("Y");
				job.setJobID(DOWNLOAD_ALERT_JOB_ID);
				job.setJobName(DOWNLOAD_ALERT_JOB_NAME);
				job.setJobDescription(DOWNLOAD_ALERT_JOB_DESC);
				job.setJobGroupName(DOWNLOAD_ALERT_JOB_GROUP_NAME);
				job.setSchedTime(jobTime);
				job.setTriggerName(DOWNLOAD_ALERT_TRIG_NAME);
				job.setTriggerStr(cornStr);
				job.setJobClass(DOWNLOAD_JOB_CLASS_NAME.getName());
				job.setProp1("jobURL");
				job.setValue1(url);
				RegisterSchedJobs.registerJob(jd, ct, DOWNLOAD_ALERT_JOB_ID,op,job);
				
			}
			else
			{
				props.put(DatabaseFactory.PROP_DL_ALERT_CARRIER_FLAG, "N");
				SchedJob job = op.getJobById(DOWNLOAD_ALERT_JOB_ID);
				if(job != null) {
					op.updateJobStatus(DOWNLOAD_ALERT_JOB_ID, "N");
					RegisterSchedJobs.removeJob(DOWNLOAD_ALERT_JOB_ID);
				}
			}
			
/*			String schedFile = req.getParameter("inactive_sched_file");
			if (schedFile == null)
				schedFile = "";
			else if (schedFile.indexOf("\\") < 0 && schedFile.indexOf("/") < 0)
			{
				// Assume file is in the default directory
				schedFile = serverInfo.getConfigDir() + schedFile;
			}
			props.put(ServerInfo.PROP_CUSTOM_DL_FAILED, schedFile);

			String nonschedFile = req.getParameter("inactive_nonsched_file");
			if (nonschedFile == null)
				nonschedFile = "";
			else if (nonschedFile.indexOf("\\") < 0 && nonschedFile.indexOf("/") < 0)
			{
				// Assume file is in the default directory
				nonschedFile = serverInfo.getConfigDir() + nonschedFile;
			}
			props.put(ServerInfo.PROP_CUSTOM_DL_STALE, nonschedFile);
*/			
			// save the properties
			op.setProperties(props);

			String configWizard = req.getParameter("config_wizard");
			if (configWizard != null && configWizard.equals("Y"))
			{
				// save the properties
				
				// load the current params
/*				String configPath = serverInfo.getConfigDir() + "config.properties";
				PropertyResourceBundle sysprops = new PropertyResourceBundle(new FileInputStream(configPath));
	
				// read current property values	
				Hashtable propMap = new Hashtable();
				Enumeration keys = sysprops.getKeys();
				while (keys.hasMoreElements())
				{
					String key = (String) keys.nextElement();
					propMap.put(key, sysprops.getString(key));
				}
				
				// update the configured flag
				propMap.put("configured", "true");
		
				// write out the values again
				PrintWriter out = new PrintWriter(new BufferedOutputStream(new FileOutputStream(configPath)));
				keys = propMap.keys();
				while (keys.hasMoreElements())
				{
					String key = (String) keys.nextElement();
					out.println(key + "=" + propMap.get(key));
				}
				out.flush();
				out.close();			
*/	
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
			LOGGER.error("Error saving Download Alert! settings", e);
			throw new ActionException("Error saving Download Alert! settings", e);
		}
				
		return nextPage;
	}
}
