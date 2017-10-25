/*
 * Created on Oct 4, 2006
 *
 */
package connective.teamup.download.ws.objects;

/**
 * @author haneym
 *
 */
public class ChangePasswordInput 
{
	protected String loginId = null;
	protected String oldPassword = null;
	protected String newPassword = null;
	protected String agentId = null;

	/**
	 * @deprecated
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
	public String getOldPassword() {
		return oldPassword;
	}
	/**
	 * @param password The password to set.
	 */
	public void setOldPassword(String password) {
		this.oldPassword = password;
	}
	/**
	 * @return Returns the password.
	 */
	public String getNewPassword() {
		return newPassword;
	}
	/**
	 * @param password The password to set.
	 */
	public void setNewPassword(String password) {
		this.newPassword = password;
	}
}
