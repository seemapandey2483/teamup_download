/*
 * 04/11/2005 - Created
 */
package connective.teamup.download.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.CarrierInfo;
import connective.teamup.download.DisplayBean;
import connective.teamup.download.DisplayBeanException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.agentimport.CommaDelimitedFileReader;
import connective.teamup.download.agentimport.ImportDefinition;
import connective.teamup.download.db.AmsInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.DistributedFileInfo;

/**
 * Display bean used for displaying the list of vendor system ID mappings used 
 * for importing agent info from a comma-delimited file.
 * 
 * @author Kyle McCreary
 */
public class ImportAgentVendorMappingDisplayBean implements DisplayBean, Serializable
{
	private static final Logger LOGGER = Logger.getLogger(ImportAgentVendorMappingDisplayBean.class);
	
	private CarrierInfo carrierInfo = null;

	private String batchNumber = null;
	private String agentsFlag = null;
	private String participantsFlag = null;
	private String overwriteFlag = null;
	private String carFlag = null;
	private ArrayList importedIds = new ArrayList();
	private ArrayList amsIds = new ArrayList();
	private ArrayList vendorLabels = new ArrayList();
	private ArrayList vendorOptions = new ArrayList();


	/* (non-Javadoc)
	 * @see connective.teamup.download.DisplayBean#init(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, connective.teamup.download.ServerInfo, connective.teamup.download.db.DatabaseOperation, org.apache.commons.fileupload.FileItem[])
	 */
	public void init(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items)
		throws DisplayBeanException
	{
		try
		{
			// Load the carrier info
			carrierInfo = serverInfo.getCarrierInfo();
			
			// Parse the import parameters from the request
			batchNumber = req.getParameter("batchnum");
			agentsFlag = req.getParameter("agents");
			participantsFlag = req.getParameter("participants");
			overwriteFlag = req.getParameter("overwrite");
			carFlag = req.getParameter("carProcess");
			
			// Load any previously saved vendor ID mappings
			ImportDefinition def = new ImportDefinition();
			def.setFileContainsAgents(getAgentsFlag().equals("Y"));
			def.setFileContainsParticipants(getParticipantsFlag().equals("Y"));
			def.setOverwriteExisting(getOverwriteFlag().equals("Y"));
			def.loadFromProperties(op);
			HashMap amsHash = def.getAmsMap();

			// Loop through the comma-delimited file to get the list of vendor
			// IDs to be mapped
			int vendorField = Integer.parseInt((String)req.getAttribute("vendor.field.mapping"));
			if (batchNumber != null && !batchNumber.equals(""))
			{
				DistributedFileInfo[] files = op.getDistributedFilesForBatch(Integer.parseInt(batchNumber));
				if (files != null && files.length > 0)
				{
					Hashtable hash = new Hashtable();
					CommaDelimitedFileReader file = 
							new CommaDelimitedFileReader(files[0].getFileContents());
					while (file.readLine() != null)
					{
						// parse the comma-delimited data
						String[] fields = file.getFieldData();
						if (fields != null && fields.length > vendorField)
						{
							String id = fields[vendorField];
							if (id != null && !id.equals("") && hash.get(id) == null)
							{
								importedIds.add(id);
								hash.put(id, id);
							}
						}	
					}
				}
			}
			
			// Sort the import vendor IDs alphabetically and get any existing mappings
			if (importedIds.size() > 1)
				Collections.sort(importedIds);
			for (int i=0; i < importedIds.size(); i++)
			{
				String id = (String) importedIds.get(i);
				String map = (String) amsHash.get(id);
				if (map == null || map.equals(""))
					amsIds.add(id);
				else
					amsIds.add(map);
			}
			
			// Load the vendor mapping options list
			AmsInfo[] ams = op.getAmsInfoList();
			vendorLabels.add(" (no mapping) ");
			vendorOptions.add("");
			for (int i=0; i < ams.length; i++)
			{
				vendorLabels.add(ams[i].getDisplayName());
				vendorOptions.add(ams[i].getId());
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error occurred building the agency import vendor system mappings page", e);
//			e.printStackTrace(System.out);
			throw new DisplayBeanException("Error occurred building the agency import vendor system mappings page", e);
		}
	}

	/**
	 * @return
	 */
	public String getAgentsFlag()
	{
		if (agentsFlag == null)
			return "";
		return agentsFlag;
	}

	/**
	 * @return
	 */
	public String getBatchNumber()
	{
		return batchNumber;
	}

	/**
	 * @return
	 */
	public CarrierInfo getCarrierInfo()
	{
		return carrierInfo;
	}

	/**
	 * @return
	 */
	public String getImportId(int index)
	{
		String id = "";
		if (index < importedIds.size())
		{
			id = (String) importedIds.get(index);
			if (id == null)
				id = "";
		}
		return id;
	}

	public int getMappingCount()
	{
		return importedIds.size();
	}

	/**
	 * @return
	 */
	public String getOverwriteFlag()
	{
		if (overwriteFlag == null)
			return "";
		return overwriteFlag;
	}

	/**
	 * @return
	 */
	public String getParticipantsFlag()
	{
		if (participantsFlag == null)
			return "";
		return participantsFlag;
	}

	/**
	 * @return
	 */
	public String getVendorLabel(int index)
	{
		if (index >= vendorLabels.size())
			return "";
		return (String) vendorLabels.get(index);
	}

	/**
	 * @return
	 */
	public String getVendorOption(int index)
	{
		if (index >= vendorOptions.size())
			return "";
		return (String) vendorOptions.get(index);
	}

	public int getVendorOptionsCount()
	{
		return vendorOptions.size();
	}

	public boolean isVendorOptionSelected(int idIndex, int optionIndex)
	{
		String id = "";
		if (idIndex < amsIds.size())
		{
			id = (String) amsIds.get(idIndex);
			if (id == null)
				id = "";
		}
		String optionValue = getVendorOption(optionIndex);
		return id.equals(optionValue);
	}

	/**
	 * @return
	 */
	public String getCarFlag()
	{
		return carFlag;
	}

}
