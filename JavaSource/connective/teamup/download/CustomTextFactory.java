package connective.teamup.download;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PropertyResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.AmsClaimInfo;
import connective.teamup.download.db.AmsInfo;
import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author Kyle McCreary
 *
 * Factory class to import customizable text files for use in the web app,
 */
public class CustomTextFactory
{
	private static final Logger LOGGER = Logger.getLogger(CustomTextFactory.class);
	
	private CarrierInfo carrierInfo = null;
	private AgentInfo agentInfo = null;
	private AmsInfo amsInfo = null;
	private AmsClaimInfo amsClaimInfo = null;
	private String textName = null;
	private String text = null;
	private String htmlText = null;
	private String emailSubject = null;
	private String downloadUrl = null;
	private String helpUrl = null;
	private String loginUrl = null;
	private String dlSupportUrl = null; //"http://download.connectivesupport.com/teamupreg/agents";
	private String priorStatus = null;
	private int outputType = 0;
	private int emailType = 0;
	private boolean customText = false;
	private boolean customHtml = false;
	private boolean customSubject = false;
	private ArrayList tagList = null;
	private ArrayList agentTagList = null;
	private ArrayList companyTagList = null;
	private HashMap tagMap = null;
	
	public final static int TYPE_HTML = 0;
	public final static int TYPE_EMAIL = 1;
	public final static int TYPE_TEXT = 2;
	
	private final static int EMAIL_TO_CARRIER = 11;
	private final static int EMAIL_TO_AGENT = 12;
	
	// Custom file constants
	public static final String TEXT_DL_URL_CHANGE = DatabaseFactory.RES_CUSTOM_DL_URL_CHANGE;
	public static final String TEXT_NEW_AGENT = DatabaseFactory.RES_CUSTOM_NEW_AGENT;
	public static final String TEXT_NEW_AGENT_CLIENT_APP = DatabaseFactory.RES_CUSTOM_NEW_AGENT_CLIENT_APP;
	public static final String TEXT_AGENT_MIGRATION = DatabaseFactory.RES_CUSTOM_AGENT_MIGRATION;
	public static final String TEXT_REGISTERED = DatabaseFactory.RES_CUSTOM_REGISTERED;
	public static final String TEXT_STATUS_CHANGE = DatabaseFactory.RES_CUSTOM_STATUS_CHANGE;
	public static final String TEXT_WELCOME = DatabaseFactory.RES_CUSTOM_WELCOME;
	public static final String TEXT_DL_FAILED = DatabaseFactory.RES_CUSTOM_DL_FAILED;
	public static final String TEXT_DL_FAILED_CLIENT_APP = DatabaseFactory.RES_CUSTOM_DL_FAILED_CLIENT_APP;
	public static final String TEXT_DL_STALE = DatabaseFactory.RES_CUSTOM_DL_STALE;
	public static final String TEXT_DL_STALE_CLIENT_APP = DatabaseFactory.RES_CUSTOM_DL_STALE_CLIENT_APP;
	public static final String TEXT_APPLIED_EDITS = DatabaseFactory.RES_CUSTOM_APPLIED_EDITS;
	public static final String TEXT_AGENT_VENDOR_CHANGE = DatabaseFactory.RES_CUSTOM_AGENT_VENDOR_CHANGE;
	public static final String TEXT_MIGRATED = DatabaseFactory.RES_CUSTOM_MIGRATED;
	
	public static final String VENDOR_HELP_RUNTIME = "vendor_runtime_";
	public static final String VENDOR_HELP_SETUP = "vendor_setup_";
	
	// Default file name constants
	public final static String DEFAULT_FILE_PATH = "/connective/teamup/files/";
	private final static String FILE_AGENT_MIGRATION = "agent_migration.txt";
	private final static String FILE_AGENT_MIGRATION_HTML = "agent_migration.html";
	private final static String FILE_DL_URL_CHANGE = "update_url.txt";
	private final static String FILE_DL_URL_CHANGE_HTML = "update_url.html";
	private final static String FILE_NEW_AGENT = "new_agent.txt";
	private final static String FILE_NEW_AGENT_HTML = "new_agent.html";
	private final static String FILE_NEW_AGENT_CLIENT_APP = "new_agent_client_app.txt";
	private final static String FILE_NEW_AGENT_CLIENT_APP_HTML = "new_agent_client_app.html";
	private final static String FILE_REGISTERED = "registered.txt";
	private final static String FILE_REGISTERED_HTML = "registered.html";
	private final static String FILE_MIGRATED = "migrated.txt";
	private final static String FILE_MIGRATED_HTML = "migrated.html";
	private final static String FILE_STATUS_CHANGE = "status_chg.txt";
	private final static String FILE_STATUS_CHANGE_HTML = "status_chg.html";
	private final static String FILE_WELCOME = "welcome.html";
	private final static String FILE_DL_FAILED = "inactive_sched.txt";
	private final static String FILE_DL_FAILED_HTML = "inactive_sched.html";
	private final static String FILE_DL_FAILED_CLIENT_APP = "inactive_sched_client_app.txt";
	private final static String FILE_DL_FAILED_CLIENT_APP_HTML = "inactive_sched_client_app.html";
	private final static String FILE_DL_STALE = "inactive_nonsched.txt";
	private final static String FILE_DL_STALE_HTML = "inactive_nonsched.html";
	private final static String FILE_DL_STALE_CLIENT_APP = "inactive_nonsched_client_app.txt";
	private final static String FILE_DL_STALE_CLIENT_APP_HTML = "inactive_nonsched_client_app.html";
	private final static String FILE_APPLIED_EDITS = "applied_edits_email.txt";
	private final static String FILE_APPLIED_EDITS_HTML = "applied_edits_email.html";
	private final static String FILE_AGENT_VENDOR_CHANGE = "vendor_chg.txt";
	private final static String FILE_AGENT_VENDOR_CHANGE_HTML = "vendor_chg.html";
	
