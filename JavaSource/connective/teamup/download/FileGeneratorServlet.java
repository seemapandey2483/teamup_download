package connective.teamup.download;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @version 	1.0
 * @author
 */
public class FileGeneratorServlet extends HttpServlet 
{
	private static final Logger LOGGER = Logger.getLogger(FileGeneratorServlet.class);
	
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
	}

	/**
	* @see javax.servlet.http.HttpServlet#void (javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	*/
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		// lookup the agent
		String agentId = req.getParameter("agent");
		AgentInfo agent = null;
		DatabaseOperation op = null;
		try 
		{
			op = DatabaseFactory.getInstance().startOperation();
			agent = op.getAgentInfo(agentId);
		}
		catch (Exception e) 
		{
			LOGGER.error(e.getMessage());
			System.out.println(e.getMessage());
		}
		finally
		{
			if (op != null)
				op.close();
		}
		
		if (agent == null)
			return;
		
		resp.setContentType("text/plain");	
		PrintWriter out = resp.getWriter();

		String file = agent.getDefaultFilename();
		int sep = file.lastIndexOf('.');
		if (sep != -1)
			file = file.substring(0, sep);
		String dir = agent.getRemoteDir();
		
		if (agent.getAms() != null && agent.getAms().getId() != null && agent.getAms().getId().equals("EBIX"))
		{
			// Use specialized batch file for Redshaw EBIX
			out.println("@echo off");
			out.println("cls");
			out.println("echo *********************************************************************");
			out.println("echo **                                                                 **");
			out.println("echo **                         TEAM-UP Download                        **");
			out.println("echo **                  EBIX - Redshaw Download Utility                **");
			out.println("echo **                                                                 **");
			out.println("echo **                                                                 **");
			out.println("echo **         This file should be run AFTER you have downloaded       **");
			out.println("echo **               files from your carrier with TEAM-UP.             **");
			out.println("echo **                                                                 **");
			out.println("echo **     This process will MOVE files from your C:\\NM\\IN\\ folder     **");
			out.println("echo **            to Redshaw, then DELETE them from your PC.           **");
			out.println("echo **                                                                 **");
			out.println("echo **                                                                 **");
			out.println("echo **                    press CTRL-C to cancel                       **");
			out.println("echo **                              or                                 **");
			out.println("echo **                   press any key to continue                     **");
			out.println("echo **                                                                 **");
			out.println("echo *********************************************************************");
			out.println("echo.");
			out.println("pause");
			out.println("C:\\NMI\\PC-UNIX.EXE");
			out.println("echo All files have been MOVED to Redshaw");
			out.println("del " + dir + "IN??.*");
			out.println("echo All download files have been DELETED from this PC...");
			out.println("echo.");
			out.println("echo.");
			out.println("echo Thank you.");
			out.println("echo.");
			out.println("pause");
		}
		else
		{
			// Use standard batch file
			out.println("@echo off");
			out.println("cls");
			out.println("echo *********************************************************************");
			out.println("echo **                                                                 **");
			out.println("echo **   YOU ARE ABOUT TO DELETE ALL OF YOUR " + file + " DOWNLOAD FILES IN:   **");
			out.println("echo **                                                                 **");
			out.println("echo **                   " + dir + file + ".*                      **");
			out.println("echo **                                                                 **");
			out.println("echo **                                                                 **");
			out.println("echo **                    press CTRL-C to cancel                       **");
			out.println("echo **                              or                                 **");
			out.println("echo **                   press any key to continue                     **");
			out.println("echo **                                                                 **");
			out.println("echo *********************************************************************");
			out.println("echo.");
			out.println("pause");
			out.println("del " + dir + file + ".*");
			out.println("echo ALL DOWNLOAD FILES HAVE BEEN DELETED...");
			out.println("echo.");
			out.println("echo Please use your TEAM-UP Download ICON on your desktop");
			out.println("echo to retrieve your new download files.");
			out.println("echo.");
			out.println("echo Thank you.");
			out.println("echo.");
			out.println("pause");
		}
		out.flush();
	}

	/**
	* @see javax.servlet.http.HttpServlet#void (javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	*/
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		doGet(req, resp);
	}

}
