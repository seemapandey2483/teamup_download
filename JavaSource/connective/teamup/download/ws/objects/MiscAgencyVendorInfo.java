package connective.teamup.download.ws.objects;

import java.io.Serializable;
import java.util.List;

public class MiscAgencyVendorInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected String systemId = null;
	protected String vendorName = null;
	protected List<MiscVendorFileSettings> fileSettings =null;
	
	
	/**
	 * Default constructor for AgencyVendorInfo.
	 */
	public MiscAgencyVendorInfo()
	{
		super();
	}


	public String getSystemId() {
		return systemId;
	}


	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}


	public String getVendorName() {
		return vendorName;
	}


	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}


	public List<MiscVendorFileSettings> getFileSettings() {
		return fileSettings;
	}


	public void setFileSettings(List<MiscVendorFileSettings> fileSettings) {
		this.fileSettings = fileSettings;
	}


	
}
