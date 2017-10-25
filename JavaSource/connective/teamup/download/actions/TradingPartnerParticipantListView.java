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
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.Participants;

public class TradingPartnerParticipantListView implements Action {
	
	private static final Logger LOGGER = Logger.getLogger(TradingPartnerParticipantListView.class);
	
	/**
	 * Constructor for TradingPartnerListView.
	 */
	public TradingPartnerParticipantListView()
	{
		super();
	}

	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items)
			throws ActionException	{
		
		HashMap<String, Object> JSONROOT = new HashMap<String, Object>();

		Gson gson = new Gson();
		List<Participants> partLst = new ArrayList<Participants>();
		String agentId = req.getParameter("agentId");
		String jsonArray ="";

		try {
			String sortBy = req.getParameter("jtSorting");
			
			if(sortBy== null || "".equals(sortBy)) {
				sortBy = "PARTCODE DSC";
				
			} else{
				if(sortBy.contains("participantCode")){
					String[] str = sortBy.split(" ");
					String sort = "PARTCODE";
					sortBy = sort +" " + str[1];
				}
			}
			partLst = op.getPartcipants(agentId,sortBy);
			JSONROOT.put("Result", "OK");
			JSONROOT.put("Records", partLst);
			jsonArray = gson.toJson(JSONROOT);
		}
		catch(SQLException se) {
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
}
