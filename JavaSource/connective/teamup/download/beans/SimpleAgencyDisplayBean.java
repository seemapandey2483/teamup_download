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

/**
 * @author Kyle W. McCreary
 *
 * Display bean used to build the agency administration pages with few data requirements.
 */
public class SimpleAgencyDisplayBean implements DisplayBean, Serializable
{
	private static final Logger LOGGER = Logger.getLogger(SimpleAgencyDisplayBean.class);
	
	private CarrierInfo carrierInfo = null;
	private String agentId;
	private String agentName;
	private String agentEmail;
	private String baseControlClassid;
	private String baseControlCodebase;
	private boolean passwordChangeAllowed = true;
	private boolean agentScheduled = false;
	private boolean upgradeShown = false;

	/**
	 * Constructor for SimpleAgencyDisplayBean.
	 */
	public SimpleAgencyDisplayBean()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.DisplayBean#init(HttpServletRequest, HttpServletResponse, ServerInfo)
	 */
	public void init(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws DisplayBeanException
	{
		try
		{
			// Load the carrier info
			carrierInfo = serverInfo.getCarrierInfo();
			passwordChangeAllowed = carrierInfo.isAgentPasswordChangeAllowed();
			
			// Load the agency info
			AgentInfo agentBean = serverInfo.getAgentInfo(req.getSession(), op);
			agentId = agentBean.getAgentId();
			agentName = agentBean.getName();
			agentEmail = agentBean.getContactEmail();
			if (agentEmail == null)
				agentEmail = "";
			agentScheduled = !agentBean.isInteractive();
			
			// Only show the upgrade notice if carrier is using the client app, but agent has not
			// yet migrated to the client app -- added 03/31/2006, kwm
			if (serverInfo.getCarrierInfo().isClientAppUsed() && !agentBean.isClientAppRegistered() &&
				serverInfo.getCarrierInfo().isDisplayMigrationBanner())
			{
				upgradeShown = true;
			}
			
			// Load the base control attributes
			baseControlClassid = serverInfo.getBaseControlClassid();
			baseControlCodebase = serverInfo.getBaseControlCodebase();
		}
		catch (Exception e)
		{
			LOGGER.error("Error occurred retrieving agency info", e);
			throw new DisplayBeanException("Error occurred retrieving agency info", e);
		}
	}

	/**
	 * Returns the agent's contact email address.
	 * @return String
	 */
	public String getAgentEmail()
	{
		return agentEmail;
	}

	/**
	 * Returns the agent ID.
	 * @return String
	 */
	public String getAgentId()
	{
		return agentId;
	}

	/**
	 * Returns the agent name.
	 * @return String
	 */
	public String getAgentName()
	{
		return agentName;
	}

	/**
	 * Returns the base ActiveX control classid.
	 * @return String
	 */
	public String getBaseControlClassid()
	 {
		return baseControlClassid;
	}

	/**
	 * Returns the base ActiveX control codebase.
	 * @return String
	 */
	public String getBaseControlCodebase()
	{
		return baseControlCodebase;
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
	 * Returns true if the agent is configured in "Scheduled" mode.
	 * @return boolean
	 */
	public boolean isAgentScheduled()
	{
		return agentScheduled;
	}

	/**
	 * Returns true if agents are allowed to change their own passwords.
	 * @return boolean
	 */
	public boolean isPasswordChangeAllowed()
	{
		return passwordChangeAllowed;
	}

	/**
	 * Returns true if upgrade (migration) image should be shown
	 * @return
	 */
	public boolean isUpgradeShown()
	{
		return upgradeShown;
	}

}
