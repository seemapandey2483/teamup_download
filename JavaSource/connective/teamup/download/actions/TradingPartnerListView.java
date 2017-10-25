package connective.teamup.download.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.DatabaseOperation;

/**
 * Action bean used to determine which view of the Trading Partner List to display.
 * 
 * @author kmccreary
 */
public class TradingPartnerListView implements Action
{

	/**
	 * Constructor for TradingPartnerListView.
	 */
	public TradingPartnerListView()
	{
		super();
	}

	/* (non-Javadoc)
	 * @see connective.teamup.download.Action#perform(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, connective.teamup.download.ServerInfo, connective.teamup.download.db.DatabaseOperation, org.apache.commons.fileupload.FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items)
		throws ActionException
	{
		return getUserDefaultView(req, serverInfo);
	}

	/**
	 * Returns the appropriate page for the user's default or most recent
	 * Trading Partner List view.
	 */
	public static String getUserDefaultView(HttpServletRequest req, ServerInfo serverInfo)
	{
		String nextPage = "tplist.search";
		
		String view = serverInfo.getTPListView(req.getSession());
		if (view.equals(ServerInfo.TPLIST_ALL))
			nextPage = "tplist.all";
		else if (view.equals(ServerInfo.TPLIST_SEARCH))
			nextPage = "tplist.search";
		else if (view.equals(ServerInfo.TPLIST_ALPHA))
			nextPage = "tplist.alpha";
		else if (view.equals(ServerInfo.TPLIST_AGENT_ID))
			nextPage = "tplist.agentid";
		
		return nextPage;
	}

}