	// Tag name constants
	public final static String TAG_AGENT_ID = "AgentId";
	public final static String TAG_AGENT_NAME = "AgentName";
	public final static String TAG_AGENT_CONTACT = "AgentContact";
	public final static String TAG_AGENT_EMAIL = "AgentEmail";
	public final static String TAG_AGENT_PASSWORD = "Password";
	public final static String TAG_AGENT_PHONE = "AgentPhone";
	public final static String TAG_AGENT_STATUS = "Status";
	public final static String TAG_AGENT_STATUS_NEW = "StatusNew";
	public final static String TAG_AGENT_STATUS_PRIOR = "StatusPrior";
	public final static String TAG_AGENT_DOWNLOAD_PATH = "AgentDownloadPath";
	public final static String TAG_AMS_NAME = "AgentVendorSystem";
	public final static String TAG_AMS_VERSION = "AgentVendorVersion";
	public final static String TAG_AMS_DOWNLOAD_PATH = "VendorDownloadPath";
	public final static String TAG_CARRIER_ID = "CarrierId";
	public final static String TAG_CARRIER_NAME = "CarrierName";
	public final static String TAG_CARRIER_EMAIL = "CarrierEmail";
	public final static String TAG_CARRIER_SHORTNAME = "CarrierShortName";
	public final static String TAG_URL_DOWNLOAD = "DownloadUrl";
	public final static String TAG_URL_DOWNLOAD_DIR = "DownloadDirectory";
	public final static String TAG_URL_DOWNLOAD_FILE = "DownloadFilename";
	public final static String TAG_URL_HELP = "HelpUrl";
	public final static String TAG_URL_LOGIN = "LoginUrl";
	public final static String TAG_URL_REGISTRATION = "RegistrationUrl";


	/**
	 * Default constructor for CustomTextFactory.  Loads the specified custom text and/or HTML data
	 * if available; otherwise loads the default version of the data.
	 * 
	 * @param textName - The static constant (CustomTextFactory.TEXT_*) representing the custom text to use
	 * @param outputType - Static constant (TYPE_HTML, TYPE_EMAIL or TYPE_TEXT) representing the final data type
	 * @param agentInfo - The agent info bean (if applicable, otherwise null)
	 */
	public CustomTextFactory(String textName, int outputType, ServerInfo serverInfo, AgentInfo agent, DatabaseOperation op) throws Exception
	{
		super();
		
		init(textName, outputType, serverInfo, agent, op, true);
	}

	/**
	 * Constructor for CustomTextFactory.  Allows you to specify whether to load any available custom
	 * text and HTML data, or to always use the default data (even if custom is available).
	 * 
	 * @param textName - The static constant (CustomTextFactory.TEXT_*) representing the custom text to use
	 * @param outputType - Static constant (TYPE_HTML, TYPE_EMAIL or TYPE_TEXT) representing the final data type
	 * @param agentInfo - The agent info bean (if applicable, otherwise null)
	 * @param useCustom - TRUE to use custom data (if available), false to only use default data
	 */
	public CustomTextFactory(String textName, int outputType, ServerInfo serverInfo, AgentInfo agent, DatabaseOperation op, boolean useCustom) throws Exception
	{
		super();
		
		init(textName, outputType, serverInfo, agent, op, useCustom);
	}
	
	private void init(String textName, int outputType, ServerInfo serverInfo, AgentInfo agent, DatabaseOperation op, boolean useCustom) throws Exception
	{
		this.outputType = outputType;
		this.textName = textName;
		setEmailType(outputType, textName);
		if (serverInfo != null)
			this.carrierInfo = serverInfo.getCarrierInfo();
		this.agentInfo = agent;
		if (agent != null)
			this.amsInfo = agent.getAms();
		
		// Retrieve the custom text data, if any has been saved
		InputStream textData  =  null;
		if (op != null) {
			textData = op.getResource(textName);
			text = readText(textData);
		}
		if (text != null && !text.trim().equals(""))
			customText = true;
		if (!useCustom || text == null || text.trim().equals(""))
		{
			// If user has specified to only use the default text, or if no custom text is available,
			// then load the default file
			try
			{
				URL url = getClass().getResource(CustomTextFactory.DEFAULT_FILE_PATH + getDefaultFilename(textName));
				textData = new BufferedInputStream(new FileInputStream(url.getFile()));
				text = readText(textData);
			}
			catch (Exception e)
			{
				LOGGER.error(e.getMessage());
				System.out.println("Cannot read default custom text file '" + textName + " --> " + e.getMessage());
				text = null;
			}
		}
		
		if (outputType == TYPE_EMAIL)
		{
			// Retrieve the custom html data, if any has been saved
			String htmlName = getHtmlTextName(textName);
			InputStream htmlData = null;
			if (op != null ) {
			  op.getResource(htmlName);
			  htmlText = readText(htmlData);
			}
			if (htmlText != null && !htmlText.trim().equals(""))
				customHtml = true;
			if (!useCustom || htmlText == null || htmlText.trim().equals(""))
			{
				// If user has specified to only use the default text, or if no custom HTML is available,
				// then load the default file
				try
				{
					URL url = getClass().getResource(CustomTextFactory.DEFAULT_FILE_PATH + getDefaultFilename(htmlName));
					htmlData = new BufferedInputStream(new FileInputStream(url.getFile()));
					htmlText = readText(htmlData);
				}
				catch (Exception e)
				{
					LOGGER.error(e.getMessage());
					// There is no matching HTML version of this file, so ignore it
					htmlText = null;
				}
			}
			
			if (useCustom)
			{
				// Retrieve the custom email subject line, if any has been saved; otherwise, set to the default
				String subjectName = getSubjectName(textName);
				InputStream subjectData = null;
				if (op != null) {
				  op.getResource(subjectName);
				  emailSubject = readText(subjectData);
				}
				if (emailSubject != null && !emailSubject.trim().equals(""))
					customSubject = true;
				else
					emailSubject = getDefaultSubject(outputType, textName);
			}
			else
			{
				// Set to the default email subject text
				emailSubject = getDefaultSubject(outputType, textName);
			}
				
		}
	}

