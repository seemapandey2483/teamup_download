/*
 * Created on Jan 23, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package connective.teamup.download.ws.commands;

import java.util.ArrayList;
import java.util.Hashtable;

import connective.teamup.download.CustomTextFactory;
import connective.teamup.download.db.AmsInfo;
import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.services.ReportsService;
import connective.teamup.download.ws.objects.AgencyVendorInfo;
import connective.teamup.download.ws.objects.AgentIdInput;
import connective.teamup.download.ws.objects.GetVendorSystemListOutput;
import connective.teamup.ws.ICommand;
import connective.teamup.ws.SecurityStatus;

/**
 * @author haneym
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class GetVendorSystemList implements ICommand
{
//	protected SecurityProvider securityProvider = null;
//	
//	public GetVendorSystemList()
//	{
//		try
//		{
//			// load the security provider
//			String provClass = System.getProperty("teamup.securityprovider");
//			if (provClass != null && !provClass.equals(""))
//				securityProvider = (SecurityProvider) Class.forName(provClass).newInstance();
//			else
//				securityProvider = new KeySecurityProvider();
//		}
//		catch (Exception e)
//		{
//			System.out.println(e.getMessage());
//		}
//	}
	

	/* (non-Javadoc)
	 * @see connective.teamup.ws.ICommand#perform(java.lang.Object)
	 */
	public Object perform(Object inputData, SecurityStatus secInfo) throws Exception 
	{
		AgentIdInput input = (AgentIdInput) inputData;
		GetVendorSystemListOutput output = null;

		ArrayList vendorList = new ArrayList();
		
		String agentId = secInfo.getAgentId();
		
		DatabaseOperation op = null;
		try
		{
			op = DatabaseFactory.getInstance().startOperation();
			
//			// Attempt to retrieve agent info and build the agent security key
//			AgentInfo agentInfo = op.getAgentInfo(agentId);
//			if (agentInfo != null)
//				agentKey = new StringBuffer(securityProvider.getSecurityKey(agentId, agentId, agentInfo.getPassword()));
			
			// Retrieve the list of available agency vendor systems
			AmsInfo[] ams = op.getAmsInfoList();
			for (int i=0; i < ams.length; i++)
				vendorList.add(getVendorInfo(ams[i], secInfo.getAgentId()));
		}
		finally
		{
			if (op != null)
				op.close();
		}
		
		AgencyVendorInfo[] ret = new AgencyVendorInfo[vendorList.size()];
		vendorList.toArray(ret);
		output = new GetVendorSystemListOutput();
		output.setVendorInfos(ret);
		
		return output;
	}

	protected AgencyVendorInfo getVendorInfo(AmsInfo ams, String agentId) throws Exception
	{
		AgencyVendorInfo vendor = new AgencyVendorInfo();
		if (ams != null)
		{
			vendor.setBatchFileNeeded(ams.isBatchFileFlag());
			vendor.setDirectoryNotes(ams.getDirectoryNotes());
			vendor.setDisplayName(ams.getDisplayName());
//			vendor.setDownloadDirectory(ams.getDefaultDir());
			vendor.setDownloadDirectory(ams.getCompanyDir());
//			vendor.setDownloadFilename(ams.getDefaultFilename());
			vendor.setDownloadFilename(ams.getCompanyFilename());
			vendor.setFileAppended(ams.isAppendFlag());
			vendor.setFilenameChangeAllowed(ams.isAgentChangeFilenameFlag());
			vendor.setFilenameNotes(ams.getFilenameNotes());
			vendor.setRegistrationControlType(ams.getRegistrationControlType());
			vendor.setSystemId(ams.getId());
			vendor.setSystemName(ams.getName());
			vendor.setSystemNote(ams.getNote());
			vendor.setVendorName(ams.getVendor());
			vendor.setDirectoryVariable(ams.isDirectoryVariable());
			vendor.setFilenameIncrementType(ams.getFilenameIncrementType());
			vendor.setCustomSystem(ams.isCustomSystem());
			
			// Check to see if setup/runtime notes exist; if so, build the URL for viewing by client app!
			ReportsService svc = new ReportsService();
			String runtimeNotes = null;
			String setupNotes = null;
			DatabaseOperation op = null;
			StringBuffer agentKey = null;
			try
			{
				op = DatabaseFactory.getInstance().startOperation();
				CustomTextFactory runtimeFactory = new CustomTextFactory(CustomTextFactory.VENDOR_HELP_RUNTIME, ams, null, op);
				runtimeNotes = runtimeFactory.getText();
				CustomTextFactory setupFactory = new CustomTextFactory(CustomTextFactory.VENDOR_HELP_SETUP, ams, null, op);
				setupNotes = setupFactory.getText();
			}
			finally
			{
				if (op != null)
					op.close();
			}
			
			Hashtable params = new Hashtable();
			params.put("amsid", ams.getId());
			if (runtimeNotes != null && !runtimeNotes.equals(""))
			{
				vendor.setRuntimeNotes(runtimeNotes);
				vendor.setRuntimeNotesUrl(svc.getReportUrl(ReportsService.CR_VENDOR_RUNTIME, "/agency", agentId, agentKey, params));
			}
			
			if (setupNotes != null && !setupNotes.equals(""))
			{
				vendor.setSetupNotes(setupNotes);
				vendor.setSetupNotesUrl(svc.getReportUrl(ReportsService.CR_VENDOR_SETUP, "/agency", agentId, agentKey, params));
			}
		}
		
		return vendor;
	}

}
