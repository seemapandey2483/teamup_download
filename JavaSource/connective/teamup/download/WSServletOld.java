package connective.teamup.download;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;

import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.ws.TeamupMessage;

/**
 * @version 	1.0
 * @author
 */
public class WSServletOld extends HttpServlet
{
	private static final Logger LOGGER = Logger.getLogger(WSServletOld.class);
	
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
		TeamupMessage msgOut = msgIn;
		msgOut.setError("DEPRECATED");
		DatabaseOperation op = null;
		try
		{
			// lookup the password in the database - NOTE, this works because for this old
			// version of security, database agentid/password was the only authentication type
			op = DatabaseFactory.getInstance().startOperation();
			AgentInfo agent = op.getAgentInfo(msgIn.getClientId());
			msgOut.setData(securityProvider.getSecurityKey(msgIn.getClientId(), msgIn.getClientId(), agent.getPassword()));	
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
			msgOut.setData("");
		}
		finally
		{
			if (op != null)
				op.close();
		}
				
		String outXML = xs.toXML(msgOut);
		resp.setContentType("text/xml; charset=\"utf-8\"");
		OutputStreamWriter out = new OutputStreamWriter(resp.getOutputStream(), "UTF8");
		out.write(outXML);
		out.flush();
		out.close();
	}

}
