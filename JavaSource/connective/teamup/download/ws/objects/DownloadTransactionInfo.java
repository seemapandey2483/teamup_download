/*
 * 05/10/2005 - Created
 */
package connective.teamup.download.ws.objects;

import java.io.Serializable;

/**
 * @author mccrearyk
 */
public class DownloadTransactionInfo implements Serializable
{
	protected String customerId = null;
	protected String description = null;
	protected String dataFormat = null;
	protected String dlStatus = null;
	protected String insuredName = null;
	protected String lob = null;
	protected String policyEffDate = null;
	protected String policyNumber = null;
	protected String transactionEffDate = null;
	protected String transactionType = null;
	protected int sequence = 0;
	protected int transactionSeq = 0;
	protected long lastDownloadDate = 0;
	protected double transactionPremium = 0.0;


	/**
	 * Constructor for DownloadTransactionInfo.
	 */
	public DownloadTransactionInfo()
	{
		super();
	}

	/**
	 * Returns the agency vendor system customer ID.
	 */
	public String getCustomerId()
	{
		return customerId;
	}

	/**
	 * Returns the data format for the transaction (e.g., 1 = AL1, 3 = AL3, X = XML).
	 */
	public String getDataFormat()
	{
		return dataFormat;
	}

	/**
	 * Returns the transaction description.
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * Returns the download status for the transaction.
	 */
	public String getDlStatus()
	{
		return dlStatus;
	}

	/**
	 * Returns the insured's name.
	 */
	public String getInsuredName()
	{
		return insuredName;
	}

	/**
	 * Returns the date the transaction was last downloaded.
	 */
	public long getLastDownloadDate()
	{
		return lastDownloadDate;
	}

	/**
	 * Returns the line of business code.
	 */
	public String getLob()
	{
		return lob;
	}

	/**
	 * Returns the policy effective date.
	 */
	public String getPolicyEffDate()
	{
		return policyEffDate;
	}

	/**
	 * Returns the policy number.
	 */
	public String getPolicyNumber()
	{
		return policyNumber;
	}

	/**
	 * Returns the sequence within the file.
	 */
	public int getSequence()
	{
		return sequence;
	}

	/**
	 * Returns the transaction effective date.
	 */
	public String getTransactionEffDate()
	{
		return transactionEffDate;
	}

	/**
	 * Returns the transaction premium amount.
	 */
	public double getTransactionPremium()
	{
		return transactionPremium;
	}

	/**
	 * Returns the transaction sequence number.
	 */
	public int getTransactionSeq()
	{
		return transactionSeq;
	}

	/**
	 * Returns the transaction type code.
	 */
	public String getTransactionType()
	{
		return transactionType;
	}

	/**
	 * Sets the agency vendor system's customer ID.
	 */
	public void setCustomerId(String string)
	{
		customerId = string;
	}

	/**
	 * Sets the data format for the transaction (e.g., 1 = AL1, 3 = AL3, X = XML).
	 */
	public void setDataFormat(String string)
	{
		dataFormat = string;
	}

	/**
	 * Sets the transaction description.
	 */
	public void setDescription(String string)
	{
		description = string;
	}

	/**
	 * Sets the download status for the transaction.
	 */
	public void setDlStatus(String string)
	{
		dlStatus = string;
	}

	/**
	 * Sets the insured's name.
	 */
	public void setInsuredName(String string)
	{
		insuredName = string;
	}

	/**
	 * Sets the date the transaction was last downloaded.
	 */
	public void setLastDownloadDate(long l)
	{
		lastDownloadDate = l;
	}

	/**
	 * Sets the line of business.
	 */
	public void setLob(String string)
	{
		lob = string;
	}

	/**
	 * Sets the policy effective date.
	 */
	public void setPolicyEffDate(String string)
	{
		policyEffDate = string;
	}

	/**
	 * Sets the policy number.
	 */
	public void setPolicyNumber(String string)
	{
		policyNumber = string;
	}

	/**
	 * Sets the sequence of this transaction within the file.
	 */
	public void setSequence(int i)
	{
		sequence = i;
	}

	/**
	 * Sets the transaction effective date.
	 */
	public void setTransactionEffDate(String string)
	{
		transactionEffDate = string;
	}

	/**
	 * Sets the transaction premium amount.
	 */
	public void setTransactionPremium(double d)
	{
		transactionPremium = d;
	}

	/**
	 * Sets the transaction sequence.
	 */
	public void setTransactionSeq(int i)
	{
		transactionSeq = i;
	}

	/**
	 * Sets the transaction type code.
	 */
	public void setTransactionType(String string)
	{
		transactionType = string;
	}

}
