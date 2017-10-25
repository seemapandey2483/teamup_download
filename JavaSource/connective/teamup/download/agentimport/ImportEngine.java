/*
 * Created on Apr 8, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package connective.teamup.download.agentimport;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.AmsInfo;
import connective.teamup.download.db.DatabaseObject;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.DuplicateParticipantException;
import connective.teamup.download.db.ParticipantInfo;

/**
 * @author haneym
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ImportEngine 
{
	private static final Logger LOGGER = Logger.getLogger(ImportEngine.class);
	
	protected ImportDefinition def = null;
	protected int agentIdIndex = -1;
	protected int partCodeIndex = -1;
	protected ArrayList newAgentList = null;


	public ImportEngine(ImportDefinition def)
	{
		super();
		
		this.def = def;
		
		this.newAgentList = new ArrayList();
	}
	
	public ImportResult[] doImport(DatabaseOperation op, InputStream is) throws IOException
	{
		ArrayList results = new ArrayList();
		CommaDelimitedFileReader reader = new CommaDelimitedFileReader(is);
		
		// read through the file 1 record at a time	
		String record = reader.readLine();
		while (record != null)
		{
			// get the field data
			String[] fields = reader.getFieldData();
			for (int i=0; i < fields.length; i++)
				fields[i] = fields[i].trim();

			// create the result
			ImportResult result = new ImportResult(record);
			
			// lookup the agent ID
			String agentId = getAgentId(fields);

			// add agent, participant, or both
			try
			{
				if (def.getFileContainsAgents() && def.getFileContainsParticipants())
				{
					// lookup the agent
					boolean agentExists = false;
					AgentInfo agentInfo = op.getAgentInfo(agentId);
					if (agentInfo == null)
					{
						// create the agent
						agentInfo = createAgent(op, agentId);
						updateAgentFields(op, agentInfo, fields, false);
					}
					else
					{
						agentExists = true;
						if (def.getOverwriteExisting())
							updateAgentFields(op, agentInfo, fields, true);
					}
					
					// lookup the participant
					String partCode = getParticipantCode(fields);
					ParticipantInfo partInfo = null;
					for (int i=0; i < agentInfo.getParticipantCount(); i++)
					{
						ParticipantInfo tmp = agentInfo.getParticipant(i);
						if (tmp.getParticipantCode().equals(partCode))
						{
							partInfo = tmp;
							break;
						}
					}
					
					if (partInfo == null)
					{
						// create the participant
						partInfo = agentInfo.createParticipant();
						partInfo.setParticipantCode(partCode);
						partInfo.setFilename(partCode);
						updateParticipantFields(partInfo, fields);
						result.setError(false);
						result.setMessage("Participant " + partCode + " created for " + 
							(agentExists ? "existing" : "new") + " agent " + agentId);
					}
					else if (def.getOverwriteExisting())
					{
						// update the participant
						updateParticipantFields(partInfo, fields);
						result.setError(false);
						result.setMessage("Participant " + partCode + " updated for agent " + agentId);
					}
					else
					{
						// error
						result.setError(true);
						result.setMessage("Duplicate Participant Code " + partCode + 
							" for Agent " + agentId);
					}
				}
				else if (def.getFileContainsAgents())
				{
					// add agent only
					// lookup the agent
					AgentInfo agentInfo = op.getAgentInfo(agentId);
					if (agentInfo == null)
					{
						// create the agent
						agentInfo = createAgent(op, agentId);
						updateAgentFields(op, agentInfo, fields, false);
						result.setError(false);
						result.setMessage("Agent " + agentId + " created.");
					}
					else if (def.getOverwriteExisting()) 
					{
						updateAgentFields(op, agentInfo, fields, true);
						result.setError(false);
						result.setMessage("Existing agent " + agentId + " updated.");
					}
					else
					{
						// error
						result.setError(true);
						result.setMessage("Agent " + agentId + " already exists.");
					}
				}
				else if (def.getFileContainsParticipants())
				{
					// add participant only

					// lookup the agent
					AgentInfo agentInfo = op.getAgentInfo(agentId);
					if (agentInfo == null)
					{
						// error
						result.setError(true);
						result.setMessage("Could not create participant for nonexistent agent " + agentId);
					}
					else
					{
						// lookup the participant
						String partCode = getParticipantCode(fields);
						ParticipantInfo partInfo = null;
						for (int i=0; i < agentInfo.getParticipantCount(); i++)
						{
							ParticipantInfo tmp = agentInfo.getParticipant(i);
							if (tmp.getParticipantCode().equals(partCode))
							{
								partInfo = tmp;
								break;
							}
						}
						
						if (partInfo == null)
						{
							// create the participant
							partInfo = agentInfo.createParticipant();
							partInfo.setParticipantCode(partCode);
							partInfo.setFilename(partCode);
							updateParticipantFields(partInfo, fields);
							result.setError(false);
							result.setMessage("Participant " + partCode + " created for agent " + agentId);
						}
						else if (def.getOverwriteExisting())
						{
							// update the participant
							updateParticipantFields(partInfo, fields);
							result.setError(false);
							result.setMessage("Participant " + partCode + " updated for agent " + agentId);
						}
						else
						{
							// error
							result.setError(true);
							result.setMessage("Duplicate Participant Code " + partCode + 
								" for Agent " + agentId);
						}
					}
				}
			}
			catch (Exception e)
			{
				LOGGER.error(e);
				result.setError(true);
				result.setMessage(e.getMessage());			
			}
			
			// add the result
			results.add(result);
			
			// read the next record
			record = reader.readLine();
		}
		
		// create the return array
		ImportResult[] ret = new ImportResult[results.size()];
		results.toArray(ret);
		
		return ret;
	}
	
	/*
	 * Returns the agent id from the mapped fields
	 */
	protected String getAgentId(String[] fields)
	{
		String ret = null;
		
		// find the mapped field
		if (agentIdIndex == -1)
		{		
			for (int i=0; i < def.getFieldList().size(); i++)
			{
				String column = (String) def.getFieldList().get(i);
				if (column.equals(DatabaseObject.colAgentId))
				{
					agentIdIndex = i;
					break;
				}
			}
		}
		
		if (agentIdIndex >= 0 && agentIdIndex < fields.length)
			ret = fields[agentIdIndex];
		
		return ret;
	}
	
	/*
	 * Returns the participant code from the mapped fields
	 */
	protected String getParticipantCode(String[] fields)
	{
		String ret = null;
		
		// find the mapped field
		if (partCodeIndex == -1)
		{		
			for (int i=0; i < def.getFieldList().size(); i++)
			{
				String column = (String) def.getFieldList().get(i);
				if (column.equals(DatabaseObject.colPartCode))
				{
					partCodeIndex = i;
					break;
				}
			}
		}
		
		if (partCodeIndex < fields.length)
			ret = fields[partCodeIndex];
		
		return ret;
	}
	
	protected AgentInfo createAgent(DatabaseOperation op, String agentId) throws SQLException, DuplicateParticipantException
	{
		AgentInfo agentInfo = null;
		agentInfo = op.createAgentInfo(agentId);
		agentInfo.setStatus(AgentInfo.STATUS_INACTIVE);
		agentInfo.setInteractive(true);
		agentInfo.setAms(null);
		agentInfo.setAmsVer("");
		agentInfo.setRemoteDir("");
		agentInfo.setContactPhone("");
		agentInfo.setContactName("");
		agentInfo.setContactEmail("");
		agentInfo.setDefaultFilename("");
		agentInfo.setName("");
		agentInfo.setPassword(getRandomPassword());
		agentInfo.setTestAgent(false);
		agentInfo.setClientAppRegistered(false);
		agentInfo.save();
		
		// create the default participant code
		ParticipantInfo part = agentInfo.createParticipant();
		part.setParticipantCode(agentId);
		System.out.println("Saving default participant code for agent " + agentId);
		part.save();
//		System.out.println("Finished saving default participant code for agent " + agentId);
		
		// Save the agent to the list of new agents for group processing
		newAgentList.add(agentInfo);
		
		return agentInfo;
	}
	
	protected void updateAgentFields(DatabaseOperation op, AgentInfo agentInfo, String[] fields, boolean updating) throws SQLException
	{
	
		for (int i=0; i < def.getFieldList().size(); i++)
		{
		    String fieldName = (String) def.getFieldList().get(i);
		    if (fields.length > i )
		    {
				if (fieldName.equals(DatabaseObject.colAgentActiveFlag) && updating)
				{
					if (fields[i].equalsIgnoreCase("Y") || fields[i].equalsIgnoreCase("Yes") || 
						fields[i].equalsIgnoreCase("T") || fields[i].equalsIgnoreCase("True"))
					{
						agentInfo.setActive(true);
					}
					else if (fields[i].equalsIgnoreCase("N") || fields[i].equalsIgnoreCase("No") || 
						fields[i].equalsIgnoreCase("F") || fields[i].equalsIgnoreCase("False"))
					{
						agentInfo.setActive(false);
					}
				}
				else if (fieldName.equalsIgnoreCase(DatabaseObject.colAgentAmsId))
				{
					AmsInfo amsInfo = op.getAmsInfo((String) def.getAmsMap().get(fields[i]));
					if (amsInfo != null)
					{
						agentInfo.setAms(amsInfo);
					}
				}
				else if (fieldName.equalsIgnoreCase(DatabaseObject.colAgentAmsVersion))
				{
					agentInfo.setAmsVer(maxLength(fields[i], 10));
				}
				else if (fieldName.equalsIgnoreCase(DatabaseObject.colAgentContactEmail))
				{
					agentInfo.setContactEmail(maxLength(fields[i], 50));
				}
				else if (fieldName.equalsIgnoreCase(DatabaseObject.colAgentContactName))
				{
					agentInfo.setContactName(maxLength(fields[i], 30));
				}
				else if (fieldName.equalsIgnoreCase(DatabaseObject.colAgentContactPhone))
				{
					agentInfo.setContactPhone(maxLength(loseChars(fields[i], "()-.Xx []"), 20));
				}
				else if (fieldName.equalsIgnoreCase(DatabaseObject.colAgentDefaultFilename))
				{
					agentInfo.setDefaultFilename(maxLength(fields[i], 50));
				}
				else if (fieldName.equalsIgnoreCase(DatabaseObject.colAgentLiveFlag) && updating)
				{
					if (fields[i].equalsIgnoreCase("Y") || fields[i].equalsIgnoreCase("Yes") || 
						fields[i].equalsIgnoreCase("T") || fields[i].equalsIgnoreCase("True"))
					{
						agentInfo.setLive(true);
					}
					else if (fields[i].equalsIgnoreCase("N") || fields[i].equalsIgnoreCase("No") || 
						fields[i].equalsIgnoreCase("F") || fields[i].equalsIgnoreCase("False"))
					{
						agentInfo.setLive(false);
					}
				}
				else if (fieldName.equalsIgnoreCase(DatabaseObject.colAgentName))
				{
					agentInfo.setName(maxLength(fields[i], 50));
				}
				else if (fieldName.equalsIgnoreCase(DatabaseObject.colAgentPassword))
				{
					agentInfo.setPassword(maxLength(fields[i], 32));
				}
				else if (fieldName.equalsIgnoreCase(DatabaseObject.colAgentRemoteDir))
				{
					agentInfo.setRemoteDir(maxLength(fields[i], 255));
				}
				else if (fieldName.equalsIgnoreCase(DatabaseObject.colAgentDestAddress))
				{
					agentInfo.setAgentDestAddress(maxLength(fields[i], 255));
				}
				else if (fieldName.equalsIgnoreCase(DatabaseObject.colAgentTestFlag))
				{
					agentInfo.setTestAgent(fields[i] != null && fields[i].trim().equalsIgnoreCase("Y"));
				}
				else if (fieldName.equalsIgnoreCase(DatabaseObject.colAgentStatus))
				{
					try
					{
						int s = Integer.parseInt(fields[i]);
						if (s >= 0 && s < 10)
							agentInfo.setStatus(s);
					} catch (Exception e) {
						LOGGER.error(e);
					}
				}
				else if (fieldName.equals(DatabaseObject.colAgentClientAppFlag))
				{
					if (fields[i].equalsIgnoreCase("Y") || fields[i].equalsIgnoreCase("Yes") || 
							fields[i].equalsIgnoreCase("T") || fields[i].equalsIgnoreCase("True"))
						{
							agentInfo.setClientAppRegistered(true);
						}
						else if (fields[i].equalsIgnoreCase("N") || fields[i].equalsIgnoreCase("No") || 
							fields[i].equalsIgnoreCase("F") || fields[i].equalsIgnoreCase("False"))
						{
							agentInfo.setClientAppRegistered(false);
						}
				}
				else if (fieldName.equalsIgnoreCase(DatabaseObject.colAgentCity))
				{
					agentInfo.setCity(maxLength(fields[i], 60));
				}
				/*else if (fieldName.equalsIgnoreCase(DatabaseObject.colAgentClaimRemoteDir))
				{
					agentInfo.setRemoteClaimDir(maxLength(fields[i], 255));
				}
				else if (fieldName.equalsIgnoreCase(DatabaseObject.colAgentDefaultClaimFilename))
				{
					agentInfo.setDefaultClaimFilename(maxLength(fields[i], 50));
				}
				else if (fieldName.equalsIgnoreCase(DatabaseObject.colAgentDefaultPolicyFilename))
				{
					agentInfo.setDefaultPolicyFilename(maxLength(fields[i], 50));
				}*/
				else if (fieldName.equalsIgnoreCase(DatabaseObject.colAgentLastDlDate))
				{
					agentInfo.setLastDownloadDate(Long.parseLong(maxLength(fields[i], 10)));
				}
				else if (fieldName.equalsIgnoreCase(DatabaseObject.colAgentLastLoginDate))
				{
					agentInfo.setLastLoginDate(Long.parseLong(maxLength(fields[i], 255)));
				}
				else if (fieldName.equalsIgnoreCase(DatabaseObject.colAgentLocationState))
				{
					agentInfo.setLocationState(maxLength(fields[i], 2));
				}
				/*else if (fieldName.equalsIgnoreCase(DatabaseObject.colAgentPolicyRemoteDir))
				{
					agentInfo.setRemotePolicyDir(maxLength(fields[i], 255));
				}*/
				else if (fieldName.equalsIgnoreCase(DatabaseObject.colAgentRegistrationEmailCount))
				{
					agentInfo.setRegistrationEmailCount(Integer.parseInt(maxLength(fields[i], 1)));
				}
				else if (fieldName.equalsIgnoreCase(DatabaseObject.colAgentStateId))
				{
					agentInfo.setStateId(maxLength(fields[i], 5));
				}
				else if (fieldName.equalsIgnoreCase(DatabaseObject.colAgentZip))
				{
					agentInfo.setZip(maxLength(fields[i], 10));
				}
			}
		}
		agentInfo.save();
	}
	
	protected String maxLength(String data, int len)
	{
		if (data == null || data.length() <= len)
			return data;
		return data.substring(0, len);
	}
	
	protected String loseChars(String data, String remove)
	{
		if (data == null || remove == null || remove.equals(""))
			return data;
		
		StringBuffer buf = new StringBuffer();
		for (int i=0; i < data.length(); i++)
		{
			char c = data.charAt(i);
			if (remove.indexOf(c) < 0)
				buf.append(c);
		}
		return buf.toString();
	}
	
	protected void updateParticipantFields(ParticipantInfo partInfo, String[] fields) throws SQLException, DuplicateParticipantException
	{
		for (int i=0; i < def.getFieldList().size(); i++)
		{
			String fieldName = (String) def.getFieldList().get(i);
		    if(fields.length >i) 
		    {
				if (fieldName.equalsIgnoreCase(DatabaseObject.colPartFilename))
				{
					partInfo.setFilename(maxLength(fields[i], 20));
				}
				if (fieldName.equalsIgnoreCase(DatabaseObject.colPartAgencyLoc))
				{
					partInfo.setAgencyLocation(maxLength(fields[i], 20));
				}
				if (fieldName.equalsIgnoreCase(DatabaseObject.colPartAgencyName))
				{
					partInfo.setAgencyName(maxLength(fields[i], 20));
				}
				if (fieldName.equalsIgnoreCase(DatabaseObject.colPartAgentDest))
				{
					partInfo.setAgentDestAddress(maxLength(fields[i], 20));
				}
				if (fieldName.equalsIgnoreCase(DatabaseObject.colPartAmsPartCode))
				{
					partInfo.setAmsPartCode(maxLength(fields[i], 10));
				}
				if (fieldName.equalsIgnoreCase(DatabaseObject.colPartCity)|| fieldName.equalsIgnoreCase("p_"+DatabaseObject.colPartCity))
				{
					partInfo.setCity(maxLength(fields[i], 50));
				}
				if (fieldName.equalsIgnoreCase(DatabaseObject.colPartContactEmail))
				{
					partInfo.setContactEmail(maxLength(fields[i], 50));
				}
				if (fieldName.equalsIgnoreCase(DatabaseObject.colPartContactName))
				{
					partInfo.setContactName(maxLength(fields[i], 50));
				}
				if (fieldName.equalsIgnoreCase(DatabaseObject.colPartContactPhone))
				{
					partInfo.setContactPhone(maxLength(fields[i], 20));
				}
				if (fieldName.equalsIgnoreCase(DatabaseObject.colPartIsPrimaryContact))
				{
					partInfo.setPrimaryContact(maxLength(fields[i], 1));
				}
				if (fieldName.equalsIgnoreCase(DatabaseObject.colPartStateId))
				{
					partInfo.setStateId(maxLength(fields[i], 5));
				}
				if (fieldName.equalsIgnoreCase(DatabaseObject.colPartZip)||fieldName.equalsIgnoreCase("p_"+DatabaseObject.colPartZip))
				{
					partInfo.setZip(maxLength(fields[i], 20));
				}
				/*if (fieldName.equalsIgnoreCase(DatabaseObject.colPartEmail))
				{
					partInfo.setEmail(maxLength(fields[i], 20));
				}
				if (fieldName.equalsIgnoreCase(DatabaseObject.colPartADDRESS1))
				{
					partInfo.setAddress1(maxLength(fields[i], 100));
				}
				if (fieldName.equalsIgnoreCase(DatabaseObject.colPartADDRESS2))
				{
					partInfo.setAddress2(maxLength(fields[i], 100));
				}
				if (fieldName.equalsIgnoreCase(DatabaseObject.colPartState))
				{
					partInfo.setState(maxLength(fields[i], 100));
				}*/
		   }	
		}
		partInfo.save();
	}
	
	/**
	 * Returns a randomly generated alpha-numeric password.
	 * @return String
	 */
	public String getRandomPassword()
	{
		String time = Long.toString(System.currentTimeMillis(), 36);
		StringBuffer ret = new StringBuffer("");
		
		for (int i=time.length()-1; i >= 0; i--)
		{
			char ch = time.charAt(i);
			if (ch == 'l')
				ret.append('1');
			else
				ret.append(ch);
		}
		
		return maxLength(ret.toString(), 32);
	}

	/**
	 * Returns an array list of new agent objects created during the import process.
	 * @return java.util.ArrayList
	 */
	public ArrayList getNewAgents()
	{
		return newAgentList;
	}

}
