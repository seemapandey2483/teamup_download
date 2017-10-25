package connective.teamup.download;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Kyle McCreary
 *
 * Info bean containing carrier-specific information and configuration settings
 * used throughout the application.
 */
public class CarrierInfo
{
	private final static int DEFAULT_BANNER_GRAPHIC_HEIGHT = 77;
	
	private String name = "";
	private String shortName = "";
	private String bannerGraphicFile = "/resource?id=banner";
	private int bannerGraphicHeight = DEFAULT_BANNER_GRAPHIC_HEIGHT;
	private String copyrightNotice = "";
	private String contactEmail = "";
	private String reportsEmail = "";
	private String miscreportsEmail = "";
	private String errorsEmail = "";
	private String importFileFormat = "";
	private String importFileCreator = "";
	private String importFileIdMode = "";
	private String agentLogoutUrl = null;
	private String carrierId = null;
	private String defaultTPListView = ServerInfo.TPLIST_ALL;
	private int implementationYear = 0;
	private Date appStartedDate = null;
	
	private boolean agentInfoChangeAllowed = true;
	private boolean agentLoginDisabled = false;
	private boolean agentPasswordChangeAllowed = true;
	private boolean clientAppUsed = true;
	private boolean deleteAllImportedFiles = false;
	private boolean displayImportReport = false;
	private boolean displayLoginShortcutControl = true;
	private boolean displayMigrationBanner = false;
	private boolean emailAsHtml = true;
	private boolean notifyOnAgentChange = false;
	private boolean notifyOnAgentMigration = false;
	private boolean notifyOnAgentRegister = false;
	private boolean notifyOnStatusChange = false;
	private boolean notifyOnDownloadError = false;
	private boolean notifyOnImportError = false;
	private boolean useNewDownloadControl = false;
	private boolean vendorListInitialized = false;
	
	private static CarrierInfo carrierBean = null;
	
	private SimpleDateFormat df = null;
	
	// member variables used for limited-time POC
	private boolean poc = false;
	private boolean pocTerminated = false;
	private Date pocTerminationDate = null;
	
	// static constants for Import File ID mode
	public static final String IDMODE_AGENTID = "agentID";
	public static final String IDMODE_FILENAME = "filename";
	
	private boolean xmlImportAllowed = false;
	private boolean claimXmlImportAllowed = false;
	private boolean policyXmlImportAllowed = false;
	
	/**
	 * Constructor for CarrierInfo.
	 */
	private CarrierInfo()
	{
		super();
		
		Calendar cal = new GregorianCalendar();
		int thisYear = cal.get(Calendar.YEAR);
		int beginYear = 2001;
		copyrightNotice = "&copy; " + beginYear;
		if (thisYear > beginYear)
			copyrightNotice += "-" + thisYear;
		copyrightNotice += " &nbsp;<a href =\"http://www.ebix.com\" target=\"_blank\"><font color='#5C95D0'>Ebix, Inc.</font></a> | All Rights Reserved |";

		// create the date format helper
		df = (SimpleDateFormat) SimpleDateFormat.getInstance();
		df.applyPattern("MM/dd/yyyy HH:mm");
	}
	
	/**
	 * Get an instance of the CarrierInfo singleton.
	 * @return CarrierInfo
	 */
	public static CarrierInfo getInstance()
	{
		if (carrierBean == null)
			carrierBean = new CarrierInfo();
		
		return carrierBean;
	}

	/**
	 * Returns the carrier's email address for download contacts.
	 * @return String
	 */
	public String getContactEmail()
	{
		return contactEmail;
	}

	/**
	 * Returns true if the carrier wishes to display import reports on completion
	 * of the import process.
	 * @return boolean
	 */
	public boolean isDisplayImportReport()
	{
		return displayImportReport;
	}

	/**
	 * Returns true to display the activeX control to create a desktop shortcut 
	 * to log in to the agent app.
	 * @return boolean
	 */
	public boolean isDisplayLoginShortcutControl()
	{
		return displayLoginShortcutControl;
	}

