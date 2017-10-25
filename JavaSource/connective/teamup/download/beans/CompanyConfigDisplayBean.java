package connective.teamup.download.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.CarrierInfo;
import connective.teamup.download.DisplayBean;
import connective.teamup.download.DisplayBeanException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author Kyle McCreary
 *
 * Display bean for the Carrier Configuration Settings page (also page 3 of
 * the TEAM-UP Download Configuration Wizard).
 */
public class CompanyConfigDisplayBean implements DisplayBean, Serializable
{
	private static final Logger LOGGER = Logger.getLogger(CompanyConfigDisplayBean.class);
	
	private CarrierInfo carrierInfo = null;
	private String servletPath = null;
	private boolean configWizard = false;	// Is this being displayed as part of the Configuration Wizard?
	
	// fields for Config Wizard page 2
	private String carrierId = "";
	private String carrierName = "";
	private String carrierShortName = "";
//	private String customFilesPath = "C:\\teamup\\download\\";		// default value
//	private String bannerGraphic = "";
	private String bannerGraphicHeight = "";
//	private String smtpServerAddress = "";
//	private String senderEmail = "";
	private String securityProvider = "connective.teamup.download.KeySecurityProvider";	// default value
	private String securityUser = "agent";			// default value
	private String securityPassword = "key";		// default value
	private String importFileFormat = "AL3";		// default value
	private String importFileCreator = "";
	private String importFileIdMode = "";
	private String appServer = "";
	private String importBlocksize = "";
	private String scheduledDLPort = "";
	private String clientAppFlag = "Y";
	private String agentLoginDisabledFlag = "N";
	private String agentLogoutUrl = "";
	private String bannerImportUrl = "";
	private String useNewDLControlFlag = "N";		// default value
	private String displayMigrationBannerFlag = "N"; //default value
	private String defaultTPListView = ServerInfo.TPLIST_ALL;	// default value (original view)
	private boolean invalidGraphicFile = false;
	private boolean invalidFilesPath = false;
	
	// fields for Config Wizard page 3 (download config settings page)
	private boolean autoPurge = true;
	private boolean excludeLob = false;
	private int numArchiveDays = 90;				// default value
	private String sourcePath = "";
	private String deleteAllFilesFlag = "N";		// default value
	private String testfile = "";
	private String contactEmail = "";
	private String errorsEmail = "";
	private String reportsEmail = "";
	private String miscreportsEmail = "";
	private boolean invalidTestfile = false;
	
	// fields for Config Wizard page 4 (advanced options page)
	private String agencyRegistrationFlag = "Y";	// default value
	private String agencyMigrationFlag = "Y";		// default value
	private String agencyStatusChangeFlag = "Y";	// default value
	private String agencyVendorChangeFlag = "N";	// default value
	private String downloadErrorsFlag = "Y";		// default value
	private String importErrorsFlag = "Y";			// default value
	private String passwordChangeFlag = "Y";		// default value
	private String agentInfoChangeFlag = "Y";		// default value
	private String displayLoginShortcutCtrl = "Y";	// default value
	private String emailAsHtml = "Y";				// default value
//	private String fileUrlUpdate = "";
//	private String fileNewAgent = "";
//	private String fileAgencyRegistered = "";
//	private String fileStatusChanged = "";
//	private String fileWelcome = "";
	private String customFileImportUrl = "";
	
	// fields for Config Wizard page 5 (Download Alert! page)
//	private String fileDLFailure = "";
//	private String fileDLStale = "";
	private String schedClassid;
	private String schedCodebase;
	private String dlAlertLink;
	private int dlFailureDays = 1;
	private int dlStaleDays = 5;
	private boolean dlAlertFlag = false;
	private boolean dlAlertCarrierFlag = false;
	private String dlSchedHour;
	private String dlSchedMinute;
	private String dlSchedAmpM;
	private boolean cliamXMLImportAllowed = false;
	private boolean policyXMLImportAllowed = false;
	
