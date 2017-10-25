package connective.teamup.download;


/**
 * @author haneym
 *
 */
public interface SecurityProvider 
{
	/**
	 * Returns a list of valid agent ids assigned to this login.
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public String[] getAgentsForLogin(String userId, String password) throws Exception;

	/** 
	 * Return the security key for the given user/agent
	 * @param agentId
	 * @param userId
	 * @param password
	 * @return
	 */
	public String getSecurityKey(String agentId, String userId, String password);
	
	/**
	 * authenticate the user
	 */ 
	public UserAuthentication authenticate(String key);

	/**
	 * Changes the users password (if the security provider supports it)
	 * 
	 * @param userId
	 * @param oldPassword
	 * @param newPassword
	 * @return Error string, or NULL if no error
	 */
	public String changePassword(String agentId, String userId, String oldPassword, String newPassword);
}
