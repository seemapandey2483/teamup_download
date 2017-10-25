package connective.teamup.download.actions;
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
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.ExcludeLob;

public class TradingPartnerParticipantLob implements Action {
	
	private static final Logger LOGGER = Logger.getLogger(TradingPartnerParticipantLob.class);
	
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items)
			throws ActionException		{
		
		HashMap<String, Object> JSONROOT = new HashMap<String, Object>();

		String method = req.getParameter("method");
		String jsonArray ="";
		Gson gson = new Gson();
		List<ExcludeLob> exLst = null;
		try {
			
			if("exlist".equals(method)) {
				exLst = new ArrayList<ExcludeLob>();
				String agentId = req.getParameter("agentId");
				exLst = op.getExcludeLobList(agentId);
				JSONROOT.put("Result", "OK");
				JSONROOT.put("Records", exLst);
				jsonArray = gson.toJson(JSONROOT);
			}else if("ex_update".equals(method)) {
				String lobid = req.getParameter("Ids");
				String type = req.getParameter("type");;
				String agentId = "";
				String lobId;
				String excludeId;
				if(lobid != null) {
					String[] ids = lobid.split(",");
					for (String str:ids){
						String[] temp = str.split("::");
						lobId = temp[0];
						agentId = temp[1];
						excludeId = temp[2];
						if("exclude".equals(type)){
							op.insertExcludeLOB(new Long(lobId), agentId,new Long(excludeId));									
						}else{
							op.removeFromExcludeLob(new Long(lobId), agentId, new Long(excludeId));
						}
					}
				}
			/*	exLst = new ArrayList<ExcludeLob>();
				exLst = op.getExcludeLobList(agentId);
				JSONROOT.put("Result", "OK");
				JSONROOT.put("Records", exLst);
				jsonArray = gson.toJson(JSONROOT);*/
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
