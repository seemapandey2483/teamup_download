package connective.teamup.download.pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import connective.teamup.download.DisplayBean;
import connective.teamup.download.PageException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.beans.AgencyInfoDisplayBean;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author Kyle McCreary
 *
 * Overrides the main Trading Partner Edit page bean to re-edit an existing 
 * trading partner.  Parses the requested trading partner's agent ID from the 
 * request, and sets the 'filename invalid' flag to show as a duplicate filename.
 */
public class TradingPartnerInvalidPage extends TradingPartnerEditPage
{

	/**
	 * Constructor for TradingPartnerInvalidPage.
	 */
	public TradingPartnerInvalidPage()
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
		bean.setFilenameInvalid(true);
		
		return bean;
	}

}
