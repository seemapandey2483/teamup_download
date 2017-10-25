package connective.teamup.download.beans;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.CarrierInfo;
import connective.teamup.download.CustomTextFactory;
import connective.teamup.download.DisplayBean;
import connective.teamup.download.DisplayBeanException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author Kyle McCreary
 *
 * Display bean used to create the introductory "Welcome" page displayed for
 * agents who have not yet completed the registration process.
 */
public class WelcomeDisplayBean implements Serializable, DisplayBean
{
	private static final Logger LOGGER = Logger.getLogger(WelcomeDisplayBean.class);
	
	private String agentId = "";
	private String agentName = "";
	private String pageText = "";
	private CarrierInfo carrierInfo = null;
	
	private String classid = "";
	private String codebase = "";
	private String forwardUrl = "";
	

	/**
	 * Constructor for WelcomeDisplayBean.
	 */
	public WelcomeDisplayBean()
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
			
			// Load the agency info
			AgentInfo agentBean = serverInfo.getAgentInfo(req.getSession(), op);
			if (agentBean != null)
			{
				agentId = agentBean.getAgentId();
				agentName = agentBean.getName();
			}
			
			// Load the custom text to be displayed
			CustomTextFactory factory = new CustomTextFactory(
				CustomTextFactory.TEXT_WELCOME, CustomTextFactory.TYPE_HTML, serverInfo, agentBean, op);
			pageText = factory.getText();
			
			// Load the base control attributes
			this.classid = serverInfo.getBaseControlClassid();
			this.codebase = serverInfo.getBaseControlCodebase();
			
			// Build the forwarding url
			forwardUrl = serverInfo.getRequestUrl(req) + "?action=welcomenext";
		}
		catch (Exception e)
		{
			LOGGER.error("Error occurred building the new agent welcome page", e);
			throw new DisplayBeanException("Error occurred building the new agent welcome page", e);
		}
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
	 * Returns the carrier info bean.
	 * @return CarrierInfo
	 */
	public CarrierInfo getCarrierInfo()
	{
		return carrierInfo;
	}

	/**
	 * Returns the page text.
	 * @return String
	 */
	public String getPageText()
	{
		if (pageText == null)
			return "";
		
		return pageText;
	}

	/**
	 * Returns the base control classid.
	 * @return String
	 */
	public String getClassid()
	{
		return classid;
	}

	/**
	 * Returns the base control codebase.
	 * @return String
	 */
	public String getCodebase()
	{
		return codebase;
	}

	/**
	 * Returns the forwarding URL for the base control.
	 * @return String
	 */
	public String getForwardUrl()
	{
		return forwardUrl;
	}

}
