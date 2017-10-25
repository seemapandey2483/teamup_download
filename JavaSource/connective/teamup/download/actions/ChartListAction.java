package connective.teamup.download.actions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import com.ebix.utility.date.DateUtility;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.LobCountReport;
import connective.teamup.download.db.LobDistribution;

public class ChartListAction implements Action {
	
	private static final Logger LOGGER = Logger.getLogger(ChartListAction.class);

	private static final String AQUA_COLOR ="#00FFFF";
	private static final String BLUE_COLOR ="#0000FF";
	private static final String CHARTER_COLOR ="#7FFF00";
	private static final String CORAL_COLOR ="#FF7F50";
	private static final String CORNFLOWERBLUE_COLOR ="#6495ED";
	private static final String CRIMSON_COLOR ="#DC143C";
	private static final String DARKBLUE_COLOR ="#00008B";
	private static final String DARKORANGE_COLOR ="#FF8C00";
	private static final String DEEPSKYBLUE_COLOR ="#00BFFF";
	private static final String GOLD_COLOR ="#FFD700";
	private static final String INDIGO_COLOR ="#4B0082";
	private static final String WHEAT_COLOR ="#F5DEB3";
	private static final String HOT_PINK="#FF69B4";
	private static final String LIME="#00FF00";
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
	
