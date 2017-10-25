package com.ebix.quartz.job;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class QuartzTestJOB implements Job{
	Logger log =Logger.getLogger(QuartzTestJOB.class);
	public void execute(JobExecutionContext jeContext) throws JobExecutionException {
		log.info("Download Alert job");
		try {
			HttpURLConnection con = null;
			JobDataMap jdMap = jeContext.getJobDetail().getJobDataMap();
			String jobURL = jdMap.get("jobURL").toString();
			URL obj = new URL(jobURL);
			if(jobURL.contains("https")){
				 con = (HttpsURLConnection) obj.openConnection();
			} else {
				 con = (HttpURLConnection) obj.openConnection();
			}
			//add reuqest header
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Length", "0");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + jobURL);
			System.out.println("Response Code : " + responseCode);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

}
