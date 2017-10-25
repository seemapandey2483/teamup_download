package connective.teamup.download.actions;

import java.io.IOException;
import java.io.PrintWriter;

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

public class FormValidator implements Action {
	
	private static final Logger LOGGER = Logger.getLogger(FormValidator.class);
	
	public FormValidator() {
		super();
	}
	
	@Override
	public String perform(HttpServletRequest req, HttpServletResponse resp,
			ServerInfo serverInfo, DatabaseOperation op, FileItem[] items)
			throws ActionException {
		boolean findAgent = true;
		boolean findLOB = true;
		boolean findParticipant = true;
		PrintWriter out = null;
		Gson gson = new Gson();
		String jsonArray ="";
		
		try {
			if (req.getParameter("Edit-agentId") != null && req.getParameter("Edit-agentId") != " ") {
				
				AgentInfo existingAgent = op.getAgentInfo(req.getParameter("Edit-agentId").toUpperCase());
				
				if (existingAgent != null) {
					findAgent = false;
				}
				
				Object[] ret = new Object[3];
				ret[0] = "Edit-agentId";
				ret[1] = findAgent;
				
				resp.setContentType("application/json");;
				out = resp.getWriter();
				out.write(gson.toJson(ret));
			}
			
			if (req.getParameter("Edit-code") != null && req.getParameter("Edit-code") != " ") {
				
				boolean foundLOB = op.getLobInfo(req.getParameter("Edit-code").toUpperCase());
				
				if (foundLOB)
					findLOB = false;
				
				Object[] ret = new Object[3];
				ret[0] = "Edit-code";
				ret[1] = findLOB;
				
				resp.setContentType("application/json");;
				out = resp.getWriter();
				out.write(gson.toJson(ret));
			}
			
			if (req.getParameter("Edit-participantCode") != null && req.getParameter("Edit-participantCode") != " ") {
				
				boolean foundParticipant = op.getPartInfo(req.getParameter("Edit-participantCode").toUpperCase());
				
				if (foundParticipant)
					findParticipant = false;
				
				Object[] ret = new Object[3];
				ret[0] = "Edit-participantCode";
				ret[1] = findParticipant;
				
				resp.setContentType("application/json");;
				out = resp.getWriter();
				out.write(gson.toJson(ret));
			}

			return jsonArray;
			
		} catch (IOException e) {
			LOGGER.error(e);
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
			return jsonArray;
		}
	}

}
