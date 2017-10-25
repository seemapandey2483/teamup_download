package connective.teamup.download.beans;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.CarrierInfo;
import connective.teamup.download.DisplayBean;
import connective.teamup.download.DisplayBeanException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.DownloadStatus;

/**
 * @author Mike
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class DownloadDisplayBean implements DisplayBean, Serializable
{
	private static final Logger LOGGER = Logger.getLogger(DownloadDisplayBean.class);
	
	private String agentId = null;
	private String key = null;
	private String downloadDir = null;
	private String fileSource = null;
	private String dlCompleteForwardUrl = null;
	private String testForwardUrl = null;
	private String interactiveFlag = null;
	private String appendFlag = null;
	private String classid = null;	
	private String codebase = null;
	private String downloadFileStatus = null;
	private boolean agentRegistered = false;
	private boolean oldDownloadControl = false;

	private String statusTest = DownloadStatus.TEST.getCode();
	private String statusCurrent = DownloadStatus.CURRENT.getCode();
	
	private CarrierInfo carrierInfo = null;

	
	public DownloadDisplayBean()
	{
		super();
		
		// Assume downloading ALL tagged (non-archived) files
		downloadFileStatus = "";
	}
	
	/**
	 * @see connective.teamup.download.DisplayBean#init(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public void init(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws DisplayBeanException 
	{
		try
		{
			// Load carrier info
			carrierInfo = serverInfo.getCarrierInfo();
			
			// Load agency info
			AgentInfo info = serverInfo.getAgentInfo(req.getSession(), op);
			agentId = info.getAgentId();
			agentRegistered = info.isRegistered();
			downloadDir = info.getRemoteDir();
			if (info.isInteractive())
				interactiveFlag = "Y";
			else
				interactiveFlag = "N";
			this.appendFlag = (info.getAms().isAppendFlag() ? "Y" : "N");
			
			// Load the download control identifiers
			try
			{
				// If vendor system requires multiple files to be combined during
				// download, OR if vendor system download filename is defined
				// using "###" to indicate the position for incrementing the file
				// count, then use the newer download control.  Otherwise, use the
				// old control. -- 07/27/2004, kwm
				if (info.getAms().isAppendFlag() ||
					info.getAms().getCompanyFilename().indexOf("###") >= 0)
				{
					this.classid = serverInfo.getDownloadClassid();
					this.codebase = serverInfo.getDownloadCodebase();
				}
				else if (carrierInfo.isNewDownloadControlUsed())		// added 06/23/2005, kwm
				{
					this.oldDownloadControl = true;		// don't include the "append" parameter
					this.classid = serverInfo.getNewDownloadClassid();
					this.codebase = serverInfo.getNewDownloadCodebase();
				}
				else
				{
					this.classid = serverInfo.getOldDownloadClassid();
					this.codebase = serverInfo.getOldDownloadCodebase();
				}
			} catch (Exception e) {
				LOGGER.error(e);
			}
			if (this.classid == null || this.codebase == null)
			{
				this.classid = serverInfo.getDownloadClassid();
				this.codebase = serverInfo.getDownloadCodebase();
			}
			
			// Get the encrypted key
			key = serverInfo.getSecurityProvider().getSecurityKey(info.getAgentId(), info.getAgentId(), info.getPassword());
			
			// Build the fileSource URL from the request
			fileSource = serverInfo.getRequestUrl(req) + "?";
			
			// Build the forwarding url (only used for the download test)
			String keyName = "key";
				//keyName = serverInfo.getPropertyValue(serverInfo.PROP_SECURITY_PASSWORD);
			//testForwardUrl = fileSource + "action=dltest_next&" + keyName + "=" + key;
			testForwardUrl = fileSource + "action=dltest_next";
			
			// Build the forwarding url (used for a scheduled download)
			dlCompleteForwardUrl = "";
			if (info.isLive() && !info.isInteractive())
				dlCompleteForwardUrl = fileSource + "action=dlsched_complete";
		}
		catch (Exception e)
		{
			LOGGER.error("Error retrieving agency info for download", e);
			throw new DisplayBeanException("Error retrieving agency info for download", e);
		}
	}
		
	/**
	 * Returns the agentId.
	 * @return String
	 */
	public String getAgentId()
	{
		return agentId;
	}

	/**
	 * Returns the forwarding URL used for a successful scheduled (non-interactive) download.
	 * @return String
	 */
	public String getDlCompleteForwardUrl()
	{
		return dlCompleteForwardUrl;
	}

	/**
	 * Returns the downloadDir.
	 * @return String
	 */
	public String getDownloadDir()
	{
		if (downloadDir == null)
			return "";
		
		return downloadDir;
	}

	/**
	 * Returns the key.
	 * @return String
	 */
	public String getKey()
	{
		if (key == null)
			return "";
		
		return key;
	}

	/**
	 * Returns the fileSource.
	 * @return String
	 */
	public String getFileSource()
	{
		return fileSource;
	}

	/**
	 * Returns the interactiveFlag.
	 * @return String
	 */
	public String getInteractiveFlag()
	{
		return interactiveFlag;
	}

	/**
	 * Sets the agentId.
	 * @param agentId The agentId to set
	 */
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	/**
	 * Sets the downloadDir.
	 * @param downloadDir The downloadDir to set
	 */
	public void setDownloadDir(String downloadDir) {
		this.downloadDir = downloadDir;
	}

	/**
	 * Sets the fileSource.
	 * @param fileSource The fileSource to set
	 */
	public void setFileSource(String fileSource) {
		this.fileSource = fileSource;
	}

	/**
	 * Sets the interactiveFlag.
	 * @param interactiveFlag The interactiveFlag to set
	 */
	public void setInteractiveFlag(String interactiveFlag) {
		this.interactiveFlag = interactiveFlag;
	}

	/**
	 * Sets the key.
	 * @param key The key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Returns the classid.
	 * @return String
	 */
	public String getClassid() {
		return classid;
	}

	/**
	 * Returns the codebase.
	 * @return String
	 */
	public String getCodebase() {
		return codebase;
	}

	/**
	 * Returns the carrier contact email address.
	 * @return String
	 */
	public String getCarrierEmail()
	{
		return carrierInfo.getContactEmail();
	}

	/**
	 * Returns the carrier's full name.
	 * @return String
	 */
	public String getCarrierName()
	{
		return carrierInfo.getName();
	}

	/**
	 * Returns the carrier short name.
	 * @return String
	 */
	public String getCarrierShortName()
	{
		return carrierInfo.getShortName();
	}

	/**
	 * Returns the banner graphic filename.
	 * @return String
	 */
	public String getBannerGraphic()
	{
		return carrierInfo.getBannerGraphicFile();
	}

	/**
	 * Returns the carrier info bean.
	 * @return CarrierInfo
	 */
	public CarrierInfo getCarrierInfo()
	 {
		return carrierInfo;
	}

	/**
	 * Returns the forwarding URL used for a successful download test.
	 * @return String
	 */
	public String getTestForwardUrl()
	{
		return testForwardUrl;
	}

	/**
	 * Returns true if the agent has previously completed the registration process.
	 * @return boolean
	 */
	public boolean isAgentRegistered()
	{
		return agentRegistered;
	}

	/**
	 * Returns the file status to be downloaded.
	 * @return String
	 */
	public String getDownloadFileStatus()
	{
		return downloadFileStatus;
	}
	
	/**
	 * Returns the title for the download page dependent on the file status to be downloaded.
	 * @return String
	 */
	public String getDownloadPageTitle()
	{
		String title = "Download Files";
		if (downloadFileStatus.equals(DownloadStatus.CURRENT.getCode()))
			title = "Download Current Files";
		else if (downloadFileStatus.equals(DownloadStatus.POLICY_DOWNLOAD.getCode()))
			title = "Download Archived Files";
		else if (downloadFileStatus.equals(DownloadStatus.RESEND.getCode()))
			title = "Resend Selected Files";
		else if (downloadFileStatus.equals(DownloadStatus.TEST.getCode()))
			title = "Download Test File";
		
		return title;
	}

	/**
	 * Sets the file status to be downloaded (blank or null to download all flagged files regardless of status).
	 * @param downloadFileStatus The download file status to set
	 */
	public void setDownloadFileStatus(DownloadStatus downloadFileStatus)
	{
		if (downloadFileStatus == null)
			this.downloadFileStatus = "";
		else
			this.downloadFileStatus = downloadFileStatus.getCode();
	}

	/**
	 * Returns the download status code for "Current".
	 * @return String
	 */
	public String getStatusCurrent()
	{
		return statusCurrent;
	}

	/**
	 * Returns the download status code for "Testing".
	 * @return String
	 */
	public String getStatusTest()
	{
		return statusTest;
	}

	/**
	 * Returns true if the page is using the old (pre-07/2004) download control.
	 * (The old control does not use the "append" control parameter.)
	 * @return boolean
	 */
	public boolean isOldDownloadControl()
	{
		return oldDownloadControl;
	}

	/**
	 * Returns a Y/N flag determining whether or not to append downloaded files
	 * if multiple or if a file already exists with the same name.
	 * @return String
	 */
	public String getAppendFlag()
	{
		return appendFlag;
	}

}
