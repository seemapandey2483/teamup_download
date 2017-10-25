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
import connective.teamup.download.beans.LogInfoDisplayBean;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author Kyle McCreary
 *
 * Overrides the generic page bean to build the Agency Transaction Log with
 * parameters to only show agent download transactions.
 */
public class TransLogByAgentPage extends GenericPage
{
	private static final Logger LOGGER = Logger.getLogger(TransLogByAgentPage.class);
	/**
	 * Constructor for TransLogByAgentPage.
	 */
	public TransLogByAgentPage()
	{
		super();
	}
	
	/**
	 * @see connective.teamup.download.GenericPage#createDisplayBean(HttpServletRequest, HttpServletResponse, ServerInfo)
	 */
	protected DisplayBean createDisplayBean(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws PageException
	{
		// Create the display bean
		LogInfoDisplayBean bean = new LogInfoDisplayBean();
		
		// Set the bean to only return download events
		bean.setEventType("D");
		
		try
		{
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
