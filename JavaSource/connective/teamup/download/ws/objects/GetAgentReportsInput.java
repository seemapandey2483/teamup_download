/*
 * Created on Jan 23, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package connective.teamup.download.ws.objects;

/**
 * @author haneym
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class GetAgentReportsInput 
{
	protected String agentId = null;
	protected String securityKey = null;
	
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
	/**
	 * @deprecated
	 * @return
	 */
	public String getAgentId() {
		return agentId;
	}

	/**
	 * @param string
	 */
	public void setAgentId(String string) {
		agentId = string;
	}

}
