package connective.teamup.download;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;

import connective.teamup.ws.ICommand;
import connective.teamup.ws.ICommandFactory;
import connective.teamup.ws.ISecurityHandler;
import connective.teamup.ws.SecurityStatus;
import connective.teamup.ws.TeamupMessage;
import connective.teamup.ws.server.CommandInfo;
import connective.teamup.ws.server.TeamupWSServer;

/**
 * @version 	1.0
 * @author
 */
public class WSServlet extends HttpServlet implements ICommandFactory, ISecurityHandler
{
	private static final Logger LOGGER = Logger.getLogger(WSServlet.class);
	
	protected TeamupWSServer ws = null;
	protected HashMap commands = null;
	protected XStream xs = null;
	protected SecurityProvider securityProvider = null;
	
	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	public void init(ServletConfig arg0) throws ServletException 
	{
		super.init(arg0);
		
		try
		{
			xs = new XStream();
			ws = new TeamupWSServer(this, xs, this);
			InputStream is = getClass().getResourceAsStream("/connective/teamup/download/wsconfig.xml");
			commands = ws.init(is);

			// load the security provider
			String provClass = System.getProperty("teamup.securityprovider");
			if (provClass != null && !provClass.equals(""))
				securityProvider = (SecurityProvider) Class.forName(provClass).newInstance();
			else
				securityProvider = new KeySecurityProvider();
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
			throw new ServletException(e);
		}
	}

	public ICommand getCommandImpl(String command) throws Exception
	{
		ICommand ret = null;
		CommandInfo ci = (CommandInfo) commands.get(command);
		if (ci != null)
		{
			ret = ci.getInstance();
			if (ret == null)
			{
				ret = (ICommand) Class.forName(ci.getImplClass()).newInstance();
				ci.setInstance(ret);
			}
		}
		
		return ret;
	}
	
	/* (non-Javadoc)
	 * @see connective.teamup.ws.ISecurityHandler#validate(java.lang.String)
	 */
	public SecurityStatus validate(String securityToken) 
	{
		UserAuthentication ua = securityProvider.authenticate(securityToken);
		SecurityStatus ret = new SecurityStatus(ua.getUserId(), ua.getPassword(), ua.getAgentId());
		if (ua.getStatus() != UserAuthentication.STATUS_GOOD)
		{
			ret.setError(true);
			switch (ua.getStatus())
			{
			case UserAuthentication.STATUS_FAILED_CREDENTIALS:
				ret.setErrorCode("SECURITY_FAILED_CREDENTIALS");
				break;
			case UserAuthentication.STATUS_FAILED_EXPIRED:
				ret.setErrorCode("SECURITY_FAILED_EXPIRED");
				ret.setErrorUrl(ua.getErrorUrl());
				break;
			case UserAuthentication.STATUS_FAILED_LOCKOUT:
				ret.setErrorCode("SECURITY_FAILED_LOCKOUT");
				ret.setErrorUrl(ua.getErrorUrl());
				break;
			case UserAuthentication.STATUS_FAILED_OTHER:
				ret.setErrorCode("SECURITY_FAILED_OTHER");
				break;
			}
		}
		
		// check for key change
		if (ua.isKeyChanged())
		{
			ret.setKeyChanged(true);
			ret.setNewKey(ua.getNewKey());
		}
		return ret;
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
		// read the input stream
		InputStreamReader is = new InputStreamReader(req.getInputStream(), "UTF8");
		String result = "";
		char[] buf = new char[32000];
		int read; 
		while ((read = is.read(buf, 0, 32000)) != -1)
			result += new String(buf, 0, read);
		
		// create and process the message
		TeamupMessage msgIn = (TeamupMessage) xs.fromXML(result);
		TeamupMessage msgOut = ws.processRequest(msgIn);
		String outXML = xs.toXML(msgOut);
		msgOut = null;
		resp.setContentType("text/xml; charset=\"utf-8\"");
		OutputStreamWriter out = new OutputStreamWriter(resp.getOutputStream(), "UTF8");
		out.write(outXML);
		out.flush();
		out.close();
	}

}
