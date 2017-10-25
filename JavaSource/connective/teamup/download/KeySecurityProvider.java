package connective.teamup.download;

import org.apache.log4j.Logger;

import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author haneym
 *
 */
public class KeySecurityProvider implements SecurityProvider 
{
	private static final Logger LOGGER = Logger.getLogger(KeySecurityProvider.class);
	
	private int[] keys = {31169, 9771, 5497, 62000, 111393, 523, 417, 712, 6468, 1225};

	/**
	 * Constructor for KeySecurityProvider.
	 */
	public KeySecurityProvider() 
	{
		super();
	}

	/* (non-Javadoc)
	 * @see connective.teamup.download.SecurityProvider#getAgentsForLogin(java.lang.String, java.lang.String)
	 */
	public String[] getAgentsForLogin(String userId, String password) throws Exception 
	{
		String[] ret = new String[1];
		ret[0] = userId;
		return ret;
	}
	
	/* (non-Javadoc)
	 * @see connective.teamup.download.SecurityProvider#getSecurityKey(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String getSecurityKey(String agentId, String userId, String password) 
	{
		return getSecurityKey(userId, password);
	}
	
	/* (non-Javadoc)
	 * @see connective.teamup.download.SecurityProvider#authenticate(java.lang.String)
	 */
	public UserAuthentication authenticate(String key) 
	{
		UserAuthentication ua = new UserAuthentication();
		String[] login = getLogin(key);
		if (login != null && login.length == 2)
		{
			// set the username and password
			ua.setUserId(login[0]);
			ua.setAgentId(login[0]);
			ua.setPassword(login[1]);
			
			// lookup the agent
			DatabaseOperation op = null;
			try
			{
				op = DatabaseFactory.getInstance().startOperation();
				AgentInfo info = op.getAgentInfo(ua.getAgentId());
				if (info != null)
				{
					// check the password
					if (info.getPassword().equals(ua.getPassword()))
						ua.setStatus(UserAuthentication.STATUS_GOOD);
					else
						ua.setStatus(UserAuthentication.STATUS_FAILED_CREDENTIALS);
				}
				else
				{
					// bad agent id
					ua.setStatus(UserAuthentication.STATUS_FAILED_CREDENTIALS);
				}
			}
			catch (Exception e)
			{
				LOGGER.error(e.getMessage());
				ua.setStatus(UserAuthentication.STATUS_FAILED_OTHER);
			}
			finally
			{
				op.close();
			}
		}
		else
		{
			ua.setStatus(UserAuthentication.STATUS_FAILED_OTHER);
		}
		
		return ua;
	}

	/**
	 * Return the username and password (array indexes 0 and 1 respectively) for the
	 * given security key
	 */
	protected String[] getLogin(String key) 
	{
		String[] ret = new String[2];
		StringBuffer user = new StringBuffer();
		StringBuffer password = new StringBuffer();
		
		// loop through
		for (int i=0; i < 10; i++)
		{
			int start = i * 8;
			int end = start + 8;
			String hex = key.substring(start, end);

			int codeval = Integer.parseInt(hex, 16);
			
			int userDigit = codeval / keys[i];
			if (userDigit != 0)
				user.append((char) userDigit);
				
			int pwdDigit = codeval % keys[i];
			if (pwdDigit != 0)
				password.append((char) pwdDigit);
		}

		ret[0] = user.toString();
		ret[1] = password.toString();
								
		return ret;
	}
	
	/**
	 * @see connective.teamup.sid.SecurityProvider#getSecurityKey(String, String)
	 */
	protected String getSecurityKey(String user, String password) 
	{
		StringBuffer ret = new StringBuffer();
		
		// loop through 10 digits
		for (int i=0; i < 10; i++)
		{
			int userDigit = 0;
			if (i < user.length())
				userDigit = (int) user.charAt(i);
				
			int pwdDigit = 0;
			if (i < password.length())
				pwdDigit = (int) password.charAt(i);
				
			int codeval = userDigit * keys[i] + pwdDigit;
			
			String hex = "00000000" + Integer.toHexString(codeval);
			hex = hex.substring(hex.length() - 8);
			
			ret.append(hex);
		}
		
		return ret.toString();
	}
	
	/* (non-Javadoc)
	 * @see connective.teamup.download.SecurityProvider#changePassword(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String changePassword(String agentId, String userId, String oldPassword,	String newPassword) 
	{
		// NOT SUPPORTED
		return null;
	}
}
