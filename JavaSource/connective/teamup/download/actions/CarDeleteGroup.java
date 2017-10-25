package connective.teamup.download.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentGroupInfo;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author Kyle McCreary
 */
public class CarDeleteGroup implements Action
{
	private static final Logger LOGGER = Logger.getLogger(CarDeleteGroup.class);
	/* (non-Javadoc)
	 * @see connective.teamup.download.Action#perform(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, connective.teamup.download.ServerInfo, connective.teamup.download.db.DatabaseOperation, org.apache.commons.fileupload.FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException
	{
		String nextPage = "car.group.list";
		String groupName = "";
		try
		{
			groupName = req.getParameter("groupName");
			if (groupName != null && !groupName.trim().equals(""))
			{
				AgentGroupInfo group = op.getAgentGroup(groupName);
				if (group != null)
					group.delete();
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error deleting agent rollout group '" + groupName + "'", e);
			throw new ActionException("Error deleting agent rollout group '" + groupName + "'", e);
		}
		
		return nextPage;
	}

}
