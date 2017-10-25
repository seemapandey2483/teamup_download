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

/**
 * @author Kyle McCreary
 *
 * Display bean used to build the agency registration pages containing the
 * registration ActiveX controls.
 */
public class SchedulerConfigDisplayBean implements DisplayBean, Serializable
{
	private static final Logger LOGGER = Logger.getLogger(SchedulerConfigDisplayBean.class);
	
	private CarrierInfo carrierInfo = null;
	private AgentInfo agentInfo = null;
	
	private String classid;
	private String codebase;
	private String downloadLink;
	private String newRegistration;

	/**
	 * Constructor for SchedulerConfigDisplayBean.
	 */
	public SchedulerConfigDisplayBean()
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

			// Load the agency info
			agentInfo = serverInfo.getAgentInfo(req.getSession(), op);
			
			// Load the registration control attributes
			this.classid = serverInfo.getRegControlClassid("scheduler");
			this.codebase = serverInfo.getRegControlCodebase("scheduler");
			
			// Build urls using info from the request
			String key = serverInfo.getSecurityProvider().getSecurityKey(agentInfo.getAgentId(), agentInfo.getAgentId(), agentInfo.getPassword());
			String keyName = "key";
				//keyName = serverInfo.getPropertyValue(serverInfo.PROP_SECURITY_PASSWORD);
			
			int port = -1;
			try
			{
				port = Integer.parseInt(op.getPropertyValue(DatabaseFactory.PROP_SCHEDULED_DL_PORT));
			}
			catch (Exception e)
			{
				LOGGER.error(e);
				port = -1;
			}
			if (port < 0)
				port = req.getServerPort();
			downloadLink = serverInfo.getRequestUrl(req, "/agency", port) +
						   "?action=dlstart&" + keyName + "=" + key;
			
			// Parse the current registration status
			newRegistration = req.getParameter("newRegistration");
			if (newRegistration == null)
				newRegistration = "";
		}
		catch (Exception e)
		{
			LOGGER.error("Error occurred retrieving agent security info", e);
			throw new DisplayBeanException("Error occurred retrieving agent security info", e);
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
	 * Returns the newRegistration.
	 * @return String
	 */
	public String getNewRegistration() {
		return newRegistration;
	}

}
