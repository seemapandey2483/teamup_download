package connective.teamup.download.actions;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import com.google.gson.Gson;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.services.LicenceService;

public class LicenceServiceAction implements Action {
	
	private static final Logger LOGGER = Logger.getLogger(LicenceServiceAction.class);

	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items)
			throws ActionException		{
		HashMap<String, Object> JSONROOT = new HashMap<String, Object>();
		String method = req.getParameter("method");
		String jsonArray ="";
		Gson gson = new Gson();
		try{
			if("claimAct".equals(method)) {
				boolean isClaimActivated = LicenceService.isClaimActivated(op) ;
				if(isClaimActivated)
					JSONROOT.put("isClaimActivated", "Y");
				else
					JSONROOT.put("isClaimActivated", "N");
				jsonArray = gson.toJson(JSONROOT);
			}		}catch(Exception ex) {
				LOGGER.error(ex);
			JSONROOT.put("Result", "ERROR");
			JSONROOT.put("Message", ex.getMessage());
			jsonArray = gson.toJson(JSONROOT);
		}
		return jsonArray;
	}
}
