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
public class GetDownloadFileInput 
{
	protected String agentId = null;
	protected String originalFilename = null;
	protected long createdDate = 0;
	
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
	public long getCreatedDate() {
		return createdDate;
	}

	/**
	 * @return
	 */
	public String getOriginalFilename() {
		return originalFilename;
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
	public void setCreatedDate(long l) {
		createdDate = l;
	}

	/**
	 * @param string
	 */
	public void setOriginalFilename(String string) {
		originalFilename = string;
	}

}
