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
 * downloading files with "Download" status only.
 */
public class DownloadArchivedFilesPage extends GenericPage
{
	private static final Logger LOGGER = Logger.getLogger(DownloadArchivedFilesPage.class);
	/**
	 * Constructor for DownloadArchivedFilesPage.
	 */
	public DownloadArchivedFilesPage()
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
			
			// Set control to only request files with status of "download"
			bean.setDownloadFileStatus(DownloadStatus.POLICY_DOWNLOAD);
		}
		catch (DisplayBeanException e)
		{
			LOGGER.error(e);
			throw new PageException(e);
		}
		
		return bean;
	}

}
