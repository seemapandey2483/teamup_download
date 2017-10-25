/*
 * 03/14/2005 - Created
 */
package connective.teamup.download.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentGroupInfo;
import connective.teamup.download.db.BatchInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.DistributedFileMappingInfo;
import connective.teamup.download.db.DownloadStatus;

/**
 * @author Kyle McCreary
 *
 * Action bean to save the selected agents as distributed file recipients.
 */
public class SelectAgentsForFileDistribution implements Action
{
	private static final Logger LOGGER = Logger.getLogger(SelectAgentsForFileDistribution.class);
	/* (non-Javadoc)
	 * @see connective.teamup.download.Action#perform(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, connective.teamup.download.ServerInfo, connective.teamup.download.db.DatabaseOperation, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException
	{
		String nextPage = "distribute.edits.select.files";
		
		try
		{
			// Parse the batch info from the request
			String distBatchNumber = req.getParameter("distBatchNumber");
			int batchNumber = Integer.parseInt(distBatchNumber);
			BatchInfo batchInfo = op.getBatchInfo(batchNumber);
			
			// Determine whether to save the selections as a new agent group
			AgentGroupInfo groupInfo = null;
			String saveNew = req.getParameter("save_query_as");
			if (saveNew != null && saveNew.equals("Y"))
			{
				String groupName = req.getParameter("save_query_name");
				if (groupName != null && !groupName.trim().equals(""))
				{
					groupInfo = op.createAgentGroup(groupName, AgentGroupInfo.TYPE_AGENTS_MISC);
					groupInfo.setDescription(groupName);
					groupInfo.setLastAction("Group creation, file distribution");
					groupInfo.setLastActionDate(System.currentTimeMillis());
					groupInfo.save();
				}
			}
			
			// Build the list of agent recipients
			String[] agents = req.getParameterValues("include");
			if (agents != null && agents.length > 0)
			{
				for (int i=0; i < agents.length; i++)
				{
					if (agents[i] != null && !agents[i].equals(""))
					{
						DistributedFileMappingInfo mapping = batchInfo.addAgentMapping(agents[i]);
						mapping.setDownloadStatus(DownloadStatus.HOLD);
						mapping.save();
						
						if (groupInfo != null)
						{
							// Add this agent as a member of the new group
							groupInfo.addMember(agents[i]);
						}
					}
				}
			}
			
			// Determine the next page
			String action = req.getParameter("action");
			if (action != null && action.indexOf("email") > 0)
				nextPage = "distribute.email.text";
		}
		catch (Exception e)
		{
			LOGGER.error("Error saving recipients for file distribution", e);
			throw new ActionException("Error saving recipients for file distribution", e);
		}
		
		return nextPage;
	}

}
