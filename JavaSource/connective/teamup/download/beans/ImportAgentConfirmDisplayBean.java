/*
 * 04/11/2005 - Created
 */
package connective.teamup.download.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.CarrierInfo;
import connective.teamup.download.DisplayBean;
import connective.teamup.download.DisplayBeanException;
import connective.teamup.download.Escape;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.agentimport.CommaDelimitedFileReader;
import connective.teamup.download.agentimport.ImportDefinition;
import connective.teamup.download.db.AmsInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.DistributedFileInfo;

/**
 * Display bean used for displaying the list of field and vendor system ID 
 * mappings used for importing agent info from a comma-delimited file.
 * 
 * @author Kyle McCreary
 */
public class ImportAgentConfirmDisplayBean implements DisplayBean, Serializable
{
	private static final Logger LOGGER = Logger.getLogger(ImportAgentConfirmDisplayBean.class);
	
	private CarrierInfo carrierInfo = null;

	private String batchNumber = null;
	private String agentsFlag = null;
	private String participantsFlag = null;
	private String overwriteFlag = null;
	private String carFlag = null;
	private String saveXmlUrl = null;
	private boolean vendorMappingIncluded = false;
	private ArrayList fieldData = new ArrayList();
	private ArrayList fieldNames = new ArrayList();
	private ArrayList importedIds = new ArrayList();
	private ArrayList vendorNames = new ArrayList();


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
			
			// Create the URL for saving the import parameters to XML
			saveXmlUrl = serverInfo.getRequestUrl(req) +
						 "?action=dl_importdef&agents=" + getAgentsFlag() +
						 "&participants=" + getParticipantsFlag();
			
			// Create the list of field mappings using table field descriptions
			HashMap hash = new HashMap();
			hash.put("NONE",             " (no mapping defined) ");
			hash.put("AGENTID",          "Agent ID");
			hash.put("NAME",             "Agent Name");
			hash.put("PARTCODE",         "Agent Sub-Code / Participant Code");
			hash.put("KEYLINK_FILE",     "Import Filename");
			hash.put("CONTACT_NAME",     "Contact Name");
			hash.put("CONTACT_EMAIL",    "Contact Email");
			hash.put("CONTACT_PHONE",    "Contact Phone");
			hash.put("DEFAULT_FILENAME", "Default Download Filename");
			hash.put("REMOTE_DIR",       "Download Files to Directory");
			hash.put("ACTIVE_FLAG",      "Enabled / Active");
			hash.put("LIVE_FLAG",        "Live Downloads with TEAM-UP");
			hash.put("PASSWORD",         "Password");
			hash.put("AMSID",            "Vendor System ID");
			hash.put("AMSVER",           "Vendor System Version");
			hash.put("LASTDL_DT",        "Last Download Date");
			hash.put("LAST_LOGIN_DT",    "Last Login Date");
			hash.put("INTERACTIVE_FLAG", "Interactive Download Mode");
			hash.put("REGISTERED_FLAG",  "Registered with TEAM-UP Download");
			hash.put("TEST_AGT",         "Test Agent");
			hash.put("STATUS",           "Status");
			hash.put("CLIENT_APP",       "Registered with Client App");
			hash.put("CREATED_DT",       "Created Date");
			hash.put("REGISTERED_DT",    "Date Agent First Registered w/ TEAM-UP");
			hash.put("LOC_STATE",        "Agent Location State");
			hash.put("REG_EMAIL_COUNT",  "Registration/Invitation Email Count");
			hash.put("CITY",    "City");
			hash.put("STATEID",        "State Id");
			hash.put("ZIP",  "ZIP");
			hash.put("DESTADDRESS",       "Destination Address");
			hash.put("REMOTECLAIMDIR",       "Remote Claim Dir");
			hash.put("REMOTEPOLICYDIR",    "Remote Policy Dir");
			hash.put("DEFAULTCLAIMFILENAME",        "Default Claim File Name");
			hash.put("DEFAULTPOLICYFILENAME",  "Default Policy File Name");
			//participants table confirmation
			hash.put("CONTACTPHONE",       "Participant Contact Phone");
			hash.put("CONTACTNAME",       "Participant Contact name");
			hash.put("AGENCYNAME",       "Participant Agency Name");
			hash.put("p_STATUS",       "Participant Status");
			hash.put("ADDRESS1",       "Participant Address1");
			hash.put("ADDRESS2",    "Participant Address2");
			hash.put("p_CITY",        "Participant City");
			hash.put("State",  "Participant State");
			hash.put("p_ZIP",       "Participant Zip");
			hash.put("EMAIL",       "Participant Email");
			hash.put("AGENCYLOCATION",    "Participant Agency Location");
			hash.put("ISCONTACTPRIMARY",        "Participant Is Contact Primary");
			hash.put("AGENTDESTADDRESS",  "Participant Agent Destination Address");
			hash.put("CONTACTEMAIL",       "Participant Contact Email");
			hash.put("p_STATEID",       "Participant State Id");
			hash.put("AMSPARTCODE",        "Participant AMS Part Code");
			// Load the saved field mappings
			ImportDefinition def = new ImportDefinition();
			def.setFileContainsAgents(getAgentsFlag().equals("Y"));
			def.setFileContainsParticipants(getParticipantsFlag().equals("Y"));
			def.setOverwriteExisting(getOverwriteFlag().equals("Y"));
			def.loadFromProperties(op);
			ArrayList fieldList = def.getFieldList();
			for (int i=0; i < fieldList.size(); i++)
			{
				String name = (String) fieldList.get(i);
				if (name == null || name.equals(""))
					name = "NONE";
				else if (name.equals("AMSID"))
					vendorMappingIncluded = true;
				String desc = (String) hash.get(name);
				if (desc == null || desc.equals(""))
					desc = (String) hash.get("NONE");
				fieldNames.add(desc);
			}
			
