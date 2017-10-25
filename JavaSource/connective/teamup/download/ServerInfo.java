package connective.teamup.download;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.PropertyResourceBundle;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import com.ebix.licence.Licence;

import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.AmsInfo;
import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.DuplicateParticipantException;
import connective.teamup.download.services.EmailService;

/**
 * @author haneym
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class ServerInfo 
{
	private static final Logger LOGGER = Logger.getLogger(ServerInfo.class);
	
	protected SimpleDateFormat df = null;
		
	// carrier information
	protected CarrierInfo carrierBean = null;
	
	/**
	 * URL pointer to TEAM-UP Central Registration web services
	 */
	//protected String teamupRegistrationUrl = "http://download.connectivesupport.com/teamupreg";
	protected String teamupRegistrationUrl = null;

	// static constants for Trading Partner list sorting
	public static final int TPSORT_AGENTID = 0;
	public static final int TPSORT_NAME = 1;
	public static final int TPSORT_STATUS = 2;
	public static final int TPSORT_LASTDL_DATE = 3;
	public static final int TPSORT_VENDOR = 4;
	public static final int TPSORT_DL_APP = 5;

	// static constants for default Trading Partner List views
	public static final String TPLIST_ALL = "tp_all";
	public static final String TPLIST_SEARCH = "tp_search";
	public static final String TPLIST_ALPHA = "tp_alpha_";
	public static final String TPLIST_AGENT_ID = "tp_agentid_";

	// init constants
	public static final int INIT_DATABASE = 1;
	public static final int INIT_EMAIL = 2;	

	// session storage attributes
	public static final String STORE_AGENT_LIST = "_NEW_AGENT_LIST_";

	// control constants
	protected String baseControlClassid = null;
	protected String baseControlCodebase = null;
	protected String dlEditsClassid = null;
	protected String dlEditsCodebase = null;
	protected String downloadClassid = null;
	protected String downloadCodebase = null;
	protected String oldDownloadClassid = null;
	protected String oldDownloadCodebase = null;
	protected String newDownloadClassid = null;		// new control for downloading over 99 files
	protected String newDownloadCodebase = null;
	protected String importClassid = null;
	protected String importCodebase = null;
	protected Hashtable regControlHash = null;

	// security
	protected SecurityProvider securityProvider = null;

	// database
	protected Hashtable pages = new Hashtable();
	protected Hashtable actions = new Hashtable();
	protected Vector menubar = new Vector();
	protected boolean updated = false;
	
	protected ServletContext context = null;
	protected String appName = null;
	protected String appVersion = null;
	protected String dbVersion = null;
	protected String downLoadAlertURL = null;
	protected Licence licence = null;
	protected boolean isClaimActivated = false;
	protected int daysBeforelicenseExpire ;
	/**
	 * Constructor for ServerInfo.
	 */
	public ServerInfo(ServletContext context, String appName) throws Exception
	{
		super();

		this.context = context;
		this.appName = appName;
		
		this.regControlHash = new Hashtable();
				
		// create the date format helpers
		df = (SimpleDateFormat) SimpleDateFormat.getInstance();
		df.applyPattern("yyyyMMddHHmmss");
		
		// load environment variables
		String regUrl = System.getProperty("teamup.registration.url");
		if (regUrl != null)
			teamupRegistrationUrl = regUrl;
		
		// load the properties
		PropertyResourceBundle props = new PropertyResourceBundle(getClass().getResourceAsStream("/teamworkdl.properties"));

		baseControlClassid = props.getString("control.base.classid");
		baseControlCodebase = props.getString("control.base.codebase");
		dlEditsClassid = props.getString("control.download.edits.classid");
		dlEditsCodebase = props.getString("control.download.edits.codebase");
		downloadClassid = props.getString("control.download.classid");
		downloadCodebase = props.getString("control.download.codebase");
		oldDownloadClassid = props.getString("control.download.old.classid");
		oldDownloadCodebase = props.getString("control.download.old.codebase");
		newDownloadClassid = props.getString("control.download.new99.classid");
		newDownloadCodebase = props.getString("control.download.new99.codebase");
		importClassid = props.getString("control.import.classid");
		importCodebase = props.getString("control.import.codebase");
		appVersion = props.getString("teamup.version");
		teamupRegistrationUrl = props.getString("download.connective.support") ;
		downLoadAlertURL = props.getString("teamup.downloadAlertURL") ;
		DatabaseOperation op = null;
		try
		{
			DatabaseFactory dbfactory = DatabaseFactory.getInstance();
			op = dbfactory.startOperation();

			// get the database type
			String dbType = op.getPropertyValue(DatabaseFactory.PROP_DBTYPE);
			
			// update the database if needed
			updated = dbfactory.updateDatabase(dbType, System.out, op);
			dbVersion = op.getPropertyValue(DatabaseFactory.PROP_DBVERSION);
			
			// check for version deployment
			String tempVer = op.getPropertyValue(DatabaseFactory.PROP_VERSION);
			if (tempVer == null || !appVersion.equals(tempVer))
			{
				SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getInstance();
				sdf.applyPattern("MM/dd/yyyy HH:mm");
				Calendar today = GregorianCalendar.getInstance();
				Hashtable hash = new Hashtable();
				hash.put(DatabaseFactory.PROP_VERSION, appVersion);
				hash.put(DatabaseFactory.PROP_VERSION_DEPLOYED, sdf.format(today.getTime()));
				op.setProperties(hash);
			}
			
			// load the carrier info from the properties database table
			carrierBean = CarrierInfo.getInstance();
			if (carrierBean.getName() == null || carrierBean.getName().equals(""))
			{
				Calendar today = GregorianCalendar.getInstance();
				carrierBean.setAppStartedDate(today.getTime());
				carrierBean.setCarrierId(op.getPropertyValue(DatabaseFactory.PROP_CARRIER_ID));
				carrierBean.setName(op.getPropertyValue(DatabaseFactory.PROP_CARRIER_NAME));
				carrierBean.setShortName(op.getPropertyValue(DatabaseFactory.PROP_CARRIER_SHORTNAME));
				String implementationYear = op.getPropertyValue(DatabaseFactory.PROP_IMPLEMENTATION_YEAR);
				if (implementationYear != null && !implementationYear.equals(""))
					carrierBean.setImplementationYear(implementationYear);
				else
				{
					// Default implementation year to current date/year
					carrierBean.setImplementationYear(today.get(Calendar.YEAR));
					Hashtable hash = new Hashtable();
					hash.put(DatabaseFactory.PROP_IMPLEMENTATION_YEAR, String.valueOf(today.get(Calendar.YEAR)));
					op.setProperties(hash);
				}
				carrierBean.setContactEmail(op.getPropertyValue(DatabaseFactory.PROP_EMAIL_CONTACT));
				carrierBean.setErrorsEmail(op.getPropertyValue(DatabaseFactory.PROP_EMAIL_ERRORS));
				carrierBean.setReportsEmail(op.getPropertyValue(DatabaseFactory.PROP_EMAIL_REPORTS));
				carrierBean.setMiscreportsEmail(op.getPropertyValue(DatabaseFactory.PROP_MISC_EMAIL_REPORTS));
				carrierBean.setImportFileCreator(op.getPropertyValue(DatabaseFactory.PROP_IMPORT_FILE_CREATION_PROCESS));
				carrierBean.setImportFileFormat(op.getPropertyValue(DatabaseFactory.PROP_IMPORT_FILE_FORMAT));
				carrierBean.setImportFileIdMode(op.getPropertyValue(DatabaseFactory.PROP_IMPORT_FILE_IDENTIFICATION));
				carrierBean.setAgentLogoutUrl(op.getPropertyValue(DatabaseFactory.PROP_AGENT_LOGOUT_URL));
				carrierBean.setDefaultTPListView(op.getPropertyValue(DatabaseFactory.PROP_TPLIST_VIEW));
				
				String flag;
				flag = op.getPropertyValue(DatabaseFactory.PROP_AGENT_INFO_CHANGE);
				if (flag == null || flag.equals(""))
					flag = "Y";	// default value should be true (allow agent to change contact & vendor system info)
				carrierBean.setAgentInfoChangeAllowed(flag == null || !flag.equals("N"));
				flag = op.getPropertyValue(DatabaseFactory.PROP_AGENT_LOGIN_DISABLED);
				carrierBean.setAgentLoginDisabled(flag != null && flag.equals("Y"));
				flag = op.getPropertyValue(DatabaseFactory.PROP_AGENT_PASSWORD_CHANGE);
				if (flag == null || flag.equals(""))
					flag = "Y";	// default value should be true (allow agent to change password)
				carrierBean.setAgentPasswordChangeAllowed(flag != null && flag.equals("Y"));
				flag = op.getPropertyValue(DatabaseFactory.PROP_CLIENT_APP_USED);
				carrierBean.setClientAppUsed(flag == null || !flag.equals("N"));   // default value is to use the non-browser Agent Workstation app
				flag = op.getPropertyValue(DatabaseFactory.PROP_DISPLAY_MIGRATION_BANNER);
				carrierBean.setDisplayMigrationBanner(flag != null && flag.equals("Y"));
				flag = op.getPropertyValue(DatabaseFactory.PROP_CONTROL_SHORTCUT_LOGIN_ALLOWED);
				carrierBean.setDisplayLoginShortcutControl(flag == null || flag.equals("Y"));
				flag = op.getPropertyValue(DatabaseFactory.PROP_EMAIL_AS_HTML);
				carrierBean.setEmailAsHtml(flag == null || !flag.equals("N"));	// default value should be to use HTML
				flag = op.getPropertyValue(DatabaseFactory.PROP_DELETE_ALL_FILES_ON_IMPORT);
				carrierBean.setDeleteAllImportedFiles(flag != null && flag.equals("Y"));
				flag = op.getPropertyValue(DatabaseFactory.PROP_NOTIFY_ON_AGENCY_MIGRATION);
				carrierBean.setNotifyOnAgentMigration(flag != null && flag.equals("Y"));
				flag = op.getPropertyValue(DatabaseFactory.PROP_NOTIFY_ON_AGENCY_REGISTRATION);
				carrierBean.setNotifyOnAgentRegister(flag != null && flag.equals("Y"));
				flag = op.getPropertyValue(DatabaseFactory.PROP_NOTIFY_ON_AGENCY_VENDOR_CHANGE);
				carrierBean.setNotifyOnAgentChange(flag != null && flag.equals("Y"));
				flag = op.getPropertyValue(DatabaseFactory.PROP_NOTIFY_ON_AGENCY_STATUS_CHANGE);
				carrierBean.setNotifyOnStatusChange(flag != null && flag.equals("Y"));
				flag = op.getPropertyValue(DatabaseFactory.PROP_NOTIFY_ON_DOWNLOAD_ERROR);
				carrierBean.setNotifyOnDownloadError(flag != null && flag.equals("Y"));
				flag = op.getPropertyValue(DatabaseFactory.PROP_NOTIFY_ON_IMPORT_ERROR);
				carrierBean.setNotifyOnImportError(flag != null && flag.equals("Y"));
				flag = op.getPropertyValue(DatabaseFactory.PROP_USE_NEW_DL_CONTROL);
				carrierBean.setUseNewDownloadControl(flag != null && flag.equals("Y"));
				
				flag = op.getPropertyValue(DatabaseFactory.PROP_XML_ALLOWED);
				carrierBean.setXmlImportAllowed(flag != null && flag.equals("DLOK"));
				
				flag = op.getPropertyValue(DatabaseFactory.PROP_CLAIM_XML_ALLOWED);
				carrierBean.setClaimXmlImportAllowed(flag != null && flag.equals("Y"));
				
				flag = op.getPropertyValue(DatabaseFactory.PROP_POLICY_XML_ALLOWED);
				carrierBean.setPolicyXmlImportAllowed(flag != null && flag.equals("Y"));
				
				// *** The following carrier data is currently defaulted
				carrierBean.setDisplayImportReport(false);
				
				// get the graphics file and check the height
				String bannerGraphicsHeight = op.getPropertyValue(DatabaseFactory.PROP_BANNER_GRAPHIC_HEIGHT);
				if (bannerGraphicsHeight != null && !bannerGraphicsHeight.equals(""))
				{
					try
					{
						int height = Integer.parseInt(bannerGraphicsHeight);
						if (height > 0)
							carrierBean.setBannerGraphicHeight(height);
					}
					catch (Exception e)
					{
						LOGGER.error(e.getMessage());
						System.out.println("Banner graphic height invalid: " + bannerGraphicsHeight + " -- default used");
					}
				}
//				byte[] graphicFile = op.getResourceBytes(RES_CARRIER_LOGO_FILENAME);
//				if (graphicFile != null && graphicFile.length > 0)
//				{
//					ImageIcon image = new ImageIcon(graphicFile);
//					carrierBean.setBannerGraphicHeight(image.getIconHeight());
//				}
				
				// Check for limited-time POC
				boolean isRegistered = false;
				try
				{
					String pocOverride = props.getString("poc.override");
					if (pocOverride != null && pocOverride.equals("cti.registered"))
						isRegistered = true;
				} catch (Exception e) {
					LOGGER.error(e.getMessage());
				}
				if (isRegistered)
				{
					carrierBean.setPoc(false);
					carrierBean.setPocTerminated(false);
				}
				else
				{
					carrierBean.setPoc(true);
					
					// get termination date
					Date terminationDate = null;
					try
					{
						String str = op.getPropertyValue(DatabaseFactory.PROP_POC_TERMINATION_DATE);
						if (str != null && str.length() > 10)
						{
							int n = str.indexOf("ID");
							terminationDate = new Date(Long.parseLong(str.substring(6, n)));
						}
					} catch (Exception e) {
						LOGGER.error(e.getMessage());
					}
					if (terminationDate != null)
					{
						carrierBean.setPocTerminationDate(terminationDate);
						carrierBean.setPocTerminated(terminationDate.before(new Date(System.currentTimeMillis())));
					}
					else
					{
						int pocDays = Integer.parseInt(props.getString("default.poc.term"));
						Calendar rightNow = Calendar.getInstance();
						rightNow.add(Calendar.DATE, pocDays);
						terminationDate = rightNow.getTime();
						carrierBean.setPocTerminationDate(terminationDate);
						carrierBean.setPocTerminated(pocDays < 0);
						
						// Save the calculated termination date to the properties table
						String str = "CTI_3F0" + String.valueOf(terminationDate.getTime()) + "ID";
						Hashtable hash = new Hashtable();
						hash.put(DatabaseFactory.PROP_POC_TERMINATION_DATE, str);
						op.setProperties(hash);
					}
				}
			}
			
			// set the tech support email address
			EmailService.getInstance().setTechSupportEmailAddress(props.getString("support.email"));
			
			// check to see if the list of agency vendor system info has been loaded /
			// initialized from the central admin server
			if (carrierBean != null && !carrierBean.isVendorListInitialized())
			{
				try {
					AmsInfo[] vendorList = op.getAmsInfoList();
					carrierBean.setVendorListInitialized(vendorList != null && vendorList.length > 1);
				} catch (Exception e) {
					LOGGER.error(e.getMessage());
				}
			}
		}
		finally
		{
			if (op != null)
				op.close();		
		}
		
		// load the security provider
		String provClass = System.getProperty("teamup.securityprovider");
		if (provClass != null && !provClass.equals(""))
			securityProvider = (SecurityProvider) Class.forName(provClass).newInstance();
		else
			securityProvider = new KeySecurityProvider();

		// override the registration server URL
		if (System.getProperty("teamup.download.regserver") != null)
			teamupRegistrationUrl = System.getProperty("teamup.download.regserver");
	}

	public String getRegistrationUrl()
	{
		return teamupRegistrationUrl;
	}
	
	public boolean isDbUpdated()
	{
		return updated;
	}
		
	public SimpleDateFormat getDateFormat()
	{
		return df;
	}
	
	public CarrierInfo getCarrierInfo()
	{
		return carrierBean;
	}

	/**
	 * Returns the baseControlClassid.
	 * @return String
	 */
	public String getBaseControlClassid() {
		return baseControlClassid;
	}

	/**
	 * Returns the baseControlCodebase.
	 * @return String
	 */
	public String getBaseControlCodebase() {
		return baseControlCodebase;
	}
	
	/**
	 * Returns the Applied edits download control classid.
	 * @return String
	 */
	public String getDownloadEditsClassid() {
		return dlEditsClassid;
	}

	/**
	 * Returns the Applied edits download control codebase.
	 * @return String
	 */
	public String getDownloadEditsCodebase() {
		return dlEditsCodebase;
	}
	
	/**
	 * Returns the downloadClassid.
	 * @return String
	 */
	public String getDownloadClassid() {
		return downloadClassid;
	}

	/**
	 * Returns the downloadCodebase.
	 * @return String
	 */
	public String getDownloadCodebase() {
		return downloadCodebase;
	}
	
	/**
	 * Returns the downloadClassid.
	 * @return String
	 */
	public String getOldDownloadClassid() {
		return oldDownloadClassid;
	}

	/**
	 * Returns the downloadCodebase.
	 * @return String
	 */
	public String getOldDownloadCodebase() {
		return oldDownloadCodebase;
	}
	
	/**
	 * Returns the downloadClassid.
	 * @return String
	 */
	public String getNewDownloadClassid() {
		return newDownloadClassid;
	}

	/**
	 * Returns the downloadCodebase.
	 * @return String
	 */
	public String getNewDownloadCodebase() {
		return newDownloadCodebase;
	}

	/**
	 * Returns the importClassid.
	 * @return String
	 */
	public String getImportClassid() {
		return importClassid;
	}

	/**
	 * Returns the importCodebase.
	 * @return String
	 */
	public String getImportCodebase() {
		return importCodebase;
	}

	/**
	 * Returns the actions.
	 * @return Hashtable
	 */
	public Hashtable getActions() {
		return actions;
	}

	/**
	 * Returns the pages.
	 * @return Hashtable
	 */
	public Hashtable getPages() {
		return pages;
	}

	public Vector getMenubar()
	{
		return menubar;
	}
	
	/**
	 * Returns a hashtable of AL3 line of business codes (key) and descriptions.
	 * @return java.util.Hashtable
	 */
	public Hashtable getLobTable()
	{
		Hashtable lobs = new Hashtable();
		
		lobs.put("AGLIA", "Agriculture Liability");
		lobs.put("AGPP",  "Agriculture Scheduled & Unscheduled Personal Property");
		lobs.put("AGPR",  "Agriculture Property");
		lobs.put("APKGE", "Agriculture Package");
		lobs.put("AUTOB", "Business Automobile");
		lobs.put("AUTOP", "Private Passenger Automobile");
		lobs.put("BANDM", "Boiler and Machinery");
		lobs.put("BLDRK", "Installation / Builders Risk");
		lobs.put("BMSBP", "Boiler and Machinery Small Business");
		lobs.put("BOAT",  "Watercraft (small boat)");
		lobs.put("BOP",   "Businessowners");
		lobs.put("BOPGL", "BOP Liability");
		lobs.put("BOPPR", "BOP Property");
		lobs.put("CFIRE", "Commercial Fire");
		lobs.put("CGL",   "General Liability");
		lobs.put("COMAR", "Commercial Articles");
		lobs.put("CONTR", "Contractor's Equipment Floater");
		lobs.put("CPKGE", "Commercial Package");
		lobs.put("CRIM",  "Crime");
		lobs.put("CUMBR", "Commercial Umbrella");
		lobs.put("DFIRE", "Dwelling Fire");
		lobs.put("EDP",   "Computers");
		lobs.put("ELIAB", "Employers Liability");
		lobs.put("EQ",    "Earthquake");
		lobs.put("FINAR", "Fine Arts");
		lobs.put("FLOOD", "Flood");
		lobs.put("GARAG", "Garage and Dealers");
		lobs.put("GLASS", "Glass");
		lobs.put("HOME",  "Homeowners");
		lobs.put("INMRC", "Commercial Inland Marine");
		lobs.put("INMRP", "Personal Inland Marine");
		lobs.put("MHOME", "Mobile Homeowners");
		lobs.put("MTRTK", "Motor Truck Cargo");
		lobs.put("PHYS",  "Physicians and Surgeons");
		lobs.put("PPKGE", "Personal Package");
		lobs.put("PROP",  "Commercial Property");
		lobs.put("PUMBR", "Personal Umbrella (excess indemnity)");
		lobs.put("RECV",  "Recreational Vehicles");
		lobs.put("SCHPR", "Scheduled Property");
		lobs.put("SFRNC", "Small Farm / Ranch");
		lobs.put("SIGNS", "Signs");
		lobs.put("SMP",   "Special Multi-Peril");
		lobs.put("SURE",  "Surety");
		lobs.put("TRANS", "Transportation");
		lobs.put("TRKRS", "Truckers");
		lobs.put("VALP",  "Valuable Papers");
		lobs.put("WIND",  "Wind");
		lobs.put("WORK",  "Worker's Compensation");
		
		return lobs;
	}
	
	
	/**
	 * Returns a hashtable of AL3 transaction type codes (key) and descriptions.
	 * @return java.util.Hashtable
	 */
	public Hashtable getTransTypeHashtable()
	{
		Hashtable transTypeHash = new Hashtable();
		
		transTypeHash.put("PCH", "Policy Change");
		transTypeHash.put("RWL", "Renewal Image");
		transTypeHash.put("NBS", "New Business");
		transTypeHash.put("ACK", "Simple Acknowledgement");
		transTypeHash.put("ACR", "Account Current Reconciliation");
		transTypeHash.put("APV", "Surety Bond Approval");
		transTypeHash.put("ARR", "Anniversary Re-Rating");
		transTypeHash.put("BDC", "Bond Closed");
		transTypeHash.put("BIL", "Billing");
		transTypeHash.put("BND", "Binder");
		transTypeHash.put("BRQ", "Surety Bond Request");
		transTypeHash.put("BRS", "Surety Bid Bond Results");
		transTypeHash.put("CER", "Certificate of Insurance");
		transTypeHash.put("CLI", "Claim Information");
		transTypeHash.put("COM", "Direct Bill Commission Detail");
		transTypeHash.put("CON", "Contact");
		transTypeHash.put("CSQ", "Claims Inquiry");
		transTypeHash.put("CST", "Claim Status Report");
		transTypeHash.put("DBR", "Direct Bill Reconciliation");
		transTypeHash.put("DBS", "Direct Bill Status");
		transTypeHash.put("DEC", "Policy Declaration Page");
		transTypeHash.put("DIV", "Dividend Transaction");
		transTypeHash.put("DSP", "Download Setup Transaction");
		transTypeHash.put("EPI", "Evidence of Property Insurance");
		transTypeHash.put("ERC", "Error Correction");
		transTypeHash.put("ERR", "Error Messages");
		transTypeHash.put("LNT", "Loss Notice");
		transTypeHash.put("MEM", "Electronic Memo");
		transTypeHash.put("NBQ", "New Business Quote");
		transTypeHash.put("NRA", "Non-renewal notification to agency");
		transTypeHash.put("PAB", "Premium Audit");
		transTypeHash.put("PCQ", "Policy Change Quote");
		transTypeHash.put("PMT", "Payment");
		transTypeHash.put("PNQ", "Policy Inquiry");
		transTypeHash.put("POL", "Policy - unspecified transaction");
		transTypeHash.put("PRT", "Miscellaneous Print");
		transTypeHash.put("REI", "Reinstatement");
		transTypeHash.put("REV", "Assignment Reversal");
		transTypeHash.put("REW", "Rewrite");
		transTypeHash.put("RIX", "Re-Issue");
		transTypeHash.put("RNR", "Reversal of Non-Renewal");
		transTypeHash.put("RRQ", "Renewal Re-Quote");
		transTypeHash.put("RWQ", "Renewal Quote");
		transTypeHash.put("RWR", "Renewal Request");
		transTypeHash.put("RWX", "Non-Renewal");
		transTypeHash.put("SYN", "Database Synchronization");
		transTypeHash.put("XLC", "Cancellation Confirmation");
		transTypeHash.put("XLN", "Cancellation Request");
		
		return transTypeHash;
	}

	/**
	 * forward to a page
	 */
	public void showPage(String name, HttpServletRequest req, HttpServletResponse resp, DatabaseOperation op, FileItem[] items) throws PageException
	{
		PageDef page = (PageDef) getPages().get(name);
		if (page != null)
		{
			page.getPageInstance(context).forward(req, resp, this, op, items);
		}
	}
	/**
	 * Returns the statusMessage.
	 * @return String
	 */
	public String getStatusMessage(HttpSession session)
	{
		return (String) session.getAttribute("status.message");
	}

	/**
	 * Sets the statusMessage.
	 * @param statusMessage The statusMessage to set
	 */
	public void setStatusMessage(HttpSession session, String statusMessage) 
	{
		session.setAttribute("status.message", statusMessage);
	}

	public SecurityProvider getSecurityProvider()
	{
		return this.securityProvider;
	}

	public AgentInfo getAgentInfo(HttpSession session, DatabaseOperation op) throws Exception
	{
		String agentId = (String) session.getAttribute("agentinfo");
		
		AgentInfo info = null;
		if (agentId != null)
			info = op.getAgentInfo(agentId);
		
		return info;		
	}

	public void setAgentInfo(HttpSession session, AgentInfo agent) 
	{
		if (agent == null)
			session.removeAttribute("agentinfo");
		else
			session.setAttribute("agentinfo", agent.getAgentId());
	}

	/**
	 * Returns any current duplicate participant information.
	 * @return DuplicateParticipantException
	 */
	public DuplicateParticipantException getDuplicateParticipantInfo(HttpSession session)
	{
		DuplicateParticipantException dupEx = (DuplicateParticipantException) session.getAttribute("duplicate.participant.info");
		return dupEx;
	}

	/**
	 * Sets the duplicate participant information.
	 * @param dupEx The dupEx to set
	 */
	public void setDuplicateParticipantInfo(HttpSession session, DuplicateParticipantException ex)
	{
		if (ex == null)
			session.removeAttribute("duplicate.participant.info");
		else
			session.setAttribute("duplicate.participant.info", ex);
	}
	
	/**
	 * Returns the dbfactory.
	 * @return DatabaseFactory
	 */
	public DatabaseFactory getDbfactory() {
		return DatabaseFactory.getInstance();
	}

	/**
	 * Returns the current TEAM-UP Download application version number.
	 * @return String
	 */
	public String getAppVersion()
	{
		return appVersion;
	}

	/**
	 * Returns the current TEAM-UP Download database version number.
	 * @return String
	 */
	public String getDbVersion()
	{
		return dbVersion;
	}

	/**
	 * Returns the context.
	 * @return ServletContext
	 */
	public ServletContext getContext() {
		return context;
	}
	
	/**
	 * Returns the classid for the specified registration ActiveX control.
	 * @param controlType The registration control type
	 * @return String
	 */
	public String getRegControlClassid(String controlType)
	{
		if (controlType == null || controlType.equals(""))
			return "";
		
		String classid = (String) this.regControlHash.get(controlType + ".classid");
		if (classid == null)
		{
			String[] props = getRegControlProperties(controlType);
			classid = props[0];
		}
		
		return classid;
	}
	
	/**
	 * Returns the codebase for the specified registration ActiveX control.
	 * @param controlType The registration control type
	 * @return String
	 */
	public String getRegControlCodebase(String controlType)
	{
		if (controlType == null || controlType.equals(""))
			return "";
		
		String codebase = (String) this.regControlHash.get(controlType + ".codebase");
		if (codebase == null)
		{
			String[] props = getRegControlProperties(controlType);
			codebase = props[1];
		}
		
		return codebase;
	}
	
	/**
	 * Returns the classid and codebase for the specified registration ActiveX control.
	 * @param controlType The registration control type
	 * @return String[]
	 */
	private String[] getRegControlProperties(String controlType)
	{
		String classid = "";
		String codebase = "";
		
		try
		{
			PropertyResourceBundle props = new PropertyResourceBundle(getClass().getResourceAsStream("/teamworkdl.properties"));
			String propKey = "control.registration." + controlType;
		
			try
			{
				// Get the classid attribute
				classid = (String) this.regControlHash.get(controlType + ".classid");
				if (classid == null)
				{
					classid = props.getString(propKey + ".classid");
					if (classid != null)
						this.regControlHash.put(controlType + ".classid", classid);
				}
			}
			catch (Exception e) {
				LOGGER.error(e.getMessage());
			}
			
			try
			{
				// Get the codebase attribute
				codebase = (String) this.regControlHash.get(controlType + ".codebase");
				if (codebase == null)
				{
					codebase = props.getString(propKey + ".codebase");
					if (codebase != null)
						this.regControlHash.put(controlType + ".codebase", codebase);
				}
			}
			catch (Exception e) {
				LOGGER.error(e.getMessage());
			}
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
			if (classid == null)
				classid = "";
			if (codebase == null)
				codebase = "";
		}
		
		String[] controlProps = new String[2];
		controlProps[0] = classid;
		controlProps[1] = codebase;
		
		return controlProps;
	}
	
	/**
	 * Returns the current base URL from the HTTP request, using the current 
	 * servlet path and port.
	 * @param request
	 * @return
	 */
	public String getRequestUrl(HttpServletRequest request)
	{
		return getRequestUrl(request, request.getServletPath(), request.getServerPort());
	}
	
	/**
	 * Returns the current base URL from the HTTP request, using the specified 
	 * servlet path.
	 * @param request
	 * @param servletPath
	 * @return
	 */
	public String getRequestUrl(HttpServletRequest request, String servletPath)
	{
		return getRequestUrl(request, servletPath, request.getServerPort());
	}
	
	/**
	 * Returns the current base URL from the HTTP request, using the specified 
	 * servlet path and port.
	 * @param request
	 * @param servletPath
	 * @return
	 */
	public String getRequestUrl(HttpServletRequest request, String servletPath, int port)
	{
		// Build the base URL for this app
		String url = "";
		String thisUrl = request.getRequestURL().toString();
		if (thisUrl != null && thisUrl.length() > 5)
		{
			if (thisUrl.substring(0, 5).equalsIgnoreCase("https"))
				url = "https://";
			else
				url = "http://";
		}
		else
		{
			if (port == 443)
				url = "https://";
			else
				url = "http://";
		}
		url += request.getServerName();
		if (port != 80 && port != 443)
			url += ":" + String.valueOf(port);
		url += request.getContextPath() + servletPath;
		
		return url;
	}

	/**
	 * Returns the application name of the servlet instance that created this object.
	 * @return String
	 */
	public String getAppName()
	{
		return appName;
	}

	/**
	 * Returns the user's most recent (or default) view configuration of the Trading Partners List page.
	 */
	public String getTPListView(HttpSession session)
	{
		String tplistView = null;
		try {
			tplistView = (String) session.getAttribute("tplist_view");
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
			tplistView = null;
		}
		
		if (tplistView == null || tplistView.equals(""))
			tplistView = getCarrierInfo().getDefaultTPListView();
		else if (tplistView.startsWith(TPLIST_ALPHA))
			tplistView = TPLIST_ALPHA;
		else if (tplistView.startsWith(TPLIST_AGENT_ID))
			tplistView = TPLIST_AGENT_ID;
		
		return tplistView;
	}

	public String getTPListViewSearchParameter(HttpSession session)
	{
		String param = "";
		try {
			String tplistView = (String) session.getAttribute("tplist_view");
			
			if (tplistView == null || tplistView.equals(""))
				param = "";
			else if (tplistView.startsWith(TPLIST_ALPHA))
			{
				param = tplistView.substring(TPLIST_ALPHA.length());
				if (param.length() > 1)
					param = String.valueOf(param.charAt(0));
			}
			else if (tplistView.startsWith(TPLIST_AGENT_ID))
				param = tplistView.substring(TPLIST_AGENT_ID.length());
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
			param = "";
		}
		
		return param;
	}

	/**
	 * Resets the user's last view configuration of the Trading Partners List page to the default view.
	 */
	public void resetTPListView(HttpSession session) 
	{
		session.removeAttribute("tplist_view");
	}

	/**
	 * Saves the user's last view configuration of the Trading Partners List page to the session.
	 */
	public void setTPListView(HttpSession session, String tplistView) 
	{
		if (tplistView == null)
			session.removeAttribute("tplist_view");
		else
			session.setAttribute("tplist_view", tplistView);
	}

	/**
	 * Saves the user's last view configuration of the Trading Partners List page to the session.
	 * @param searchParam - The first letter (for alpha view) or the agent ID search parameter
	 */
	public void setTPListView(HttpSession session, String tplistView, String searchParam)
	{
		if (tplistView == null)
			session.removeAttribute("tplist_view");
		else if (searchParam != null && 
				 (tplistView.equals(TPLIST_AGENT_ID) || tplistView.equals(TPLIST_ALPHA)))
			session.setAttribute("tplist_view", tplistView + searchParam);
		else
			session.setAttribute("tplist_view", tplistView);
	}

	public String getDownLoadAlertURL() {
		return downLoadAlertURL;
	}

	public void setDownLoadAlertURL(String downLoadAlertURL) {
		this.downLoadAlertURL = downLoadAlertURL;
	}

	public Licence getLicence() {
		return licence;
	}

	public void setLicence(Licence licence) {
		this.licence = licence;
	}

	public boolean isClaimActivated() {
		return isClaimActivated;
	}

	public void setClaimActivated(boolean isClaimActivated) {
		this.isClaimActivated = isClaimActivated;
	}

	public int getDaysBeforelicenseExpire() {
		return daysBeforelicenseExpire;
	}

	public void setDaysBeforelicenseExpire(int daysBeforelicenseExpire) {
		this.daysBeforelicenseExpire = daysBeforelicenseExpire;
	}

}
