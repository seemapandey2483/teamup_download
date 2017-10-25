package connective.teamup.download;

import java.io.IOException;
import java.util.Hashtable;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import connective.teamup.download.beans.DatabaseConfigDisplayBean;
import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author haneym
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class DBConfigServlet extends HttpServlet
{
	private static final Logger LOGGER = Logger.getLogger(DBConfigServlet.class);
	
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
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
		try
		{
			String action = req.getParameter("action");
			if (action == null)
			{
				// show the database parameter page
				doPrepareDBParamPage(req, resp, false);
			}
			else if (action.equals("save"))
			{
				doProcessDBParams(req, resp);
			}
		}
		catch(Throwable theException)
		{
			LOGGER.error(theException.getMessage());
			// uncomment the following line when unexpected exceptions
			// are occuring to aid in debugging the problem.
			theException.printStackTrace();
		}
	}
	
	protected void doPrepareDBParamPage(HttpServletRequest req, HttpServletResponse resp, boolean invalid) throws Exception
	{
		DatabaseConfigDisplayBean bean = new DatabaseConfigDisplayBean(req, invalid);
		req.setAttribute("DisplayBean", bean);
		getServletContext().getRequestDispatcher("/carrier/dbconfig.jsp").forward(req, resp);
	}
	
	protected void doProcessDBParams(HttpServletRequest req, HttpServletResponse resp) throws Exception
	{
		// try to create the database connection
		boolean goodParams = true;
		try
		{
			DatabaseFactory dbfactory = DatabaseFactory.getInstance();

			DatabaseOperation op = null;
			try
			{
				op = dbfactory.startOperation();

				// update the database
				dbfactory.updateDatabase(req.getParameter("dbtype"), System.out, op);
				
				Hashtable props = new Hashtable();
				props.put(DatabaseFactory.PROP_DBTYPE, req.getParameter("dbtype"));
				op.setProperties(props);
			}
			catch (Exception e)
			{
				LOGGER.error(e.getMessage());
				goodParams = false;
			}
			finally
			{
				if (op != null)
					op.close();
			}	
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
			goodParams = false;
		}

		// if good, forward to the rest of the config, otherwise show the param page again		
		if (goodParams)
		{
			getServletContext().getRequestDispatcher("/pconfig").forward(req, resp);
		}
		else
		{
			doPrepareDBParamPage(req, resp, true);
		}
	}
}
