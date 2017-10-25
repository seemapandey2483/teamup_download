package connective.teamup.download.beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.CarrierInfo;
import connective.teamup.download.CustomTextFactory;
import connective.teamup.download.DisplayBean;
import connective.teamup.download.DisplayBeanException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentGroupInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author Kyle McCreary
 */
public class CarEmailDisplayBean implements Serializable, DisplayBean
{
	private static final Logger LOGGER = Logger.getLogger(CarEmailDisplayBean.class);
	
	private CarrierInfo carrierInfo = null;
	private String groupName = null;
	private String nextAction = null;
	private String agentName = null;
	private String agentEmail = null;
	private String sentDate = null;
	private String subject = null;
	private String body = null;
	private ArrayList agentList = null;


	/* (non-Javadoc)
	 * @see connective.teamup.download.DisplayBean#init(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, connective.teamup.download.ServerInfo, connective.teamup.download.db.DatabaseOperation, org.apache.commons.fileupload.FileItem[])
	 */
	public void init(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws DisplayBeanException
	{
		try
		{
			// Load the carrier info
			this.carrierInfo = serverInfo.getCarrierInfo();
			
			// Load the group info
			groupName = req.getParameter("groupName");
			nextAction = req.getParameter("nextAction");
			AgentGroupInfo group = op.getAgentGroup(groupName);
			
			// Parse and save the list of selected agent recipients
			agentList = new ArrayList();
			ArrayList idList = new ArrayList();
			String[] agentId = null;
			try
			{
				agentId = req.getParameterValues("agent");
			} catch (Exception e) {
				LOGGER.error(e);
			}
			try
			{
				if (agentId == null || agentId.length == 0)
				{
					String strAgentId = req.getParameter("agent");
					if (strAgentId != null && !strAgentId.trim().equals(""))
					{
						agentId = new String[1];
						agentId[0] = strAgentId;
					}
				}
			} catch (Exception e) {
				LOGGER.error(e);
			}
			if (agentId != null)
			{
				for (int i=0; i < agentId.length; i++)
				{
					if (agentId[i] != null && !agentId[i].equals(""))
					{
						AgentInfo agent = op.getAgentInfo(agentId[i]);
						if (agent != null)
						{
							agentList.add(agent);
							idList.add(agent.getAgentId());
						}
					}
				}
				req.getSession().setAttribute(ServerInfo.STORE_AGENT_LIST, idList);
			}
			
			// Build the sample email for display/review
			AgentInfo agent = (AgentInfo) agentList.get(0);
			agentName = agent.getName();
			agentEmail = agent.getContactEmail();
			
			SimpleDateFormat df = (SimpleDateFormat)SimpleDateFormat.getDateTimeInstance();
			df.applyPattern("E, MMMM d, yyyy h:mm a");
			sentDate = df.format(new Date(System.currentTimeMillis()));
			
			CustomTextFactory factory = null;
			if (serverInfo.getCarrierInfo().isClientAppUsed())
				factory = new CustomTextFactory(CustomTextFactory.TEXT_NEW_AGENT_CLIENT_APP, CustomTextFactory.TYPE_EMAIL, serverInfo, agent, op);
			else
				factory = new CustomTextFactory(CustomTextFactory.TEXT_NEW_AGENT, CustomTextFactory.TYPE_EMAIL, serverInfo, agent, op);
			factory.setLoginUrl(serverInfo.getRequestUrl(req, "/agency"));
			
			subject = factory.getEmailSubject();
			if (nextAction.equals(DatabaseFactory.CAR_2ND_EMAIL))
				subject = "2nd Request: " + subject;
			else if (nextAction.equals(DatabaseFactory.CAR_3RD_EMAIL))
				subject = "3rd Request: " + subject;
			else if (nextAction.equals(DatabaseFactory.CAR_FINAL_EMAIL))
				subject = "Final Request: " + subject;
			
			String temp = factory.getText();
			StringBuffer buf = new StringBuffer();
			for (int i=0; i < temp.length(); i++)
			{
				char c = temp.charAt(i);
				if (c == '\n')
					buf.append("&nbsp;<BR>\n");
				else
					buf.append(c);
			}
			body = buf.toString();
		}
		catch (Exception e)
		{
			LOGGER.error("Error occurred building sample invitation email and agent recipient list", e);
			throw new DisplayBeanException("Error occurred building sample invitation email and agent recipient list", e);
		}
	}

	/**
	 * @return
	 */
	public String getAgentEmail()
	{
		return agentEmail;
	}

	/**
	 * @return
	 */
	public AgentInfo getAgent(int index)
	{
		if (index >= agentList.size())
			return null;
		return (AgentInfo)agentList.get(index);
	}

	public int getAgentCount()
	{
		return agentList.size();
	}

	/**
	 * @return
	 */
	public String getAgentName()
	{
		return agentName;
	}

	/**
	 * @return
	 */
	public String getBody()
	{
		return body;
	}

	/**
	 * @return
	 */
	public CarrierInfo getCarrierInfo()
	{
		return carrierInfo;
	}

	/**
	 * @return
	 */
	public String getGroupName()
	{
		return groupName;
	}

	/**
	 * @return
	 */
	public String getNextAction()
	{
		return nextAction;
	}

	/**
	 * @return
	 */
	public String getSubject()
	{
		return subject;
	}

	/**
	 * @return
	 */
	public String getSentDate()
	{
		return sentDate;
	}

}