	/**
	 * Returns the carrier's email address for import and download error reports.
	 * @return String
	 */
	public String getErrorsEmail()
	{
		return errorsEmail;
	}

	/**
	 * Returns the implementationYear.
	 * @return int
	 */
	public int getImplementationYear()
	{
		return implementationYear;
	}

	/**
	 * Returns the carrier's full display name.
	 * @return String
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Returns true if the agents are allowed to change their own contact and
	 * management system info.
	 * @return boolean
	 */
	public boolean isAgentInfoChangeAllowed()
	{
		return agentInfoChangeAllowed;
	}

	/**
	 * Returns true if the agent login page is disabled.
	 * @return boolean
	 */
	public boolean isAgentLoginDisabled()
	{
		return agentLoginDisabled;
	}

	/**
	 * Returns true if the agents are allowed to change their own passwords.
	 * @return boolean
	 */
	public boolean isAgentPasswordChangeAllowed()
	{
		return agentPasswordChangeAllowed;
	}

	/**
	 * Returns true if all files should be deleted on successful import; false to
	 * only delete files imported for "live" agents.
	 * @return boolean
	 */
	public boolean isDeleteAllImportedFiles()
	{
		return deleteAllImportedFiles;
	}

	/**
	 * Returns true if the carrier wishes to receive email notification when an
	 * agent changes certain configuration settings (vendor system or version number,
	 * status, etc.).
	 * @return boolean
	 */
	public boolean isNotifyOnAgentChange()
	{
		return notifyOnAgentChange;
	}

	/**
	 * Returns true if the carrier wishes to receive emails as HTML.
	 * @return boolean
	 */
	public boolean isEmailAsHtml()
	{
		return emailAsHtml;
	}

	/**
	 * Returns true if the carrier wishes to receive download error reports.
	 * @return boolean
	 */
	public boolean isNotifyOnDownloadError()
	{
		return notifyOnDownloadError;
	}

	/**
	 * Returns true if the carrier wishes to receive import error reports.
	 * @return boolean
	 */
	public boolean isNotifyOnImportError()
	{
		return notifyOnImportError;
	}

	/**
	 * Returns the carrier's email address for download reports.
	 * @return String
	 */
	public String getReportsEmail()
	{
		return reportsEmail;
	}

	/**
	 * Returns the carrier's short name.
	 * @return String
	 */
	public String getShortName()
	{
		return shortName;
	}

	/**
	 * Resets the height (in pixels) of the banner graphic image to the default value.
	 */
	public void resetBannerGraphicHeight()
	{
		bannerGraphicHeight = DEFAULT_BANNER_GRAPHIC_HEIGHT;
	}

	/**
	 * Sets the "allow agent to change contact/mgt system info" flag.
	 * @param changeAllowed The flag value to set
	 */
	public void setAgentInfoChangeAllowed(boolean changeAllowed)
	{
		agentInfoChangeAllowed = changeAllowed;
	}

	/**
	 * Sets the "allow agent to change password" flag.
	 * @param changeAllowed The flag value to set
	 */
	public void setAgentPasswordChangeAllowed(boolean changeAllowed)
	{
		agentPasswordChangeAllowed = changeAllowed;
	}

	/**
	 * Sets the height (in pixels) of the banner graphic image.
	 * @param height The image height, in pixels
	 */
	public void setBannerGraphicHeight(int height)
	{
		if (height < 1)
			height = 77;
		this.bannerGraphicHeight = height;
	}

	/**
	 * Sets the carrier's contact email address.
	 * @param contactEmail The email address to set
	 */
	public void setContactEmail(String contactEmail)
	{
		this.contactEmail = contactEmail;
	}

	/**
	 * Sets the "display import report" flag.
	 * @param displayImportReport The flag value to set
	 */
	public void setDisplayImportReport(boolean displayImportReport)
	{
		this.displayImportReport = displayImportReport;
	}

	/**
	 * Sets the carrier's email address for receiving error reports.
	 * @param errorsEmail The email address to set
	 */
	public void setErrorsEmail(String errorsEmail)
	{
		this.errorsEmail = errorsEmail;
	}

