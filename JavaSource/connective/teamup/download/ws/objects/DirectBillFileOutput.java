package connective.teamup.download.ws.objects;

import java.io.Serializable;

public class DirectBillFileOutput implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected String filename = null;
	protected DownloadDirectBillTransInfo[] directBillTransactions = null;
	protected int directBillTransactionCount = 0;
	
	/**
	 * Constructor for DownloadFileInfo.
	 */
	public DirectBillFileOutput()
	{
		super();
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	
	public DownloadDirectBillTransInfo[] getDirectBillTransactions() {
		return directBillTransactions;
	}

	public void setDirectBillTransactions(
			DownloadDirectBillTransInfo[] directBillTransactions) {
		this.directBillTransactions = directBillTransactions;
	}

	public int getDirectBillTransactionCount() {
		return directBillTransactionCount;
	}

	public void setDirectBillTransactionCount(int directBillTransactionCount) {
		this.directBillTransactionCount = directBillTransactionCount;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
