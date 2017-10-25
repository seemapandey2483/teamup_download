/*
 * 05/12/2005 - Created
 */
package connective.teamup.download.ws.objects;

/**
 * @author mccrearyk
 */
public class AgentConfiguration
{
	protected String agentId = null;
	protected String name = null;
	protected String amsId = null;
	protected String amsVersion = null;
	protected String remoteDirectory = null;
	protected String contactName = null;
	protected String contactEmail = null;
	protected String contactPhone = null;
	protected String defaultFilename = null;
	protected String locationState = null;
	protected String svcMessage = "OK";
	protected boolean live = false;
	protected boolean scheduled = false;
	protected boolean disabled = false;
	protected boolean contactChangeAllowed = false;
	protected boolean testAgent = false;
	protected long lastDownloadDate = 0;
	protected long lastLoginDate = 0;


	/**
	 * Constructor for AgentConfiguration.
	 */
	public AgentConfiguration()
	{
		super();
	}

	/**
	 * @return
	 */
	public boolean isDisabled()
	{
		return disabled;
	}

	/**
	 * @deprecated
	 * @return
	 */
	public String getAgentId()
	{
		return agentId;
	}

	/**
	 * @return
	 */
	public String getAmsId()
	{
		return amsId;
	}

	/**
	 * @return
	 */
	public String getAmsVersion()
	{
		return amsVersion;
	}

	/**
	 * @return
	 */
	public String getContactEmail()
	{
		return contactEmail;
	}

	/**
	 * @return
	 */
	public String getContactName()
	{
		return contactName;
	}

	/**
	 * @return
	 */
	public String getContactPhone()
	{
		return contactPhone;
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
	public long getLastDownloadDate()
	{
		return lastDownloadDate;
	}

	/**
	 * @return
	 */
	public long getLastLoginDate()
	{
		return lastLoginDate;
	}

	/**
	 * @return
	 */
	public boolean isLive()
	{
		return live;
	}

	/**
	 * @return
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return
	 */
	public String getRemoteDirectory()
	{
		return remoteDirectory;
	}

	/**
	 * @return
	 */
	public boolean isScheduled()
	{
		return scheduled;
	}

	/**
	 * @param b
	 */
	public void setDisabled(boolean b)
	{
		disabled = b;
	}

	/**
	 * @param string
	 */
	public void setAgentId(String string)
	{
		agentId = string;
	}

	/**
	 * @param string
	 */
	public void setAmsId(String string)
	{
		amsId = string;
	}

	/**
	 * @param string
	 */
	public void setAmsVersion(String string)
	{
		amsVersion = string;
	}

	/**
	 * @param string
	 */
	public void setContactEmail(String string)
	{
		contactEmail = string;
	}

	/**
	 * @param string
	 */
	public void setContactName(String string)
	{
		contactName = string;
	}

	/**
	 * @param string
	 */
	public void setContactPhone(String string)
	{
		contactPhone = string;
	}

	/**
	 * @param string
	 */
	public void setDefaultFilename(String string)
	{
		defaultFilename = string;
	}

	/**
	 * @param l
	 */
	public void setLastDownloadDate(long l)
	{
		lastDownloadDate = l;
	}

	/**
	 * @param l
	 */
	public void setLastLoginDate(long l)
	{
		lastLoginDate = l;
	}

	/**
	 * @param b
	 */
	public void setLive(boolean b)
	{
		live = b;
	}

	/**
	 * @param string
	 */
	public void setName(String string)
	{
		name = string;
	}

	/**
	 * @param string
	 */
	public void setRemoteDirectory(String string)
	{
		remoteDirectory = string;
	}

	/**
	 * @param b
	 */
	public void setScheduled(boolean b)
	{
		scheduled = b;
	}

	/**
	 * @return
	 */
	public boolean isContactChangeAllowed()
	{
		return contactChangeAllowed;
	}

	/**
	 * @param b
	 */
	public void setContactChangeAllowed(boolean b)
	{
		contactChangeAllowed = b;
	}

	/**
	 * @return
	 */
	public String getSvcMessage()
	{
		return svcMessage;
	}

	/**
	 * @param string
	 */
	public void setSvcMessage(String string)
	{
		svcMessage = string;
	}

	/**
	 * @return
	 */
	public String getLocationState()
	{
		return locationState;
	}

	/**
	 * @return
	 */
	public boolean isTestAgent()
	{
		return testAgent;
	}

	/**
	 * @param string
	 */
	public void setLocationState(String string)
	{
		locationState = string;
	}

	/**
	 * @param b
	 */
	public void setTestAgent(boolean b)
	{
		testAgent = b;
	}

}
