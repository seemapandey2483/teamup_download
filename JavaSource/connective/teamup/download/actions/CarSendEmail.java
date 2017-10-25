package connective.teamup.download.actions;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.CustomTextFactory;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentGroupInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.services.EmailService;

/**
 * @author Kyle McCreary
 */
public class CarSendEmail implements Action
{
	private static final Logger LOGGER = Logger.getLogger(CarSendEmail.class);
	/* (non-Javadoc)
	 * @see connective.teamup.download.Action#perform(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, connective.teamup.download.ServerInfo, connective.teamup.download.db.DatabaseOperation, org.apache.commons.fileupload.FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException
	{
		String nextPage = "car.group.detail";
		
		ArrayList goodEmails = new ArrayList();
		ArrayList badEmails = new ArrayList();
		ActionException actionExc = null;
		
		String groupName = req.getParameter("groupName");
		String nextAction = req.getParameter("nextAction");
		String subject = req.getParameter("subject");
		String groupType = req.getParameter("groupType");
		if (groupType == null || groupType.equals(""))
			groupType = "R";				// rollout
		else if (groupType.equals("M"))		// migration
			nextPage = "camm.group.detail";
		
		try
		{
			AgentGroupInfo group = op.getAgentGroup(groupName);
			
			String url = serverInfo.getRequestUrl(req, "/agency");
			
			// Retrieve the list of agent recipients and send the emails
			ArrayList agtList = (ArrayList) req.getSession().getAttribute(ServerInfo.STORE_AGENT_LIST);
			if (agtList == null || agtList.size() == 0)
			{
				throw new ActionException("Error retrieving list of agents for group email", null);
			}
			else
			{
				for (int i=0; i < agtList.size(); i++)
				{
					AgentInfo agent = op.getAgentInfo((String) agtList.get(i));
					CustomTextFactory factory = null;
					if (groupType != null && groupType.equals("M"))		// migration
						factory = new CustomTextFactory(CustomTextFactory.TEXT_AGENT_MIGRATION, CustomTextFactory.TYPE_EMAIL, serverInfo, agent, op);
					else if (serverInfo.getCarrierInfo().isClientAppUsed())
						factory = new CustomTextFactory(CustomTextFactory.TEXT_NEW_AGENT_CLIENT_APP, CustomTextFactory.TYPE_EMAIL, serverInfo, agent, op);
					else
						factory = new CustomTextFactory(CustomTextFactory.TEXT_NEW_AGENT, CustomTextFactory.TYPE_EMAIL, serverInfo, agent, op);
					factory.setLoginUrl(url);
					String body = factory.getText();
					
					String htmlMsg = null;
					if (serverInfo.getCarrierInfo().isEmailAsHtml())
						htmlMsg = factory.getHtml();
					
					try {
						EmailService.getInstance().sendEMail(agent.getContactEmail(), subject, body, htmlMsg);
						goodEmails.add(agent);
						
						// Update the agent status to show that invitation email has been sent
						agent.setSentAgentInvitation();
						agent.save();
					} catch (ActionException ax) {
						LOGGER.error(ax);
						badEmails.add(agent);
						if (actionExc == null)
							actionExc = ax;
					} catch (Exception ex) {
						LOGGER.error(ex);
						badEmails.add(agent);
						if (actionExc == null)
							actionExc = new ActionException("Error sending agent rollout email", ex);
					}
				}
				
				// Handle the first exception encountered
				if (actionExc != null)
				{
					System.out.println("Error sending agent rollout email: " + actionExc.getMessage());
					actionExc.printStackTrace();
				
					// look up the current agent id, if applicable
					String agentId = null;
					try
					{
						AgentInfo agent = serverInfo.getAgentInfo(req.getSession(), op);
						if (agent != null)
							agentId = agent.getAgentId();
					} catch (Exception e) {
						LOGGER.error(e);
					}
					
					// Always send a notification email when an unexpected exception
					// occurs -- possibly change this to depend on a carrier-set flag
					// in the future...  -- 06/09/2003, kwm
					EmailService.getInstance().sendErrorNotification(actionExc, serverInfo.getAppName(), agentId, serverInfo.getAppVersion(), serverInfo.getDbVersion());
				}
				
				// Create and send the confirmation/summary email
				String sendSummary = req.getParameter("sendsummary");
				if (sendSummary != null && sendSummary.equals("Y"))
				{
					String summaryAddress = req.getParameter("summaryaddress");
					if (summaryAddress == null || summaryAddress.trim().equals(""))
						summaryAddress = serverInfo.getCarrierInfo().getReportsEmail();
					
					subject = "TEAM-UP Download: Agent Rollout Process";
					
					String body = "Agent Rollout Group:  " + req.getParameter("groupName");
					body += "\nRollout Action:       " + req.getParameter("nextAction");
					body += "\nEmail Subject Line:   " + req.getParameter("subject");
					if (goodEmails.size() == 0)
						body += "\n\n\nNo emails were successfully processed.";
					else
					{
						if (goodEmails.size() == 1)
							body += "\n\n\nThe email has been sent to the following agency:";
						else
							body += "\n\n\nEmails have been sent to the following agencies:";
						for (int i=0; i < goodEmails.size(); i++)
						{
							AgentInfo agentInfo = (AgentInfo) goodEmails.get(i);
							body += "\n     " + agentInfo.getAgentId() + " - " + 
									agentInfo.getName() + "  (" +
									agentInfo.getContactEmail() + ")";
						}
					}
					
					if (badEmails.size() > 0)
					{
						body += "\n\n\nAn error occurred when trying to process the email for the following ";
						if (badEmails.size() == 1)
							body += "agency:";
						else
							body += "agencies:";
						for (int i=0; i < badEmails.size(); i++)
						{
							AgentInfo agentInfo = (AgentInfo) badEmails.get(i);
							body += "\n     " + agentInfo.getAgentId() + " - " +
									agentInfo.getName() + "  (";
							if (agentInfo.getContactEmail() == null || agentInfo.getContactEmail().trim().equals(""))
								body += "** no email address defined **)";
							else
								body += agentInfo.getContactEmail() + ")";
						}
					}
					body += "\n";
					
					EmailService.getInstance().sendEMail(summaryAddress, subject, body, null);
				}
				
				// Remove the list of agent recipients from the session
				req.getSession().removeAttribute(ServerInfo.STORE_AGENT_LIST);
				
				// Log the email distribution and update the group's last action info
				long eventDate = System.currentTimeMillis();
				op.logGroupEvent(groupName, nextAction, DatabaseFactory.EVENT_EMAIL, nextAction, eventDate, true, null);
				group.setLastAction(nextAction);
				group.setLastActionDate(eventDate);
				group.save();
			}
		}
		catch (ActionException ae)
		{
			LOGGER.error(ae);
			throw ae;
		}
		catch (Exception e)
		{
			LOGGER.error("Error sending agent rollout email", e);
			throw new ActionException("Error sending agent rollout email", e);
		}
		
		return nextPage;
	}

}
