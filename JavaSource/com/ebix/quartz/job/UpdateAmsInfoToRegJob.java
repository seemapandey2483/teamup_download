package com.ebix.quartz.job;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.services.AmsUpdateService;
import connective.teamup.registration.ws.objects.AmsGroupSummaryOutput;
import connective.teamup.registration.ws.objects.CarrierAmsGroupOutput;
import connective.teamup.registration.ws.objects.CarrierIdRequest;
import connective.teamup.ws.client.TeamupWSClient;
import connective.teamup.ws.client.TeamupWSException;

public class UpdateAmsInfoToRegJob implements Job{
	Logger log =Logger.getLogger(UpdateAmsInfoToRegJob.class);
	
	@Override
	public void execute(JobExecutionContext jeContext) throws JobExecutionException {
		
		DatabaseOperation op = null;		
		try {

			JobDataMap jdMap = jeContext.getJobDetail().getJobDataMap();
			String endpoint = jdMap.get("regurl").toString();
			String carrierId = jdMap.get("carrierId").toString();
			TeamupWSClient ws = new TeamupWSClient(endpoint, "TUDL");
			CarrierIdRequest carrierIdInput = new CarrierIdRequest();
			carrierIdInput.setCarrierId(carrierId);
			op = DatabaseFactory.getInstance().startOperation();
			AmsGroupSummaryOutput inputData = AmsUpdateService.getAmsSumarry(op, carrierId);
			CarrierAmsGroupOutput outObject = (CarrierAmsGroupOutput) ws.callService("UpdateAgencySummaryForCarrier", inputData);
			if(outObject!= null){
				System.out.println("Regadmin service to update AmsSummary called for ::"+outObject.getCarrierId() +"::" +outObject.getMessage());
				log.info("Regadmin service to update AmsSummary called for ::"+outObject.getCarrierId() +"::" +outObject.getMessage());
			}
		} catch(JobExecutionException je){
			log.error("UpdateAmsInfoToRegJob: failed" +je.getMessage());
			throw je;
		}catch (TeamupWSException e) {
			// TODO Auto-generated catch block
			log.error("UpdateAmsInfoToRegJob: failed" +e.getMessage());
		}catch(Exception e){
			log.error("UpdateAmsInfoToRegJob: failed" +e.getMessage());
		}finally {
			op.close();
		}
		
	}
}