	/**
	 * Constructor for CustomTextFactory.  This version of the constructor should
	 * only be used for getting the HELP TEXT associated with agency vendor systems
	 * defined in the database table.
	 * 
	 * @param amsInfo - The agency vendor system info bean
	 * @param vendorHelpType - Static constant (VENDOR_HELP_RUNTIME or VENDOR_HELP_SETUP) showing which type of help to retrieve
	 * @param serverInfo - The server info bean
	 */
	public CustomTextFactory(String vendorHelpType, AmsInfo amsInfo, ServerInfo serverInfo, DatabaseOperation op) throws Exception
	{
		super();
		
		if (serverInfo != null)
			this.carrierInfo = serverInfo.getCarrierInfo();
		this.amsInfo = amsInfo;
		
		textName = vendorHelpType;
		if (amsInfo != null)
			textName += amsInfo.getId();
		else
			textName += "OTHER";

		InputStream textData = op.getResource(textName);
		if (textData == null)
		{
			try {
				String filename = CustomTextFactory.DEFAULT_FILE_PATH +	textName + ".jsp";
				URL url = getClass().getResource(filename);
				if (url != null)
					textData = new BufferedInputStream(new FileInputStream(url.getFile()));
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
				textData = null;
			}
		}
		
		text = readText(textData);
		if (text == null)
			text = "";
	}

	/**
	 * Constructor for CustomTextFactory.  This version of the constructor should
	 * only be used for getting the HELP TEXT associated with agency vendor systems
	 * defined in the database table.
	 * 
	 * @param amsInfo - The agency vendor system info bean
	 * @param vendorHelpType - Static constant (VENDOR_HELP_RUNTIME or VENDOR_HELP_SETUP) showing which type of help to retrieve
	 * @param serverInfo - The server info bean
	 */
	public CustomTextFactory(String vendorHelpType, AmsClaimInfo amsInfo, ServerInfo serverInfo, DatabaseOperation op) throws Exception
	{
		super();
		
		if (serverInfo != null)
			this.carrierInfo = serverInfo.getCarrierInfo();
		this.amsClaimInfo = amsInfo;
		
		textName = vendorHelpType;
		if (amsInfo != null)
			textName += amsInfo.getId();
		else
			textName += "OTHER";

		InputStream textData = op.getResource(textName);
		if (textData == null)
		{
			try {
				String filename = CustomTextFactory.DEFAULT_FILE_PATH +	textName + ".jsp";
				URL url = getClass().getResource(filename);
				if (url != null)
					textData = new BufferedInputStream(new FileInputStream(url.getFile()));
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
				textData = null;
			}
		}
		
		text = readText(textData);
		if (text == null)
			text = "";
	}
	private String readText(InputStream instr)
	{
		String text = null;
		if (instr != null)
		{
			try
			{
				text = "";
				byte[] buf = new byte[256];
				int read; 
				while ((read = instr.read(buf, 0, 256)) != -1)
					text += new String(buf, 0, read);
			}
			catch (Exception e)
			{
				LOGGER.error(e.getMessage());
				text = null;
			}
		}
		return text;
	}

	private void setEmailType(int type, String textId)
	{
		if (type == TYPE_EMAIL && textId != null && !textId.trim().equals(""))
		{
			if (textId.equals(TEXT_MIGRATED))
				emailType = EMAIL_TO_CARRIER;
			else if (textId.equals(TEXT_REGISTERED))
				emailType = EMAIL_TO_CARRIER;
			else if (textId.equals(TEXT_STATUS_CHANGE))
				emailType = EMAIL_TO_CARRIER;
			else if (textId.equals(TEXT_AGENT_VENDOR_CHANGE))
				emailType = EMAIL_TO_CARRIER;
			else
				emailType = EMAIL_TO_AGENT;
		}
	}

