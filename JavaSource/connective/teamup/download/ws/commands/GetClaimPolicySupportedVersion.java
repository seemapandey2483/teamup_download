package connective.teamup.download.ws.commands;

import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.ws.objects.ApplicationVersionOutput;
import connective.teamup.ws.ICommand;
import connective.teamup.ws.SecurityStatus;

public class GetClaimPolicySupportedVersion implements ICommand {

	public Object perform(Object inputData, SecurityStatus secInfo) throws Exception 
	{

		ApplicationVersionOutput output = null;
		String claimVersion = "";
		String policyVersion = "";
		DatabaseOperation op = null;

		try
		{
			op = DatabaseFactory.getInstance().startOperation();
			claimVersion = op.getPropertyValue(DatabaseFactory.PROP_CLAIM_XML_ALLOWED);
			if(claimVersion!= null && "Y".equals(claimVersion))
				claimVersion ="1";
			else
				claimVersion ="0";
			policyVersion = op.getPropertyValue(DatabaseFactory.PROP_POLICY_XML_ALLOWED);
			
			if(policyVersion!= null && "Y".equals(policyVersion))
				policyVersion ="1";
			else
				policyVersion ="0";
		}
		finally
		{
			if (op != null)
				op.close();
		}

		output = new ApplicationVersionOutput();
		output.setAppVersion(claimVersion+"#"+policyVersion);	
		return output;
	}
}
