/*
 * Created on Nov 15, 2005
 */
package connective.teamup.download.actions;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentGroupInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author Kyle McCreary
 */
public class CammCreateGroup implements Action
{
	private static final Logger LOGGER = Logger.getLogger(CammCreateGroup.class);
	/* (non-Javadoc)
	 * @see connective.teamup.download.Action#perform(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, connective.teamup.download.ServerInfo, connective.teamup.download.db.DatabaseOperation, org.apache.commons.fileupload.FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException
	{
		String nextPage = "camm.group.detail";
		
		try
		{
			String groupName = req.getParameter("groupName");
			String firstGroup = groupName;
			String groupDesc = req.getParameter("groupDesc");
			String flag = req.getParameter("multiGroups");
			boolean createMultiGroups = (flag != null && flag.equals("Y"));
			int maxAgents = 0;
			if (createMultiGroups)
			{
				try
				{
					maxAgents = Integer.parseInt(req.getParameter("maxAgts"));
				} catch (Exception e) {
					LOGGER.error(e);
				}
			}
			if (maxAgents <= 0)
				createMultiGroups = false;
			boolean addToExisting = false;
			
			if (groupName == null || groupName.trim().equals(""))
				return "camm.group.name";
			
			AgentGroupInfo group = op.getAgentGroup(groupName);
			if (group != null && !addToExisting)
				nextPage = "camm.group.name";
			else
			{
				int groupCount = 1;
				int agtCount = 0;
				
				// Parse and save the list of selected agent recipients
				ArrayList agtList = new ArrayList();
				String[] agentId = req.getParameterValues("agent");
				for (int i=0; i < agentId.length; i++)
				{
					if (agentId[i] != null && !agentId[i].equals(""))
					{
						AgentInfo agent = op.getAgentInfo(agentId[i]);
						if (agent != null)
							agtList.add(agent);
					}
				}
				
				if (group == null)
				{
					// Create the new agent migration group
					String tempName = groupName;
					if (createMultiGroups && maxAgents > 0 && agtList.size() > maxAgents)
						tempName += " #1";
					firstGroup = tempName;
					group = op.createAgentGroup(tempName, AgentGroupInfo.TYPE_MIGRATION);
					group.setDescription(groupDesc);
					group.setLastAction("Created");
					group.setLastActionDate(System.currentTimeMillis());
					group.save();
				}
				
				// Add the newly created agents to the rollout group
				for (int i=0; i < agtList.size(); i++)
				{
					if (createMultiGroups && agtCount >= maxAgents)
					{
						// Create a new group
						groupCount++;
						String tempName = groupName + " #" + String.valueOf(groupCount);
						group = op.createAgentGroup(tempName, AgentGroupInfo.TYPE_MIGRATION);
						group.setDescription(groupDesc);
						group.setLastAction("Created");
						group.setLastActionDate(System.currentTimeMillis());
						group.save();
						agtCount = 0;
					}
					
					agtCount++;
					AgentInfo agent = (AgentInfo) agtList.get(i);
					group.addMember(agent.getAgentId());
				}
				req.getSession().removeAttribute(ServerInfo.STORE_AGENT_LIST);
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error creating new agent rollout group", e);
			throw new ActionException("Error creating new agent rollout group", e);
		}
		
		return nextPage;
	}

}
