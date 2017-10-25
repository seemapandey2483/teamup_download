package connective.teamup.download.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.CarrierInfo;
import connective.teamup.download.DisplayBean;
import connective.teamup.download.DisplayBeanException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentGroupInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.LogInfo;

/**
 * @author Kyle McCreary
 */
public class CarGroupDetailDisplayBean implements Serializable, DisplayBean
{
	private static final Logger LOGGER = Logger.getLogger(CarGroupDetailDisplayBean.class);
	
	private CarrierInfo carrierInfo = null;
	private String groupName = null;
	private String groupDesc = null;
	private String percentComplete = null;
	private String lastEvent = null;
	private String nextEvent = null;
	private int totalAgents = 0;
	private int registeredAgents = 0;
	private AgentInfo[] agents = null;
	private ArrayList eventNames = null;
	private ArrayList eventDates = null;
	private ArrayList actions = null;
	private Hashtable selectHash = null;


	/* (non-Javadoc)
	 * @see connective.teamup.download.DisplayBean#init(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, connective.teamup.download.ServerInfo, connective.teamup.download.db.DatabaseOperation, org.apache.commons.fileupload.FileItem[])
	 */
	public void init(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws DisplayBeanException
	{
		try
		{
			// Load the carrier info
			this.carrierInfo = serverInfo.getCarrierInfo();
			
			// Load the rollout group info and member list
			AgentGroupInfo group = op.getAgentGroup(req.getParameter("groupName"));
			if (group == null)
			{
				String flag = req.getParameter("multiGroups");
				if (flag != null && flag.equals("Y"))
					group = op.getAgentGroup(req.getParameter("groupName") + " #1");
			}
			groupName = group.getName();
			groupDesc = group.getDescription();
			agents = group.getGroupMembers();
			if (agents != null)
				totalAgents = agents.length;
			
			// Determine the suggested next action for this group
			lastEvent = group.getLastAction();
			if (lastEvent == null)
				nextEvent = DatabaseFactory.CAR_1ST_EMAIL;
			else if (lastEvent.equals(DatabaseFactory.CAR_1ST_EMAIL))
				nextEvent = DatabaseFactory.CAR_2ND_EMAIL;
			else if (lastEvent.equals(DatabaseFactory.CAR_2ND_EMAIL))
				nextEvent = DatabaseFactory.CAR_3RD_EMAIL;
			else if (lastEvent.equals(DatabaseFactory.CAR_3RD_EMAIL))
				nextEvent = DatabaseFactory.CAR_FINAL_EMAIL;
			else if (lastEvent.equals(DatabaseFactory.CAR_FINAL_EMAIL))
				nextEvent = DatabaseFactory.CAR_FINAL_EMAIL;
			else
				nextEvent = DatabaseFactory.CAR_1ST_EMAIL;
			
			actions = new ArrayList();
			actions.add(DatabaseFactory.CAR_1ST_EMAIL);
			actions.add(DatabaseFactory.CAR_2ND_EMAIL);
			actions.add(DatabaseFactory.CAR_3RD_EMAIL);
			actions.add(DatabaseFactory.CAR_FINAL_EMAIL);
			
			// Load the group statistics
			String strAction = req.getParameter("action");
			boolean selectAll = (strAction != null && strAction.indexOf("select_all") > 0);
			if (strAction != null && strAction.equals("car_create_group"))
				selectAll = true;
			boolean selectUnreg = (strAction != null && strAction.indexOf("select_unreg") > 0);
			selectHash = new Hashtable();
			if (agents != null && totalAgents > 0)
			{
				for (int i=0; i < agents.length; i++)
				{
					if (selectAll)
						selectHash.put(agents[i].getAgentId(), "checked");
					
					if (agents[i].isRegistered())
						registeredAgents++;
					else if (selectUnreg)
						selectHash.put(agents[i].getAgentId(), "checked");
				}
				percentComplete = String.valueOf((int)(registeredAgents * 100 / totalAgents)) + "%";
			}
			else
				percentComplete = "";
			
			// Load the pre-selected group list
			if (!selectAll && !selectUnreg)
			{
				ArrayList agtList = (ArrayList) req.getSession().getAttribute(ServerInfo.STORE_AGENT_LIST);
				if (agtList != null)
				{
					for (int i=0; i < agtList.size(); i++)
					{
						selectHash.put((String) agtList.get(i), "checked");
					}
					req.getSession().removeAttribute(ServerInfo.STORE_AGENT_LIST);
				}
			}
			
			// Load the group history
			eventNames = new ArrayList();
			eventDates = new ArrayList();
			LogInfo[] history = group.getHistory();
			if (history == null || history.length == 0)
			{
				eventNames.add(group.getLastAction());
				eventDates.add(group.getLastActionDateStrShort());
			}
			else
			{
				for (int i=0; i < history.length; i++)
				{
					eventNames.add(history[i].getFileName());
					eventDates.add(history[i].getCreatedDateStrShort());
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error occurred retrieving application properties", e);
			throw new DisplayBeanException("Error occurred retrieving application properties", e);
		}
	}

	/**
	 * @return
	 */
	public AgentInfo getAgent(int index)
	{
		if (agents == null || index >= agents.length)
			return null;
		return agents[index];
	}

	public int getAgentCount()
	{
		if (agents == null)
			return 0;
		return agents.length;
	}

	/**
	 * Returns the specified agent's vendor system name and version.
	 * @return String
	 */
	public String getAgentVendorSystem(int index)
	{
		String vendor;
		
		if (agents == null || index > agents.length || agents[index].getAms() == null ||
			agents[index].getAms().getName() == null)
		{
			vendor = "";
		}
		else
		{
			vendor = agents[index].getAms().getName();
			if (agents[index].getAmsVer() != null && !agents[index].getAmsVer().equals(""))
				vendor += " " + agents[index].getAmsVer();
		}
		
		return vendor;
	}

	/**
	 * @return
	 */
	public CarrierInfo getCarrierInfo()
	{
		return carrierInfo;
	}

	public int getEventCount()
	{
		return eventNames.size();
	}

	/**
	 * @return
	 */
	public String getEventDate(int index)
	{
		if (index >= eventDates.size())
			return "";
		return (String) eventDates.get(index);
	}

	/**
	 * @return
	 */
	public String getEventName(int index)
	{
		if (index >= eventNames.size())
			return "";
		return (String) eventNames.get(index);
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
	 * @return
	 */
	public String getPercentComplete()
	{
		return percentComplete;
	}

	/**
	 * @return
	 */
	public int getRegisteredAgents()
	{
		return registeredAgents;
	}

	public int getRemainingAgents()
	{
		return totalAgents - registeredAgents;
	}

	/**
	 * @return
	 */
	public int getTotalAgents()
	{
		return totalAgents;
	}

	public boolean isAgentEmailOkay(int index)
	{
		if (agents == null || index >= agents.length)
			return false;
		
		String email = agents[index].getContactEmail();
		return (email != null && !email.trim().equals(""));
	}

	public String getLastEvent()
	{
		return lastEvent;
	}

	public String getNextEvent()
	{
		return nextEvent;
	}

	public int getNextActionCount()
	{
		return actions.size();
	}

	public String getNextAction(int index)
	{
		if (index >= actions.size())
			return "";
		return (String) actions.get(index);
	}

	public String getChecked(int index)
	{
		if (agents == null || index >= agents.length)
			return "";
		
		String checked = (String) selectHash.get(agents[index].getAgentId());
		if (checked == null)
			checked = "";
		return checked;
	}

}