	private String getDefaultSubject(int type, String textId)
	{
		String subject = "";
		if (type == TYPE_EMAIL && textId != null && !textId.trim().equals(""))
		{
			if (textId.equals(TEXT_AGENT_MIGRATION))
				subject = getTag(TAG_CARRIER_NAME) + " download migration";
			else if (textId.equals(TEXT_AGENT_VENDOR_CHANGE))
				subject = "Agency vendor system change: " + getTag(TAG_AGENT_ID) + " - " + getTag(TAG_AGENT_NAME);
			else if (textId.equals(TEXT_APPLIED_EDITS))
				subject = "Applied Screens and Edits available";
			else if (textId.equals(TEXT_DL_FAILED))
				subject = getTag(TAG_CARRIER_SHORTNAME) + " Scheduled Download Alert";
			else if (textId.equals(TEXT_DL_FAILED_CLIENT_APP))
				subject = getTag(TAG_CARRIER_SHORTNAME) + " Scheduled Download Alert";
			else if (textId.equals(TEXT_DL_STALE))
				subject = getTag(TAG_CARRIER_SHORTNAME) + " Scheduled Download Alert";
			else if (textId.equals(TEXT_DL_STALE_CLIENT_APP))
				subject = getTag(TAG_CARRIER_SHORTNAME) + " Scheduled Download Alert";
			else if (textId.equals(TEXT_DL_URL_CHANGE))
				subject = getTag(TAG_CARRIER_NAME) + " download registration";
			else if (textId.equals(TEXT_MIGRATED))
				subject = "Download Trading Partner migrated: " + getTag(TAG_AGENT_ID) + " - " + getTag(TAG_AGENT_NAME);
			else if (textId.equals(TEXT_NEW_AGENT))
				subject = getTag(TAG_CARRIER_NAME) + " download registration";
			else if (textId.equals(TEXT_NEW_AGENT_CLIENT_APP))
				subject = getTag(TAG_CARRIER_NAME) + " download registration";
			else if (textId.equals(TEXT_REGISTERED))
				subject = "Download Trading Partner registered: " + getTag(TAG_AGENT_ID) + " - " + getTag(TAG_AGENT_NAME);
			else if (textId.equals(TEXT_STATUS_CHANGE))
				subject = "Download Trading Partner status change: " + getTag(TAG_AGENT_ID) + " - " + getTag(TAG_AGENT_NAME);
		}
		
		return subject;
	}

	/**
	 * Returns the default email subject line text for this email.  (Returns empty string for 
	 * non-email files.)
	 */
	public String getDefaultSubject()
	{
		return getDefaultSubject(outputType, textName);
	}

	public static String getHtmlTextName(String textName)
	{
		String htmlName = textName;
		if (htmlName != null && !htmlName.equals(""))
			htmlName += "_html";
		return htmlName;
	}

	private String getSubjectName(String textName)
	{
		String subjectName = textName;
		if (subjectName != null && !subjectName.equals(""))
			subjectName += "_subject";
		return subjectName;
	}

	public static String getDefaultFilename(String fileType)
	{
		String filename = "";
		
		if (fileType != null && !fileType.equals(""))
		{
			if (fileType.equals(TEXT_DL_URL_CHANGE))
				filename = FILE_DL_URL_CHANGE;
			else if (fileType.equals(getHtmlTextName(TEXT_DL_URL_CHANGE)))
				filename = FILE_DL_URL_CHANGE_HTML;
			
			else if (fileType.equals(TEXT_NEW_AGENT))
				filename = FILE_NEW_AGENT;
			else if (fileType.equals(getHtmlTextName(TEXT_NEW_AGENT)))
				filename = FILE_NEW_AGENT_HTML;
			
			else if (fileType.equals(TEXT_NEW_AGENT_CLIENT_APP))
				filename = FILE_NEW_AGENT_CLIENT_APP;
			else if (fileType.equals(getHtmlTextName(TEXT_NEW_AGENT_CLIENT_APP)))
				filename = FILE_NEW_AGENT_CLIENT_APP_HTML;
			
			else if (fileType.equals(TEXT_AGENT_MIGRATION))
				filename = FILE_AGENT_MIGRATION;
			else if (fileType.equals(getHtmlTextName(TEXT_AGENT_MIGRATION)))
				filename = FILE_AGENT_MIGRATION_HTML;
			
			else if (fileType.equals(TEXT_REGISTERED))
				filename = FILE_REGISTERED;
			else if (fileType.equals(getHtmlTextName(TEXT_REGISTERED)))
				filename = FILE_REGISTERED_HTML;
			
			else if (fileType.equals(TEXT_STATUS_CHANGE))
				filename = FILE_STATUS_CHANGE;
			else if (fileType.equals(getHtmlTextName(TEXT_STATUS_CHANGE)))
				filename = FILE_STATUS_CHANGE_HTML;
			
			else if (fileType.equals(TEXT_DL_FAILED))
				filename = FILE_DL_FAILED;
			else if (fileType.equals(getHtmlTextName(TEXT_DL_FAILED)))
				filename = FILE_DL_FAILED_HTML;
			
			else if (fileType.equals(TEXT_DL_FAILED_CLIENT_APP))
				filename = FILE_DL_FAILED_CLIENT_APP;
			else if (fileType.equals(getHtmlTextName(TEXT_DL_FAILED_CLIENT_APP)))
				filename = FILE_DL_FAILED_CLIENT_APP_HTML;
			
			else if (fileType.equals(TEXT_DL_STALE))
				filename = FILE_DL_STALE;
			else if (fileType.equals(getHtmlTextName(TEXT_DL_STALE)))
				filename = FILE_DL_STALE_HTML;
			
			else if (fileType.equals(TEXT_DL_STALE_CLIENT_APP))
				filename = FILE_DL_STALE_CLIENT_APP;
			else if (fileType.equals(getHtmlTextName(TEXT_DL_STALE_CLIENT_APP)))
				filename = FILE_DL_STALE_CLIENT_APP_HTML;
			
			else if (fileType.equals(TEXT_APPLIED_EDITS))
				filename = FILE_APPLIED_EDITS;
			else if (fileType.equals(getHtmlTextName(TEXT_APPLIED_EDITS)))
				filename = FILE_APPLIED_EDITS_HTML;
			
			else if (fileType.equals(TEXT_AGENT_VENDOR_CHANGE))
				filename = FILE_AGENT_VENDOR_CHANGE;
			else if (fileType.equals(getHtmlTextName(TEXT_AGENT_VENDOR_CHANGE)))
				filename = FILE_AGENT_VENDOR_CHANGE_HTML;
			
			else if (fileType.equals(TEXT_MIGRATED))
				filename = FILE_MIGRATED;
			else if (fileType.equals(getHtmlTextName(TEXT_MIGRATED)))
				filename = FILE_MIGRATED_HTML;

			else if (fileType.equals(TEXT_WELCOME))
				filename = FILE_WELCOME;
		}
		
		return filename;
	}

