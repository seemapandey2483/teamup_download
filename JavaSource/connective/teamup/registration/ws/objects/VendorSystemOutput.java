/*
 * 07/15/2005 - Created
 */
package connective.teamup.registration.ws.objects;


/**
 * @author Kyle W. McCreary
 */
public class VendorSystemOutput
{
	protected String id = null;
	protected String vendorName = null;
	protected String systemName = null;
	protected String systemNameNote = null;
	protected String defaultDirectory = null;
	protected String directoryVariable = null;
	protected String companyChangeDirectory = null;
	protected String directoryNotes = null;
	protected String defaultFilename = null;
	protected String companyChangeFilename = null;
	protected String agentChangeFilename = null;
	protected String filenameNotes = null;
	protected String filenameIncrementType = null;
	protected String appendFlag = null;
	protected String regControlType = null;
	protected String batchfile = null;
	protected String setupNotes = null;
	protected String runtimeNotes = null;
	protected int sortSequence = 0;
	protected long lastUpdated = 0;


	/**
	 * Default constructor for VendorSystemOutput.
	 */
	public VendorSystemOutput()
	{
		super();
	}

	/**
	 * @return
	 */
	public String getAgentChangeFilename()
	{
		return agentChangeFilename;
	}

	/**
	 * @return
	 */
	public String getAppendFlag()
	{
		return appendFlag;
	}

	/**
	 * @return
	 */
	public String getBatchfile()
	{
		return batchfile;
	}

	/**
	 * @return
	 */
	public String getCompanyChangeDirectory()
	{
		return companyChangeDirectory;
	}

	/**
	 * @return
	 */
	public String getCompanyChangeFilename()
	{
		return companyChangeFilename;
	}

	/**
	 * @return
	 */
	public String getDefaultDirectory()
	{
		return defaultDirectory;
	}

	/**
	 * @return
	 */
	public String getDefaultFilename()
	{
		return defaultFilename;
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
	public String getDirectoryVariable()
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
	public String getFilenameNotes()
	{
		return filenameNotes;
	}

	/**
	 * @return
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * @return
	 */
	public long getLastUpdated()
	{
		return lastUpdated;
	}

	/**
	 * @return
	 */
	public String getRegControlType()
	{
		return regControlType;
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
	 * @return
	 */
	public int getSortSequence()
	{
		return sortSequence;
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
	public String getSystemNameNote()
	{
		return systemNameNote;
	}

	/**
	 * @return
	 */
	public String getVendorName()
	{
		return vendorName;
	}

	/**
	 * @param string
	 */
	public void setAgentChangeFilename(String string)
	{
		agentChangeFilename = string;
	}

	/**
	 * @param string
	 */
	public void setAppendFlag(String string)
	{
		appendFlag = string;
	}

	/**
	 * @param string
	 */
	public void setBatchfile(String string)
	{
		batchfile = string;
	}

	/**
	 * @param string
	 */
	public void setCompanyChangeDirectory(String string)
	{
		companyChangeDirectory = string;
	}

	/**
	 * @param string
	 */
	public void setCompanyChangeFilename(String string)
	{
		companyChangeFilename = string;
	}

	/**
	 * @param string
	 */
	public void setDefaultDirectory(String string)
	{
		defaultDirectory = string;
	}

	/**
	 * @param string
	 */
	public void setDefaultFilename(String string)
	{
		defaultFilename = string;
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
	public void setDirectoryVariable(String string)
	{
		directoryVariable = string;
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
	public void setFilenameNotes(String string)
	{
		filenameNotes = string;
	}

	/**
	 * @param string
	 */
	public void setId(String string)
	{
		id = string;
	}

	/**
	 * @param l
	 */
	public void setLastUpdated(long l)
	{
		lastUpdated = l;
	}

	/**
	 * @param string
	 */
	public void setRegControlType(String string)
	{
		regControlType = string;
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
	 * @param i
	 */
	public void setSortSequence(int i)
	{
		sortSequence = i;
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
	public void setSystemNameNote(String string)
	{
		systemNameNote = string;
	}

	/**
	 * @param string
	 */
	public void setVendorName(String string)
	{
		vendorName = string;
	}

}
