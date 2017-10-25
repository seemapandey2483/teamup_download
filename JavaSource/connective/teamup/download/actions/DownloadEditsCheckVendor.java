/*
 * 03/20/2005 - Created
 */
package connective.teamup.download.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author Kyle McCreary
 *
 * Action bean used to only allow Applied users to navigate to the Applied edits
 * download page.
 */
public class DownloadEditsCheckVendor implements Action
{
	private static final Logger LOGGER = Logger.getLogger(DownloadEditsCheckVendor.class);
	/* (non-Javadoc)
	 * @see connective.teamup.download.Action#perform(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, connective.teamup.download.ServerInfo, connective.teamup.download.db.DatabaseOperation, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException
	{
		String nextPage = "menu.download.edits";
		
		try
		{
			AgentInfo agent = serverInfo.getAgentInfo(req.getSession(), op);
			if (agent.getAms() == null || agent.getAms().getId() == null ||
				!agent.getAms().getId().equals("APPLIED"))
			{
				serverInfo.setStatusMessage(req.getSession(), "This function is only valid for Applied users");
				nextPage = "splash";
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error loading agent info", e);
			throw new ActionException("Error loading agent info", e);
		}
		
		return nextPage;
	}

}
