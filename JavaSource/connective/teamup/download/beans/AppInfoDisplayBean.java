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
import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.services.EmailService;

/**
 * @author Kyle McCreary
 *
 * Display bean for building the "About..." application info page.
 */
public class AppInfoDisplayBean implements Serializable, DisplayBean
{
	private static final Logger LOGGER = Logger.getLogger(AppInfoDisplayBean.class);
	
	private String agentId = "";
	private String appVersion = "";
	private String dbVersion = "";
	private String appDeployDate = "";
	private String appStartDate = "";
	private String techSupport = "";
	
	private CarrierInfo carrierInfo = null;


	/**
	 * Constructor for SimpleDisplayBean.
	 */
	public AppInfoDisplayBean()
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
			// Load the carrier info
			this.carrierInfo = serverInfo.getCarrierInfo();
			this.techSupport = EmailService.getInstance().getTechSupportEmailAddress();
			
			// Load the application info
			this.appVersion = serverInfo.getAppVersion();
			this.dbVersion = op.getPropertyValue(DatabaseFactory.PROP_DBVERSION);
			this.appDeployDate = op.getPropertyValue(DatabaseFactory.PROP_VERSION_DEPLOYED);
			this.appStartDate = carrierInfo.getAppStartedDateFormatted();
			
			// Load the agent info, if applicable
			AgentInfo agentBean = serverInfo.getAgentInfo(req.getSession(), op);
			if (agentBean != null)
			{
				this.agentId = agentBean.getAgentId();
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error occurred retrieving application properties", e);
			throw new DisplayBeanException("Error occurred retrieving application properties", e);
		}
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
	 * Returns the carrier's contact email address.
	 * @return String
	 */
	public String getCarrierEmail()
	{
		return carrierInfo.getContactEmail();
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
	 * Returns the carrier's display name.
	 * @return String
	 */
	public String getCarrierName()
	 {
		return carrierInfo.getName();
	}

	/**
	 * Returns the carrier's short name.
	 * @return String
	 */
	public String getCarrierShortName()
	{
		return carrierInfo.getShortName();
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
	 * Returns the date the current app version was deployed.
	 * @return String
	 */
	public String getAppDeployDate()
	{
		return appDeployDate;
	}

	/**
	 * Returns the date and time the app was last restarted.
	 * @return String
	 */
	public String getAppStartDate()
	{
		return appStartDate;
	}
	
	/**
	 * Returns the technical support email address.
	 * @return String
	 */
	public String getTechSupportEmail()
	{
		if (techSupport == null)
			techSupport = "";
		return techSupport;
	}

	/**
	 * Returns the current agent ID, if applicable.
	 * @return String
	 */
	public String getAgentId()
	{
		return agentId;
	}

}
