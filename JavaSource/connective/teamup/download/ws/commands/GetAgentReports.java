/*
 * Created on Jan 23, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package connective.teamup.download.ws.commands;

import java.util.ArrayList;

import connective.teamup.download.services.ReportsService;
import connective.teamup.download.ws.objects.AgentIdInput;
import connective.teamup.download.ws.objects.GetAgentReportsOutput;
import connective.teamup.download.ws.objects.OptionBean;
import connective.teamup.ws.ICommand;
import connective.teamup.ws.SecurityStatus;

/**
 * @author haneym
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class GetAgentReports implements ICommand {

	/* (non-Javadoc)
	 * @see connective.teamup.ws.ICommand#perform(java.lang.Object)
	 */
	public Object perform(Object inputData, SecurityStatus secInfo) throws Exception 
	{
		AgentIdInput input = (AgentIdInput) inputData;
		GetAgentReportsOutput output = null;
		
		String path = "/agency";
		String agentId = secInfo.getAgentId();
		StringBuffer agentKey = null;  
			
		// Create the list of available agent reports
		ArrayList reportList = new ArrayList();
		ReportsService svc = new ReportsService();
		reportList.add(new OptionBean("Last Download Details", svc.getReportUrl(ReportsService.CR_LAST_DL_DETAILS, path, agentId, agentKey, null)));
		reportList.add(new OptionBean("Download Log", svc.getReportUrl(ReportsService.CR_DOWNLOAD_LOG, path, agentId, agentKey, null)));
		reportList.add(new OptionBean("Download Details", svc.getReportUrl(ReportsService.CR_DOWNLOAD_DETAILS, path, agentId, agentKey, null)));
		
		if (reportList.size() == 0)
			reportList.add(new OptionBean("No reports available", ""));
		
		OptionBean[] ret = new OptionBean[reportList.size()];
		reportList.toArray(ret);
		
		output = new GetAgentReportsOutput();
		output.setOptions(ret);
		
		return output;
	}

}
