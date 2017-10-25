package connective.teamup.download.beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

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
 */
public class CarGroupDefineDisplayBean implements Serializable, DisplayBean
{
	private static final Logger LOGGER = Logger.getLogger(CarGroupDefineDisplayBean.class);
	
	private CarrierInfo carrierInfo = null;
	private String groupName = null;
	private String groupDesc = null;
	private int totalAgents = 0;
	private ArrayList agentList = null;


	/* (non-Javadoc)
	 * @see connective.teamup.download.DisplayBean#init(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, connective.teamup.download.ServerInfo, connective.teamup.download.db.DatabaseOperation, org.apache.commons.fileupload.FileItem[])
	 */
	public void init(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws DisplayBeanException
	{
		try
		{
			// Load the carrier info
			carrierInfo = serverInfo.getCarrierInfo();
			
			// Load the rollout group info
			groupName = req.getParameter("groupName");
			if (groupName == null || groupName.equals(""))
			{
				SimpleDateFormat df = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
				df.applyPattern("MM/dd/yyyy");
				groupName = "Agent Rollout - " + df.format(new Date(System.currentTimeMillis()));
			}
			groupDesc = req.getParameter("groupDesc");
			if (groupDesc == null)
				groupDesc = "";
			
			// Compile hashtable of agents that are already members of a rollout group
			Hashtable hash = new Hashtable();
			AgentInfo[] members = op.getAgentMembersForGroupType("R");
			if (members != null)
			{
				for (int i=0; i < members.length; i++)
					hash.put(members[i].getAgentId(), members[i]);
			}
			
			// Compile list of all agents that are NOT registered and are NOT already members of a rollout group
			agentList = new ArrayList();
			AgentInfo[] agents = op.getAllAgentsByName(true);
			if (agents != null)
			{
				for (int i=0; i < agents.length; i++)
				{
					if (!agents[i].isRegistered())
					{
						totalAgents++;
						if (hash.get(agents[i].getAgentId()) == null)
							agentList.add(agents[i]);
					}
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error occurred retrieving new rollout group list", e);
			throw new DisplayBeanException("Error occurred retrieving new rollout group list", e);
		}
	}

	/**
	 * @return
	 */
	public CarrierInfo getCarrierInfo()
	{
		return carrierInfo;
	}

	/**
	 * @return
	 */
	public String getGroupDesc()
	{
		return groupDesc;
	}

	/**
	 * @return
	 */
	public String getGroupName()
	{
		return groupName;
	}

	/**
	 * @param info
	 */
	public void setCarrierInfo(CarrierInfo info)
	{
		carrierInfo = info;
	}

	/**
	 * @param string
	 */
	public void setGroupDesc(String string)
	{
		groupDesc = string;
	}

	/**
	 * @param string
	 */
	public void setGroupName(String string)
	{
		groupName = string;
	}

	/**
	 * @return
	 */
	public AgentInfo getAgent(int index)
	{
		if (index < 0 || index >= agentList.size())
			return null;
		return (AgentInfo) agentList.get(index);
	}

	public int getAgentCount()
	{
		if (agentList == null)
			return 0;
		return agentList.size();
	}

	/**
	 * @return
	 */
	public int getGroupMembers()
	{
		return (totalAgents - getAgentCount());
	}

	/**
	 * @return
	 */
	public int getListedAgents()
	{
		return getAgentCount();
	}

	/**
	 * @return
	 */
	public int getTotalAgents()
	{
		return totalAgents;
	}

	/**
	 * Returns the specified agent's vendor system name and version.
	 * @return String
	 */
	public String getAgentVendorSystem(int index)
	{
		String vendor;
		
		AgentInfo agentInfo = getAgent(index);
		if (agentInfo == null || agentInfo.getAms() == null ||
			agentInfo.getAms().getName() == null)
		{
			vendor = "";
		}
		else
		{
			vendor = agentInfo.getAms().getName();
			if (agentInfo.getAmsVer() != null && !agentInfo.getAmsVer().equals(""))
				vendor += " " + agentInfo.getAmsVer();
		}
		
		return vendor;
	}

}
