package connective.teamup.download.ws.objects;

import java.io.Serializable;

public class MiscVendorFileSettings implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String fileName;
	private String directory;
	private String incrementType;
	private String type;
	private String misc1;
	private String misc2;
	private String misc3;
	private String misc4;
	private String misc5;

	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getDirectory() {
		return directory;
	}
	public void setDirectory(String directory) {
		this.directory = directory;
	}
	public String getIncrementType() {
		return incrementType;
	}
	public void setIncrementType(String incrementType) {
		this.incrementType = incrementType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMisc1() {
		return misc1;
	}
	public void setMisc1(String misc1) {
		this.misc1 = misc1;
	}
	public String getMisc2() {
		return misc2;
	}
	public void setMisc2(String misc2) {
		this.misc2 = misc2;
	}
	public String getMisc3() {
		return misc3;
	}
	public void setMisc3(String misc3) {
		this.misc3 = misc3;
	}
	public String getMisc4() {
		return misc4;
	}
	public void setMisc4(String misc4) {
		this.misc4 = misc4;
	}
	public String getMisc5() {
		return misc5;
	}
	public void setMisc5(String misc5) {
		this.misc5 = misc5;
	}
	

}
