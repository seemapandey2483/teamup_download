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
public class GetDownloadFileContentsInput 
{
	protected String agentId = null;
	protected DownloadFileInfo file = null;

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
	public DownloadFileInfo getDownloadFile() {
		return file;
	}

	/**
	 * @param string
	 */
	public void setAgentId(String string) {
		agentId = string;
	}

	/**
	 * @param l
	 */
	public void setDownloadFile(DownloadFileInfo file) {
		this.file = file;
	}
}