	/**
	 * Returns true if specified custom text is not null or empty.
	 */
	public boolean isTextAvailable()
	{
		return (text != null && !text.trim().equals(""));
	}

	/**
	 * Returns true if the HTML version of the specified custom text is not null or empty.
	 */
	public boolean isHtmlAvailable()
	{
		return (htmlText != null && !htmlText.trim().equals(""));
	}

	/**
	 * Returns the raw text data without any tags formatted with data.
	 */
	public String getRawText()
	{
		return text;
	}

	/**
	 * Returns the raw HTML version of the text data without any tags formatted with data.
	 */
	public String getRawHtml()
	{
		return htmlText;
	}

	/**
	 * Returns the raw email subject text without any tags formatted with data.
	 */
	public String getRawEmailSubject()
	{
		return emailSubject;
	}

	/**
	 * Returns the text with TUDL tags replaced with data.
	 */
	public String getText()
	{
		return getText(null);
	}

	/**
	 * Returns the text with TUDL tags replaced with data.
	 */
	public String getText(DatabaseOperation op)
	{
		if (text == null)
			return "";
		
		return fillTags(text, op);
	}

	/**
	 * Returns the HTML version of the text, with TUDL tags replaced with data.
	 */
	public String getHtml()
	{
		return getHtml(null);
	}

	/**
	 * Returns the HTML version of the text, with TUDL tags replaced with data.
	 */
	public String getHtml(DatabaseOperation op)
	{
		if (htmlText == null)
			return "";
		
		return fillTags(htmlText, op);
	}

	/**
	 * Returns the email subject text, with TUDL tags replaced with data.
	 */
	public String getEmailSubject()
	{
		return getEmailSubject(null);
	}

	/**
	 * Returns the email subject text, with TUDL tags replaced with data.
	 */
	public String getEmailSubject(DatabaseOperation op)
	{
		if (emailSubject == null)
			return "";
		
		return fillTags(emailSubject, op);
	}

	private String fillTags(String inputText, DatabaseOperation op)
	{
		String str = "";
		
		if (inputText != null)
		{
			str = inputText;
			int n = str.indexOf("<TEAMUP:");
			while (n >= 0)
			{
				String temp = "";
				if (n > 0)
					temp = str.substring(0, n);
				if (n >= str.length() - 1)
					str = "";
				else
				{
					str = str.substring(n + 8);
					n = str.indexOf(">");
					if (n == 0)
						str = str.substring(1);
					else if (n > 0)
					{
						String tag = str.substring(0, n);
						temp += getData(tag, op);
						str = str.substring(n+1);
					}
				}
				str = temp + str;
				
				n = str.indexOf("<TEAMUP:");
			}
		}
		
		return str;
	}
	
