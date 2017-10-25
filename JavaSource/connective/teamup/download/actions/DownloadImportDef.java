package connective.teamup.download.actions;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.Action;
import connective.teamup.download.ActionException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.agentimport.ImportDefinition;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author haneym
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class DownloadImportDef implements Action
{
	private static final Logger LOGGER = Logger.getLogger(DownloadImportDef.class);
	/**
	 * Constructor for DownloadAck.
	 */
	public DownloadImportDef()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.Action#perform(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws ActionException 
	{
		try
		{
			// parameters
			String agentsStr = req.getParameter("agents");
			String partsStr = req.getParameter("participants");
			
			ImportDefinition def = new ImportDefinition();
			if ("Y".equals(agentsStr))
				def.setFileContainsAgents(true);
			else
				def.setFileContainsAgents(false); 

			if ("Y".equals(partsStr))
				def.setFileContainsParticipants(true);
			else
				def.setFileContainsParticipants(false); 
			def.loadFromProperties(op);
			
			resp.setContentType("application/xml");
			def.saveToXML(resp.getWriter());
		}
		catch (SQLException e)
		{
			LOGGER.error("Error loading import definition", e);
			throw new ActionException("Error loading import definition", e);
		}
		catch (IOException e)
		{
			LOGGER.error("Error writing import definition to XML", e);
			throw new ActionException("Error writing import definition to XML", e);
		}
		
		return null;
	}

}
