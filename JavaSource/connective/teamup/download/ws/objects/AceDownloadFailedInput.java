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
public class AceDownloadFailedInput 
{
	protected String agentId = null;
	protected int batchNumber = 0;
	protected String errorMsg = null;
	protected String stackTrace = null;
	
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
	public int getBatchNumber() {
		return batchNumber;
	}

	/**
	 * @return
	 */
	public String getErrorMsg() {
		return errorMsg;
	}

	/**
	 * @return
	 */
	public String getStackTrace() {
		return stackTrace;
	}

	/**
	 * @param string
	 */
	public void setAgentId(String string) {
		agentId = string;
	}

	/**
	 * @param i
	 */
	public void setBatchNumber(int i) {
		batchNumber = i;
	}

	/**
	 * @param string
	 */
	public void setErrorMsg(String string) {
		errorMsg = string;
	}

	/**
	 * @param string
	 */
	public void setStackTrace(String string) {
		stackTrace = string;
	}

}
