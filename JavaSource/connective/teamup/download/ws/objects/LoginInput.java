/*
 * Created on Oct 4, 2006
 *
 */
package connective.teamup.download.ws.objects;

/**
 * @author haneym
 *
 */
public class LoginInput 
{
	protected String loginId = null;
	protected String password = null;
	protected String agentId = null;
	
	/**
	 * 
	 * @return Returns the agentId.
	 */
	public String getAgentId() {
		return agentId;
	}
	/**
	 * @param agentId The agentId to set.
	 */
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	/**
	 * @return Returns the loginId.
	 */
	public String getLoginId() {
		return loginId;
	}
	/**
	 * @param loginId The loginId to set.
	 */
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
