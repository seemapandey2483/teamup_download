package connective.teamup.download.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.DatabaseOperation;

/**
 * Action bean to search for the registered agent with the specified agent ID,
 * participant code or import filename.
 * 
 * @author Kyle McCreary
 */
public class TradingPartnerAgentIdSearch implements Action
{
	private static final Logger LOGGER = Logger.getLogger(TradingPartnerAgentIdSearch.class);
	/**
	 * Constructor for TradingPartnerAgentIdSearch.
	 */
	public TradingPartnerAgentIdSearch()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.Action#perform(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException
	{
		String nextPage = "tplist.agentid";
		
		try
		{
			String searchType = req.getParameter("search_type");
			if (searchType != null)
			{
				AgentInfo agent = null;
				
				if (searchType.equals("agentid") || searchType.equals("partcode"))
				{
					// Search for the specified participant code
					String partcode = req.getParameter("agentID");
					if (partcode == null)
						partcode = req.getParameter("partcode");
					if (partcode != null && !partcode.trim().equals(""))
						agent = op.getAgentInfo(partcode.trim().toUpperCase());
				}
				else if (searchType.equals("filename"))
				{
					// Search for the specified import filename
					String filename = req.getParameter("filename");
					if (filename != null && !filename.trim().equals(""))
						agent = op.getAgentInfoForFilename(filename.trim().toUpperCase());
					else
						nextPage = "tplist.search.error";
				}
				
				if (agent != null)
				{
					// If agent was found, forward to the Trading Partner Maintenance page
					req.setAttribute("search.partcode.agentID", agent.getAgentId());
					nextPage = "tplist.edit";
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error processing participant code search", e);
			throw new ActionException("Error processing participant code search", e);
		}
		
		return nextPage;
	}

}
