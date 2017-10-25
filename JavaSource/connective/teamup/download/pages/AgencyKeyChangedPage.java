package connective.teamup.download.pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import connective.teamup.download.DisplayBean;
import connective.teamup.download.GenericPage;
import connective.teamup.download.PageException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.beans.AgencyInfoDisplayBean;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author Kyle McCreary
 *
 * Overrides the main page bean to display the agency info settings page 
 * showing a special message that the download URL has been updated.
 */
public class AgencyKeyChangedPage extends GenericPage
{

	/**
	 * Constructor for AgencyKeyChangedPage.
	 */
	public AgencyKeyChangedPage()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.GenericPage#createDisplayBean(HttpServletRequest, HttpServletResponse, ServerInfo)
	 */
	protected DisplayBean createDisplayBean(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws PageException
	{
		// Create the display bean
		AgencyInfoDisplayBean bean = (AgencyInfoDisplayBean) super.createDisplayBean(req, resp, serverInfo, op, items);
		bean.setKeyUpdated(true);
		
		return bean;
	}

}
