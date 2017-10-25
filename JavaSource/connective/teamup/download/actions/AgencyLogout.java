package connective.teamup.download.actions;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author haneym
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class AgencyLogout implements Action
{
	private static final Logger LOGGER = Logger.getLogger(AgencyLogout.class);
	/**
	 * Constructor for AgencyLogout.
	 */
	public AgencyLogout() {
		super();
	}

	/**
	 * @see connective.teamup.download.Action#perform(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException 
	{
		req.getSession().invalidate();
		
		String ret = "logon";
		String logoutUrl = serverInfo.getCarrierInfo().getAgentLogoutUrl();
		if (logoutUrl != null && !logoutUrl.equals(""))
		{
			ret = null;
			try
			{
				resp.sendRedirect(logoutUrl);
			}
			catch (IOException e)
			{
				LOGGER.error(e.getMessage());
				throw new ActionException("Error redirecting to agent logout URL", e);
			}
		}
		
		return ret;
	}

}
