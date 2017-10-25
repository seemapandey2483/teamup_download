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
import connective.teamup.download.beans.ImportDisplayBean;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author Kyle McCreary
 *
 * Overrides the generic page bean to build the import control page for
 * batch (non-interactive) imports.
 */
public class BatchImportPage extends GenericPage
{
	private static final Logger LOGGER = Logger.getLogger(BatchImportPage.class);
	/**
	 * Constructor for BatchImportPage.
	 */
	public BatchImportPage()
	 {
		super();
	}

	/**
	 * @see connective.teamup.download.GenericPage#createDisplayBean(HttpServletRequest, HttpServletResponse, ServerInfo)
	 */
	protected DisplayBean createDisplayBean(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws PageException
	{
		// Create the display bean
		ImportDisplayBean bean = new ImportDisplayBean();
		
		try
		{
			bean.init(req, resp, serverInfo, op, items);
			
			// Set control to run in batch (non-interactive) mode
			bean.setInteractive(false);
		}
		catch (DisplayBeanException e)
		{
			LOGGER.error(e);
			throw new PageException(e);
		}
		
		return bean;
	}

}
