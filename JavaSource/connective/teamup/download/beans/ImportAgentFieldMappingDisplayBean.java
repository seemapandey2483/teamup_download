/*
 * 04/08/2005 - Created
 */
package connective.teamup.download.beans;

import java.io.Serializable;
import java.util.ArrayList;

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
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.DistributedFileInfo;

/**
 * Display bean used for displaying the list of field mappings used for importing
 * agent info from a comma-delimited file.
 * 
 * @author Kyle McCreary
 */
public class ImportAgentFieldMappingDisplayBean implements DisplayBean, Serializable
{
	private static final Logger LOGGER = Logger.getLogger(ImportAgentFieldMappingDisplayBean.class);
	
	private CarrierInfo carrierInfo = null;

	private String batchNumber = null;
	private String agentsFlag = null;
	private String participantsFlag = null;
	private String overwriteFlag = null;
	private String carFlag = null;
	private ArrayList fieldData = null;
	private ArrayList fieldNames = null;
	private ArrayList fieldNameLabels = null;
	private ArrayList fieldNameOptions = null;


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
			String action = null;
			if (items == null)
			{
				batchNumber = req.getParameter("batchnum");
				agentsFlag = req.getParameter("agents");
				participantsFlag = req.getParameter("participants");
				overwriteFlag = req.getParameter("overwrite");
				carFlag = req.getParameter("carProcess");
			}
			else
			{
				for (int i=0; i < items.length; i++)
				{
					if (items[i].isFormField())
					{
						String name = items[i].getFieldName();
						if (name == null)
						{
							// do nothing
						}
						else if (name.length() > 9 && name.substring(0, 9).equals("batchnum_"))
							batchNumber = name.substring(9);
						else if (name.equals("agents"))
							agentsFlag = items[i].getString();
						else if (name.equals("participants"))
							participantsFlag = items[i].getString();
						else if (name.equals("overwrite"))
							overwriteFlag = items[i].getString();
						else if (name.equals("carProcess"))
							carFlag = items[i].getString();
					}
				}
			}

			// Parse the first line of the comma-delimited file to show as
			// sample text for mapping to agent/participant table fields
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
			
			// Load any previously saved field mappings
			ImportDefinition def = new ImportDefinition();
			def.setFileContainsAgents(getAgentsFlag().equals("Y"));
			def.setFileContainsParticipants(getParticipantsFlag().equals("Y"));
			def.setOverwriteExisting(getOverwriteFlag().equals("Y"));
			def.loadFromProperties(op);
			fieldNames = def.getFieldList();
			
