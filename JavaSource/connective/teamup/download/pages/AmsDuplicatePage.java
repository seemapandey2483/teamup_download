package connective.teamup.download.pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import connective.teamup.download.DisplayBean;
import connective.teamup.download.GenericPage;
import connective.teamup.download.PageException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.beans.AmsInfoDisplayBean;
import connective.teamup.download.db.AmsInfo;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author Kyle McCreary
 *
 * Overrides the default page bean to re-edit an existing agency vendor system.
 * Parses the system ID from the request, and sets the 'duplicate system' flag.
 */
public class AmsDuplicatePage extends GenericPage
{

	/**
	 * Constructor for AmsDuplicatePage.
	 */
	public AmsDuplicatePage()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.GenericPage#createDisplayBean(HttpServletRequest, HttpServletResponse, ServerInfo)
	 */
	protected DisplayBean createDisplayBean(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws PageException
	{
		// Create the display bean
		AmsInfoDisplayBean bean = (AmsInfoDisplayBean) super.createDisplayBean(req, resp, serverInfo, op, items);
		bean.setDuplicateSystem(true);
		
		// Pull the vendor system info from the session, if available
		AmsInfo ams = (AmsInfo) req.getSession().getAttribute("ams.info");
		if (ams != null)
		{
			// Load the ams info in the display bean
			bean.setAmsInfo(ams);
			
			// Remove the ams bean from the session
			req.getSession().removeAttribute("ams.info");
		}
		
		String newSystem = req.getParameter("newsystem");
		if (newSystem != null && newSystem.equals("Y"))
			bean.setNewAms(true);
		
		return bean;
	}

}
