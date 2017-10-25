/*
 * Created on Apr 3, 2006
 */
package connective.teamup.download.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.CustomTextFactory;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.services.EmailService;

/**
 * Action bean to send the agency migration invitation email to the current agent.
 * 
 * @author Kyle McCreary
 */
public class SendMigrationEmail implements Action
{
	private static final Logger LOGGER = Logger.getLogger(SendMigrationEmail.class);
	/**
	 * Constructor for SendMigrationEmail.
	 */
	public SendMigrationEmail()
	{
		super();
	}

	/* (non-Javadoc)
	 * @see connective.teamup.download.Action#perform(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, connective.teamup.download.ServerInfo, connective.teamup.download.db.DatabaseOperation, org.apache.commons.fileupload.FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException
	{
		String nextPage = "splash";
		
		try
		{
			// Load the agent info bean and database connection info
			AgentInfo agent = serverInfo.getAgentInfo(req.getSession(), op);
			
			// Create and send the email
			String to = agent.getContactEmail();
			String from = serverInfo.getCarrierInfo().getContactEmail();
			
			CustomTextFactory factory = new CustomTextFactory(CustomTextFactory.TEXT_AGENT_MIGRATION, 
															  CustomTextFactory.TYPE_EMAIL, 
															  serverInfo, agent, op);
			String subject = factory.getEmailSubject();
			String body = factory.getText();
			
			String html = null;
			if (serverInfo.getCarrierInfo().isEmailAsHtml())
				html = factory.getHtml();
			
			EmailService.getInstance().sendEMail(agent.getContactEmail(), subject, body, html);
			
			if (!agent.isLive())
			{
				// Update the agent status to show that invitation email has been sent
				agent.setSentAgentInvitation();
				agent.save();
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error sending agent migration email", e);
			throw new ActionException("Error sending agent migration email", e);
		}
		
		return nextPage;
	}

}
