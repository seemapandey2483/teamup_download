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
import connective.teamup.download.beans.DownloadDisplayBean;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.DownloadStatus;

/**
 * @author Kyle McCreary
 *
 * Overrides the generic page bean to build the download control page for
 * downloading files with "Current" status only.
 */
public class DownloadCurrentFilesPage extends GenericPage
{
	private static final Logger LOGGER = Logger.getLogger(DownloadCurrentFilesPage.class);
	/**
	 * Constructor for DownloadCurrentFilesPage.
	 */
	public DownloadCurrentFilesPage()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.GenericPage#createDisplayBean(HttpServletRequest, HttpServletResponse, ServerInfo)
	 */
	protected DisplayBean createDisplayBean(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws PageException
	{
		// Create the display bean
		DownloadDisplayBean bean = new DownloadDisplayBean();
		
		try
		{
			bean.init(req, resp, serverInfo, op, items);
			
			// Set control to only request files with status of "current" (this will also retrieve
			// "direct bill current" files -- 07/24/2007, kwm)
			bean.setDownloadFileStatus(DownloadStatus.CURRENT);
		}
		catch (DisplayBeanException e)
		{
			LOGGER.error(e);
			throw new PageException(e);
		}
		
		return bean;
	}

}
