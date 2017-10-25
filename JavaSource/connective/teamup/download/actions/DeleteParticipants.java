package connective.teamup.download.actions;

import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import com.google.gson.Gson;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.CarrierInfo;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.DuplicateParticipantException;
import connective.teamup.download.db.ParticipantInfo;

/**
 * @author	Anand Alok
 *
 * Action bean to save participant codes for a trading partner
 * from the Carrier Admin pages.
 */
public class DeleteParticipants implements Action
{
	private static final Logger LOGGER = Logger.getLogger(DeleteParticipants.class);
	/**
	 * Constructor for SaveParticipants.
	 */
	public DeleteParticipants()
	{
		super();
	}
	
	/**
	 * @see connective.teamup.download.Action#perform(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException
	{
		HashMap<String, Object> JSONROOT = new HashMap<String, Object>();
		String nextPage = TradingPartnerListView.getUserDefaultView(req, serverInfo);
		String jsonArray ="";
		Gson gson = new Gson();
		
		String[] secondSplit;
		String[] firstSplit;
		
		String agentId ="";
		String fileName ="";
		try
		{
			
			String ids = req.getParameter("ids");
			if(ids != null ) {
				firstSplit = ids.split(",");
				for(String s:firstSplit) {
					if(s!= null && s.contains(":")) {
						secondSplit = s.split(":");
						agentId = secondSplit[0];
						fileName = secondSplit[1];
						if(agentId != null && fileName!= null)
							op.deletePartcipant(agentId, fileName);
						
					}
					
				}
				JSONROOT.put("Result","OK");
				
			}
			
		}
		catch (SQLException e)
		{
			LOGGER.error(e);
			JSONROOT.put("Result","ERROR");
			JSONROOT.put("Message", e.getStackTrace().toString());
			jsonArray = gson.toJson(JSONROOT);
			throw new ActionException("Error saving participant code settings", e);
		}finally {
			return jsonArray;

		}
		
	}

}