	private String downloadReptSchedHour;
	private String downloadReptSchedMinute;
	private String downloadReptSchedAmpM;
	private String downloadReptActType;
	private String downloadReptFileType;
	private boolean downloadReptCarrierFlag = false;
	private String downloadReptAttachType;
	private boolean sendDetailReptFlag = false;
	
	public static List<KeyValueBean> actList = new ArrayList<KeyValueBean>();
	public static List<KeyValueBean> fileList = new ArrayList<KeyValueBean>();
	public static List<KeyValueBean> fileTypeList = new ArrayList<KeyValueBean>();
	static {
		actList.add(new KeyValueBean("","ALL"));
		actList.add(new KeyValueBean("D","Download"));
		actList.add(new KeyValueBean("I","Import"));
		
		fileList.add(new KeyValueBean("","ALL"));
		fileList.add(new KeyValueBean("POLICY","Policy"));
		fileList.add(new KeyValueBean("CLAIM","Claim"));
		
		
		fileTypeList.add(new KeyValueBean("P","Pdf"));
		fileTypeList.add(new KeyValueBean("X","Excel"));
		fileTypeList.add(new KeyValueBean("C","CSV"));
	}
	
	/**
	 * Constructor for CompanyConfigDisplayBean.
	 */
	public CompanyConfigDisplayBean()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.DisplayBean#init(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public void init(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws DisplayBeanException 
	{
		try
		{
			// Load the carrier info and default settings
			carrierInfo = serverInfo.getCarrierInfo();
//			customFilesPath = serverInfo.getConfigDir();
			
			// Get the current servlet path from the request
			servletPath = req.getServletPath();
			
			// Load the registration control attributes
			this.schedClassid = serverInfo.getRegControlClassid("scheduler");
			this.schedCodebase = serverInfo.getRegControlCodebase("scheduler");
			
			// Build urls using info from the request
			dlAlertLink = serverInfo.getRequestUrl(req, "/import");
			dlAlertLink += "?action=download_report";
			bannerImportUrl = serverInfo.getRequestUrl(req, "/resource");
			bannerImportUrl += "?id=banner_config";
			customFileImportUrl = serverInfo.getRequestUrl(req, "/resource");
			customFileImportUrl += "?id=template_config";
			
			// Load the configuration values from the database
			Hashtable props = op.getProperties();
			Enumeration keys = props.keys();
			while (keys.hasMoreElements())
			{
				String name = (String) keys.nextElement();
				String value = (String) props.get(name);
				
				if (name == null || value == null)
				{
					// ignore
				}
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_AGENT_INFO_CHANGE))
				{
					this.agentInfoChangeFlag = value;
				}
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_AGENT_LOGIN_DISABLED))
				{
					if (value == null || value.equals(""))
						value = "N";
					this.agentLoginDisabledFlag = value;
				}
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_AGENT_LOGOUT_URL))
				{
					this.agentLogoutUrl = value;
				}
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_AGENT_PASSWORD_CHANGE))
				{
					this.passwordChangeFlag = value;
				}
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_ARCHIVE_PERIOD) )
				{
					try
					{
						this.numArchiveDays = Integer.parseInt(value);
					}
					catch (Exception e) {
						LOGGER.error(e);
					}
				}
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_AUTO_PURGE))
				{
					this.autoPurge = value.equals("Y");
				}
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_EXCLUDE_LOB))
				{
					this.excludeLob = value.equals("Y");
				}
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_CLAIM_XML_ALLOWED))
				{
					this.cliamXMLImportAllowed = value.equals("Y");
				}
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_POLICY_XML_ALLOWED))
				{
					this.policyXMLImportAllowed = value.equals("Y");
				}
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_BANNER_GRAPHIC_HEIGHT))
				{
					this.bannerGraphicHeight = value;
				}
