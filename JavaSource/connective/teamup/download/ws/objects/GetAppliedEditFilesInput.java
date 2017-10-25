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
public class GetAppliedEditFilesInput 
{
	protected String agentId = null;
	protected String dlStatus = null;
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
	public String getDlStatus() {
		return dlStatus;
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
	public void setDlStatus(String string) {
		dlStatus = string;
	}
}
