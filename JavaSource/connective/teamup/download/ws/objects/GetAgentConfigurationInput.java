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
public class GetAgentConfigurationInput 
{
	protected String agentId = null;
	protected String password = null;
	/**
	 * @deprecated
	 * @return
	 */
	public String getAgentId() {
		return agentId;
	}

	/**
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param string
	 */
	public void setAgentId(String string) {
		agentId = string;
	}

	/**
	 * @param string
	 */
	public void setPassword(String string) {
		password = string;
	}

}
