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
import connective.teamup.download.dao.StatesDao;
import connective.teamup.download.db.AmsOptionBean;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.StateOptionBean;

public class BeanListAction implements Action {
	
	private static final Logger LOGGER = Logger.getLogger(BeanListAction.class);

	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items)
			throws ActionException		{
		HashMap<String, Object> JSONROOT = new HashMap<String, Object>();
		String method = req.getParameter("method");
		String jsonArray ="";
		Gson gson = new Gson();
		try{
			if("slist".equals(method)) {
				List<StateOptionBean> lstCustomer=new ArrayList<StateOptionBean>();
				lstCustomer=StatesDao.getStateList();
				JSONROOT.put("Result", "OK");
				JSONROOT.put("Options", lstCustomer);
				 jsonArray = gson.toJson(JSONROOT);
			}else if("amslist".equals(method)) {
				List<AmsOptionBean> lstCustomer=new ArrayList<AmsOptionBean>();
				lstCustomer=op.geAllAmsBeanList();
				JSONROOT.put("Result", "OK");
				JSONROOT.put("Options", lstCustomer);
				jsonArray = gson.toJson(JSONROOT);

			}
			else if ("agentList".equals(method)) {
				List<String> list = new ArrayList<String>();
				list = op.getAgentsName();
				JSONArray json = new JSONArray(list);
				if(json != null)
					jsonArray = json.toString();
			}
			else if ("participantList".equals(method)) {
				List<String> list = new ArrayList<String>();
				list = op.getParticipantsName();
				JSONArray json = new JSONArray(list);
				if(json != null)
					jsonArray = json.toString();
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