	/**
	 * Sets the carrier's name.
	 * @param name The name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Sets the "notify when agent changes" flag.
	 * @param notifyOnAgentChange The flag value to set
	 */
	public void setNotifyOnAgentChange(boolean notifyOnAgentChange)
	{
		this.notifyOnAgentChange = notifyOnAgentChange;
	}

	/**
	 * Sets the "receive email as HTML" flag.
	 * @param receiveEmailAsHtml The flag value to set
	 */
	public void setEmailAsHtml(boolean receiveEmailAsHtml)
	{
		this.emailAsHtml = receiveEmailAsHtml;
	}

	/**
	 * Sets the "notify on download error" flag.
	 * @param notifyOnDownloadError The flag value to set
	 */
	public void setNotifyOnDownloadError(boolean notifyOnDownloadError)
	{
		this.notifyOnDownloadError = notifyOnDownloadError;
	}

	/**
	 * Sets the "notify on import error" flag.
	 * @param notifyOnImportError The flag value to set
	 */
	public void setNotifyOnImportError(boolean notifyOnImportError)
	{
		this.notifyOnImportError = notifyOnImportError;
	}

	/**
	 * Sets the carrier's email address for receiving reports.
	 * @param reportsEmail The email address to set
	 */
	public void setReportsEmail(String reportsEmail)
	{
		this.reportsEmail = reportsEmail;
	}

	/**
	 * Sets the carrier's short name.
	 * @param shortName The name to set
	 */
	public void setShortName(String shortName)
	{
		this.shortName = shortName;
	}

	/**
	 * Sets the implementationYear.
	 * @param implementationYear The implementation year to set
	 */
	public void setImplementationYear(int implementationYear)
	{
		this.implementationYear = implementationYear;
	}

