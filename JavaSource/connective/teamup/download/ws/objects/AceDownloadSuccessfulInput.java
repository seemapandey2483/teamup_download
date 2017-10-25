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
public class AceDownloadSuccessfulInput 
{
	protected String agentId = null;
	protected int batchNumber = 0;
	protected DownloadFileInfo[] files = null;
	
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
	public DownloadFileInfo[] getFiles() {
		return files;
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
	 * @param infos
	 */
	public void setFiles(DownloadFileInfo[] infos) {
		files = infos;
	}

}
