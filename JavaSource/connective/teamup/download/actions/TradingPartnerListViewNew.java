package connective.teamup.download.actions;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import com.google.gson.Gson;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.CarrierInfo;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.Agent;
import connective.teamup.download.db.DatabaseOperation;

/**
 * Action bean used to determine which view of the Trading Partner List to display.
 * 
 * @author kmccreary
 */
public class TradingPartnerListViewNew implements Action
{
	private static final Logger LOGGER = Logger.getLogger(TradingPartnerListViewNew.class);
	/**
	 * Constructor for TradingPartnerListView.
	 */
	public TradingPartnerListViewNew()
	{
		super();
	}
	private CarrierInfo carrierInfo = null;

	/* (non-Javadoc)
	 * @see connective.teamup.download.Action#perform(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, connective.teamup.download.ServerInfo, connective.teamup.download.db.DatabaseOperation, org.apache.commons.fileupload.FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items)
		throws ActionException
	{
		 HashMap<String, Object> JSONROOT = new HashMap<String, Object>();

		String actionType = req.getParameter("actionType");
		if(!"JSON".equalsIgnoreCase(actionType)) 
			return getUserDefaultView(req, serverInfo);
		
		String jsonArray ="";
		Gson gson = new Gson();
		carrierInfo = serverInfo.getCarrierInfo();
		int startPageIndex = 1;
		int recordsPerPage = 0;
		List<Agent> lstAgent = new ArrayList<Agent>();
		boolean keyLinkFile = false;
		String sortBy = "AGENTID";
		
		String searchAgentId = "";
		String searchAgentName = "";
		String searchAgentIdH = "";
		String searchAgentNameH = "";
		
		try {

			if (carrierInfo.getImportFileCreator().equalsIgnoreCase("KEYLINK"))
				keyLinkFile = true;
			if(req.getParameter("jtStartIndex") != null)
				startPageIndex = Integer.parseInt(req.getParameter("jtStartIndex"));
			else
				startPageIndex = 1;
			if(req.getParameter("jtPageSize") != null)
				recordsPerPage = Integer.parseInt(req.getParameter("jtPageSize"));
			else
				recordsPerPage = 10;
			if(req.getParameter("jtSorting") != null)
			 sortBy = req.getParameter("jtSorting");
			
			if(sortBy== null || "".equals(sortBy)) {
				sortBy = "AGENTID ASC";
				
			} else{
				sortBy = Agent.getColumName(sortBy);
			}
			int start = startPageIndex + 1;
			int end = recordsPerPage + startPageIndex;
			
			searchAgentId = req.getParameter("searchAgentId");
			if (searchAgentId != null) {
				req.getSession().setAttribute("tempId", searchAgentId);
			}
			if(searchAgentId == null || "".equals(searchAgentId)) {
				if (req.getSession().getAttribute("agentId") != null) {
					if (req.getSession().getAttribute("tempId") != null) {
						searchAgentId = (String)req.getSession().getAttribute("agentId");
						req.getSession().removeAttribute("tempId");
					} else if (req.getSession().getAttribute("tempId") == null && req.getSession().getAttribute("tempIdH") == null){
						searchAgentId = null;
					} else {
						searchAgentId = (String)req.getSession().getAttribute("agentId");
					}
				}
				req.getSession().removeAttribute("agentId");
			}
			
			searchAgentIdH = (String)req.getSession().getAttribute("searchAgentIdH");
			searchAgentNameH = (String)req.getSession().getAttribute("searchAgentNameH");
			
			searchAgentName = req.getParameter("searchAgentName");
			if(searchAgentName == null || "".equals(searchAgentName))
				searchAgentName = (String)req.getSession().getAttribute("searchAgentName");
			
			//lstAgent = op.getAllAgents(start, end, sortBy,keyLinkFile,searchAgentId,searchAgentName, menu_action);
			if (searchAgentIdH != null && !"".equals(searchAgentIdH)) {
				req.getSession().setAttribute("tempIdH", searchAgentIdH);
			}
			if (searchAgentIdH == null) {
				if (req.getSession().getAttribute("tempIdH") != null) {
					searchAgentIdH = (String)req.getSession().getAttribute("agentId");
					req.getSession().removeAttribute("tempIdH");
				}
			}
			if (searchAgentIdH != null) {
				searchAgentId = searchAgentIdH;
			}
			if (searchAgentNameH != null && !"".equals(searchAgentNameH)) {
				searchAgentName = searchAgentNameH;
			}
			
			String separateAgent = (String)req.getSession().getAttribute("separateAgent");
			
			if(separateAgent!= null && !"".equals(separateAgent)){
				searchAgentId = separateAgent;
			}
			req.getSession().removeAttribute("separateAgent");
			
			lstAgent = op.getAllAgents(start, end, sortBy,keyLinkFile,searchAgentId,searchAgentName);
			
			//int count = op.getAgentCount(searchAgentId,searchAgentName, menu_action);
			int count = op.getAgentCount(searchAgentId,searchAgentName);
			JSONROOT.put("Result", "OK");
			JSONROOT.put("Records", lstAgent);
			JSONROOT.put("TotalRecordCount", count);
			 jsonArray = gson.toJson(JSONROOT);


		}catch(SQLException se) {
			LOGGER.error(se);
			
			JSONROOT.put("ERROR", se.toString());
			jsonArray = gson.toJson(JSONROOT);
			throw new ActionException("Error retrieving agent list", se);
		}catch(Exception se) {
			LOGGER.error(se);
			JSONROOT.put("ERROR", se.toString());
			jsonArray = gson.toJson(JSONROOT);
			throw new ActionException("Error retrieving agent list", se);
		}
		
		
		return jsonArray;
	}

	/**
	 * Returns the appropriate page for the user's default or most recent
	 * Trading Partner List view.
	 */
	public static String getUserDefaultView(HttpServletRequest req, ServerInfo serverInfo)
	{
		String nextPage = "tplist.new";
		
		req.getSession().removeAttribute("searchAgentId");
		req.getSession().removeAttribute("searchAgentName");
		
		req.getSession().removeAttribute("searchAgentIdH");
		req.getSession().removeAttribute("searchAgentNameH");
		
		if(req.getParameter("searchAgentId")!=null && !"".equals(req.getParameter("searchAgentId"))){
			req.getSession().setAttribute("searchAgentId", req.getParameter("searchAgentId"));			
		}else if(req.getParameter("searchAgentIdHome")!=null && !"".equals(req.getParameter("searchAgentIdHome"))){
			req.getSession().setAttribute("searchAgentIdH", req.getParameter("searchAgentIdHome"));
		}
		if(req.getParameter("searchAgentName")!=null && !"".equals(req.getParameter("searchAgentName"))){
			req.getSession().setAttribute("searchAgentName", req.getParameter("searchAgentName"));			
		}else if(req.getParameter("searchAgentNameHome")!=null && !"".equals(req.getParameter("searchAgentNameHome"))){
			req.getSession().setAttribute("searchAgentNameH", req.getParameter("searchAgentNameHome"));
		}
	
		/*String view = serverInfo.getTPListView(req.getSession());
		if (view.equals(ServerInfo.TPLIST_ALL))
			nextPage = "tplist.all";
		else if (view.equals(ServerInfo.TPLIST_SEARCH))
			nextPage = "tplist.search";
		else if (view.equals(ServerInfo.TPLIST_ALPHA))
			nextPage = "tplist.alpha";
		else if (view.equals(ServerInfo.TPLIST_AGENT_ID))
			nextPage = "tplist.agentid";*/
		
		return nextPage;
	}

}