	private String getData(String tagname, DatabaseOperation op) 
	{
		String data = "";
		
		try
		{
			PropertyResourceBundle props = new PropertyResourceBundle(getClass().getResourceAsStream("/teamworkdl.properties"));
			dlSupportUrl = props.getString("download.connective.support") + "/agents";
			
			if (tagname.equalsIgnoreCase(TAG_AGENT_ID))
			{
				if (agentInfo != null)
					data = agentInfo.getAgentId();
			}
			else if (tagname.equalsIgnoreCase(TAG_AGENT_NAME))
			{
				if (agentInfo != null)
					data = agentInfo.getName();
			}
			else if (tagname.equalsIgnoreCase(TAG_AGENT_CONTACT))
			{
				if (agentInfo != null)
					data = agentInfo.getContactName();
			}
			else if (tagname.equalsIgnoreCase(TAG_AGENT_DOWNLOAD_PATH))
			{
				if (agentInfo != null)
					data = agentInfo.getRemoteDir();
			}
			else if (tagname.equalsIgnoreCase(TAG_AGENT_EMAIL))
			{
				if (agentInfo != null)
					data = agentInfo.getContactEmail();
			}
			else if (tagname.equalsIgnoreCase(TAG_AGENT_PASSWORD))
			{
				if (agentInfo != null)
					data = agentInfo.getPassword();
			}
			else if (tagname.equalsIgnoreCase(TAG_AGENT_PHONE))
			{
				if (agentInfo != null)
				{
					data = agentInfo.getContactPhone().trim();
					if (data.length() >= 10)
					{
						String ext = "";
						if (data.length() > 10)
							ext = " ext. " + data.substring(10);
						else
							data += " ";
						data = "(" + data.substring(0, 3) + ") " + data.substring(3, 6) + "-" + data.substring(6, 10) + ext;
					}
				}
			}
			else if (tagname.equalsIgnoreCase(TAG_AGENT_STATUS_PRIOR))
			{
				if (priorStatus == null || priorStatus.equals(""))
					data = "(unknown)";
				else
					data = priorStatus;
			}
			else if (tagname.equalsIgnoreCase(TAG_AGENT_STATUS_NEW) ||
					 tagname.equalsIgnoreCase(TAG_AGENT_STATUS))
			{
				if (agentInfo == null)
					data = "(unknown)";
				else
				{
					data = agentInfo.getStatusDisplay().toLowerCase();
					if (data.equals(""))
						data = "(unknown)";
				}
			}
			else if (tagname.equalsIgnoreCase(TAG_AMS_DOWNLOAD_PATH))
			{
				if (amsInfo != null)
					data = amsInfo.getCompanyDir();
				else if (agentInfo != null && agentInfo.getAms() != null)
					data = agentInfo.getAms().getCompanyDir();
			}
			else if (tagname.equalsIgnoreCase(TAG_AMS_NAME))
			{
				if (amsInfo != null)
					data = amsInfo.getDisplayName();
				else if (agentInfo != null && agentInfo.getAms() != null)
					data = agentInfo.getAms().getDisplayName();
			}
			else if (tagname.equalsIgnoreCase(TAG_AMS_VERSION))
			{
				if (agentInfo != null)
					data = agentInfo.getAmsVer();
			}
			else if (tagname.equalsIgnoreCase(TAG_CARRIER_ID))
			{
				if (carrierInfo != null)
					data = carrierInfo.getCarrierId();
				else if (op != null)
					data = op.getPropertyValue(DatabaseFactory.PROP_CARRIER_ID);
			}
			else if (tagname.equalsIgnoreCase(TAG_CARRIER_NAME))
			{
				if (carrierInfo != null)
					data = carrierInfo.getName();
				else if (op != null)
					data = op.getPropertyValue(DatabaseFactory.PROP_CARRIER_NAME);
			}
			else if (tagname.equalsIgnoreCase(TAG_CARRIER_EMAIL))
			{
				if (carrierInfo != null)
					data = carrierInfo.getContactEmail();
				else if (op != null)
					data = op.getPropertyValue(DatabaseFactory.PROP_EMAIL_CONTACT);
			}
			else if (tagname.equalsIgnoreCase(TAG_CARRIER_SHORTNAME))
			{
				if (carrierInfo != null)
					data = carrierInfo.getShortName();
				else if (op != null)
					data = op.getPropertyValue(DatabaseFactory.PROP_CARRIER_SHORTNAME);
			}
			else if (tagname.equalsIgnoreCase(TAG_URL_DOWNLOAD))
			{
				if (downloadUrl != null)
					data = downloadUrl;
			}
			else if (tagname.equalsIgnoreCase(TAG_URL_DOWNLOAD_DIR))
			{
				if (agentInfo != null)
					data = agentInfo.getRemoteDir();
				else if (amsInfo != null)
					data = amsInfo.getCompanyDir();
			}
			else if (tagname.equalsIgnoreCase(TAG_URL_DOWNLOAD_FILE))
			{
				if (agentInfo != null)
					data = agentInfo.getDefaultFilename();
				else if (amsInfo != null)
					data = amsInfo.getCompanyFilename();
			}
			else if (tagname.equalsIgnoreCase(TAG_URL_HELP))
			{
				if (helpUrl != null)
					data = helpUrl;
			}
			else if (tagname.equalsIgnoreCase(TAG_URL_LOGIN))
			{
				if (loginUrl != null)
					data = loginUrl;
			}
			else if (tagname.equalsIgnoreCase(TAG_URL_REGISTRATION))
			{
				if (carrierInfo != null)
				{
					data += "carid=" + carrierInfo.getCarrierId() +
							"&shortname=" + Escape.forUrl(carrierInfo.getShortName());
				}
				else if (op != null)
				{
					data += "carid=" + op.getPropertyValue(DatabaseFactory.PROP_CARRIER_ID) +
							"&shortname=" + Escape.forUrl(op.getPropertyValue(DatabaseFactory.PROP_CARRIER_SHORTNAME));
				}
				if (agentInfo != null)
				{
					if (!data.equals(""))
						data += "&";
					data += "avs=";
					if (agentInfo.getAms() == null || agentInfo.getAms().getId() == null || agentInfo.getAms().getId().equals(""))
						data += "unk";
					else
						data += agentInfo.getAms().getId();
					
					data += "&agentid=";
					data += agentInfo.getAgentId();
					
					data += "&mig=";
					if (agentInfo.isLive() && !agentInfo.isClientAppRegistered())
						data += "Y";
					else
						data += "N";
				}
				if (data.equals(""))
					data = dlSupportUrl;
				else
					data = dlSupportUrl + "?" + data;
			}
		}
		catch(IOException io){
			LOGGER.error(io.getMessage());
			io.printStackTrace();
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
			// data could not be retrieved or compiled -- ignore error and return empty data
		}
		
		return data;
	}

	public static String getTag(String tagname)
	{
		if (tagname == null || tagname.equals(""))
			return "";
		
		return "<TEAMUP:" + tagname + ">";
	}

	public String getTagValue(String tagname, DatabaseOperation op)
	{
		String value = getData(tagname, op);
		if (value == null)
			value = "";
		
		return value;
	}

	/**
	 * Sets the download URL.
	 * @param downloadUrl The URL to set
	 */
	public void setDownloadUrl(String downloadUrl)
	{
		this.downloadUrl = downloadUrl;
	}

	/**
	 * Sets the help URL.
	 * @param helpUrl The URL to set
	 */
	public void setHelpUrl(String helpUrl)
	{
		this.helpUrl = helpUrl;
	}

	/**
	 * Sets the login URL.
	 * @param loginUrl The URL to set
	 */
	public void setLoginUrl(String loginUrl)
	{
		this.loginUrl = loginUrl;
	}

	/**
	 * Sets the prior status.
	 * @param priorStatus The prior status to set
	 */
	public void setPriorStatus(String priorStatus)
	{
		this.priorStatus = priorStatus;
	}

	/**
	 * Saves the updated text out to the resource table.
	 * @param text - The updated text to save
	 * @param op - The database operation
	 */
	public void updateText(String text, DatabaseOperation op) throws Exception
	{
		this.text = text;
		if (text == null || text.trim().equals(""))
			op.removeResource(textName);
		else
			op.saveResource(textName, new ByteArrayInputStream(text.getBytes()), text.length());
	}

