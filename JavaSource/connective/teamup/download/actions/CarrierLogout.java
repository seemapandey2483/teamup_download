package connective.teamup.download.actions;

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
public class CarrierLogout implements Action
{
	private static final Logger LOGGER = Logger.getLogger(CarrierLogout.class);
	/**
	 * Constructor for AgencyLogout.
	 */
	public CarrierLogout() {
		super();
	}

	/**
	 * @see connective.teamup.download.Action#perform(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException 
	{
		req.getSession().invalidate();
		try
		{
			//serverInfo.getContext().getRequestDispatcher("/carrier/loggedout.jsp").forward(req, resp);
			resp.sendRedirect(req.getContextPath() + "/company");
		}
		catch (Exception e)
		{
			LOGGER.error("Error forwarding to carrier logout page", e);
			throw new ActionException("Error forwarding to carrier logout page", e);
		}
		return null;
	}

}
