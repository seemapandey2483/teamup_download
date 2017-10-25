package connective.teamup.download.ws.objects;

import java.io.Serializable;

public class ClaimFileOutput implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected String filename = null;
	protected DownloadClaimFileInfo[] claimTransactions = null;
	protected int claimTransactionsCount = 0;
	
	/**
	 * Constructor for DownloadFileInfo.
	 */
	public ClaimFileOutput()
	{
		super();
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	
	public DownloadClaimFileInfo[] getClaimTransactions() {
		return claimTransactions;
	}

	public void setClaimTransactions(DownloadClaimFileInfo[] claimTransactions) {
		this.claimTransactions = claimTransactions;
	}

	public int getClaimTransactionsCount() {
		return claimTransactionsCount;
	}

	public void setClaimTransactionsCount(int claimTransactionsCount) {
		this.claimTransactionsCount = claimTransactionsCount;
	}
}
