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
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author Kyle McCreary
 *
 * Action bean used to delete an existing trading partner and all dependent records.
 */
public class DeleteTradingPartner implements Action
{
	private static final Logger LOGGER = Logger.getLogger(DeleteTradingPartner.class);
	/**
	 * Constructor for DeleteTradingPartner.
	 */
	public DeleteTradingPartner()
	{
		super();
	}
	
	
	/**
	 * @see connective.teamup.download.Action#perform(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException
	{
		HashMap<String, Object> JSONROOT = new HashMap<String, Object>();
		String agentID = req.getParameter("agentId");
		Gson gson = new Gson();
		String jsonArray = "";
		try
		{
			if (agentID != null && agentID.trim().length() > 0)
			{
				AgentInfo agent = op.getAgentInfo(agentID.trim());
				agent.delete();
			}
		}
		catch (SQLException e)
		{
			LOGGER.error(e);
			JSONROOT.put("ERROR", e.getStackTrace().toString());
			jsonArray = gson.toJson(JSONROOT);
			throw new ActionException("Error deleting agent info", e);

		}
		JSONROOT.put("Result", "OK");
		jsonArray = gson.toJson(JSONROOT);

		return jsonArray;
	}

}
