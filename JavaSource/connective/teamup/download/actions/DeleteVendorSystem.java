package connective.teamup.download.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.AmsInfo;
import connective.teamup.download.db.DatabaseOperation;

/**
 * Action bean to delete an agency vendor system from the database.
 * 
 * @author Kyle McCreary
 */
public class DeleteVendorSystem extends SortVendorSystems
{
	private static final Logger LOGGER = Logger.getLogger(DeleteVendorSystem.class);
	/**
	 * @see connective.teamup.download.Action#perform(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException
	{
		String nextPage = "menu.ams";
		
		try
		{
			String amsId = req.getParameter("amsid");
			if (amsId != null && !amsId.trim().equals(""))
			{
				AmsInfo ams = op.getAmsInfo(amsId);
				if (ams != null && ams.isCustomSystem())
				{
					// Verify that no agents are currently configured for this vendor system
					AgentInfo[] agents = op.getAgentsForAms(amsId);
					if (agents == null || agents.length == 0)
						ams.delete();
					else
					{
						nextPage = "splash";
						String msg = "Delete invalid:  There ";
						if (agents.length == 1)
							msg = "is currently 1 agent";
						else
							msg = "are currently " + String.valueOf(agents.length) + " agents";
						msg += " configured with this vendor system.";
						serverInfo.setStatusMessage(req.getSession(), msg);
					}
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error saving vendor system info", e);
			throw new ActionException("Error saving vendor system info", e);
		}
		
		return nextPage;
	}

}
