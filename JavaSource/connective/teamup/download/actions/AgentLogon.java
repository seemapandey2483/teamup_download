package connective.teamup.download.actions;

import java.sql.SQLException;

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
 * @author haneym
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class AgentLogon implements Action
{
	private static final Logger LOGGER = Logger.getLogger(AgentLogon.class);
	/**
	 * Constructor for AgentLogon.
	 */
	public AgentLogon()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.Action#perform(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException 
	{
		String nextPage = "";
		
		try
		{		
			// authenticate the user
			String agent = req.getParameter("userid");
			String password = req.getParameter("password");
			
			AgentInfo info = op.getAgentInfo(agent);
			if (info != null && info.getAgentId().equals(agent) && info.getPassword().equals(password))
			{
				info.loadParticipantsFromDb();
				serverInfo.setAgentInfo(req.getSession(), info);
				if (!info.isActive())
				{
					// Agent ID is not in the database or is flagged as disabled
					nextPage = "logon.error";
				}
				else if (info.isRegistered())
				{
					info.updateLastLoginDate();
					
					// Navigate to the agency admin menu page to display current settings
					nextPage = "splash";
				}
				else
				{
					info.updateLastLoginDate();
					
					// Navigate to the new agency Welcome page
					nextPage = "welcome";
				}
			}
			else
			{
				nextPage = "logon.error";
			}
		}
		catch (SQLException e)
		{
			LOGGER.error("Error occurred during agent login process", e);
			throw new ActionException("Error occurred during agent login process", e);
		}

		return nextPage;
	}

}
