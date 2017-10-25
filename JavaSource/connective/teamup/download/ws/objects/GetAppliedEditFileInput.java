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
public class GetAppliedEditFileInput 
{
	protected String agentId = null;
	protected int batchNumber = 0;
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
	public int getBatchNumber() {
		return batchNumber;
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
	 * @param i
	 */
	public void setBatchNumber(int i) {
		batchNumber = i;
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
