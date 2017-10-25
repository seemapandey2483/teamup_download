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
 * Action bean to set the agency for 'live' downloads.
 */
public class AgentGoLive implements Action
{
	private static final Logger LOGGER = Logger.getLogger(AgentGoLive.class);
	/**
	 * Constructor for AgentGoLive.
	 */
	public AgentGoLive()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.Action#perform(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException
	{
		String nextPage = "menu.settings";
		
		try
		{
			AgentInfo agentBean = serverInfo.getAgentInfo(req.getSession(),op);
			if (agentBean.isRegistered())
			{
				agentBean.setStatus(AgentInfo.STATUS_LIVE);
				agentBean.save();
				serverInfo.setAgentInfo(req.getSession(), agentBean);
				nextPage = "menu.system.help";
				
				if (serverInfo.getCarrierInfo().isNotifyOnStatusChange())
				{
					// Send an email to the carrier notifying that the agent has
					// changed their download status
					CustomTextFactory factory = new CustomTextFactory(
						CustomTextFactory.TEXT_STATUS_CHANGE, CustomTextFactory.TYPE_EMAIL,
						serverInfo, agentBean, op);
					factory.setPriorStatus("active (registered)");
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
		}
		catch (ActionException ae)
		{
			LOGGER.error("Error sending agent status change notification", ae);
			throw new ActionException("Error sending agent status change notification", ae);
		}
		catch (Exception e)
		{
			LOGGER.error("Error retrieving custom text for agent status change notification", e);
			throw new ActionException("Error retrieving custom text for agent status change notification", e);
		}
		
		return nextPage;
	}

}
