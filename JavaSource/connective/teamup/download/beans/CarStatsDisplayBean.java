/*
 * Created on Sep 27, 2005
 */
package connective.teamup.download.beans;

import java.io.Serializable;

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
 */
public class CarStatsDisplayBean implements Serializable, DisplayBean
{
	private static final Logger LOGGER = Logger.getLogger(CarStatsDisplayBean.class);
	
	class GroupStats implements Serializable
	{
		String name = "";
		String description = "";
		String pctComplete = "";
		String lastAction = "";
		String lastActionDate = "";
		int agentCount = 0;
		int registeredAgents = 0;		
	}

	private CarrierInfo carrierInfo = null;
	private GroupStats[] rolloutGroups = null;
	private GroupStats[] migrationGroups = null;
	private int totalRolloutAgents = 0;
	private int totalRegistered = 0;
	private int totalMigrationAgents = 0;
	private int totalMigrated = 0;


	/* (non-Javadoc)
	 * @see connective.teamup.download.DisplayBean#init(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, connective.teamup.download.ServerInfo, connective.teamup.download.db.DatabaseOperation, org.apache.commons.fileupload.FileItem[])
	 */
	public void init(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws DisplayBeanException
	{
		try
		{
			// Load the carrier info
			this.carrierInfo = serverInfo.getCarrierInfo();
			
			// Load the rollout group list
			AgentGroupInfo[] carGroups = op.getAllAgentGroups(AgentGroupInfo.TYPE_ROLLOUT);
			
			if (carGroups != null && carGroups.length > 0)
			{
				rolloutGroups = new GroupStats[carGroups.length];
				for (int i=0; i < carGroups.length; i++)
				{
					// Load the embedded stats
					rolloutGroups[i] = new GroupStats();
					rolloutGroups[i].name = carGroups[i].getName();
					rolloutGroups[i].description = carGroups[i].getDescription();
					rolloutGroups[i].lastAction = carGroups[i].getLastAction();
					rolloutGroups[i].lastActionDate = carGroups[i].getLastActionDateStrShort();
					
					// Check the group members to calculate remaining stats
					AgentInfo[] agents = carGroups[i].getGroupMembers();
					if (agents != null && agents.length > 0)
					{
						rolloutGroups[i].agentCount = agents.length;
						for (int j=0; j < agents.length; j++)
						{
							if (agents[j].isRegistered())
								rolloutGroups[i].registeredAgents++;
						}
						int pct = (int)(rolloutGroups[i].registeredAgents * 100 / rolloutGroups[i].agentCount);
						rolloutGroups[i].pctComplete = String.valueOf(pct) + "%";
						if (pct < 100)
							rolloutGroups[i].pctComplete = "<font color='red'>" + String.valueOf(pct) + "%</font>";
						else
							rolloutGroups[i].pctComplete = String.valueOf(pct) + "%";
					}
					
					// Add to total stats
					totalRolloutAgents += rolloutGroups[i].agentCount;
					totalRegistered += rolloutGroups[i].registeredAgents;
				}
			}
			
			// Load the migration group list
			carGroups = op.getAllAgentGroups(AgentGroupInfo.TYPE_MIGRATION);
			
			if (carGroups != null && carGroups.length > 0)
			{
				migrationGroups = new GroupStats[carGroups.length];
				for (int i=0; i < carGroups.length; i++)
				{
					// Load the embedded stats
					migrationGroups[i] = new GroupStats();
					migrationGroups[i].name = carGroups[i].getName();
					migrationGroups[i].description = carGroups[i].getDescription();
					migrationGroups[i].lastAction = carGroups[i].getLastAction();
					migrationGroups[i].lastActionDate = carGroups[i].getLastActionDateStrShort();
					
					// Check the group members to calculate remaining stats
					AgentInfo[] agents = carGroups[i].getGroupMembers();
					if (agents != null && agents.length > 0)
					{
						migrationGroups[i].agentCount = agents.length;
						for (int j=0; j < agents.length; j++)
						{
							if (agents[j].isClientAppRegistered())
								migrationGroups[i].registeredAgents++;
						}
						int pct = (int)(migrationGroups[i].registeredAgents * 100 / migrationGroups[i].agentCount);
						migrationGroups[i].pctComplete = String.valueOf(pct) + "%";
						if (pct < 100)
							migrationGroups[i].pctComplete = "<font color='red'>" + String.valueOf(pct) + "%</font>";
						else
							migrationGroups[i].pctComplete = String.valueOf(pct) + "%";
					}
					
					// Add to total stats
					totalMigrationAgents += migrationGroups[i].agentCount;
					totalMigrated += migrationGroups[i].registeredAgents;
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error occurred retrieving agency rollout groups", e);
			throw new DisplayBeanException("Error occurred retrieving agency rollout groups", e);
		}
	}

	public CarrierInfo getCarrierInfo()
	{
		return carrierInfo;
	}

	public int getRolloutGroupAgentCount(int index)
	{
		if (rolloutGroups == null || index >= rolloutGroups.length)
			return 0;
		return rolloutGroups[index].agentCount;
	}

	public int getRolloutGroupCount()
	{
		if (rolloutGroups == null)
			return 0;
		return rolloutGroups.length;
	}

	public String getRolloutGroupDesc(int index)
	{
		if (rolloutGroups == null || index >= rolloutGroups.length)
			return "";
		return rolloutGroups[index].description;
	}

	public String getRolloutGroupLastAction(int index)
	{
		if (rolloutGroups == null || index >= rolloutGroups.length)
			return "";
		return rolloutGroups[index].lastAction;
	}

	public String getRolloutGroupLastActionDate(int index)
	{
		if (rolloutGroups == null || index >= rolloutGroups.length)
			return "";
		return rolloutGroups[index].lastActionDate;
	}

	public String getRolloutGroupName(int index)
	{
		if (rolloutGroups == null || index >= rolloutGroups.length)
			return "";
		return rolloutGroups[index].name;
	}

	public String getRolloutGroupPercentComplete(int index)
	{
		if (rolloutGroups == null || index >= rolloutGroups.length)
			return "";
		return rolloutGroups[index].pctComplete;
	}

	public int getRolloutGroupRegisteredCount(int index)
	{
		if (rolloutGroups == null || index >= rolloutGroups.length)
			return 0;
		return rolloutGroups[index].registeredAgents;
	}

	public int getRolloutGroupUnregisteredCount(int index)
	{
		if (rolloutGroups == null || index >= rolloutGroups.length)
			return 0;
		return (rolloutGroups[index].agentCount - rolloutGroups[index].registeredAgents);
	}

	public int getTotalRolloutAgents()
	{
		return totalRolloutAgents;
	}

	public int getTotalRegistered()
	{
		return totalRegistered;
	}

	public int getTotalUnregistered()
	{
		return (totalRolloutAgents - totalRegistered);
	}

	public int getMigrationGroupAgentCount(int index)
	{
		if (migrationGroups == null || index >= migrationGroups.length)
			return 0;
		return migrationGroups[index].agentCount;
	}

	public int getMigrationGroupCount()
	{
		if (migrationGroups == null)
			return 0;
		return migrationGroups.length;
	}

	public String getMigrationGroupDesc(int index)
	{
		if (migrationGroups == null || index >= migrationGroups.length)
			return "";
		return migrationGroups[index].description;
	}

	public String getMigrationGroupLastAction(int index)
	{
		if (migrationGroups == null || index >= migrationGroups.length)
			return "";
		return migrationGroups[index].lastAction;
	}

	public String getMigrationGroupLastActionDate(int index)
	{
		if (migrationGroups == null || index >= migrationGroups.length)
			return "";
		return migrationGroups[index].lastActionDate;
	}

	public String getMigrationGroupName(int index)
	{
		if (migrationGroups == null || index >= migrationGroups.length)
			return "";
		return migrationGroups[index].name;
	}

	public String getMigrationGroupPercentComplete(int index)
	{
		if (migrationGroups == null || index >= migrationGroups.length)
			return "";
		return migrationGroups[index].pctComplete;
	}

	public int getMigrationGroupMigratedCount(int index)
	{
		if (migrationGroups == null || index >= migrationGroups.length)
			return 0;
		return migrationGroups[index].registeredAgents;
	}

	public int getMigrationGroupUnmigratedCount(int index)
	{
		if (migrationGroups == null || index >= migrationGroups.length)
			return 0;
		return (migrationGroups[index].agentCount - migrationGroups[index].registeredAgents);
	}

	public int getTotalMigrationAgents()
	{
		return totalMigrationAgents;
	}

	public int getTotalMigrated()
	{
		return totalMigrated;
	}

	public int getTotalUnmigrated()
	{
		return (totalMigrationAgents - totalMigrated);
	}

	public int getTotalGroups()
	{
		return totalRolloutAgents + totalMigrationAgents;
	}

	public String escapeForHtml(String str)
	{
		return Escape.forHtml(str);
	}

	public String escapeForFunction(String str)
	{
		StringBuffer buf = new StringBuffer("");
		for (int i=0; i < str.length(); i++)
		{
			char c = str.charAt(i);
			
			if (c == '\'')
				buf.append("\\'");
			else if (c == '"')
				buf.append("\\\"");
			else if (c == '\\')
				buf.append("\\\\");
			else
				buf.append(c);
		}
		
		return buf.toString();
	}

}
