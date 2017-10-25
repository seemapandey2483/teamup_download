package connective.teamup.download.ws.objects;

import java.io.Serializable;
/**
 * @author anand alok
 * 05/07/2015
 */
public class DownloadDirectBillTransInfo implements Serializable {
	
	protected Long transId = null;
	protected String itemNumber = null;
	protected String insuredName = null;
	protected String agentId = null;
	protected String producerSubCode = null;
	protected String policyNumber = null;
	protected String transEffectiveDate = null;
	protected String transTypeCode = null;
	protected Double grossAmount = 0.0;
	protected Double comissionRate = 0.0;
	protected Double comissionAmount = 0.0;
	protected int installMentNumber = 0;
	protected String paymentPlanCode = null;
	protected String lob = null;
	protected String lobSubCode = null;
	protected String companyProducerCode = null;
	protected String policyEffectiveDate = null;
	protected String policyExpiryDate = null;
	protected int policyVersion = 0;
	protected String billingAccountNumber = null;
	protected Double comissionAdjustedAmount = 0.0;
	protected String customerId = null;
	protected int sequence = 0;
	protected String fileType = null;
	protected long createdDate = 0;
	
	/**
	 * Constructor for DownloadTransactionInfo.
	 */
	public DownloadDirectBillTransInfo()
	{
		super();
	}

	public Long getTransId() {
		return transId;
	}

	public void setTransId(Long transId) {
		this.transId = transId;
	}

	public String getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}

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

	public String getProducerSubCode() {
		return producerSubCode;
	}

	public void setProducerSubCode(String producerSubCode) {
		this.producerSubCode = producerSubCode;
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

	public Double getGrossAmount() {
		return grossAmount;
	}

	public void setGrossAmount(Double grossAmount) {
		this.grossAmount = grossAmount;
	}

	public Double getComissionRate() {
		return comissionRate;
	}

	public void setComissionRate(Double comissionRate) {
		this.comissionRate = comissionRate;
	}

	public Double getComissionAmount() {
		return comissionAmount;
	}

	public void setComissionAmount(Double comissionAmount) {
		this.comissionAmount = comissionAmount;
	}

	public int getInstallMentNumber() {
		return installMentNumber;
	}

	public void setInstallMentNumber(int installMentNumber) {
		this.installMentNumber = installMentNumber;
	}

	public String getPaymentPlanCode() {
		return paymentPlanCode;
	}

	public void setPaymentPlanCode(String paymentPlanCode) {
		this.paymentPlanCode = paymentPlanCode;
	}

	public String getLob() {
		return lob;
	}

	public void setLob(String lob) {
		this.lob = lob;
	}

	public String getLobSubCode() {
		return lobSubCode;
	}

	public void setLobSubCode(String lobSubCode) {
		this.lobSubCode = lobSubCode;
	}

	public String getCompanyProducerCode() {
		return companyProducerCode;
	}

	public void setCompanyProducerCode(String companyProducerCode) {
		this.companyProducerCode = companyProducerCode;
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

	public int getPolicyVersion() {
		return policyVersion;
	}

	public void setPolicyVersion(int policyVersion) {
		this.policyVersion = policyVersion;
	}

	public String getBillingAccountNumber() {
		return billingAccountNumber;
	}

	public void setBillingAccountNumber(String billingAccountNumber) {
		this.billingAccountNumber = billingAccountNumber;
	}

	public Double getComissionAdjustedAmount() {
		return comissionAdjustedAmount;
	}

	public void setComissionAdjustedAmount(Double comissionAdjustedAmount) {
		this.comissionAdjustedAmount = comissionAdjustedAmount;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
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
}
