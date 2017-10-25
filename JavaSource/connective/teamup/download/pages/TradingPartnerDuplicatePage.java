package connective.teamup.download.pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.DisplayBean;
import connective.teamup.download.PageException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.beans.AgencyInfoDisplayBean;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author Kyle McCreary
 *
 * Overrides the main Trading Partner Edit page bean to re-edit an existing 
 * trading partner.  Parses the requested trading partner's agent ID from the 
 * request, and sets the 'filename invalid' flag to show as a duplicate filename.
 */
public class TradingPartnerDuplicatePage extends TradingPartnerEditPage
{
	private static final Logger LOGGER = Logger.getLogger(TradingPartnerDuplicatePage.class);
	/**
	 * Constructor for TradingPartnerInvalidPage.
	 */
	public TradingPartnerDuplicatePage()
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
		bean.setDuplicateAgent(true);

		try
		{
			// Pull the agent info from the session, if available
			AgentInfo agent = serverInfo.getAgentInfo(req.getSession(), op);
			if (agent != null)
			{
				bean.setAgencyInfo(agent);
				
				// Remove the agent bean from the session
				serverInfo.setAgentInfo(req.getSession(), null);
			}
			
			String newAgent = req.getParameter("newAgent");
			if (newAgent != null && newAgent.equals("Y"))
				bean.setNewAgent(true);
		}
		catch (Exception e)
		{
			LOGGER.error("Error occurred retrieving agent info", e);
			throw new PageException("Error occurred retrieving agent info", e);
		}
		
		return bean;
	}

}
