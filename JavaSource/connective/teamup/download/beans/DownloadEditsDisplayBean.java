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
 * @author Kyle McCreary
 *
 * Display bean used to create the page to hold the ActiveX control for
 * downloading Applied edits files.
 */
public class DownloadEditsDisplayBean implements DisplayBean, Serializable
{
	private static final Logger LOGGER = Logger.getLogger(DownloadEditsDisplayBean.class);
	
	private String agentId = null;
	private String key = null;
	private String downloadDir = null;
	private String fileSource = null;
	private String dlCompleteForwardUrl = null;
	private String interactiveFlag = null;
	private String classid = null;	
	private String codebase = null;
	private String downloadFileStatus = null;
	
	private CarrierInfo carrierInfo = null;

	
	public DownloadEditsDisplayBean()
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
			//downloadDir = info.getRemoteDir();
			interactiveFlag = "Y";
			
			// Load the download control identifiers
			classid = serverInfo.getDownloadEditsClassid();
			codebase = serverInfo.getDownloadEditsCodebase();
			
			// Get the encrypted key
			key = serverInfo.getSecurityProvider().getSecurityKey(info.getAgentId(), info.getAgentId(), info.getPassword());
			
			// Build the fileSource URL from the request
			fileSource = serverInfo.getRequestUrl(req) + "?";
			
			// Build the forwarding url (used for a scheduled download)
			dlCompleteForwardUrl = "";
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
	 * Returns the file status to be downloaded.
	 * @return String
	 */
	public String getDownloadFileStatus()
	{
		return downloadFileStatus;
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

}