	private static final List<String> colorList = new ArrayList<String>();
	{
		colorList.add(CORAL_COLOR);
		colorList.add(CORNFLOWERBLUE_COLOR);
		colorList.add(CHARTER_COLOR);
		colorList.add(WHEAT_COLOR);
		colorList.add(DARKORANGE_COLOR);
		colorList.add(GOLD_COLOR);
		colorList.add(HOT_PINK);
		colorList.add(LIME);
		colorList.add(AQUA_COLOR);
		colorList.add(BLUE_COLOR);
		
		
		
		colorList.add(CRIMSON_COLOR);
		colorList.add(DARKBLUE_COLOR);
		
		colorList.add(DEEPSKYBLUE_COLOR);
		
		colorList.add(INDIGO_COLOR);
		
	}
	private Long[] getReportFrequency(String type) {
		Long [] dates = new Long[2];
		if("D".equalsIgnoreCase(type)) {
			
		}
		return dates;
	}

	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items)
			throws ActionException		{
		HashMap<String, Object> JSONROOT = new HashMap<String, Object>();
		String method = req.getParameter("method");
		String jsonArray ="";
		Gson gson = new Gson();
		Long dateRange[] = null;
		String searchFromDate = "";
		String searchToDate = "";
		String cAgentId = "";
		String eventType ="";
		Long fromMillSeconds = null;
		Long toMillSeconds = null;
		try{
			
			String reportType = req.getParameter("reportType");
			
			searchFromDate = req.getParameter("frmReportDate");
			searchToDate = req.getParameter("toReportDate");
			cAgentId = req.getParameter("cAgentId");
			eventType = req.getParameter("eventType");
			
			if(searchFromDate != null  && searchFromDate.length()>=10) {
				Date datefrom  = sdf.parse(searchFromDate +" 00:00:00");
				fromMillSeconds = datefrom.getTime();	
			}
			if(searchToDate != null && searchToDate.length()>=10) {
				if(searchToDate.equals(searchFromDate)){
					searchToDate = searchToDate +" 23:59:00";
				}else{
					searchToDate = searchToDate +" 00:00:00";
				}
				Date dateTo = sdf.parse(searchToDate);
				toMillSeconds = dateTo.getTime();
			}
			if(fromMillSeconds != null && toMillSeconds!= null){
				reportType ="";
				dateRange = new Long[2];
				dateRange[0]= fromMillSeconds;
				dateRange[1]= toMillSeconds;
			}else{
					if(reportType== null || "".equals(reportType))
						reportType ="Y";
					dateRange = new Long[2];
					dateRange = DateUtility.getDateRange(reportType);
				
			}
			if(eventType== null || "".equals(eventType))
				eventType ="D";
			
			//Thread.sleep(3000);
			if("FileReportCount".equals(method)) {
				
				
				List<LobCountReport> reportList = op.getFileTypeReprtCount(dateRange,cAgentId,eventType);	
				JsonObject reportObject = null;
				List<JsonObject> reportDataList = new ArrayList<JsonObject>();
				int counter =0;		
				if(reportList== null || reportList.size()==0){
					reportObject = new JsonObject();
					reportObject.addProperty("type", "");
					reportObject.addProperty("count", 0);
					reportObject.addProperty("color", colorList.get(counter));
					reportDataList.add(reportObject);
				} else{
					for(LobCountReport report:reportList) {
						reportObject = new JsonObject();
						reportObject.addProperty("type", report.getLob());
						reportObject.addProperty("count", report.getCount());
						reportObject.addProperty("color", colorList.get(counter));
						reportDataList.add(reportObject);
						counter =counter+1;
					}
					
				}
             
				 jsonArray = gson.toJson(reportDataList);
			}
			
			if("EventeReportCount".equals(method)) {
				
				
				List<LobCountReport> reportList = op.getEventReprtCount(dateRange,cAgentId);	

				List<JsonObject> reportDataList = new ArrayList<JsonObject>();
				JsonObject reportObject = null;
				
				int counter =0;
				if(reportList== null || reportList.size()==0){
					reportObject = new JsonObject();
					reportObject.addProperty("type", "");
					reportObject.addProperty("count", 0);
					reportObject.addProperty("color", colorList.get(counter));
					reportDataList.add(reportObject);
				} else{
					for(LobCountReport report:reportList) {
						reportObject = new JsonObject();
						reportObject.addProperty("type", report.getLob());
						reportObject.addProperty("count", report.getCount());
						reportObject.addProperty("color", colorList.get(counter));
						reportDataList.add(reportObject);
						counter =counter+1;
					}
				}
			            
				 jsonArray = gson.toJson(reportDataList);
			}
			if("LoBReprtCount".equals(method)) {
				
				
				List<LobCountReport> reportList = op.getLoBReprtCount(dateRange,cAgentId,eventType);	

				List<JsonObject> reportDataList = new ArrayList<JsonObject>();
				JsonObject reportObject = null;
				
				int counter =0;
				if(reportList== null || reportList.size()==0){
					reportObject = new JsonObject();
					reportObject.addProperty("lob", "");
					reportObject.addProperty("count", 0);
					reportObject.addProperty("color", colorList.get(counter));
					reportDataList.add(reportObject);
				} else{
					for(LobCountReport report:reportList) {
						reportObject = new JsonObject();
						reportObject.addProperty("lob", report.getLob());
						reportObject.addProperty("count", report.getCount());
						reportObject.addProperty("color", colorList.get(counter));
						reportDataList.add(reportObject);
						counter =counter+1;
					}
				}
			            
				 jsonArray = gson.toJson(reportDataList);
			}
				
				if("LoBDistributionReport".equals(method)) {
					
					LobDistribution lobDist = op.getLoBDistributionReportList(dateRange,cAgentId,eventType);
					
					
					List<JsonObject> reportDataList = new ArrayList<JsonObject>();
					List<String> lobList = lobDist.getLob();
					JsonObject reportObject = null;
					
					for(String lob1:lobList){
						reportObject = new JsonObject();
						reportObject.addProperty("lob", lob1);
						reportDataList.add(reportObject);
					}
					List<String> txnList = lobDist.getTxn();
					
					for(String txn1:txnList){
						reportObject = new JsonObject();
						reportObject.addProperty("txn", txn1);
						reportDataList.add(reportObject);
					}
					
					
					List<String> dataList = lobDist.getDatalist();
					
					for(String data:dataList){
						reportObject = new JsonObject();
						reportObject.addProperty("datalist", data);
						reportDataList.add(reportObject);
					}
					String dataset ="1,2,3,4#8,7,6,5#10,11,12,13#90,91,92,93";
					JsonObject piedata1  = new JsonObject();
	                piedata1.addProperty("count", txnList.size());
	                piedata1.addProperty("dataset", dataset);
	                reportDataList.add(piedata1);
	                jsonArray = gson.toJson(reportDataList);
	                

					
				}
		}catch(Exception ex) {
			LOGGER.error(ex);
			JSONROOT.put("Result", "ERROR");
			JSONROOT.put("Message", ex.getMessage());
			jsonArray = gson.toJson(JSONROOT);
		}
		return jsonArray;
	}
}