/*				else if (name.equalsIgnoreCase(ServerInfo.PROP_CARRIER_LOGO_FILENAME))
				{
					this.bannerGraphic = value;
					
					String configDir = serverInfo.getConfigDir();
					int n = configDir.length();
					if (value != null && value.length() > n && value.substring(0, n).equalsIgnoreCase(configDir))
					{
						// Graphic file is located in the config directory; don't
						// show the full path name
						this.bannerGraphic = value.substring(n);
					}
				}
*/
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_CARRIER_ID))
				{
					this.carrierId = value;
				}
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_CARRIER_NAME))
				{
					this.carrierName = value;
				}
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_CARRIER_SHORTNAME))
				{
					this.carrierShortName = value;
				}
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_CLIENT_APP_USED))
				{
					this.clientAppFlag = value;
				}
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_CONTROL_SHORTCUT_LOGIN_ALLOWED))
				{
					this.displayLoginShortcutCtrl = value;
				}
//				else if (name.equalsIgnoreCase(ServerInfo.PROP_CUSTOM_FILES_PATH))
//				{
//					this.customFilesPath = value;
//				}
/*				else if (name.equalsIgnoreCase(ServerInfo.PROP_CUSTOM_DL_FAILED))
				{
					this.fileDLFailure = value;
					
					String configDir = serverInfo.getConfigDir();
					int n = configDir.length();
					if (value != null && value.length() > n && value.substring(0, n).equalsIgnoreCase(configDir))
					{
						// Custom file is located in the config directory; don't
						// show the full path name
						this.fileDLFailure = value.substring(n);
					}
				}
				else if (name.equalsIgnoreCase(ServerInfo.PROP_CUSTOM_DL_STALE))
				{
					this.fileDLStale = value;
					
					String configDir = serverInfo.getConfigDir();
					int n = configDir.length();
					if (value != null && value.length() > n && value.substring(0, n).equalsIgnoreCase(configDir))
					{
						// Custom file is located in the config directory; don't
						// show the full path name
						this.fileDLStale = value.substring(n);
					}
				}
				else if (name.equalsIgnoreCase(ServerInfo.PROP_CUSTOM_DL_URL_CHANGE))
				{
					this.fileUrlUpdate = value;
					
					String configDir = serverInfo.getConfigDir();
					int n = configDir.length();
					if (value != null && value.length() > n && value.substring(0, n).equalsIgnoreCase(configDir))
					{
						// Custom file is located in the config directory; don't
						// show the full path name
						this.fileUrlUpdate = value.substring(n);
					}
				}
				else if (name.equalsIgnoreCase(ServerInfo.PROP_CUSTOM_NEW_AGENT))
				{
					this.fileNewAgent = value;
					
					String configDir = serverInfo.getConfigDir();
					int n = configDir.length();
					if (value != null && value.length() > n && value.substring(0, n).equalsIgnoreCase(configDir))
					{
						// Custom file is located in the config directory; don't
						// show the full path name
						this.fileNewAgent = value.substring(n);
					}
				}
				else if (name.equalsIgnoreCase(ServerInfo.PROP_CUSTOM_REGISTERED))
				{
					this.fileAgencyRegistered = value;
					
					String configDir = serverInfo.getConfigDir();
					int n = configDir.length();
					if (value != null && value.length() > n && value.substring(0, n).equalsIgnoreCase(configDir))
					{
						// Custom file is located in the config directory; don't
						// show the full path name
						this.fileAgencyRegistered = value.substring(n);
					}
				}
				else if (name.equalsIgnoreCase(ServerInfo.PROP_CUSTOM_STATUS_CHANGE))
				{
					this.fileStatusChanged = value;
					
					String configDir = serverInfo.getConfigDir();
					int n = configDir.length();
					if (value != null && value.length() > n && value.substring(0, n).equalsIgnoreCase(configDir))
					{
						// Custom file is located in the config directory; don't
						// show the full path name
						this.fileStatusChanged = value.substring(n);
					}
				}
				else if (name.equalsIgnoreCase(ServerInfo.PROP_CUSTOM_WELCOME))
				{
					this.fileWelcome = value;
					
					String configDir = serverInfo.getConfigDir();
					int n = configDir.length();
					if (value != null && value.length() > n && value.substring(0, n).equalsIgnoreCase(configDir))
					{
						// Custom file is located in the config directory; don't
						// show the full path name
						this.fileWelcome = value.substring(n);
					}
				}
*/				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_DELETE_ALL_FILES_ON_IMPORT))
				{
					this.deleteAllFilesFlag = value;
				}
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_DISPLAY_MIGRATION_BANNER))
				{
					this.displayMigrationBannerFlag = value;
				}
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_DL_ALERT_FLAG))
				{
					this.dlAlertFlag = value.equalsIgnoreCase("Y");
				}
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_DL_ALERT_CARRIER_FLAG))
				{
					this.dlAlertCarrierFlag = value.equalsIgnoreCase("Y");
				}
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_DL_FAILED_DAYS))
				{
					this.dlFailureDays = Integer.parseInt(value);
				}
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_DL_STALE_DAYS))
				{
					this.dlStaleDays = Integer.parseInt(value);
				}
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_EMAIL_AS_HTML))
				{
					this.emailAsHtml = value;
				}
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_EMAIL_CONTACT))
				{
					this.contactEmail = value;
				}
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_EMAIL_ERRORS))
				{
					this.errorsEmail = value;
				}
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_EMAIL_REPORTS))
				{
					this.reportsEmail = value;
				}
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_MISC_EMAIL_REPORTS))
				{
					this.miscreportsEmail = value;	
					
				}
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_IMPORT_BLOCK_SIZE))
				{
					if (!value.equals("0"))
						this.importBlocksize = value;
				}
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_IMPORT_FILE_CREATION_PROCESS))
				{
					this.importFileCreator = value;
				}
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_IMPORT_FILE_FORMAT))
				{
					this.importFileFormat = value;
				}
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_IMPORT_FILE_IDENTIFICATION))
				{
					this.importFileIdMode = value;
				}
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_IMPORT_SOURCE_PATH))
				{
					this.sourcePath = value;
				}
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_NOTIFY_ON_AGENCY_MIGRATION))
				{
					agencyMigrationFlag = value;
				}
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_NOTIFY_ON_AGENCY_REGISTRATION))
				{
					agencyRegistrationFlag = value;
				}
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_NOTIFY_ON_AGENCY_STATUS_CHANGE))
				{
					agencyStatusChangeFlag = value;
				}
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_NOTIFY_ON_AGENCY_VENDOR_CHANGE))
				{
					agencyVendorChangeFlag = value;
				}
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_NOTIFY_ON_DOWNLOAD_ERROR))
				{
					downloadErrorsFlag = value;
				}
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_NOTIFY_ON_IMPORT_ERROR))
				{
					importErrorsFlag = value;
				}
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_SCHEDULED_DL_PORT))
				{
					scheduledDLPort = value;
				}
