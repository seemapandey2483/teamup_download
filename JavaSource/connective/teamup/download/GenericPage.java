package connective.teamup.download;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.beans.MenuBean;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author haneym
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class GenericPage implements Page 
{
	private static final Logger LOGGER = Logger.getLogger(GenericPage.class);
	
	protected PageDef def = null;
	protected ServletContext context = null;
	
	/**
	 * Constructor for GenericPage.
	 */
	public GenericPage() 
	{
		super();
	}

	/**
	 * @see connective.teamup.download.Page#init(PageDef)
	 */
	public void init(PageDef def, ServletContext context) 
	{
		this.def = def;
		this.context = context;
	}

	/**
	 * @see connective.teamup.download.Page#forward(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public void forward(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws PageException
	{
		try
		{
			// create the display bean
			DisplayBean bean = createDisplayBean(req, resp, serverInfo, op, items);
			if (bean != null)
				req.setAttribute("DisplayBean", bean);
				
			// create the menu bean
			MenuBean menubean = new MenuBean(serverInfo.getMenubar(), def.getMenuItems(), serverInfo.getCarrierInfo());
			req.setAttribute("MenuBean", menubean);
			
			resp.setHeader("Pragma", "no-cache");
			resp.setHeader("Cache-Control", "no-cache");
			
			context.getRequestDispatcher(def.getJsp()).forward(req, resp);
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
			throw new PageException("Error creating generic page", e);
		}
	}
	
	/**
	 * creates and returns a display bean - subclasses typically only need to override this method for
	 * special display bean creation
	 */
	protected DisplayBean createDisplayBean(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws PageException
	{
		DisplayBean bean = null;
		
		try
		{
			if (def.getDisplayBean() != null)
			{
				// create the display bean
				bean = (DisplayBean) Class.forName(def.getDisplayBean()).newInstance();
				bean.init(req, resp, serverInfo, op, items);
			}
		}
		catch (DisplayBeanException e)
		{
			LOGGER.error(e.getMessage());
			throw new PageException(e);
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
			throw new PageException("Error creating the display bean", e);
		}
		return bean;
	}
}
