package connective.teamup.download.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;
import org.json.JSONArray;

import com.google.gson.Gson;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.LineOfBusiness;

public class LOBController implements Action {
	
	private static final Logger LOGGER = Logger.getLogger(LOBController.class);
	
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items)
			throws ActionException		{
		
		HashMap<String, Object> JSONROOT = new HashMap<String, Object>();

		String method = req.getParameter("method");
		String jsonArray ="";
		Gson gson = new Gson();
		
		String actionType = req.getParameter("actionType");
		if(!"JSON".equalsIgnoreCase(actionType)) 
			return getUserDefaultView(req, serverInfo);
		try{
			
			if ("lobList".equals(method)){
				List<LineOfBusiness> lobList = new ArrayList<LineOfBusiness>();
				lobList = op.getLineOfBusiness();
				JSONROOT.put("Result", "OK");
				JSONROOT.put("Records", lobList);
				//JSONROOT.put("TotalRecordCount", count);
				// Convert Java Object to Json
				 jsonArray = gson.toJson(JSONROOT);
			} else if ("lobCodeList".equals(method)) {
				List<LineOfBusiness> lobCList = new ArrayList<LineOfBusiness>();
				lobCList = op.getCLineOfBusiness();
				JSONArray json = new JSONArray(lobCList);
				if (json != null)
					jsonArray = json.toString();
			} else if("updateActive".equals(method)) {
				String lobIds = (String) req.getParameter("ids");
				String active = (String) req.getParameter("active");
				op.activateDeactiveLOB(active, lobIds);
			}else if("lobDelete".equals(method)) {
				String lobId = (String) req.getParameter("id");
				op.deleteLOB(lobId);
				JSONROOT.put("Result", "OK");
				jsonArray = gson.toJson(JSONROOT);
			}else if ("lobCreate".equals(method) || "lobUpdate".equals(method)){
				
				LineOfBusiness lineOfBusiness = new LineOfBusiness();
				String lobCode1 = req.getParameter("code");
				boolean returnLobInfo = op.getLobInfo(req.getParameter("code"));
				if (returnLobInfo && "lobCreate".equals(method)) {
					String msg = "LineOfBusiness [" + lobCode1 +"] already Exist";
					req.getSession().setAttribute("errorMessage", msg);
					
					JSONROOT.put("Result", "ERROR");
					JSONROOT.put("Message", "LOB: " +lobCode1 +"already exist");
					jsonArray = gson.toJson(JSONROOT);
					
				} else {
					String id =  req.getParameter("id");
					
					if (req.getParameter("active")!=null){
						String active = (String) req.getParameter("active");
						if("true".equals(active))
						lineOfBusiness.setActive(true);
						else
							lineOfBusiness.setActive(false);
					}
					if (req.getParameter("code")!=null){
						String lobCode = (String) req.getParameter("code");
						lineOfBusiness.setCode(lobCode);
					}
					if (req.getParameter("description")!=null){
						String lobDesc = (String) req.getParameter("description");
						lineOfBusiness.setDescription(lobDesc);
					}
					if("lobCreate".equals(method))
						op.createLOB(lineOfBusiness);
					
					else
						op.updateLOB(lineOfBusiness, id);
					
					JSONROOT.put("Result", "OK");
					JSONROOT.put("Record", lineOfBusiness);
					jsonArray = gson.toJson(JSONROOT);

				}
			}
		}catch(Exception ex) {
			LOGGER.error(ex);
			JSONROOT.put("Result", "ERROR");
			JSONROOT.put("Message", ex.getMessage());
			jsonArray = gson.toJson(JSONROOT);
		}
		return jsonArray;
	}
	public static String getUserDefaultView(HttpServletRequest req, ServerInfo serverInfo)
	{
		return "loblist.new";
	}
}
