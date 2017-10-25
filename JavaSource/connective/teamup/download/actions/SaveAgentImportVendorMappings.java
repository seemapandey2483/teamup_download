/*
 * 04/11/2005 - Created
 */
package connective.teamup.download.actions;

import java.util.HashMap;

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
 * Action bean to save the list of vendor system ID mappings used for importing 
 * agent info from a comma-delimited file.
 * 
 * @author Kyle McCreary
 */
public class SaveAgentImportVendorMappings implements Action
{
	private static final Logger LOGGER = Logger.getLogger(SaveAgentImportVendorMappings.class);
	/* (non-Javadoc)
	 * @see connective.teamup.download.Action#perform(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, connective.teamup.download.ServerInfo, connective.teamup.download.db.DatabaseOperation, org.apache.commons.fileupload.FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items)
		throws ActionException
	{
		String nextPage = "import.agents.confirm";
		try
		{
			// Parse the vendor system ID mappings from the request
			HashMap vendors = new HashMap();
			String[] importedIds = req.getParameterValues("importedId");
			String[] mappings = req.getParameterValues("vendormap");
			if (mappings != null && importedIds != null)
			{
				for (int i=0; i < importedIds.length; i++)
				{
					String amsId = importedIds[i];
					if (i < mappings.length)
						amsId = mappings[i];
					vendors.put(importedIds[i], amsId);
				}
			}
			
			// Save the field mappings to the properties table
			// Parse the import parameters from the request
			String agtsFlag = req.getParameter("agents");
			String partFlag = req.getParameter("participants");
			ImportDefinition def = new ImportDefinition();
			def.setFileContainsAgents(agtsFlag != null && agtsFlag.equals("Y"));
			def.setFileContainsParticipants(partFlag != null && partFlag.equals("Y"));
			def.loadFromProperties(op);
			def.setAmsMap(vendors);
			def.saveToProperties(op);
		}
		catch (Exception e)
		{
			LOGGER.error("Error saving agent import vendor system mappings", e);
			e.printStackTrace(System.out);
			throw new ActionException("Error saving agent import vendor system mappings", e);
		}
		
		return nextPage;
	}

}
