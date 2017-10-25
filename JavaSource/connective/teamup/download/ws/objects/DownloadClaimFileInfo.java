package connective.teamup.download.ws.objects;

import java.io.Serializable;
/**
 * @author anand alok
 * 05/07/2016
 */
public class DownloadClaimFileInfo implements Serializable {

	protected String insuredName = null;
	protected String agentId = null;
	protected String policyNumber = null;
	protected String transEffectiveDate = null;
	protected String transTypeCode = null;
	protected String lob = null;
	protected String policyEffectiveDate = null;
	protected String policyExpiryDate = null;
	protected int sequence = 0;
	protected String fileType = null;
	protected long createdDate = 0;
	protected String claimNumber;
	protected int transactionSeq = 0;
	protected long lastDownloadDate = 0;
	protected String eventDate;
	protected String reportedDate;
	protected String bussinessPCode;
	protected String claimStatus;
	protected String dlStatus;
	protected String origFileName;
	
	public String getInsuredName() {
		return insuredName;
	}
	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public String getPolicyNumber() {
		return policyNumber;
	}
	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	public String getTransEffectiveDate() {
		return transEffectiveDate;
	}
	public void setTransEffectiveDate(String transEffectiveDate) {
		this.transEffectiveDate = transEffectiveDate;
	}
	public String getTransTypeCode() {
		return transTypeCode;
	}
	public void setTransTypeCode(String transTypeCode) {
		this.transTypeCode = transTypeCode;
	}
	public String getLob() {
		return lob;
	}
	public void setLob(String lob) {
		this.lob = lob;
	}
	public String getPolicyEffectiveDate() {
		return policyEffectiveDate;
	}
	public void setPolicyEffectiveDate(String policyEffectiveDate) {
		this.policyEffectiveDate = policyEffectiveDate;
	}
	public String getPolicyExpiryDate() {
		return policyExpiryDate;
	}
	public void setPolicyExpiryDate(String policyExpiryDate) {
		this.policyExpiryDate = policyExpiryDate;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public long getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(long createdDate) {
		this.createdDate = createdDate;
	}
	public String getClaimNumber() {
		return claimNumber;
	}
	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}
	public int getTransactionSeq() {
		return transactionSeq;
	}
	public void setTransactionSeq(int transactionSeq) {
		this.transactionSeq = transactionSeq;
	}
	public long getLastDownloadDate() {
		return lastDownloadDate;
	}
	public void setLastDownloadDate(long lastDownloadDate) {
		this.lastDownloadDate = lastDownloadDate;
	}
	public String getEventDate() {
		return eventDate;
	}
	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}
	public String getReportedDate() {
		return reportedDate;
	}
	public void setReportedDate(String reportedDate) {
		this.reportedDate = reportedDate;
	}
	public String getBussinessPCode() {
		return bussinessPCode;
	}
	public void setBussinessPCode(String bussinessPCode) {
		this.bussinessPCode = bussinessPCode;
	}
	public String getClaimStatus() {
		return claimStatus;
	}
	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}
	
	public String getOrigFileName() {
		return origFileName;
	}
	public void setOrigFileName(String origFileName) {
		this.origFileName = origFileName;
	}
	public String getDlStatus() {
		return dlStatus;
	}
	public void setDlStatus(String dlStatus) {
		this.dlStatus = dlStatus;
	} 
	

}
