package connective.teamup.download.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.CarrierInfo;
import connective.teamup.download.DisplayBean;
import connective.teamup.download.DisplayBeanException;
import connective.teamup.download.Escape;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentGroupInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author Kyle McCreary
 *
 */
public class MigrationReportDisplayBean implements Serializable, DisplayBean
{
	private static final Logger LOGGER = Logger.getLogger(MigrationReportDisplayBean.class);
	
	private ArrayList clientList = null;
	private ArrayList browserList = null;
	private HashMap groupHash = null;

	private CarrierInfo carrierInfo = null;


	/**
	 * @see connective.teamup.download.DisplayBean#init(HttpServletRequest, HttpServletResponse, ServerInfo, DatabaseOperation, FileItem[])
	 */
	public void init(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws DisplayBeanException 
	{
		try
		{
			// Load the carrier info
			carrierInfo = serverInfo.getCarrierInfo();
			
			// Load the trading partner info and sort into separate lists
			clientList = new ArrayList();
			browserList = new ArrayList();
			groupHash = new HashMap();
			AgentInfo[] agents = op.getRegisteredAgents();
			if (agents != null)
			{
				for (int i=0; i < agents.length; i++)
				{
					if (agents[i].getAms() == null)
					{
						// ignore
					}
					else if (agents[i].isClientAppRegistered())
						clientList.add(agents[i]);
					else
						browserList.add(agents[i]);
					
					AgentGroupInfo[] groups = agents[i].getAgentGroups(AgentGroupInfo.TYPE_MIGRATION);
					if (groups != null && groups.length > 0)
						groupHash.put(agents[i].getAgentId(), groups[0]);
				}
			}
		}
		catch (SQLException e)
		{
			LOGGER.error("Error occurred building the list of download agents", e);
			throw new DisplayBeanException("Error occurred building the list of download agents", e);
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
	 * Returns the carrier info bean.
	 * @return CarrierInfo
	 */
	public CarrierInfo getCarrierInfo()
	{
		return carrierInfo;
	}

	/**
	 * Returns the number of agents using the java client app.
	 * @return int
	 */
	public int getJavaAppCount()
	{
		if (clientList == null)
			return 0;
		
		return clientList.size();
	}

	/**
	 * Returns the specified agent info.
	 * @return AgentInfo
	 */
	public AgentInfo getJavaAppAgent(int index)
	{
		if (clientList == null || index >= clientList.size())
			return null;
		
		return (AgentInfo) clientList.get(index);
	}

	/**
	 * Returns the number of agents using the browser version of the app.
	 * @return int
	 */
	public int getBrowserCount()
	{
		if (browserList == null)
			return 0;
		
		return browserList.size();
	}

	/**
	 * Returns the specified agent info.
	 * @return AgentInfo
	 */
	public AgentInfo getBrowserAgent(int index)
	{
		if (browserList == null || index >= browserList.size())
			return null;
		
		return (AgentInfo) browserList.get(index);
	}

	public String getMigrationGroup(String agentId)
	{
		String ret = "";
		AgentGroupInfo group = (AgentGroupInfo) groupHash.get(agentId);
		if (group != null)
			ret = Escape.forHtml(group.getName());
		return ret;
	}

}
