/*
 * Created on Nov 14, 2007
 */
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
 * @author kmccreary
 */
public class AgencyTransactionReport implements Action
{
	private static final Logger LOGGER = Logger.getLogger(AgencyTransactionReport.class);
	/**
	 * Constructor for AgencyTransactionReport action class.
	 */
	public AgencyTransactionReport()
	{
		super();
	}

	/* (non-Javadoc)
	 * @see connective.teamup.download.Action#perform(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, connective.teamup.download.ServerInfo, connective.teamup.download.db.DatabaseOperation, org.apache.commons.fileupload.FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items)
		throws ActionException
	{
		String nextPage = "report.agytrans";
		
		String sourcePage = req.getParameter("src_page");
		if (sourcePage != null && sourcePage.equalsIgnoreCase("agytrans_param"));
		{
			// Check to see if an exact agent ID match is found; if so, proceed to report; otherwise
			// return to the parameters page and present a select-box list of agents
			String agentID = req.getParameter("agentID");
			if (agentID == null || agentID.equals(""))
			{
				String searchMethod = req.getParameter("search_by");
				if (searchMethod == null || searchMethod.equals("agt_name"))
					nextPage = "menu.report.agytrans";
				else
				{
					String searchStr = req.getParameter("search_str");
					if (searchStr == null || searchStr.equals(""))
						nextPage = "menu.report.agytrans";
					else
					{
						try {
							AgentInfo agentInfo = op.getAgentInfo(searchStr);
							if (agentInfo == null)
								nextPage = "menu.report.agytrans";
						}
						catch (Exception e)
						{
							LOGGER.error(e.getMessage());
							nextPage = "menu.report.agytrans";
						}
					}
				}
			}
		}
		
		return nextPage;
	}

}
