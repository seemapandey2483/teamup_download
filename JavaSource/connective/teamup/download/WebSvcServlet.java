package connective.teamup.download;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import connective.teamup.download.agentimport.ImportDefinition;
import connective.teamup.download.agentimport.ImportEngine;
import connective.teamup.download.agentimport.ImportResult;
import connective.teamup.download.db.AgentInfo;
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
public class WebSvcServlet extends HttpServlet
{
	private static final Logger LOGGER = Logger.getLogger(WebSvcServlet.class);
	
	protected KeySecurityProvider secProvider = null;
	
	protected String securityKey = "AGasdgfEF12dDSA56d3";
	
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);

		try
		{
			secProvider = new KeySecurityProvider();
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
		try
		{
			String action = req.getParameter("action");
			if (action != null)
			{
				if (action.equals("securitykey"))
				{
					getSecurityKey(req, resp);
				}
				else if (action.equals("agentimport"))
				{
					System.out.println("WebSvcServlet - doPost, action=agentimport");
					importAgents(req, resp);
				}
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
	
	protected void getSecurityKey(HttpServletRequest req, HttpServletResponse resp) throws Exception
	{
		DatabaseOperation op = null;
		try
		{
			PrintWriter out = resp.getWriter();

			// get the request parameters
			String secToken = req.getParameter("reqid");
			String agent = req.getParameter("agentid");
			if (agent == null)
				agent = "";
			String password = req.getParameter("agentpw");		
			
			if (secToken != null && secToken.equals(securityKey))
			{
				// lookup the agent
				op = DatabaseFactory.getInstance().startOperation();
				AgentInfo agentInfo = op.getAgentInfo(agent);
				if (agentInfo != null)
				{
					// check the password
					if (agentInfo.getPassword().equals(password))
					{
						// print out the registered flag
						String reg = "N";
						if (agentInfo.isRegistered())
							reg = "Y";
						out.print(reg);
						
						// print out the live flag
						String live = "N";
						if (agentInfo.isLive())
							live = "Y";
						out.print(live);
						
						// print out the disabled flag
						String disabled = "Y";
						if (agentInfo.isActive())
							disabled = "N";
						out.print(disabled);
						
						// get the key
						String key = secProvider.getSecurityKey(agent, agent, password);
						out.print("key=" + key);					
					}
					else
					{
						out.print("AGENT_LOGIN_FAILED");
					}
				}
				else
				{
					out.print("AGENT_ID_NOT_FOUND");
				}
			}
			else
			{
				out.print("SECURITY_TOKEN_ERROR");
			}
	
			// finish up
			out.flush();
		}
		finally
		{
			if (op != null)
				op.close();
		}
	}
	
	protected void importAgents(HttpServletRequest req, HttpServletResponse resp) throws Exception
	{
		System.out.println("WebSvcServlet - entering importAgents");
		DatabaseOperation op = null;
		try
		{
			PrintWriter out = resp.getWriter();
	
			String secToken = req.getParameter("reqid");
			if (secToken != null && secToken.equals(securityKey))
			{
				System.out.println("WebSvcServlet:importAgents - security token passed");
				// create the import definition
				String defstr = req.getParameter("importdef");
				System.out.println("WebSvcServlet:importAgents - import definition:");
				System.out.print(defstr);
				System.out.print("\n");
				ByteArrayInputStream bis = new ByteArrayInputStream(defstr.getBytes());
				ImportDefinition def = new ImportDefinition();
				def.loadFromXML(bis);
	
				// import the data
				String data = req.getParameter("importdata");
				System.out.println("WebSvcServlet:importAgents - import data:");
				System.out.print(data);
				System.out.print("\n");
				bis = new ByteArrayInputStream(data.getBytes());
				ImportEngine ie = new ImportEngine(def);
				op = DatabaseFactory.getInstance().startOperation();
				ImportResult[] results = ie.doImport(op, bis);				
				for (int i=0; i < results.length; i++)
				{
					System.out.println(results[i].getMessage());
					out.println(results[i].getMessage());
				}							
			}
			else
			{
				out.println("Bad security token - import aborted.");
			}
			
			out.flush();
		}
		finally
		{
			if (op != null)
				op.close();
		}
		LOGGER.info("WebSvcServlet - exiting importAgents");
		System.out.println("WebSvcServlet - exiting importAgents");
	}
}
