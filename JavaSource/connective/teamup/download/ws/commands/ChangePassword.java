/*
 * Created on Jan 23, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package connective.teamup.download.ws.commands;

import org.apache.log4j.Logger;

import connective.teamup.download.KeySecurityProvider;
import connective.teamup.download.SecurityProvider;
import connective.teamup.download.ws.objects.ChangePasswordInput;
import connective.teamup.download.ws.objects.ChangePasswordOutput;
import connective.teamup.ws.ICommand;
import connective.teamup.ws.SecurityStatus;

/**
 * @author haneym
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ChangePassword implements ICommand 
{
	private static final Logger LOGGER = Logger.getLogger(ChangePassword.class);
	
	protected SecurityProvider securityProvider = null;
	
	public ChangePassword()
	{
		try
		{
			// load the security provider
			String provClass = System.getProperty("teamup.securityprovider");
			if (provClass != null && !provClass.equals(""))
				securityProvider = (SecurityProvider) Class.forName(provClass).newInstance();
			else
				securityProvider = new KeySecurityProvider();
		}
		catch (Exception e)
		{
			LOGGER.error(e);
			System.out.println(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see connective.teamup.ws.ICommand#perform(java.lang.Object)
	 */
	public Object perform(Object inputData, SecurityStatus secInfo) throws Exception 
	{
		ChangePasswordInput input = (ChangePasswordInput) inputData;
		ChangePasswordOutput output = new ChangePasswordOutput();
		String error = securityProvider.changePassword(secInfo.getAgentId(), input.getLoginId(), input.getOldPassword(), input.getNewPassword());
		if (error != null)
			output.setError(error);
		else
			output.setSecurityKey(securityProvider.getSecurityKey(secInfo.getAgentId(), input.getLoginId(), input.getNewPassword()));
		
		return output;
	}
}
