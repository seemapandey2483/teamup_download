/*
 * Created on Oct 4, 2006
 *
 */
package connective.teamup.download.ws.objects;

/**
 * @author haneym
 *
 */
public class ChangePasswordOutput 
{
	protected String securityKey = null;
	protected String error = null;
	/**
	 * @return Returns the error.
	 */
	public String getError() {
		return error;
	}
	/**
	 * @param error The error to set.
	 */
	public void setError(String error) {
		this.error = error;
	}
	/**
	 * @return Returns the securityKey.
	 */
	public String getSecurityKey() {
		return securityKey;
	}
	/**
	 * @param securityKey The securityKey to set.
	 */
	public void setSecurityKey(String securityKey) {
		this.securityKey = securityKey;
	}
}
