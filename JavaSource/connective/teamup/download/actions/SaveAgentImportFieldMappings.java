/*
 * 04/11/2005 - Created
 */
package connective.teamup.download.actions;

import java.util.ArrayList;

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
 * Action bean to save the list of field mappings used for importing agent info 
 * from a comma-delimited file.
 * 
 * @author Kyle McCreary
 */
public class SaveAgentImportFieldMappings implements Action
{
	private static final Logger LOGGER = Logger.getLogger(SaveAgentImportFieldMappings.class);
	/* (non-Javadoc)
	 * @see connective.teamup.download.Action#perform(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, connective.teamup.download.ServerInfo, connective.teamup.download.db.DatabaseOperation, org.apache.commons.fileupload.FileItem[])
	 */
	public String perform(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items)
		throws ActionException
	{
		String nextPage = "import.agents.confirm";
		try
		{
			// Parse the import parameters from the request
			String agentsFlag = req.getParameter("agents");
			String participantsFlag = req.getParameter("participants");
			ImportDefinition def = new ImportDefinition();
			def.setFileContainsAgents(agentsFlag != null && agentsFlag.equals("Y"));
			def.setFileContainsParticipants(participantsFlag != null && participantsFlag.equals("Y"));
			
			// Parse the field mappings from the request
			boolean mapVendors = false;
			int vendorField = -1;
			ArrayList fieldList = new ArrayList();
			String[] mappings = req.getParameterValues("fieldmap");
			if (mappings != null)
			{
				for (int i=0; i < mappings.length; i++)
				{
					fieldList.add(mappings[i]);
					if (mappings[i] != null && mappings[i].equals("AMSID"))
					{
						mapVendors = true;
						if (vendorField < 0)
							vendorField = i;
					}
				}
			}
			
			if (mapVendors)
			{
				// Navigate next page to map vendor system IDs
				nextPage = "import.agents.map.vendors";
				req.setAttribute("vendor.field.mapping", String.valueOf(vendorField));
			}
			
			// Save the field mappings to the properties table
			def.setFieldList(fieldList);
			def.saveToProperties(op);
		}
		catch (Exception e)
		{
			LOGGER.error("Error saving agent import field mappings", e);
//			e.printStackTrace(System.out);
			throw new ActionException("Error saving agent import field mappings", e);
		}
		
		return nextPage;
	}

}
