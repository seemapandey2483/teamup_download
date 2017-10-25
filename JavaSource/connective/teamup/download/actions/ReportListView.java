package connective.teamup.download.actions;

import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import com.google.gson.Gson;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.LogTransInfoBO;

public class ReportListView implements Action{

	public static Logger log =Logger.getLogger(ReportListView.class);

	/**
	 * Constructor for TradingPartnerListView.
	 */
	public ReportListView()
	{
		super();
	}

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
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");

	
		Long fromMillSeconds = null;
		Long toMillSeconds = null;
		Long fromActMillSeconds = null;
		Long toActMillSeconds = null;
		InputStream in = null;
		ServletOutputStream out = null;
		List<LogTransInfoBO> lstLog = new ArrayList<LogTransInfoBO>();
	
		String method = req.getParameter("method");
		try {

			
			if("replist".equals(method)) {
				String srchClaimNumber = req.getParameter("srchClaimNumber");
				String srchClmPolicyNumber = req.getParameter("srchClmPolicyNumber");
				String srchclaimStatus = req.getParameter("srchclaimStatus");
				String searchAgentId = req.getParameter("searchAgentId");
				
				String frmClaimFileDate = req.getParameter("frmClaimFileDate");
				String toClaimFileDate = req.getParameter("toClaimFileDate");
				
				String frmClaimEventDate = req.getParameter("frmClaimEventDate");
				String toClaimEventDate = req.getParameter("toClaimEventDate");
				int startPageIndex = 1;
				int recordsPerPage = 0;
				startPageIndex = Integer.parseInt(req.getParameter("jtStartIndex"));
				recordsPerPage = Integer.parseInt(req.getParameter("jtPageSize"));
				int start = startPageIndex + 1;
				int end = recordsPerPage + startPageIndex;
				String sortBy = req.getParameter("jtSorting");
				
				if(sortBy== null || "".equals(sortBy)) {
					sortBy = "EVENT_DATE DESC";
					
				} else{
					sortBy = LogTransInfoBO.getColumName(sortBy);
				}
				
				
				if(frmClaimFileDate != null  && frmClaimFileDate.length()>=10) {
					Date datefrom  = sdf.parse(frmClaimFileDate +" 00:00:00");
					fromMillSeconds = datefrom.getTime();	
				}

				if(toClaimFileDate != null && toClaimFileDate.length()>=10) {
					if(toClaimFileDate.equals(frmClaimFileDate)){
						toClaimFileDate = toClaimFileDate +" 23:59:00";
					}else{
						toClaimFileDate = toClaimFileDate +" 00:00:00";
					}
					Date dateTo = sdf.parse(toClaimFileDate);
					toMillSeconds = dateTo.getTime();
				}
				
				
				if(frmClaimEventDate != null  && frmClaimEventDate.length()>=10) {
					Date datefrom  = sdf.parse(frmClaimEventDate +" 00:00:00");
					fromActMillSeconds = datefrom.getTime();	
				}

				if(toClaimEventDate != null && toClaimEventDate.length()>=10) {
					if(toClaimEventDate.equals(frmClaimEventDate)){
						toClaimEventDate = toClaimEventDate +" 23:59:00";
					}else{
						toClaimEventDate = toClaimEventDate +" 00:00:00";
					}
					Date dateTo = sdf.parse(toClaimEventDate);
					toActMillSeconds = dateTo.getTime();
				}
				
				lstLog = op.getTransactionDetails(start,end,sortBy,srchClaimNumber,srchClmPolicyNumber,srchclaimStatus,searchAgentId,fromMillSeconds,toMillSeconds,fromActMillSeconds,toActMillSeconds);
				int count =  op.getTransactionDetailsCount(srchClaimNumber,srchClmPolicyNumber,srchclaimStatus,searchAgentId,fromMillSeconds,toMillSeconds,fromActMillSeconds,toActMillSeconds);
				JSONROOT.put("Result", "OK");
				JSONROOT.put("Records", lstLog);
				JSONROOT.put("TotalRecordCount", count);	
				 jsonArray = gson.toJson(JSONROOT);
			}
			
		}catch(SQLException se) {
			log.error(se);
			JSONROOT.put("ERROR", se.toString());
			jsonArray = gson.toJson(JSONROOT);
			log.error("Error retrieving getTransactionDetails list" +se.getMessage());
			throw new ActionException("Error retrieving Log list", se);
		}catch(Exception se) {
			log.error(se);
			JSONROOT.put("ERROR", se.toString());
			jsonArray = gson.toJson(JSONROOT);
			log.error("Error retrieving getTransactionDetails list" +se.getMessage());
			throw new ActionException("Error retrieving Log list", se);
		}
		
		
		return jsonArray;
	}

	/**
	 * Returns the appropriate page for the user's default or most recent
	 * Trading Partner List view.
	 */
	public static String getUserDefaultView(HttpServletRequest req, ServerInfo serverInfo)
	{
		String nextPage = "reportlist";
		
		return nextPage;
	}

}
