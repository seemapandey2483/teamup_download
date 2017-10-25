/*
 * Created on Sep 25, 2006
 */
package connective.teamup.download;

/**
 * @author haneym
 *
 */
public class UserAuthentication 
{
	public static final int STATUS_NONE = 0;			// none - user hasn't been authenticated yet
	public static final int STATUS_GOOD = 1;			// user has been authenticated
	public static final int STATUS_FAILED_EXPIRED = 2;	// password expired - errorUrl may contain url to change
	public static final int STATUS_FAILED_LOCKOUT = 3;	// user is locked out - errorUrl may contain url
	public static final int STATUS_FAILED_CREDENTIALS = 4;	// login failed - bad username or password
	public static final int STATUS_FAILED_OTHER = 5;	// login failed for other/unknown reason
	
	protected String userId = null;
	protected String password = null;
	protected String errorUrl = null;
	protected int status = 0;
	protected String agentId = null;	
	protected boolean keyChanged = false;
	protected String newKey = null;
	
	/**
	 * @return Returns the keyChanged.
	 */
	public boolean isKeyChanged() {
		return keyChanged;
	}
	/**
	 * @param keyChanged The keyChanged to set.
	 */
	public void setKeyChanged(boolean keyChanged) {
		this.keyChanged = keyChanged;
	}
	/**
	 * @return Returns the newKey.
	 */
	public String getNewKey() {
		return newKey;
	}
	/**
	 * @param newKey The newKey to set.
	 */
	public void setNewKey(String newKey) {
		this.newKey = newKey;
	}
	/**
	 * @return Returns the errorUrl.
	 */
	public String getErrorUrl() {
		return errorUrl;
	}
	/**
	 * @param errorUrl The errorUrl to set.
	 */
	public void setErrorUrl(String errorUrl) {
		this.errorUrl = errorUrl;
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
	/**
	 * @return Returns the status.
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status The status to set.
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * @return Returns the userId.
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId The userId to set.
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
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
}
