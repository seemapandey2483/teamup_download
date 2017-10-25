package connective.teamup.download.pages;

import java.sql.SQLException;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.DisplayBean;
import connective.teamup.download.DisplayBeanException;
import connective.teamup.download.GenericPage;
import connective.teamup.download.PageException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.beans.AgencyInfoDisplayBean;
import connective.teamup.download.db.AgentGroupInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author Kyle McCreary
 *
 * Overrides the generic page bean to edit an existing trading partner.  Parses
 * the requested trading partner's agent ID from the request.
 */
public class TradingPartnerEditPage extends GenericPage
{
	private static final Logger LOGGER = Logger.getLogger(TradingPartnerEditPage.class);
	/**
	 * Constructor for TradingPartnerEditPage.
	 */
	public TradingPartnerEditPage()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.GenericPage#createDisplayBean(HttpServletRequest, HttpServletResponse, ServerInfo)
	 */
	protected DisplayBean createDisplayBean(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws PageException
	{
		// Create the display bean
		AgencyInfoDisplayBean bean = (AgencyInfoDisplayBean) super.createDisplayBean(req, resp, serverInfo, op, items);
		
		try
		{
			// Get the requested trading partner
			String agentID = req.getParameter("agentID");
			if (agentID == null)
				agentID = (String) req.getAttribute("search.partcode.agentID");
			if (agentID != null && !agentID.equals(""))
			{
				boolean newAgent = false;
				AgentInfo agent = op.getAgentInfo(agentID);
				if (agent == null)
				{
					newAgent = true;
					agent = op.createAgentInfo(agentID);
				}
				else
				{
					agent.loadParticipantsFromDb();
					if (agent.isRegistered())
					{
						try
						{
							// Build urls using info from the request
							String keyName = "key";
								//keyName = serverInfo.getPropertyValue(serverInfo.PROP_SECURITY_PASSWORD);
							String loginLink = serverInfo.getRequestUrl(req, "/agency");
							loginLink += "?" + keyName + "=" + serverInfo.getSecurityProvider().getSecurityKey(agent.getAgentId(),agent.getAgentId(), agent.getPassword());
							
							// Set the login id for user to login as agent
							bean.setLoginLink(loginLink);
						}
						catch (Exception e) {
							LOGGER.error(e);
						}
					}
				}
				bean.setAgencyInfo(agent);
				bean.setNewAgent(newAgent);
				
				// Load the list of agency groups
				AgentGroupInfo[] groups = op.getAllAgentGroups();
				if (groups != null)
				{
					Hashtable groupHash = new Hashtable();
					AgentGroupInfo[] memberships = agent.getAgentGroups(null);
					if (memberships != null)
					{
						for (int i=0; i < memberships.length; i++)
						{
							if (memberships[i].getType().equals(AgentGroupInfo.TYPE_AGENTS_MISC))
								groupHash.put(memberships[i].getName(), memberships[i]);
							else if (memberships[i].getType().equals(AgentGroupInfo.TYPE_MIGRATION))
								bean.setMigrationGroup(memberships[i].getName());
							else if (memberships[i].getType().equals(AgentGroupInfo.TYPE_ROLLOUT))
								bean.setRolloutGroup(memberships[i].getName());
						}
					}
					
					for (int i=0; i < groups.length; i++)
					{
						boolean isMember = false;
						if (groupHash.get(groups[i].getName()) != null)
							isMember = true;
						bean.addAgentGroup(groups[i], isMember);
					}
				}
			}
			
			// Load the table of agency vendor systems
			bean.loadAmsTable(op);
		}
		catch (DisplayBeanException e)
		{
			LOGGER.error(e);
			throw new PageException(e);
		}
		catch (SQLException e)
		{
			LOGGER.error(e);
			throw new PageException("Error occurred retrieving agent info", e);
		}
		
		return bean;
	}

}
