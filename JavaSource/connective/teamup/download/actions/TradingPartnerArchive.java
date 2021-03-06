package connective.teamup.download.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author Kyle McCreary
 *
 * Action bean used to view the archived files for an existing trading partner.
 * This action is called when the user selects "Work with Archive..." from 
 * the "Trading Partners" menu in the company admin pages.
 */
public class TradingPartnerArchive implements Action
{

	/**
	 * Constructor for TradingPartnerArchive.
	 */
	public TradingPartnerArchive()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.Action#perform(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException
	{
		String nextPage = TradingPartnerListView.getUserDefaultView(req, serverInfo);
		
		// See if a current agent has been specified; if so, change the next
		// page to go directly to the trading partner info
		String agentID = req.getParameter("agentID");
		if (agentID != null && agentID.trim().length() > 0)
			nextPage = "archive";
		
		return nextPage;
	}

}