	/**
	 * Saves the updated HTML version of the text data to the resource table.
	 * @param htmlText - The updated HTML text to save
	 * @param op - The database operation
	 */
	public void updateHtml(String htmlText, DatabaseOperation op) throws Exception
	{
		this.htmlText = htmlText;
		if (htmlText == null || htmlText.trim().equals(""))
			op.removeResource(getHtmlTextName(textName));
		else
			op.saveResource(getHtmlTextName(textName), new ByteArrayInputStream(htmlText.getBytes()), htmlText.length());
	}

	/**
	 * Saves the updated email subject line text to the resource table.
	 * @param htmlText - The updated email subject line text to save
	 * @param op - The database operation
	 */
	public void updateEmailSubject(String subjectText, DatabaseOperation op) throws Exception
	{
		this.emailSubject = subjectText;
		if (subjectText == null || subjectText.trim().equals(""))
			op.removeResource(getSubjectName(textName));
		else
			op.saveResource(getSubjectName(textName), new ByteArrayInputStream(subjectText.getBytes()), subjectText.length());
	}

	/**
	 * Returns a description of the specified tag name.
	 */
	public String getTagDescription(String tagname)
	{
		String desc = null;
		if (tagname != null && !tagname.equals(""))
			desc = (String) getTagDescriptions().get(tagname);
		if (desc == null)
			desc = "";
		
		return desc;
	}

	/**
	 * Returns a hash map of all defined tag names and their corresponding descriptions.
	 */
	public HashMap getTagDescriptions()
	{
		if (tagMap == null)
		{
			tagMap = new HashMap();
			tagMap.put(TAG_AGENT_ID, "Agent ID");
			tagMap.put(TAG_AGENT_NAME, "Agent Name");
			tagMap.put(TAG_AGENT_CONTACT, "Agent Contact Name");
			tagMap.put(TAG_AGENT_EMAIL, "Agent Contact Email");
			tagMap.put(TAG_AGENT_PASSWORD, "Agent Password");
			tagMap.put(TAG_AGENT_PHONE, "Agent Phone Number");
			tagMap.put(TAG_AGENT_STATUS, "Agent Status");
			tagMap.put(TAG_AGENT_STATUS_NEW, "Agent Status (New)");
			tagMap.put(TAG_AGENT_STATUS_PRIOR, "Agent Status (Prior)");
			tagMap.put(TAG_AGENT_DOWNLOAD_PATH, "Agent Download Path");
			tagMap.put(TAG_AMS_NAME, "Vendor System Name");
			tagMap.put(TAG_AMS_VERSION, "Vendor Software Version");
			tagMap.put(TAG_AMS_DOWNLOAD_PATH, "Vendor System Download Path");
			tagMap.put(TAG_CARRIER_ID, "Company ID");
			tagMap.put(TAG_CARRIER_NAME, "Company Name");
			tagMap.put(TAG_CARRIER_EMAIL, "Company Contact Email");
			tagMap.put(TAG_CARRIER_SHORTNAME, "Company Short Name");
			tagMap.put(TAG_URL_DOWNLOAD, "Download URL");
			tagMap.put(TAG_URL_DOWNLOAD_DIR, "Default Download Path");
			tagMap.put(TAG_URL_DOWNLOAD_FILE, "Default Download Filename");
			tagMap.put(TAG_URL_HELP, "Help Page URL");
			tagMap.put(TAG_URL_LOGIN, "Agent Browser Login URL");
			tagMap.put(TAG_URL_REGISTRATION, "Agent Registration URL");
		}
		return tagMap;
	}

	/**
	 * Returns a list of all defined tag names.
	 */
	public ArrayList getFullTagList()
	{
		if (tagList == null)
		{
			tagList = new ArrayList();
			tagList.add(TAG_AGENT_ID);
			tagList.add(TAG_AGENT_NAME);
			tagList.add(TAG_AGENT_CONTACT);
			tagList.add(TAG_AGENT_EMAIL);
			tagList.add(TAG_AGENT_PASSWORD);
			tagList.add(TAG_AGENT_PHONE);
			tagList.add(TAG_AGENT_STATUS);
			tagList.add(TAG_AGENT_STATUS_NEW);
			tagList.add(TAG_AGENT_STATUS_PRIOR);
			tagList.add(TAG_AGENT_DOWNLOAD_PATH);
			tagList.add(TAG_AMS_NAME);
			tagList.add(TAG_AMS_VERSION);
			tagList.add(TAG_AMS_DOWNLOAD_PATH);
			tagList.add(TAG_CARRIER_ID);
			tagList.add(TAG_CARRIER_NAME);
			tagList.add(TAG_CARRIER_EMAIL);
			tagList.add(TAG_CARRIER_SHORTNAME);
			tagList.add(TAG_URL_DOWNLOAD);
			tagList.add(TAG_URL_DOWNLOAD_DIR);
			tagList.add(TAG_URL_DOWNLOAD_FILE);
			tagList.add(TAG_URL_HELP);
			tagList.add(TAG_URL_LOGIN);
			tagList.add(TAG_URL_REGISTRATION);
		}
		return tagList;
	}

	/**
	 * Returns a list of all defined agent-specific tag names.
	 */
	public ArrayList getAgentTagList()
	{
		if (agentTagList == null)
		{
			agentTagList = new ArrayList();
			agentTagList.add(TAG_AGENT_ID);
			agentTagList.add(TAG_AGENT_NAME);
			agentTagList.add(TAG_AGENT_CONTACT);
			agentTagList.add(TAG_AGENT_EMAIL);
			agentTagList.add(TAG_AGENT_PASSWORD);
			agentTagList.add(TAG_AGENT_PHONE);
			agentTagList.add(TAG_AGENT_STATUS);
			agentTagList.add(TAG_AGENT_STATUS_NEW);
			agentTagList.add(TAG_AGENT_STATUS_PRIOR);
			agentTagList.add(TAG_AGENT_DOWNLOAD_PATH);
			agentTagList.add(TAG_AMS_NAME);
			agentTagList.add(TAG_AMS_VERSION);
			agentTagList.add(TAG_AMS_DOWNLOAD_PATH);
			agentTagList.add(TAG_URL_DOWNLOAD_DIR);
			agentTagList.add(TAG_URL_DOWNLOAD_FILE);
		}
		return agentTagList;
	}

