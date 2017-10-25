package connective.teamup.download.ws.commands;

import java.util.ArrayList;
import java.util.List;

import connective.teamup.download.db.AmsClaimInfo;
import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.ws.objects.GetMiscVendorSystemInfoOutput;
import connective.teamup.download.ws.objects.MiscAgencyVendorInfo;
import connective.teamup.download.ws.objects.MiscVendorFileSettings;
import connective.teamup.download.ws.objects.VendorIdInput;
import connective.teamup.ws.ICommand;
import connective.teamup.ws.SecurityStatus;

public class GetMiscVendorInfo implements ICommand
{


	/* (non-Javadoc)
	 * @see connective.teamup.ws.ICommand#perform(java.lang.Object)
	 */
	public Object perform(Object inputData, SecurityStatus secInfo) throws Exception 
	{
		VendorIdInput input = (VendorIdInput) inputData;
		GetMiscVendorSystemInfoOutput output = null;

		
		String vendorId = input.getVendorId();
		
		DatabaseOperation op = null;
		try
		{
			op = DatabaseFactory.getInstance().startOperation();
			

			AmsClaimInfo ams = op.getAmsClaimInfo(vendorId);
			MiscAgencyVendorInfo ret =getVendorInfo( ams,  vendorId);
			output = new GetMiscVendorSystemInfoOutput();
			output.setVendorInfos(ret);
		}
			
		finally
		{
			if (op != null)
				op.close();
		}
		
	
		
		return output;
	}

	protected MiscAgencyVendorInfo getVendorInfo(AmsClaimInfo ams, String vendorId) throws Exception
	{
		MiscAgencyVendorInfo vendor = new MiscAgencyVendorInfo();
		List<MiscVendorFileSettings> fileSettings = new ArrayList<MiscVendorFileSettings>();
		MiscVendorFileSettings file = null;
		vendor.setSystemId(vendorId);
		vendor.setVendorName(ams.getName());
		
		file= new MiscVendorFileSettings();
		//set for Claim
		file.setDirectory((ams.getCompanyClaimDir()!=null && !"".equals(ams.getCompanyClaimDir()))?ams.getCompanyClaimDir():ams.getDefaultClaimDirectory());
		file.setFileName((ams.getCompanyClaimFilename()!=null && !"".equals(ams.getCompanyClaimFilename()))?ams.getCompanyClaimFilename():ams.getDeafultClaimFileName());
		file.setIncrementType(ams.getFilenameClaimIncrementType());
		file.setType("CLAIM");
		fileSettings.add(file);
		
		
		file= new MiscVendorFileSettings();
		//set for Policy
		file.setDirectory((ams.getCompanyPolicyDir()!=null && !"".equals(ams.getCompanyPolicyDir()))?ams.getCompanyPolicyDir():ams.getDefaultPolicyDirectory());
		file.setFileName((ams.getCompanyPolicyFilename()!=null && !"".equals(ams.getCompanyPolicyFilename()))?ams.getCompanyPolicyFilename():ams.getDeafultPolicyFileName());
		file.setIncrementType(ams.getFilenamePolicyIncrementType());
		file.setType("POLICY");
		fileSettings.add(file);
		
		vendor.setFileSettings(fileSettings);
		
		return vendor;
	}


}
