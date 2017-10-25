package connective.teamup.download;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.log4j.Logger;

import connective.teamup.download.db.DatabaseOperation;

/**
 * @author haneym
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class ConfigServlet extends HttpServlet
{
	private static final Logger LOGGER = Logger.getLogger(ConfigServlet.class);
	
	protected ServerInfo serverInfo = null;
	
	protected String defaultPage = null;
	protected String appName = null;
					
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);

		try
		{
			// create the server info object
			serverInfo = new ServerInfo(getServletContext(), appName);
			
			// get the xml file
			String filename = config.getInitParameter("config_file");
			if (filename != null)
			{
				InputStream instr = getClass().getResourceAsStream(filename);
				
				// load the pages and actions from the xml file
				ConfigFileHandler handler = new ConfigFileHandler(serverInfo.getPages(), 
					serverInfo.getActions(), serverInfo.getMenubar());
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser parser = factory.newSAXParser();
				parser.parse(instr, handler);
			}
			
			// get the default page
			defaultPage = config.getInitParameter("default_page");
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
			throw new ServletException(e);
		}
	}
		
	/**
	* @see javax.servlet.http.HttpServlet#void (javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	*/
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		doPost(req, resp);
	}
	
	/**
	* @see javax.servlet.http.HttpServlet#void (javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	*/
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		DatabaseOperation op = null;
		try
		{
			// clear the status
			serverInfo.setStatusMessage(req.getSession(), "");
			
			String actionParam = null;
			FileItem[] fileItems = null;
			if (!FileUpload.isMultipartContent(req))
			{
				actionParam = req.getParameter("action");
			}
			else
			{
				DiskFileUpload upload = new DiskFileUpload();
				ArrayList /* FileItem */ items = new ArrayList(upload.parseRequest(req));
				Iterator it = items.iterator();
				while (it.hasNext())
				{
					FileItem item = (FileItem) it.next();
					if (item.isFormField() && item.getFieldName() != null && item.getFieldName().equals("action"))
					{
						actionParam = item.getString();
						break;
					}
				}
				
				if (items != null && items.size() > 0)
				{
					fileItems = new FileItem[items.size()];
					items.toArray(fileItems);
				}
			} 
			
			if (actionParam != null && !actionParam.equals(""))
			{
				op = serverInfo.getDbfactory().startOperation();

				ActionDef action = (ActionDef) serverInfo.getActions().get(actionParam);
				if (action != null)
				{
					if (action.getAlias() != null)
					{
						// action aliases to a page
						serverInfo.showPage(action.getAlias(), req, resp, op, fileItems);
					}
					else
					{
						// use an action class
						String nextPage = action.getActionInstance().perform(req, resp, serverInfo, op, fileItems);
						if (nextPage != null)
							serverInfo.showPage(nextPage, req, resp, op, fileItems);	
					}
				}
				else
				{
					// show the default page
					serverInfo.showPage(defaultPage, req, resp, op, fileItems);
				}
			}
			else
			{
				// recreate the server info
				serverInfo = new ServerInfo(getServletContext(), appName);
	
				op = serverInfo.getDbfactory().startOperation();
			
				// show the default page
				serverInfo.showPage(defaultPage, req, resp, op, fileItems);
			}
		}
		catch(Throwable theException)
		{
			LOGGER.error(theException.getMessage());
			// uncomment the following line when unexpected exceptions
			// are occuring to aid in debugging the problem.
			theException.printStackTrace();
		}
		finally
		{
			if (op != null)
				op.close();
		}
	}
}
