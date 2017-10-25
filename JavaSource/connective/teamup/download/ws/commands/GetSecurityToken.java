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
import connective.teamup.download.ws.objects.LoginInput;
import connective.teamup.ws.ICommand;
import connective.teamup.ws.SecurityStatus;

/**
 * @author haneym
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class GetSecurityToken implements ICommand 
{
	private static final Logger LOGGER = Logger.getLogger(GetSecurityToken.class);
	
	protected SecurityProvider securityProvider = null;
	
	public GetSecurityToken()
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
		LoginInput input = (LoginInput) inputData;
		String ret = securityProvider.getSecurityKey(input.getAgentId(), input.getLoginId(), input.getPassword());
		return ret;
	}
}