//				else if (name.equalsIgnoreCase(ServerInfo.PROP_SECURITY_PASSWORD))
//				{
//					this.securityPassword = value;
//				}
//				else if (name.equalsIgnoreCase(ServerInfo.PROP_SECURITY_PROVIDER))
//				{
//					this.securityProvider = value;
//				}
//				else if (name.equalsIgnoreCase(ServerInfo.PROP_SECURITY_USER))
//				{
//					this.securityUser = value;
//				}
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_SERVER_APP_SOFTWARE))
				{
					this.appServer = value;
				}
/*				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_SMTP_SENDER_ADDRESS))
				{
					this.senderEmail = value;
				}
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_SMTP_SERVER))
				{
					this.smtpServerAddress = value;
				}
				else if (name.equalsIgnoreCase(ServerInfo.PROP_TESTFILE))
				{
					this.testfile = value;
				}
*/
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_TPLIST_VIEW))
				{
					this.defaultTPListView = value;
				}
				else if (name.equalsIgnoreCase(DatabaseFactory.PROP_USE_NEW_DL_CONTROL))
				{
					this.useNewDLControlFlag = value;
				}else if (name.equalsIgnoreCase(DatabaseFactory.PROP_DL_SCHED_HOUR))
				{
					this.dlSchedHour = value;
				}else if (name.equalsIgnoreCase(DatabaseFactory.PROP_DL_SCHED_MINUTE))
				{
					this.dlSchedMinute = value;
				}else if (name.equalsIgnoreCase(DatabaseFactory.PROP_DL_SCHED_AMPM))
				{
					this.dlSchedAmpM = value;
				}else if (name.equalsIgnoreCase(DatabaseFactory.PROP_DOWNLOAD_REPORT_SCHED_HOUR))
				{
					this.downloadReptSchedHour = value;
				}else if (name.equalsIgnoreCase(DatabaseFactory.PROP_DOWNLOAD_REPORT_SCHED_MINUTE))
				{
					this.downloadReptSchedMinute = value;
				}else if (name.equalsIgnoreCase(DatabaseFactory.PROP_DOWNLOAD_REPORT_SCHED_AMPM))
				{
					this.downloadReptSchedAmpM = value;
				}else if (name.equalsIgnoreCase(DatabaseFactory.PROP_DOWNLOAD_REPORT_ACTTYPE))
				{
					this.downloadReptActType = value;
				}else if (name.equalsIgnoreCase(DatabaseFactory.PROP_DOWNLOAD_REPORT_FILETYPE))
				{
					this.downloadReptFileType = value;
				}else if (name.equalsIgnoreCase(DatabaseFactory.PROP_DOWNLOAD_REPORT_CARRIER_FLAG))
				{
					this.downloadReptCarrierFlag = value.equalsIgnoreCase("Y");
				}else if (name.equalsIgnoreCase(DatabaseFactory.PROP_DOWNLOAD_REPORT_ATTACH_TYPE))
				{
					this.downloadReptAttachType = value;
				}else if (name.equalsIgnoreCase(DatabaseFactory.PROP_DOWNLOAD_REPORT_DETAIL_FLAG))
				{
					this.sendDetailReptFlag = value.equalsIgnoreCase("Y");
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error building carrier configuration display bean", e);
			throw new DisplayBeanException("Error building carrier configuration display bean", e);
		}
	}
	
	/**
	 * Returns the specified string escaped for HTML.
	 * @param text The text string to convert
	 * @return String
	 */
	public String escapeForHtml(String text)
	{
		StringBuffer str = new StringBuffer("");
		
		if (text != null)
		{
			for (int i=0; i < text.length(); i++)
			{
				char c = text.charAt(i);
				if (c == '\\')
					str.append("\\\\");
				else if (c == '"')
					str.append("&quot;");
				else
					str.append(c);
			}
		}
		return str.toString();
	}

	/**
	 * Returns the autoPurge flag.
	 * @return boolean
	 */
	public boolean isAutoPurge()
	{
		return autoPurge;
	}
	
	/**
	 * Returns the excludeLob flag.
	 * @return boolean
	 */
	public boolean isCliamXMLImportAllowed() {
		return cliamXMLImportAllowed;
	}

	/**
	 * Returns the number of days to keep (archive) downloaded files.
	 * @return int
	 */
	public int getNumArchiveDays()
	{
		return numArchiveDays;
	}

	/**
	 * Returns the import source path.
	 * @return String
	 */
	public String getSourcePath()
	{
		return sourcePath;
	}

	/**
	 * Returns the testfile.
	 * @return String
	 */
	public String getTestfile()
	{
		return testfile;
	}

	/**
	 * Returns the banner graphic filename.
	 * @return String
	 */
