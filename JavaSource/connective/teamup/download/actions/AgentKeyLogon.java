package connective.teamup.download.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.SecurityProvider;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.UserAuthentication;
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
public class AgentKeyLogon implements Action
{
	private static final Logger LOGGER = Logger.getLogger(AgentKeyLogon.class);
	/**
	 * Constructor for AgentLogon.
	 */
	public AgentKeyLogon()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.Action#perform(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException 
	{
		String nextPage = null;
		
		try
		{
			SecurityProvider sp = serverInfo.getSecurityProvider();
			
			// authenticate the user
			UserAuthentication ua = sp.authenticate(req.getParameter("key"));
			if (ua.getStatus() == UserAuthentication.STATUS_GOOD)
			{
				AgentInfo info = op.getAgentInfo(ua.getAgentId());
				info.updateLastLoginDate();
				serverInfo.setAgentInfo(req.getSession(), info);
				if ("logonkey".equals(req.getParameter("action")))
				{
					info.updateLastLoginDate();
					
					if (info.isRegistered())
					{
						// Navigate to the agency admin menu page to display current settings
						nextPage = "splash";
					}
					else
					{
						// Navigate to the new agency Welcome page
						nextPage = "welcome";
					}
				}
			}
			else
			{
				nextPage = "accessdenied";
			}
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
			nextPage = "accessdenied";
			//throw new ActionException(e.getMessage());
		}

		return nextPage;
	}

}
