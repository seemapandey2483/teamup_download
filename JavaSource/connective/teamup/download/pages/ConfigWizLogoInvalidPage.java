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
import connective.teamup.download.beans.CompanyConfigDisplayBean;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author Kyle McCreary
 *
 * Sets the "graphic file invalid" flag on the Download Configuration display bean.
 */
public class ConfigWizLogoInvalidPage extends GenericPage
{
	private static final Logger LOGGER = Logger.getLogger(ConfigWizLogoInvalidPage.class);
	/**
	 * Constructor for ConfigWizLogoInvalidPage.
	 */
	public ConfigWizLogoInvalidPage()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.GenericPage#createDisplayBean(HttpServletRequest, HttpServletResponse, ServerInfo)
	 */
	protected DisplayBean createDisplayBean(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws PageException
	{
		// Create the display bean
		CompanyConfigDisplayBean bean = new CompanyConfigDisplayBean();
		
		try
		{
			bean.init(req, resp, serverInfo, op, items);
			bean.setConfigWizard(true);
			bean.setInvalidGraphicFile(true);
		}
		catch (DisplayBeanException e)
		{
			LOGGER.error(e);
			throw new PageException(e);
		}
		
		return bean;
	}

}
