package connective.teamup.download.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.CarrierInfo;
import connective.teamup.download.CustomTextFactory;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.services.EmailService;

/**
 * @author Mike Haney
 *
 * Action bean to send email to agents with failed/inactive downloads
 */
public class DownloadReport implements Action
{
	private static final Logger LOGGER = Logger.getLogger(DownloadReport.class);
	/**
	 * Constructor for DownloadTestComplete.
	 */
	public DownloadReport()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.Action#perform(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException
	{
		try
		{
			// Determine if carrier summary report should be sent
			String flag = op.getPropertyValue(DatabaseFactory.PROP_DL_ALERT_CARRIER_FLAG);
			boolean createCarrierReport = (flag != null && flag.equals("Y"));
			String carrierReport = "";
			
			// Build urls using info from the request
			String keyName = "key";

			String loginLink = serverInfo.getRequestUrl(req, "/agency");
			String downloadLink = serverInfo.getRequestUrl(req, "/agency");
			downloadLink += "?action=dlstart&" + keyName + "=";

			int days = Integer.parseInt(op.getPropertyValue(DatabaseFactory.PROP_DL_FAILED_DAYS));
			AgentInfo[] agents = null;

/*
 *  Only report agents that have "stale" files; do not report scheduled agents
 *  that have not attempted a download -- 07/10/2006, kwm
 * 
			// get a list of scheduled downloads that failed
			agents = op.getScheduledDownloadFailures(days);
			if (createCarrierReport && agents.length > 0)
				carrierReport += "The following scheduled download agents have not attempted to retrieve their download files in at least " + days + " day(s):\n\n";
			for (int i=0; i < agents.length; i++)
			{
				// send an email to the agent
				CustomTextFactory factory = null;
				if (agents[i].isClientAppRegistered())
					factory = new CustomTextFactory(CustomTextFactory.TEXT_DL_FAILED_CLIENT_APP,
											CustomTextFactory.TYPE_EMAIL, serverInfo, agents[i], op);
				else
					factory = new CustomTextFactory(CustomTextFactory.TEXT_DL_FAILED,
											CustomTextFactory.TYPE_EMAIL, serverInfo, agents[i], op);

				String key = serverInfo.getSecurityProvider().getSecurityKey(agents[i].getAgentId(), agents[i].getPassword());
				factory.setDownloadUrl(downloadLink + key);
				factory.setLoginUrl(loginLink);
									
				String message = factory.getText();
				String subject = factory.getEmailSubject();
				String to = agents[i].getContactEmail();
				try
				{
					EmailService.getInstance().sendEMail(to, subject, message);
				}
				catch (Exception e) {}
				
				// add agent to the carrier report
				if (createCarrierReport)
					carrierReport += "    " + agents[i].getAgentId() + " - " + agents[i].getName() + "\n";
			}
*/
						
			// get all the agents with stale files
			days = Integer.parseInt(op.getPropertyValue(DatabaseFactory.PROP_DL_STALE_DAYS));
			agents = op.getLiveAgents();
			String staleAgents = "";
			for (int i=0; i < agents.length; i++)
			{
				if (op.hasStaleFiles(agents[i].getAgentId(), days))
				{
					// send an email to the agent
					CustomTextFactory factory = null;
					if (agents[i].isClientAppRegistered())
						factory = new CustomTextFactory(CustomTextFactory.TEXT_DL_STALE_CLIENT_APP,
											CustomTextFactory.TYPE_EMAIL, serverInfo, agents[i], op);
					else
						factory = new CustomTextFactory(CustomTextFactory.TEXT_DL_STALE,
											CustomTextFactory.TYPE_EMAIL, serverInfo, agents[i], op);
					
					String key = serverInfo.getSecurityProvider().getSecurityKey(agents[i].getAgentId(), agents[i].getAgentId(), agents[i].getPassword());
					factory.setDownloadUrl(downloadLink + key);
					
					String message = factory.getText();
					String subject = factory.getEmailSubject();
					
					String htmlMsg = null;
					if (serverInfo.getCarrierInfo().isEmailAsHtml())
						htmlMsg = factory.getHtml();
					
					String to = agents[i].getContactEmail();
					try
					{
						EmailService.getInstance().sendEMail(to, subject, message, htmlMsg);
					}
					catch (Exception e) {
						LOGGER.error(e);
					}
				
					// add agent to the carrier report
					if (createCarrierReport)
						staleAgents += "    " + agents[i].getAgentId() + " - " + agents[i].getName() + "\n";
				}
			}
			
			if (createCarrierReport && (!carrierReport.equals("") || !staleAgents.equals("")))
			{
				if (!staleAgents.equals(""))
				{
					if (!carrierReport.equals(""))
						carrierReport += "\n---------------\n";
					carrierReport += "The following download agents have not retrieved their download files in at least " + days + " day";
					if (days > 1)
						carrierReport += "s";
					carrierReport += ":\n\n";
					carrierReport += staleAgents;
				}
				carrierReport += "\n---------------\n\nThese agents have been sent a Download Alert! email concerning the status of their download files.\n";
				
				String to = serverInfo.getCarrierInfo().getReportsEmail();
				String subject = "TEAM-UP Download Alert! Carrier Report for " + CarrierInfo.getInstance().getShortName();
				
				try
				{
					EmailService.getInstance().sendEMail(to, subject, carrierReport);
				}
				catch (Exception e) {
					LOGGER.error(e);
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error building download report", e);
			throw new ActionException("Error building download report", e);
		}
		
		return "dlreport_complete";
	}
	
	
	public static void downloadAlertReport( ServerInfo serverInfo, String downloadLink ) throws ActionException
	{
		DatabaseOperation op = null;
		try
		{

			DatabaseFactory dbfactory = DatabaseFactory.getInstance();
			op = dbfactory.startOperation();
			String flag = op.getPropertyValue(DatabaseFactory.PROP_DL_ALERT_CARRIER_FLAG);
			// Determine if carrier summary report should be sent
			boolean createCarrierReport = (flag != null && flag.equals("Y"));
			String carrierReport = "";
			
			// Build urls using info from the request
			String keyName = "key";

			//String loginLink = serverInfo.getRequestUrl(req, "/agency");
			//String downloadLink = serverInfo.getRequestUrl(req, "/agency");
			//downloadLink += "?action=dlstart&" + keyName + "=";

			int days = Integer.parseInt(op.getPropertyValue(DatabaseFactory.PROP_DL_FAILED_DAYS));
			AgentInfo[] agents = null;

		
			// get all the agents with stale files
			days = Integer.parseInt(op.getPropertyValue(DatabaseFactory.PROP_DL_STALE_DAYS));
			agents = op.getLiveAgents();
			String staleAgents = "";
			for (int i=0; i < agents.length; i++)
			{
				if (op.hasStaleFiles(agents[i].getAgentId(), days))
				{
					// send an email to the agent
					CustomTextFactory factory = null;
					if (agents[i].isClientAppRegistered())
						factory = new CustomTextFactory(CustomTextFactory.TEXT_DL_STALE_CLIENT_APP,
											CustomTextFactory.TYPE_EMAIL, serverInfo, agents[i], op);
					else
						factory = new CustomTextFactory(CustomTextFactory.TEXT_DL_STALE,
											CustomTextFactory.TYPE_EMAIL, serverInfo, agents[i], op);
					
					String key = serverInfo.getSecurityProvider().getSecurityKey(agents[i].getAgentId(), agents[i].getAgentId(), agents[i].getPassword());
					factory.setDownloadUrl(downloadLink + key);
					
					String message = factory.getText();
					String subject = factory.getEmailSubject();
					
					String htmlMsg = null;
					if (serverInfo.getCarrierInfo().isEmailAsHtml())
						htmlMsg = factory.getHtml();
					
					String to = agents[i].getContactEmail();
					try
					{
						EmailService.getInstance().sendEMail(to, subject, message, htmlMsg);
					}
					catch (Exception e) {
						LOGGER.error(e);
					}
				
					// add agent to the carrier report
					if (createCarrierReport)
						staleAgents += "    " + agents[i].getAgentId() + " - " + agents[i].getName() + "\n";
				}
			}
			
			if (createCarrierReport && (!carrierReport.equals("") || !staleAgents.equals("")))
			{
				if (!staleAgents.equals(""))
				{
					if (!carrierReport.equals(""))
						carrierReport += "\n---------------\n";
					carrierReport += "The following download agents have not retrieved their download files in at least " + days + " day";
					if (days > 1)
						carrierReport += "s";
					carrierReport += ":\n\n";
					carrierReport += staleAgents;
				}
				carrierReport += "\n---------------\n\nThese agents have been sent a Download Alert! email concerning the status of their download files.\n";
				
				String to = serverInfo.getCarrierInfo().getReportsEmail();
				String subject = "TEAM-UP Download Alert! Carrier Report for " + CarrierInfo.getInstance().getShortName();
				
				try
				{
					if(to != null && !to.equals(""))
						EmailService.getInstance().sendEMail(to, subject, carrierReport);
				}
				catch (Exception e) {
					LOGGER.error(e);
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error building download report", e);
			throw new ActionException("Error building download report", e);
		}finally{
			if (op != null)
				op.close();
		}
		
		//return "dlreport_complete";
	}
}
