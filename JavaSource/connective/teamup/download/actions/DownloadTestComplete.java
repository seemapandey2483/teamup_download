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
 * @author Kyle McCreary
 *
 * Action bean to update agency flags to complete the registration process.
 */
public class DownloadTestComplete implements Action
{
	private static final Logger LOGGER = Logger.getLogger(DownloadTestComplete.class);
	/**
	 * Constructor for DownloadTestComplete.
	 */
	public DownloadTestComplete()
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
			AgentInfo agentBean = serverInfo.getAgentInfo(req.getSession(), op);
			boolean previouslyRegistered = agentBean.isRegistered();
			if (!agentBean.isLive())
				agentBean.setStatus(AgentInfo.STATUS_REGISTERED);
			agentBean.save();
			serverInfo.setAgentInfo(req.getSession(), agentBean);
			
			if (!previouslyRegistered && serverInfo.getCarrierInfo().isNotifyOnAgentRegister())
			{
				// Send an email to the carrier notifying that the agent has
				// completed the registration process
				CustomTextFactory factory = new CustomTextFactory(
					CustomTextFactory.TEXT_REGISTERED, CustomTextFactory.TYPE_EMAIL,
					serverInfo, agentBean, op);
				String message = factory.getText();
				String subject = factory.getEmailSubject();
				
				String htmlMsg = null;
				if (serverInfo.getCarrierInfo().isEmailAsHtml())
					htmlMsg = factory.getHtml();
				
				String to = serverInfo.getCarrierInfo().getReportsEmail();
				if (to == null || to.equals(""))
					to = serverInfo.getCarrierInfo().getContactEmail();
				EmailService.getInstance().sendEMail(to, subject, message, htmlMsg);
			}
		}
		catch (ActionException e)
		{
			LOGGER.error("Error sending email notification of agent registration", e);
			throw new ActionException("Error sending email notification of agent registration", e);
		}
		catch (Exception e)
		{
			LOGGER.error("Error completing the download test and registration process", e);
			throw new ActionException("Error completing the download test and registration process", e);
		}
		
		String nextPage = "menu.settings";

		// determine the next page		
		String desktopDownload = req.getParameter("desktopDownload");
		String desktopConfig = req.getParameter("desktopConfig");
		String scheduleTask = req.getParameter("scheduleTask");
		String removeSchedule = req.getParameter("removeSchedule");

		if ((desktopDownload != null && desktopDownload.equals("Y")) || (desktopConfig != null && desktopConfig.equals("Y")))
			nextPage = "config.desktop";
		else if (scheduleTask != null && scheduleTask.equals("Y"))
			nextPage = "config.scheduler";
		else if (removeSchedule != null && removeSchedule.equals("Y"))
			nextPage = "remove.scheduler";
		return nextPage;
	}

}
