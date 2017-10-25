/*
 * 05/20/2005 - Created
 */
package connective.teamup.download.ws.objects;

import java.io.Serializable;

/**
 * @author mccrearyk
 */
public class AgencyVendorInfo implements Serializable
{
	protected String systemId = null;
	protected String vendorName = null;
	protected String systemName = null;
	protected String systemNote = null;
	protected String displayName = null;
	protected String downloadDirectory = null;
	protected String downloadFilename = null;
	protected boolean fileAppended = false;
	protected String filenameNotes = null;
	protected String directoryNotes = null;
	protected boolean filenameChangeAllowed = false;
	protected String registrationControlType = null;
	protected boolean batchFileNeeded = false;
	protected boolean directoryVariable = false;
	protected String filenameIncrementType = null;
	protected boolean customSystem = false;
	protected String setupNotes = null;
	protected String runtimeNotes = null;
	protected String setupNotesUrl = null;
	protected String runtimeNotesUrl = null;


	/**
	 * Default constructor for AgencyVendorInfo.
	 */
	public AgencyVendorInfo()
	{
		super();
	}

	/**
	 * @return
	 */
	public boolean isBatchFileNeeded()
	{
		return batchFileNeeded;
	}

	/**
	 * @return
	 */
	public String getDirectoryNotes()
	{
		return directoryNotes;
	}

	/**
	 * @return
	 */
	public String getDisplayName()
	{
		return displayName;
	}

	/**
	 * @return
	 */
	public String getDownloadDirectory()
	{
		return downloadDirectory;
	}

	/**
	 * @return
	 */
	public String getDownloadFilename()
	{
		return downloadFilename;
	}

	/**
	 * @return
	 */
	public boolean isFileAppended()
	{
		return fileAppended;
	}

	/**
	 * @return
	 */
	public boolean isFilenameChangeAllowed()
	{
		return filenameChangeAllowed;
	}

	/**
	 * @return
	 */
	public String getFilenameNotes()
	{
		return filenameNotes;
	}

	/**
	 * @return
	 */
	public String getRegistrationControlType()
	{
		return registrationControlType;
	}

	/**
	 * @return
	 */
	public String getSystemId()
	{
		return systemId;
	}

	/**
	 * @return
	 */
	public String getSystemName()
	{
		return systemName;
	}

	/**
	 * @return
	 */
	public String getSystemNote()
	{
		return systemNote;
	}

	/**
	 * @return
	 */
	public String getVendorName()
	{
		return vendorName;
	}

	/**
	 * @param b
	 */
	public void setBatchFileNeeded(boolean b)
	{
		batchFileNeeded = b;
	}

	/**
	 * @param string
	 */
	public void setDirectoryNotes(String string)
	{
		directoryNotes = string;
	}

	/**
	 * @param string
	 */
	public void setDisplayName(String string)
	{
		displayName = string;
	}

	/**
	 * @param string
	 */
	public void setDownloadDirectory(String string)
	{
		downloadDirectory = string;
	}

	/**
	 * @param string
	 */
	public void setDownloadFilename(String string)
	{
		downloadFilename = string;
	}

	/**
	 * @param b
	 */
	public void setFileAppended(boolean b)
	{
		fileAppended = b;
	}

	/**
	 * @param b
	 */
	public void setFilenameChangeAllowed(boolean b)
	{
		filenameChangeAllowed = b;
	}

	/**
	 * @param string
	 */
	public void setFilenameNotes(String string)
	{
		filenameNotes = string;
	}

	/**
	 * @param string
	 */
	public void setRegistrationControlType(String string)
	{
		registrationControlType = string;
	}

	/**
	 * @param string
	 */
	public void setSystemId(String string)
	{
		systemId = string;
	}

	/**
	 * @param string
	 */
	public void setSystemName(String string)
	{
		systemName = string;
	}

	/**
	 * @param string
	 */
	public void setSystemNote(String string)
	{
		systemNote = string;
	}

	/**
	 * @param string
	 */
	public void setVendorName(String string)
	{
		vendorName = string;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		if (displayName == null)
			return "";
		return displayName;
	}

	/**
	 * @return
	 */
	public boolean isCustomSystem()
	{
		return customSystem;
	}

	/**
	 * @return
	 */
	public boolean isDirectoryVariable()
	{
		return directoryVariable;
	}

	/**
	 * @return
	 */
	public String getFilenameIncrementType()
	{
		return filenameIncrementType;
	}

	/**
	 * @return
	 */
	public String getRuntimeNotes()
	{
		return runtimeNotes;
	}

	/**
	 * @return
	 */
	public String getSetupNotes()
	{
		return setupNotes;
	}

	/**
	 * @param b
	 */
	public void setCustomSystem(boolean b)
	{
		customSystem = b;
	}

	/**
	 * @param b
	 */
	public void setDirectoryVariable(boolean b)
	{
		directoryVariable = b;
	}

	/**
	 * @param string
	 */
	public void setFilenameIncrementType(String string)
	{
		filenameIncrementType = string;
	}

	/**
	 * @param string
	 */
	public void setRuntimeNotes(String string)
	{
		runtimeNotes = string;
	}

	/**
	 * @param string
	 */
	public void setSetupNotes(String string)
	{
		setupNotes = string;
	}

	/**
	 * @return
	 */
	public String getRuntimeNotesUrl()
	{
		return runtimeNotesUrl;
	}

	/**
	 * @return
	 */
	public String getSetupNotesUrl()
	{
		return setupNotesUrl;
	}

	/**
	 * @param string
	 */
	public void setRuntimeNotesUrl(String string)
	{
		runtimeNotesUrl = string;
	}

	/**
	 * @param string
	 */
	public void setSetupNotesUrl(String string)
	{
		setupNotesUrl = string;
	}

}