	/**
	 * Sets the implementationYear.
	 * @param implementationYear The implementation year to set (passed as a String value)
	 */
	public void setImplementationYear(String implementationYear)
	{
		try
		{
			int year = Integer.parseInt(implementationYear);
			this.implementationYear = year;
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Returns the banner graphic filename.
	 * @return String
	 */
	public String getBannerGraphicFile()
	{
		return bannerGraphicFile;
	}

	/**
	 * Returns the height (in pixels) of the banner graphic image.
	 * @return int
	 */
	public int getBannerGraphicHeight()
	{
		return bannerGraphicHeight;
	}

	/**
	 * Returns the copyright notice text.
	 * @return String
	 */
	public String getCopyrightNotice()
	{
		return copyrightNotice;
	}

	/**
	 * Sets the copyright notice text.
	 * @param copyrightNotice The copyright notice text to set
	 */
	public void setCopyrightNotice(String copyrightNotice)
	{
		this.copyrightNotice = copyrightNotice;
	}

	/**
	 * Returns the "notify when agent completes migration" flag.
	 * @return boolean
	 */
	public boolean isNotifyOnAgentMigration()
	 {
		return notifyOnAgentMigration;
	}

	/**
	 * Returns the "notify when agent completes registration" flag.
	 * @return boolean
	 */
	public boolean isNotifyOnAgentRegister()
	 {
		return notifyOnAgentRegister;
	}

	/**
	 * Returns the "notify on agent status change" flag.
	 * @return boolean
	 */
	public boolean isNotifyOnStatusChange()
	{
		return notifyOnStatusChange;
	}

	/**
	 * Sets the "notify on agent migration" flag.
	 * @param notifyOnAgentMigration - The flag value to set
	 */
	public void setNotifyOnAgentMigration(boolean notifyOnAgentMigration)
	{
		this.notifyOnAgentMigration = notifyOnAgentMigration;
	}

	/**
	 * Sets the "notify on agent registration" flag.
	 * @param notifyOnAgentRegister The flag value to set
	 */
	public void setNotifyOnAgentRegister(boolean notifyOnAgentRegister)
	{
		this.notifyOnAgentRegister = notifyOnAgentRegister;
	}

	/**
	 * Sets the "notify on agent status change" flag.
	 * @param notifyOnStatusChange The flag value to set
	 */
	public void setNotifyOnStatusChange(boolean notifyOnStatusChange)
	{
		this.notifyOnStatusChange = notifyOnStatusChange;
	}

	/**
	 * Sets the "deleteAllImportedFiles" flag.
	 * @param deleteAllImportedFiles True to delete all files on successful import; false to only delete files imported for "live" agents
	 */
	public void setDeleteAllImportedFiles(boolean deleteAllImportedFiles)
	 {
		this.deleteAllImportedFiles = deleteAllImportedFiles;
	}

	/**
	 * Returns the import file creation process.
	 * @return String
	 */
	public String getImportFileCreator()
	{
		return importFileCreator;
	}

	/**
	 * Returns the import file format.
	 * @return String
	 */
	public String getImportFileFormat()
	 {
		return importFileFormat;
	}

	/**
	 * Returns the import file identification mode.
	 * @return String
	 */
	public String getImportFileIdMode()
	{
		return importFileIdMode;
	}

	/**
	 * Sets the import file creation process.
	 * @param importFileCreator The process name to set
	 */
	public void setImportFileCreator(String importFileCreator)
	{
		this.importFileCreator = importFileCreator;
	}

	/**
	 * Sets the import file format.
	 * @param importFileFormat The file format to set
	 */
	public void setImportFileFormat(String importFileFormat)
	{
		this.importFileFormat = importFileFormat;
	}

	/**
	 * Sets the import file ID mode.
	 * @param importFileIdMode The identification mode to set
	 */
	public void setImportFileIdMode(String importFileIdMode)
	{
		this.importFileIdMode = importFileIdMode;
	}

	/**
	 * Returns the appStartedDate.
	 * @return Date
	 */
	public Date getAppStartedDate()
	{
		return appStartedDate;
	}

	/**
	 * Returns the appStartedDate, formatted as a string.
	 * @return String
	 */
	public String getAppStartedDateFormatted()
	{
		return df.format(appStartedDate);
	}

	/**
	 * Sets the appStartedDate.
	 * @param appStartedDate The appStartedDate to set
	 */
	public void setAppStartedDate(Date appStartedDate)
	{
		this.appStartedDate = appStartedDate;
	}

	/**
	 * Returns true if app is configured as a limited time POC (proof of concept).
	 * @return boolean
	 */
	public boolean isPoc()
	{
		return poc;
	}

	/**
	 * Returns true if POC time limit has terminated.
	 * @return boolean
	 */
	public boolean isPocTerminated()
	{
		if (!pocTerminated && pocTerminationDate != null)
		{
			Date today = new Date(System.currentTimeMillis());
			if (today.after(pocTerminationDate))
				pocTerminated = true;
		}
		
		return pocTerminated;
	}

	/**
	 * Returns the POC termination date.
	 * @return java.util.Date
	 */
	public Date getPocTerminationDate()
	{
		return pocTerminationDate;
	}

	/**
	 * Sets the POC flag.
	 * @param poc True if app is a limited-time POC
	 */
	void setPoc(boolean poc)
	{
		this.poc = poc;
	}

	/**
	 * Sets the POC terminated flag.
	 * @param pocTerminated True if terminated POC
	 */
	void setPocTerminated(boolean pocTerminated)
	{
		this.pocTerminated = pocTerminated;
	}

	/**
	 * Sets the POC termination date.
	 * @param pocTerminationDate The termination date to set
	 */
	void setPocTerminationDate(Date pocTerminationDate)
	{
		this.pocTerminationDate = pocTerminationDate;
	}

	/**
	 * Returns the forwarding URL to use on agent logout; <b>null</b> to use
	 * default TEAM-UP Download logout (return to login page).
	 */
	public String getAgentLogoutUrl()
	{
		return agentLogoutUrl;
	}

	/**
	 * Sets the flag to disable the agent login page.
	 * @param disable - True to disable (default = false)
	 */
	public void setAgentLoginDisabled(boolean disable)
	{
		agentLoginDisabled = disable;
	}

	/**
	 * Sets the forwarding URL to use on agent logout; <b>null</b> to use the
	 * default TEAM-UP Download logout (return to login page).
	 * @param string - The forwarding URL
	 */
	public void setAgentLogoutUrl(String string)
	{
		agentLogoutUrl = string;
		if (string != null && string.trim().equals(""))
			agentLogoutUrl = null;
	}

	/**
	 * Sets the flag to display/hide the activeX control for creating a desktop
	 * shortcut to the agency app login page.
	 * @param display - True to display the control, false to hide
	 */
	public void setDisplayLoginShortcutControl(boolean display)
	{
		displayLoginShortcutControl = display;
	}

	/**
	 * Returns true if carrier is configured to use the new download control for agents
	 * (for downloading more than 99 files at a time).  -- 06/23/2005, kwm
	 * @return boolean
	 */
	public boolean isNewDownloadControlUsed()
	{
		return useNewDownloadControl;
	}

	/**
	 * Sets the flag indicating whether or not to use the new download control for agents
	 * (for downloading more than 99 files at a time).  -- 06/23/2005, kwm
	 */
	public void setUseNewDownloadControl(boolean b)
	{
		useNewDownloadControl = b;
	}

	/**
	 * @return
	 */
	public String getCarrierId()
	{
		return carrierId;
	}

	/**
	 * @param string
	 */
	public void setCarrierId(String string)
	{
		carrierId = string;
	}

	/**
	 * @return
	 */
	public boolean isClientAppUsed()
	{
		return clientAppUsed;
	}

	/**
	 * @param b
	 */
	public void setClientAppUsed(boolean b)
	{
		clientAppUsed = b;
	}

	/**
	 * @return
	 */
	public boolean isDisplayMigrationBanner()
	{
		return displayMigrationBanner;
	}

	/**
	 * @param b
	 */
	public void setDisplayMigrationBanner(boolean b)
	{
		displayMigrationBanner = b;
	}

	/**
	 * @return Returns the default Trading Partner List view.
	 */
	public String getDefaultTPListView()
	{
		return defaultTPListView;
	}
	/**
	 * Sets the default Trading Partner List view.
	 */
	public void setDefaultTPListView(String tplistView)
	{
		if (tplistView == null || tplistView.equals(""))
			defaultTPListView = ServerInfo.TPLIST_ALL;
		else
			defaultTPListView = tplistView;
	}

	/**
	 * Checks to see if the agency vendor list has been initialized/sync'ed since 
	 * the time of installation/deployment.
	 */
	public boolean isVendorListInitialized()
	{
		return vendorListInitialized;
	}
	/**
	 * Sets the flag showing whether or not the agency vendor list has been initialized/sync'ed
	 * since the time of installation/deployment.
	 */
	public void setVendorListInitialized(boolean b)
	{
		vendorListInitialized = b;
	}
	/**
	 * Checks to see if the carrier is configured to allow import and download of 
	 * ACORD XML files.
	 */
	public boolean isXmlImportAllowed() {
		return xmlImportAllowed;
	}
	/**
	 * Sets the flag showing whether or not the carrier is allowed to import and download
	 * ACORD XML files.
	 */
	public void setXmlImportAllowed(boolean xmlImportAllowed) {
		this.xmlImportAllowed = xmlImportAllowed;
	}
	/**
	 * Checks to see if the carrier is configured to allow import and download of 
	 * ACORD XML files.
	 */
	public boolean isClaimXmlImportAllowed() {
		return claimXmlImportAllowed;
	}
	/**
	 * Sets the flag showing whether or not the carrier is allowed to import and download
	 * ACORD XML files.
	 */
	public void setClaimXmlImportAllowed(boolean claimXmlImportAllowed) {
		this.claimXmlImportAllowed = claimXmlImportAllowed;
	}
	
	public String getMiscreportsEmail() {
		return miscreportsEmail;
	}

	public void setMiscreportsEmail(String miscreportsEmail) {
		this.miscreportsEmail = miscreportsEmail;
	}

	public boolean isPolicyXmlImportAllowed() {
		return policyXmlImportAllowed;
	}

	public void setPolicyXmlImportAllowed(boolean policyXmlImportAllowed) {
		this.policyXmlImportAllowed = policyXmlImportAllowed;
	}

}
