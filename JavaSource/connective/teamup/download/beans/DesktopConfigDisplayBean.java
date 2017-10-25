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
 * @author Kyle McCreary
 *
 * Display bean used to build the agency registration pages containing the
 * registration ActiveX controls.
 */
public class DesktopConfigDisplayBean implements DisplayBean, Serializable
{
	private static final Logger LOGGER = Logger.getLogger(DesktopConfigDisplayBean.class);
	
	private CarrierInfo carrierInfo = null;
	private AgentInfo agentInfo = null;
	
	private String classid;
	private String codebase;
	private String downloadLink;
	private String configLink;
	private String scheduleTask;
	private String desktopDownload;
	private String desktopConfig;
	private String removeScheduleFlag;
	
	private String newRegistration;
	
	private boolean loginControlDisplayed = true;


	/**
	 * Constructor for DesktopConfigDisplayBean.
	 */
	public DesktopConfigDisplayBean()
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
			carrierInfo = serverInfo.getCarrierInfo();
			loginControlDisplayed = carrierInfo.isDisplayLoginShortcutControl();

			// Load the agency info
			agentInfo = serverInfo.getAgentInfo(req.getSession(), op);
			
			// Load the registration control attributes
			this.classid = serverInfo.getRegControlClassid("desktop");
			this.codebase = serverInfo.getRegControlCodebase("desktop");
			
			// Build urls using info from the request
			String serverName = req.getServerName();
			int port = req.getServerPort();
			String servletPath = req.getServletPath();
			String contextPath = req.getContextPath();
			
			String key = serverInfo.getSecurityProvider().getSecurityKey(agentInfo.getAgentId(),agentInfo.getAgentId(), agentInfo.getPassword());
			String keyName = "key";
			
			downloadLink = serverInfo.getRequestUrl(req, "/agency");
			downloadLink += "?action=dlstart&" + keyName + "=" + key;

			configLink = serverInfo.getRequestUrl(req, "/agency");
			
			scheduleTask = req.getParameter("scheduleTask");
			if (scheduleTask == null)
				scheduleTask = "N";
			desktopDownload = req.getParameter("desktopDownload");
			if (desktopDownload == null)
				desktopDownload = "N";
			desktopConfig = req.getParameter("desktopConfig");			
			if (desktopConfig == null)
				desktopConfig = "N";
			removeScheduleFlag = req.getParameter("removeSchedule");
			if (removeScheduleFlag == null)
				removeScheduleFlag = "N";
			
			// Parse the current registration status
			newRegistration = req.getParameter("newRegistration");
			if (newRegistration == null)
				newRegistration = "";
		}
		catch (Exception e)
		{
			LOGGER.error("Error occurred building the desktop config display bean", e);
			throw new DisplayBeanException("Error occurred building the desktop config display bean", e);
		}
	}

	/**
	 * Returns the registration control classid.
	 * @return String
	 */
	public String getClassid()
	{
		return classid;
	}

	/**
	 * Returns the registration control codebase.
	 * @return String
	 */
	public String getCodebase()
	{
		return codebase;
	}

	public String getTaskName()
	{
		String ret = carrierInfo.getShortName() + "_" + agentInfo.getAgentId() + " Download";
		return ret;
	}
	
	public String getDownloadURL()
	{
		return downloadLink;
	}
	
	public String getDownloadDescription()
	{
		String ret = getAgentId() + " Download from " + carrierInfo.getShortName();
		return ret;
	}

	public String getConfigURL()
	{
		return configLink;
	}
	
	public String getConfigDescription()
	{
		String ret = getAgentId() + " Login for " + carrierInfo.getShortName();
		return ret;
	}
	
	public String getCarrierName()
	{
		return carrierInfo.getName();
	}

	public String getAgentId()
	{
		return agentInfo.getAgentId();
	}
	
	public CarrierInfo getCarrierInfo()
	{
		return carrierInfo;
	}
	
	/**
	 * Returns the desktopConfig.
	 * @return String
	 */
	public String getDesktopConfig() {
		return desktopConfig;
	}

	/**
	 * Returns the desktopDownload.
	 * @return String
	 */
	public String getDesktopDownload() {
		return desktopDownload;
	}

	/**
	 * Returns the scheduleTask.
	 * @return String
	 */
	public String getScheduleTask() {
		return scheduleTask;
	}

	/**
	 * Returns the removeScheduleFlag.
	 * @return String
	 */
	public String getRemoveScheduleFlag() {
		return removeScheduleFlag;
	}

	/**
	 * Returns the newRegistration.
	 * @return String
	 */
	public String getNewRegistration() {
		return newRegistration;
	}

	/**
	 * Returns true if the ActiveX control to create a shortcut to the agency
	 * login page should be displayed.
	 * @return boolean
	 */
	public boolean isLoginControlDisplayed()
	{
		return loginControlDisplayed;
	}

}