			// Parse the first line of the comma-delimited file to show as
			// sample text for displaying the field mappings
			fieldData = new ArrayList();
			if (batchNumber != null && !batchNumber.equals(""))
			{
				DistributedFileInfo[] files = op.getDistributedFilesForBatch(Integer.parseInt(batchNumber));
				if (files != null && files.length > 0)
				{
					CommaDelimitedFileReader file = 
							new CommaDelimitedFileReader(files[0].getFileContents());
					while (file.readLine() != null)
					{
						// parse the comma-delimited data
						String[] fields = file.getFieldData();
						if (fields != null)
						{
							for (int i=0; i < fields.length; i++)
							{
								String data = "&nbsp; &lt;empty field&gt;";
								if (fields[i] != null && !fields[i].trim().equals(""))
									data = Escape.forHtml(fields[i]);
								fieldData.add(data);
							}
							break;
						}	
					}
				}
			}
			
			if (vendorMappingIncluded)
			{
				// Load the vendor system mapping tables
				HashMap amsHash = new HashMap();
				AmsInfo[] vendors = op.getAmsInfoList();
				for (int i=0; i < vendors.length; i++)
					amsHash.put(vendors[i].getId(), vendors[i].getDisplayName());
				
				HashMap vendorHash = op.getVendorIdMappings();
				Iterator it = vendorHash.keySet().iterator();
				while (it.hasNext())
				{
					String id = (String) it.next();
					if (id != null && !id.equals(""))
						importedIds.add(id);
				}
				Collections.sort(importedIds);
				
				for (int i=0; i < importedIds.size(); i++)
				{
					String id = (String) importedIds.get(i);
					String map = (String) vendorHash.get(id);
					if (map != null && !map.equals(""))
						map = (String) amsHash.get(map);
					if (map == null || map.equals(""))
						map = " (no mapping defined) ";
					vendorNames.add(map);
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error occurred while building the agency import mappings confirmation page", e);
//			e.printStackTrace(System.out);
			throw new DisplayBeanException("Error occurred while building the agency import mappings confirmation page", e);
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

	public int getFieldCount()
	{
		return fieldData.size();
	}

	/**
	 * @return
	 */
	public String getFieldData(int index)
	{
		String data = "";
		if (index < fieldData.size())
		{
			data = (String) fieldData.get(index);
			if (data == null)
				data = "";
		}
		return data;
	}

	/**
	 * @return
	 */
	public String getFieldName(int index)
	{
		String name = "";
		if (fieldNames != null && index < fieldNames.size())
			name = (String) fieldNames.get(index);
		
		return name;
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
	public String getImportedId(int index)
	{
		if (index >= importedIds.size())
			return "";
		return (String) importedIds.get(index);
	}

	public int getVendorCount()
	{
		return importedIds.size();
	}

	/**
	 * @return
	 */
	public String getVendorName(int index)
	{
		if (index >= vendorNames.size())
			return " (no mapping defined) ";
		return (String) vendorNames.get(index);
	}

	/**
	 * @return
	 */
	public boolean isVendorMappingIncluded()
	{
		return vendorMappingIncluded;
	}

	/**
	 * @return
	 */
	public String getSaveXmlUrl()
	{
		if (saveXmlUrl == null)
			return "";
		return saveXmlUrl;
	}

	/**
	 * @return
	 */
	public String getCarFlag()
	{
		return carFlag;
	}

}