	/**
	 * Returns a list of all defined company-specific tag names.
	 */
	public ArrayList getCompanyTagList()
	{
		if (companyTagList == null)
		{
			companyTagList = new ArrayList();
			companyTagList.add(TAG_CARRIER_ID);
			companyTagList.add(TAG_CARRIER_NAME);
			companyTagList.add(TAG_CARRIER_EMAIL);
			companyTagList.add(TAG_CARRIER_SHORTNAME);
			companyTagList.add(TAG_URL_DOWNLOAD);
//			companyTagList.add(TAG_URL_HELP);
			companyTagList.add(TAG_URL_LOGIN);
			companyTagList.add(TAG_URL_REGISTRATION);
		}
		return companyTagList;
	}

	public String getFileDescription()
	{
		return getFileDescription(textName);
	}

	public static String getFileDescription(String textId)
	{
		String ret = "";
		if (textId != null && !textId.trim().equals(""))
		{
			if (textId.equals(TEXT_AGENT_MIGRATION))
				ret = "Migration of existing agent to Agent Workstation";
			else if (textId.equals(TEXT_AGENT_VENDOR_CHANGE))
				ret = "Notification of agent change of vendor system info";
			else if (textId.equals(TEXT_APPLIED_EDITS))
				ret = "Notification of Applied Edits to be downloaded";
			else if (textId.equals(TEXT_DL_FAILED))
				ret = "Download Alert! email (browser)";
			else if (textId.equals(TEXT_DL_FAILED_CLIENT_APP))
				ret = "Download Alert! email";
			else if (textId.equals(TEXT_DL_STALE))
				ret = "Download Alert! email (browser)";
			else if (textId.equals(TEXT_DL_STALE_CLIENT_APP))
				ret = "Download Alert! email";
			else if (textId.equals(TEXT_DL_URL_CHANGE))
				ret = "Notification of URL or password change";
			else if (textId.equals(TEXT_MIGRATED))
				ret = "Notification of agent migration completion";
			else if (textId.equals(TEXT_NEW_AGENT))
				ret = "New agent registration invitation (browser)";
			else if (textId.equals(TEXT_NEW_AGENT_CLIENT_APP))
				ret = "New agent registration invitation";
			else if (textId.equals(TEXT_REGISTERED))
				ret = "Notification of agent registration completion";
			else if (textId.equals(TEXT_STATUS_CHANGE))
				ret = "Notification of agent status change";
			else if (textId.equals(TEXT_WELCOME))
				ret = "Agent browser registration Welcome page";
		}
		
		return ret;
	}

	/**
	 * Returns true if the HTML text is customized, false if retrieved from the default file.
	 */
	public boolean isCustomHtml()
	{
		return customHtml;
	}

	/**
	 * Returns true if the text is customized, false if retrieved from the default file.
	 */
	public boolean isCustomText()
	{
		return customText;
	}

	/**
	 * Returns true if the email subject is customized, false if the default is used.
	 */
	public boolean isCustomSubject()
	{
		return customSubject;
	}

	/**
	 * Returns true if email target/recipient is the agent.
	 */
	public boolean isEmailToAgent()
	{
		return (emailType == EMAIL_TO_AGENT);
	}

	/**
	 * Returns true if email target/recipient is the carrier.
	 */
	public boolean isEmailToCarrier()
	{
		return (emailType == EMAIL_TO_CARRIER);
	}

	/**
	 * Returns the agent info bean currently being used by the custom text factory.
	 */
	public AgentInfo getAgentInfo()
	{
		return agentInfo;
	}

	/**
	 * Sets the text factory to use a set of "dummy" agent info (should be used for display purposes only).
	 */
	public void useDummyAgent(DatabaseOperation op, ServerInfo serverInfo, HttpServletRequest req)
	{
		// Create the dummy agent info bean
		agentInfo = new AgentInfo(null);
		agentInfo.setName("XYZ Insurance Agency");
		agentInfo.setAgentId("12345678");
		try {
			agentInfo.setStatus(AgentInfo.STATUS_LIVE);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		agentInfo.setClientAppRegistered(true);
		agentInfo.setContactName("Betty Sue Agent");
		agentInfo.setContactEmail("bettysue@xyz.com");
		agentInfo.setContactPhone("8005551212");
		agentInfo.setLocationState("OH");
		agentInfo.setPassword("password");
		this.priorStatus = "active (registered)";
		
		// Set vendor system specific data
		try
		{
			amsInfo = op.getAmsInfo("AFW");
		} catch (Exception e) {}
		if (amsInfo != null)
		{
			agentInfo.setAms(amsInfo);
			agentInfo.setAmsVer("1.23");
			agentInfo.setDefaultFilename(amsInfo.getDefaultFilename());
			agentInfo.setRemoteDir(amsInfo.getDefaultDir());
		}
		
		// Set dummy url and link data
		String url = serverInfo.getRequestUrl(req, "/agency");
		setLoginUrl(url);
		String key = serverInfo.getSecurityProvider().getSecurityKey(agentInfo.getAgentId(), agentInfo.getAgentId(), agentInfo.getPassword());
		setDownloadUrl(url + "?action=dlstart&key=" + key);
	}

}
