package connective.teamup.download;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
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

import com.ebix.licence.Licence;
import com.ebix.quartz.job.RegisterSchedJobs;

import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.services.EmailService;
import connective.teamup.registration.ws.objects.CarrierIdRequest;
import connective.teamup.registration.ws.objects.GetCarrierActCodeOutput;
import connective.teamup.ws.client.TeamupWSClient;

/**
 * @author haneym
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class ControllerServlet extends HttpServlet
{
	public static Logger log =Logger.getLogger(ControllerServlet.class);

	protected ServerInfo serverInfo = null;
	
	protected String defaultPage = null;
	protected boolean useSecurity = false;
	protected String logonPage = null;
	protected String logonAction = null;
	protected String logonKeyAction = null;
	protected boolean configured = false;
	private  boolean isenabled = false;				
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
		DatabaseOperation op = null;

		try
		{
			
		

			// create the server info object
			String appName = config.getInitParameter("app_name");
			serverInfo = new ServerInfo(getServletContext(), appName);
			op = serverInfo.getDbfactory().startOperation();
			RegisterSchedJobs.loadAndStartJob(op);
			String regurl = serverInfo.getRegistrationUrl() + "/ws";
			
			String amsCountConfigAllowed = op.getPropertyValue(DatabaseFactory.PROP_SEND_AMSCOUNT);
			if("Y".equals(amsCountConfigAllowed))
				RegisterSchedJobs.loadBackEndJob(op, regurl, serverInfo.getCarrierInfo().getCarrierId(), "Y");
			
			// see if installation is a POC and is past its time limit
			if (serverInfo.getCarrierInfo().isPoc() && serverInfo.getCarrierInfo().isPocTerminated())
			{
				configured = false;
				return;
			}
			
			// see if the database is up to date
			if (!serverInfo.isDbUpdated())
			{
				configured = false;
				return;
			}		
			
			configured = true;			

			// see if security is enabled
			useSecurity = "true".equals(config.getInitParameter("security"));

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
			
			// get the logon page and action (if we are using security)
			if (useSecurity)
			{
				logonPage = config.getInitParameter("logon_page");
				logonAction = config.getInitParameter("logon_action");
				logonKeyAction = config.getInitParameter("logonkey_action");
			}	
			
			//updateActivationCode(op,null);
			
		}
		catch (Exception e)
		{
			log.error(e.getMessage());
			throw new ServletException(e);
		}finally
		{
			if (op != null)
				op.close();
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
			// check to see if the app is configured
			if (!configured)
			{
				handleAppNotConfigured(req, resp);
				return;
			}
			
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
			
			// start a database transaction
			op = serverInfo.getDbfactory().startOperation();
			
			// if we are using security, check to see if we are logged in
			if (useSecurity)
			{
				AgentInfo agent = serverInfo.getAgentInfo(req.getSession(), op);
				if (agent == null)
				{
					// Check for terminated POC
					if (serverInfo.getCarrierInfo().isPoc() && serverInfo.getCarrierInfo().isPocTerminated())
					{
						handleAppNotConfigured(req, resp);
						return;
					}
					
					// NOT logged in
					if (!logonAction.equals(actionParam))
					{
						String key = req.getParameter("key");
						if (key == null)
						{
							// show the logon page
							serverInfo.showPage(logonPage, req, resp, op, fileItems);
							return;
						}
						else
						{
							// logon with the key
							ActionDef keyaction = (ActionDef) serverInfo.getActions().get(logonKeyAction);
							String nextPage = keyaction.getActionInstance().perform(req, resp, serverInfo, op, fileItems);
							String actionType =req.getParameter("actionType") ;
							if("JSON".equalsIgnoreCase(actionType)) {
								resp.getWriter().print(nextPage);
								return;
							}
							if (nextPage != null)
							{
								// redirect to a page - this usually means the key was bad
								serverInfo.showPage(nextPage, req, resp, op, fileItems);	
								return;
							}
						}
					}
				}
			}
			else if (serverInfo.getCarrierInfo().isPoc() && serverInfo.getCarrierInfo().isPocTerminated())
			{
				// Project is terminated POC -- don't allow login
				handleAppNotConfigured(req, resp);
				return;
			}
			
			
			if (actionParam != null && !actionParam.equals(""))
			{
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
						String actionType =req.getParameter("actionType") ;
						if("JSON".equalsIgnoreCase(actionType)) {
							resp.setContentType("application/json");
							resp.getWriter().print(nextPage);
							return;
						}
						if (nextPage != null)
							serverInfo.showPage(nextPage, req, resp, op, fileItems);	
					}
				}
				// check to see if agency vendor system list has been initialized;
				// if not, then forward to the vendor system list page
				else if (!serverInfo.getCarrierInfo().isVendorListInitialized())
				{
					serverInfo.showPage("menu.ams", req, resp, op, fileItems);
				}
				else
				{
					// show the default page
					serverInfo.showPage(defaultPage, req, resp, op, fileItems);
				}
			}
			// check to see if agency vendor system list has been initialized;
			// if not, then forward to the vendor system list page
			else if (!serverInfo.getCarrierInfo().isVendorListInitialized())
			{
				serverInfo.showPage("menu.ams", req, resp, op, fileItems);
			}
			else
			{
				
				//update Activation code from Registration Admin Server;
				
				//updateActivationCode(op,req);
				// show the default page
				serverInfo.showPage(defaultPage, req, resp, op, fileItems);
			}
		}
		catch (SQLException sqle)
		{
			log.error(sqle.getMessage());
			// uncomment the following line when unexpected exceptions
			// are occuring to aid in debugging the problem.
			System.out.println(sqle.getMessage());
			sqle.printStackTrace();
			
			// look up the current agent id, if applicable
			String agentId = null;
			try
			{
				AgentInfo agent = serverInfo.getAgentInfo(req.getSession(), op);
				if (agent != null)
					agentId = agent.getAgentId();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
			
			// Always send a notification email when an unexpected exception
			// occurs -- possibly change this to depend on a carrier-set flag
			// in the future...  -- 06/09/2003, kwm
			EmailService.getInstance().sendErrorNotification(sqle, serverInfo.getAppName(), agentId,
											serverInfo.getAppVersion(), serverInfo.getDbVersion());
			
			if (sqle.getErrorCode() == -904)
			{
				// shut down the app
				configured = false;
				try {
					handleAppNotConfigured(req, resp);
				} catch (Exception e) {
					log.error(e.getMessage());
				}
			}
		}
		catch (Throwable theException)
		{
			log.error(theException.getMessage());
			if (theException.getMessage() != null &&
				theException.getMessage().indexOf("by peer: socket write error") > 0)
			{
				//*** SPECIAL CIRCUMSTANCE:
				// User hit another link or closed the browser before the new page
				// was loaded -- do not send an error email! -- 09/17/2003, kwm
				System.out.println(theException.getMessage());
			}
			else
			{
				// uncomment the following line when unexpected exceptions
				// are occuring to aid in debugging the problem.
				System.out.println(theException.getMessage());
				theException.printStackTrace();
			
				// look up the current agent id, if applicable
				String agentId = null;
				try
				{
					AgentInfo agent = serverInfo.getAgentInfo(req.getSession(), op);
					if (agent != null)
						agentId = agent.getAgentId();
				} catch (Exception e) {
					log.error(e.getMessage());
				}
				
				// Always send a notification email when an unexpected exception
				// occurs -- possibly change this to depend on a carrier-set flag
				// in the future...  -- 06/09/2003, kwm
				EmailService.getInstance().sendErrorNotification(theException, serverInfo.getAppName(), 
									agentId, serverInfo.getAppVersion(), serverInfo.getDbVersion());
			}
		}
		finally
		{
			if (op != null)
				op.close();
		}
	}
	
	protected void handleAppNotConfigured(HttpServletRequest req, HttpServletResponse resp) throws Exception
	{
		getServletContext().getRequestDispatcher("/noncfg.jsp").forward(req, resp);
	}
	
	/*private void updateActivationCode(DatabaseOperation op, HttpServletRequest req) {
		//update Activation code from Registration Admin Server;
		
		try {
			String endpoint = serverInfo.getRegistrationUrl() + "/ws";
			TeamupWSClient ws = new TeamupWSClient(endpoint, "TUDL");
			CarrierIdRequest carrierIdInput = new CarrierIdRequest();
			carrierIdInput.setCarrierId(serverInfo.getCarrierInfo().getCarrierId());
			GetCarrierActCodeOutput outObject = (GetCarrierActCodeOutput) ws.callService("GetActivationCode", carrierIdInput);		
			if(outObject != null  && outObject.getOutput() != null) {
				if(serverInfo.getCarrierInfo().getCarrierId().equals(outObject.getOutput().getCarrierId())){
					String actCode = outObject.getOutput().getActivationCode();
					if(null!= actCode  && !"".equals(actCode)) {
						op.updateProperty(DatabaseFactory.PROP_CARRIER_ACTIVATION,actCode);
						op.updateProperty(DatabaseFactory.PROP_CARRIER_DEPLOYMENTID,outObject.getOutput().getDeploymentId());
						Licence lic = new Licence();
						serverInfo.setLicence(Licence.getLicence(actCode, outObject.getOutput().getDeploymentId(), lic));
						serverInfo.setClaimActivated(Licence.isClaimActivated(serverInfo.getLicence()));
						if(req != null){
							req.getSession().setAttribute("isClaimActivated", serverInfo.isClaimActivated());
							req.getSession().setAttribute("daysUntilExpiration", serverInfo.getLicence().getDaysUntilExpiration());
						}

					}
				}
			}
		}catch(Exception e){
			log.error("GetActivationCode ::" + e.getMessage());
		}
	}*/
}