			// Load the field mapping options list
			fieldNameLabels = new ArrayList();
			fieldNameOptions = new ArrayList();
			addMapping(" (no mapping) ", "NONE");
			addMapping("Agent ID", "AGENTID");
			if (def.getFileContainsAgents())
			{
				// Add agent table fields
				addMapping("Agent Name", "NAME");
				if (def.getFileContainsParticipants())
				{
					// Add participants table fields
					addMapping("Agent Sub-Code / Participant Code", "PARTCODE");
					if (carrierInfo.getImportFileIdMode().equals(CarrierInfo.IDMODE_FILENAME))
						addMapping("Import Filename", "KEYLINK_FILE");
				}
				addMapping("Agent Contact Name", "CONTACT_NAME");
				addMapping("Agent Contact Email", "CONTACT_EMAIL");
				addMapping("Agent Contact Phone", "CONTACT_PHONE");
				addMapping("Agent Location State", "LOC_STATE");

				//addMapping("Agent Default Download Filename", "DEFAULT_FILENAME");
				//addMapping("Agent Download Files to Directory", "REMOTE_DIR");
				addMapping("Agent Enabled / Active", "ACTIVE_FLAG");
				//addMapping("Agent Live Downloads with TEAM-UP", "LIVE_FLAG");
				//addMapping("Agent Test Agent", "TEST_AGT");
				addMapping("Agent Vendor System ID", "AMSID");
				addMapping("Agent Vendor System Version", "AMSVER");
				//addMapping("AgentCreated Date", "CREATE_DT");
				//addMapping("Agent Date First Registered with TEAM-UP", "REGISTERED_DT");
				//addMapping("Agent Last Download Date", "LASTDL_DT");
				//addMapping("Agent Last Login Date", "LAST_LOGIN_DT");
				//addMapping("Agent Interactive Download Mode", "INTERACTIVE_FLAG");
				//addMapping("Agent Registered with Client App", "CLIENT_APP");
				addMapping("Agent Registered with TEAM-UP Download", "REGISTERED_FLAG");
				//addMapping("Agent Registration/Invitation Email Count", "REG_EMAIL_COUNT");
				addMapping("Agent Status", "STATUS");
				addMapping("Agent City", "CITY");
				addMapping("Agent Password", "PASSWORD");
				addMapping("Agent ZIP", "ZIP");
				//addMapping("Agent State", "STATE");
				//addMapping("Agent STATEID", "STATEID");
				addMapping("Agent CLIENT REG", "CLIENT_REG");
				addMapping("Agent DEST ADDRESS", "DESTADDRESS");
				//addMapping("Agent REMOTE CLAIM DIR", "REMOTECLAIMDIR");
				//addMapping("Agent REMOTE POLICY DIR", "REMOTEPOLICYDIR");
				//addMapping("Agent DEFAULT CLAIM FILE NAME", "DEFAULTCLAIMFILENAME");
				//addMapping("Agent DEFAULT POLICY FILE NAME", "DEFAULTPOLICYFILENAME");
				
				addMapping("Participant AGENCY NAME", "AGENCYNAME");
				addMapping("Participant CONTACT NAME", "CONTACTNAME");
				addMapping("Participant CONTACT PHONE", "CONTACTPHONE");
				//addMapping("Participant Agent ADDRESS1", "ADDRESS1");
				//addMapping("Participant ADDRESS2", "ADDRESS2");
				addMapping("Participant City", "p_CITY");
				addMapping("Participant DEST ADDRESS", "AGENTDESTADDRESS");
				addMapping("Participant EMAIL", "EMAIL");
				addMapping("Participant CONTACTEMAIL", "CONTACTEMAIL");
				addMapping("Participant AGENCYLOCATION", "AGENCYLOCATION");
				addMapping("Participant AMSPARTCODE", "AMSPARTCODE");
				addMapping("Participant ISCONTACTPRIMARY", "ISCONTACTPRIMARY");
				addMapping("Participant STATEID", "p_STATEID");
				addMapping("Participant ZIP", "p_ZIP");
				addMapping("Participant Status", "p_STATUS");
				
			}
			else if (def.getFileContainsParticipants())
			{
				// Add participants table fields
				addMapping("Agent Sub-Code / Participant Code", "PARTCODE");
				if (carrierInfo.getImportFileIdMode().equals(CarrierInfo.IDMODE_FILENAME))
					addMapping("Import Filename", "KEYLINK_FILE");
			}
		}
		catch (Exception e)
		{
			LOGGER.error(e);
//			e.printStackTrace(System.out);
			throw new DisplayBeanException("Error occurred building the agency import field mappings page", e);
		}
	}
	
	private void addMapping(String label, String value)
	{
		fieldNameLabels.add(label);
		fieldNameOptions.add(value);
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
		String name = null;
		if (fieldNames != null && index < fieldNames.size())
			name = (String) fieldNames.get(index);
		if (name == null || name.trim().equals(""))
			name = "NONE";
		
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
	public String getFieldNameLabel(int index)
	{
		if (index >= fieldNameLabels.size())
			return "";
		return (String) fieldNameLabels.get(index);
	}

	/**
	 * @return
	 */
	public String getFieldNameOption(int index)
	{
		if (index >= fieldNameOptions.size())
			return "";
		return (String) fieldNameOptions.get(index);
	}

	public int getFieldNameOptionsCount()
	{
		return fieldNameOptions.size();
	}

	public boolean isFieldNameOptionSelected(int fieldIndex, int optionIndex)
	{
		String fieldValue = getFieldName(fieldIndex);
		String optionValue = getFieldNameOption(optionIndex);
		return fieldValue.equals(optionValue);
	}

	public String getCarFlag()
	{
		return carFlag;
	}

}
