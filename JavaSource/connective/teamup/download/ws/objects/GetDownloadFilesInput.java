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
public class GetDownloadFilesInput 
{
	protected String agentId = null;
	protected String dlStatus = null;
	/*protected int pageNo = 1;
	protected int rowsPerSelected = 0;
	
	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getRowsPerSelected() {
		return rowsPerSelected;
	}

	public void setRowsPerSelected(int rowsPerSelected) {
		this.rowsPerSelected = rowsPerSelected;
	}*/
	
	/**
	 * @return
	 * @deprecated
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
