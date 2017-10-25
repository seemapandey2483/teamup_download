package connective.teamup.download.pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.DisplayBean;
import connective.teamup.download.DisplayBeanException;
import connective.teamup.download.GenericPage;
import connective.teamup.download.PageException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.beans.AgencyInfoDisplayBean;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author Kyle McCreary
 *
 * Overrides the generic page bean to create/edit a new trading partner.
 */
public class TradingPartnerNewPage extends GenericPage
{
	private static final Logger LOGGER = Logger.getLogger(TradingPartnerNewPage.class);
	/**
	 * Constructor for TradingPartnerNewPage.
	 */
	public TradingPartnerNewPage()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.GenericPage#createDisplayBean(HttpServletRequest, HttpServletResponse, ServerInfo)
	 */
	protected DisplayBean createDisplayBean(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws PageException
	{
		// Clear any existing agency info out of the session
		serverInfo.setAgentInfo(req.getSession(), null);
		
		// Create the display bean
		AgencyInfoDisplayBean bean = (AgencyInfoDisplayBean) super.createDisplayBean(req, resp, serverInfo, op, items);
		bean.setNewAgent(true);
		
		try
		{
			// Load the table of agency vendor systems
			bean.loadAmsTable(op);
		}
		catch (DisplayBeanException e)
		{
			LOGGER.error(e);
			throw new PageException(e);
		}

		return bean;
	}

}
