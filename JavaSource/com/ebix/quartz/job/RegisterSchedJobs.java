package com.ebix.quartz.job;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.SchedJob;

public class RegisterSchedJobs {
	
	private static final Logger LOGGER = Logger.getLogger(RegisterSchedJobs.class);

	private  static Scheduler quartzScheduler = null;
	private static Map<String,JobDetail> jobs = new HashMap<String,JobDetail> (); 
	
	private static String UPDATE_AMS_JOBID = "JOBAMS01";
	private static String UPDATE_AMS_JOBNAME = "UPDATE_AMSINFO";
	private static String UPDATE_AMS_JOB_TRIGNAME = "UPDATE_AMSINFO_TRIG";
	private static String UPDATE_AMS_JOB_GROUP = "AMSGRP";
	private static String UPDATE_AMS_CORN_STR = "0 30 22 ? * SUN";
	//private static String UPDATE_AMS_CORN_STR = "0 0/2 * * * ?";
	private static Class UPDATE_AMS_JOB_CLASS = UpdateAmsInfoToRegJob.class;
	
	static {
		try {
			if(quartzScheduler == null) 
				quartzScheduler = StdSchedulerFactory.getDefaultScheduler();
		} catch (SchedulerException e) {
			LOGGER.error(e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static Scheduler getQuartzScheduler() {
		return quartzScheduler;
	}

	public static boolean registerJob(JobDetail jd,CronTrigger ct,String jobID,DatabaseOperation op,SchedJob job) throws SchedulerException, SQLException  {
		if(jobs != null){
			if(jobs.get(jobID)!= null) {
				quartzScheduler.deleteJob(jd.getName(), jd.getGroup());
			}
		}
		quartzScheduler.scheduleJob(jd,ct);
		jobs.put(jobID, jd);
		op.deleteJob(jobID);
		op.addJob(job);
	
		return true;
	}
	public static boolean loadAndStartJob(DatabaseOperation op ) throws SQLException, ClassNotFoundException, ParseException, SchedulerException {
		List<SchedJob> jobList = null;
		jobList = op.getJobList();
		for(SchedJob job:jobList) {
			if(jobs.get(job.getJobID())== null && ("Y".equals(job.getActive()))) {
				Class jobClass = Class.forName(job.getJobClass());
				JobDetail jd = new JobDetail(job.getJobName(),job.getJobGroupName(),jobClass);
				CronTrigger ct = new CronTrigger(job.getTriggerName(),job.getJobGroupName(),job.getTriggerStr());
				
				if(job.getProp1() != null){
					jd.getJobDataMap().put(job.getProp1(), job.getValue1());
				}
				quartzScheduler.scheduleJob(jd,ct);
				jobs.put(job.getJobID(), jd);
			}
		}
		
		return true;
		
	}
	
	public static boolean loadBackEndJob(DatabaseOperation op, String regurl, String carrierId, String active ) throws SQLException, ClassNotFoundException, ParseException, SchedulerException {
		
		if(jobs.get(UPDATE_AMS_JOBID)== null && ("Y".equals(active))) {
			JobDetail jd = new JobDetail(UPDATE_AMS_JOBNAME,UPDATE_AMS_JOB_GROUP,UPDATE_AMS_JOB_CLASS);
			CronTrigger ct = new CronTrigger(UPDATE_AMS_JOB_TRIGNAME,UPDATE_AMS_JOB_GROUP,UPDATE_AMS_CORN_STR);
			jd.getJobDataMap().put("regurl", regurl);
			jd.getJobDataMap().put("carrierId", carrierId);
			quartzScheduler.scheduleJob(jd,ct);
			jobs.put(UPDATE_AMS_JOBID, jd);
			
		}
		return true;

	}
	public static void removeJob(String jobId) throws Exception {
		if(jobs != null){
			if(jobs.get(jobId)!= null) {
				JobDetail jd = jobs.get(jobId);
				quartzScheduler.deleteJob(jd.getName(), jd.getGroup());
				jobs.remove(jobId);
			}
		}
	}
}
