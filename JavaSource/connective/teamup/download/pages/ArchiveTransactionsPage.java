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
import connective.teamup.download.beans.ArchiveCompanyDisplayBean;
import connective.teamup.download.beans.ArchiveDisplayBean;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author Kyle McCreary
 *
 * Overrides the generic page bean to view the current and archived download
 * files (showing transaction information) for an existing trading partner.
 * Parses the requested trading partner's agent ID from the request.
 */
public class ArchiveTransactionsPage extends GenericPage
{
	private static final Logger LOGGER = Logger.getLogger(ArchiveTransactionsPage.class);
	/**
	 * Constructor for ArchiveTransactionsPage.
	 */
	public ArchiveTransactionsPage()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.GenericPage#createDisplayBean(HttpServletRequest, HttpServletResponse, ServerInfo)
	 */
	protected DisplayBean createDisplayBean(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws PageException
	{
		String pageVersion = req.getParameter("pagever");
		if (pageVersion != null && pageVersion.equalsIgnoreCase("C"))
		{
			// Create the display bean for the company admin version of the archive page
			ArchiveCompanyDisplayBean bean = new ArchiveCompanyDisplayBean();
			
			try
			{
				bean.showTransactions(true);
				bean.init(req, resp, serverInfo, op, items);
			}
			catch (DisplayBeanException e)
			{
				LOGGER.error(e);
				throw new PageException(e);
			}
			
			return bean;
		}
		
		
		// Create the display bean
		ArchiveDisplayBean bean = new ArchiveDisplayBean();
		
		try
		{
			bean.showTransactions(true);
			bean.init(req, resp, serverInfo, op, items);
		}
		catch (DisplayBeanException e)
		{
			LOGGER.error(e);
			throw new PageException(e);
		}
		
		return bean;
	}

}