//	public String getBannerGraphic()
//	{
//		return bannerGraphic;
//	}

	/**
	 * Returns the carrier's contact email address.
	 * @return String
	 */
	public String getContactEmail()
	{
		if (contactEmail == null)
			return "";
		else
			return contactEmail;
	}

	/**
	 * Returns the carrier's unique registration ID.
	 * @return String
	 */
	public String getCarrierId()
	{
		return carrierId;
	}

	/**
	 * Returns the carrier's display name.
	 * @return String
	 */
	public String getCarrierName()
	{
		return carrierName;
	}

	/**
	 * Returns the carrier's short name.
	 * @return String
	 */
	public String getCarrierShortName()
	{
		return carrierShortName;
	}

	/**
	 * Returns the errors email address.
	 * @return String
	 */
	public String getErrorsEmail()
	{
		return errorsEmail;
	}

	/**
	 * Returns the reports email address.
	 * @return String
	 */
	public String getReportsEmail()
	{
		return reportsEmail;
	}

	/**
	 * Returns the invalidTestfile flag.
	 * @return boolean
	 */
	public boolean isInvalidTestfile()
	{
		return invalidTestfile;
	}

	/**
	 * Sets the invalidTestfile flag.
	 * @param invalidTestfile The flag value to set
	 */
	public void setInvalidTestfile(boolean invalidTestfile)
	{
		this.invalidTestfile = invalidTestfile;
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
	 * Returns true if page is being displayed as part of the TEAM-UP Download
	 * Configuration Wizard; false if displayed from the Carrier Admin menu bar.
	 * @return boolean
	 */
	public boolean isConfigWizard()
	{
		return configWizard;
	}
	
	/**
	 * Returns flag value ("Y" or "N") to indicate whether page is being displayed
	 * as part of the TEAM-UP Download Configuration Wizard.
	 * @return String
	 */
	public String getConfigWizardFlag()
	{
		if (configWizard)
			return "Y";
		else
			return "N";
	}

	/**
	 * Sets the configWizard flag.
	 * @param configWizard True if part of the configuration wizard
	 */
	public void setConfigWizard(boolean configWizard)
	{
		this.configWizard = configWizard;
	}

	/**
	 * Returns the agencyMigrationFlag.
	 * @return String
	 */
	public String getAgencyMigrationFlag() {
		return agencyMigrationFlag;
	}

	/**
	 * Returns the agencyRegistrationFlag.
	 * @return String
	 */
	public String getAgencyRegistrationFlag() {
		return agencyRegistrationFlag;
	}

	/**
	 * Returns the agencyStatusChangeFlag.
	 * @return String
	 */
	public String getAgencyStatusChangeFlag() {
		return agencyStatusChangeFlag;
	}

	/**
	 * Returns the customFilesPath.
	 * @return String
	 */
/*	public String getCustomFilesPath() {
		return customFilesPath;
	}
*/
	/**
	 * Returns the fileAgencyRegistered.
	 * @return String
	 */
/*	public String getFileAgencyRegistered() {
		return fileAgencyRegistered;
	}
*/
	/**
	 * Returns the fileNewAgent.
	 * @return String
	 */
/*	public String getFileNewAgent() {
		return fileNewAgent;
	}
*/
	/**
	 * Returns the fileStatusChanged.
	 * @return String
	 */
/*	public String getFileStatusChanged() {
		return fileStatusChanged;
	}
*/
	/**
	 * Returns the fileUrlUpdate.
	 * @return String
	 */
/*	public String getFileUrlUpdate() {
		return fileUrlUpdate;
	}
*/
	/**
	 * Returns the fileWelcome.
	 * @return String
	 */
/*	public String getFileWelcome() {
		return fileWelcome;
	}
*/
	/**
	 * Returns the importErrorsFlag.
	 * @return String
	 */
	public String getImportErrorsFlag() {
		return importErrorsFlag;
	}

	/**
	 * Returns the invalidFilesPath.
	 * @return boolean
	 */
	public boolean isInvalidFilesPath() {
		return invalidFilesPath;
	}

	/**
	 * Returns the invalidGraphicFile.
	 * @return boolean
	 */
	public boolean isInvalidGraphicFile() {
		return invalidGraphicFile;
	}

	/**
	 * Returns the securityPassword.
	 * @return String
	 */
	public String getSecurityPassword() {
		return securityPassword;
	}

	/**
	 * Returns the securityProvider.
	 * @return String
	 */
	public String getSecurityProvider() {
		return securityProvider;
	}

	/**
	 * Returns the securityUser.
	 * @return String
	 */
	public String getSecurityUser() {
		return securityUser;
	}

//	/**
//	 * Returns the senderEmail.
//	 * @return String
//	 */
//	public String getSenderEmail() {
//		return senderEmail;
//	}

//	/**
//	 * Returns the smtpServerAddress.
//	 * @return String
//	 */
//	public String getSmtpServerAddress() {
//		return smtpServerAddress;
//	}

	/**
	 * Sets the invalidFilesPath.
	 * @param invalidFilesPath The invalidFilesPath to set
	 */
	public void setInvalidFilesPath(boolean invalidFilesPath) {
		this.invalidFilesPath = invalidFilesPath;
	}

	/**
	 * Sets the invalidGraphicFile.
	 * @param invalidGraphicFile The invalidGraphicFile to set
	 */
	public void setInvalidGraphicFile(boolean invalidGraphicFile) {
		this.invalidGraphicFile = invalidGraphicFile;
	}

	/**
	 * Returns the servletPath.
	 * @return String
	 */
	public String getServletPath() {
		return servletPath;
	}

	/**
	 * Returns the importFileCreator.
	 * @return String
	 */
	public String getImportFileCreator() {
		return importFileCreator;
	}

	/**
	 * Returns the importFileFormat.
	 * @return String
	 */
	public String getImportFileFormat() {
		return importFileFormat;
	}

	/**
	 * Returns the importFileIdMode.
	 * @return String
	 */
	public String getImportFileIdMode() {
		return importFileIdMode;
	}

	/**
	 * Returns the downloadErrorsFlag.
	 * @return String
	 */
	public String getDownloadErrorsFlag() {
		return downloadErrorsFlag;
	}

	/**
	 * Returns the deleteAllFilesFlag.
	 * @return String
	 */
	public String getDeleteAllFilesFlag() {
		return deleteAllFilesFlag;
	}

	/**
	 * Returns the importBlocksize.
	 * @return String
	 */
	public String getImportBlocksize() {
		return importBlocksize;
	}

	/**
	 * Returns the appServer.
	 * @return String
	 */
	public String getAppServer() {
		return appServer;
	}

	/**
	 * Returns the passwordChangeFlag.
	 * @return String
	 */
	public String getPasswordChangeFlag() {
		return passwordChangeFlag;
	}
	
	/**
	 * Returns the dlAlertFlag.
	 * @return boolean
	 */
	public boolean getDlAlertFlag() {
		return dlAlertFlag;
	}
	
	/**
	 * Returns the dlAlertCarrierFlag.
	 * @return boolean
	 */
	public boolean getDlAlertCarrierFlag() {
		return dlAlertCarrierFlag;
	}

	/**
	 * Returns the dlFailureDays.
	 * @return int
	 */
	public int getDlFailureDays() {
		return dlFailureDays;
	}

	/**
	 * Returns the dlStaleDays.
	 * @return int
	 */
	public int getDlStaleDays() {
		return dlStaleDays;
	}

	/**
	 * Returns the fileDLFailure.
	 * @return String
	 */
/*	public String getFileDLFailure() {
		return fileDLFailure;
	}
*/
	/**
	 * Returns the fileDLStale.
	 * @return String
	 */
/*	public String getFileDLStale() {
		return fileDLStale;
	}
*/
	/**
	 * Returns the dlAlertLink.
	 * @return String
	 */
	public String getDlAlertLink() {
		return dlAlertLink;
	}

	/**
	 * Returns the schedClassid.
	 * @return String
	 */
	public String getSchedClassid() {
		return schedClassid;
	}

	/**
	 * Returns the schedCodebase.
	 * @return String
	 */
	public String getSchedCodebase() {
		return schedCodebase;
	}

	/**
	 * Returns the custom scheduled download port; blank to use default port.
	 * @return String
	 */
	public String getScheduledDLPort()
	{
		return scheduledDLPort;
	}

	/**
	 * Returns 'Y/N' whether or not to allow agents to change their own contact
	 * and vendor system info.
	 * @return String
	 */
	public String getAgentInfoChangeFlag()
	{
		return agentInfoChangeFlag;
	}

	/**
	 * Returns 'Y/N' whether or not to disable the default TEAM-UP Download log-in
	 * page for the agency app.
	 * @return String
	 */
	public String getAgentLoginDisabledFlag()
	{
		if (agentLoginDisabledFlag == null)
			return "";
		return agentLoginDisabledFlag;
	}

	/**
	 * Returns the page URL to use when the user clicks the "LOG-OFF" link in the
	 * agency app.
	 * @return String
	 */
	public String getAgentLogoutUrl()
	{
		if (agentLogoutUrl == null)
			return "";
		return agentLogoutUrl;
	}

	/**
	 * Returns 'Y/N' whether or not to display the ActiveX control for creating
	 * a desktop shortcut to the agency app login page in the agency app.
	 * @return String
	 */
	public String getDisplayLoginShortcutCtrl()
	{
		return displayLoginShortcutCtrl;
	}

	/**
	 * Returns 'Y/N' whether or not to email the carrier when the agent changes
	 * their agency vendor system or version settings.
	 * @return String
	 */
	public String getAgencyVendorChangeFlag()
	{
		return agencyVendorChangeFlag;
	}

	/**
	 * Returns the banner graphic height (in pixels), if set to other than the default.
	 * @return String
	 */
	public String getBannerGraphicHeight()
	{
		return bannerGraphicHeight;
	}

	/**
	 * Returns the URL to use for importing a new banner graphic file.
	 * @return String
	 */
	public String getBannerImportUrl()
	{
		if (bannerImportUrl == null)
			return "";
		return bannerImportUrl;
	}

	/**
	 * Returns the URL to use for importing new data for a custom file template.
	 * @return String
	 */
	public String getCustomFileImportUrl()
	{
		if (customFileImportUrl == null)
			return "";
		return customFileImportUrl;
	}

	/**
	 * Returns 'Y/N' whether or not to have agents use the new (06/23/2005) download control
	 * (for downloading more than 99 files at a time).
	 * @return String
	 */
	public String getUseNewDLControlFlag()
	{
		return useNewDLControlFlag;
	}

	/**
	 * Returns 'Y/N' whether or not to display the migration banner for agents to request
	 * the migration email.
	 * @return String
	 */
	public String getDisplayMigrationBannerFlag()
	{
		return displayMigrationBannerFlag;
	}

	/**
	 * Returns 'Y/N' whether agents are expected to use the new client app or not.
	 * @return String
	 */
	public String getClientAppFlag()
	{
		if (clientAppFlag == null)
			return "";
		return clientAppFlag;
	}

	/**
	 * Returns the default Trading Partner List view.
	 * @return String
	 */
	public String getDefaultTPListView()
	{
		return defaultTPListView;
	}

	/**
	 * Returns 'Y/N' whether to send automatic notification emails as HTML (where applicable).
	 * @return String
	 */
	public String getEmailAsHtml()
	{
		return emailAsHtml;
	}

	public String getDlSchedHour() {
		return dlSchedHour;
	}

	public String getDlSchedMinute() {
		return dlSchedMinute;
	}

	public String getDlSchedAmpM() {
		return dlSchedAmpM;
	}

	public String getMiscreportsEmail() {
		return miscreportsEmail;
	}
	
	/**
	 * Returns the excludeLob flag.
	 * @return boolean
	 */
	public boolean isExcludeLob() {
		return excludeLob;
	}

	public String getDownloadReptSchedHour() {
		return downloadReptSchedHour;
	}

	public String getDownloadReptSchedMinute() {
		return downloadReptSchedMinute;
	}

	public String getDownloadReptSchedAmpM() {
		return downloadReptSchedAmpM;
	}

	public String getDownloadReptActType() {
		return downloadReptActType;
	}

	public String getDownloadReptFileType() {
		return downloadReptFileType;
	}
	public String getDownloadReptAttachType() {
		return downloadReptAttachType;
	}
	/**
	 * Returns the dlAlertCarrierFlag.
	 * @return boolean
	 */
	public boolean getDownloadReptCarrierFlag() {
		return downloadReptCarrierFlag;
	}

	public static List<KeyValueBean> getActList() {
		return actList;
	}

	public static List<KeyValueBean> getFileList() {
		return fileList;
	}
	public static List<KeyValueBean> getFileTypeList() {
		return fileTypeList;
	}
	
	/**
	 * Returns the dlAlertCarrierFlag.
	 * @return boolean
	 */
	public boolean getSendDetailReptFlag() {
		return sendDetailReptFlag;
	}

	public boolean isPolicyXMLImportAllowed() {
		return policyXMLImportAllowed;
	}
}
