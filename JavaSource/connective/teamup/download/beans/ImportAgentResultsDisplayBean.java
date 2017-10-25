/*
 * 04/12/2005 - Created
 */
package connective.teamup.download.beans;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.CarrierInfo;
import connective.teamup.download.DisplayBean;
import connective.teamup.download.DisplayBeanException;
import connective.teamup.download.Escape;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.agentimport.ImportDefinition;
import connective.teamup.download.agentimport.ImportEngine;
import connective.teamup.download.agentimport.ImportResult;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.DistributedFileInfo;

/**
 * Display bean used for completing and displaying the results of the import 
 * of agent info from a comma-delimited file.
 * 
 * @author Kyle McCreary
 */
public class ImportAgentResultsDisplayBean implements DisplayBean, Serializable
{
	private static final Logger LOGGER = Logger.getLogger(ImportAgentResultsDisplayBean.class);
	
	private CarrierInfo carrierInfo = null;

	private String batchNumber = null;
	private String agentsFlag = null;
	private String participantsFlag = null;
	private String newAgentsMsg = null;
	private String carGroupName = null;
	private ArrayList errorResults = null;
	private ArrayList errorIndices = null;
	private int successfulImports = 0;
	private int totalImports = 0;
	private boolean carProcess = false;


	/* (non-Javadoc)
	 * @see connective.teamup.download.DisplayBean#init(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, connective.teamup.download.ServerInfo, connective.teamup.download.db.DatabaseOperation, org.apache.commons.fileupload.FileItem[])
	 */
	public void init(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items)
		throws DisplayBeanException
	{
		ImportResult[] results = null;
		try
		{
			// Load the carrier info
			carrierInfo = serverInfo.getCarrierInfo();
			
			// Parse the import parameters from the request
			batchNumber = req.getParameter("batchnum");
			agentsFlag = req.getParameter("agents");
			participantsFlag = req.getParameter("participants");
			String overwriteFlag = req.getParameter("overwrite");
			String carFlag = req.getParameter("carProcess");
			carProcess = (carFlag != null && carFlag.equals("Y"));
			if (carProcess)
			{
				SimpleDateFormat df = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
				df.applyPattern("MM/dd/yyyy");
				carGroupName = "Agent Rollout - " + df.format(new Date(System.currentTimeMillis()));
			}
			
			// Load the saved field and vendor system mappings
			ImportDefinition def = new ImportDefinition();
			def.setFileContainsAgents(getAgentsFlag().equals("Y"));
			def.setFileContainsParticipants(getParticipantsFlag().equals("Y"));
			def.setOverwriteExisting(overwriteFlag != null && overwriteFlag.equals("Y"));
			def.loadFromProperties(op);
			
			if (batchNumber != null && !batchNumber.equals(""))
			{
				DistributedFileInfo[] files = op.getDistributedFilesForBatch(Integer.parseInt(batchNumber));
				if (files != null && files.length > 0)
				{
					ByteArrayInputStream is = new ByteArrayInputStream(files[0].getFileContents());
					
					ImportEngine ie = new ImportEngine(def);
					results = ie.doImport(op, is);
					
					// Remove the temporarily stored files from the database
					for (int i=0; i < files.length; i++)
						files[i].delete();
					
					// Get list of newly created agents
					ArrayList newAgtList = ie.getNewAgents();
					if (newAgtList == null || newAgtList.size() == 0)
					{
						newAgentsMsg = "There were no new agents created.";
						carProcess = false;
					}
					else
					{
						ArrayList idList = new ArrayList();
						for (int i=0; i < newAgtList.size(); i++)
						{
							AgentInfo agt = (AgentInfo) newAgtList.get(i);
							idList.add(agt.getAgentId());
						}
						
						if (newAgtList.size() == 1)
							newAgentsMsg = "There was 1 new agent created.";
						else
							newAgentsMsg = "There were " + String.valueOf(newAgtList.size()) + " new agents created.";
						if (isCarProcess())
							req.getSession().setAttribute(ServerInfo.STORE_AGENT_LIST, idList);
					}
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error occurred importing agency info from the comma-delimited file", e);
//			e.printStackTrace(System.out);
			throw new DisplayBeanException("Error occurred importing agency info from the comma-delimited file", e);
		}
		
		try
		{
			// Collect and display the results of the import
			if (results != null)
			{
				totalImports = results.length;
				successfulImports = 0;
				errorResults = new ArrayList();
				errorIndices = new ArrayList();
				for (int i=0; i < results.length; i++)
				{
					if (results[i].isError())
					{
						errorResults.add(results[i]);
						errorIndices.add(String.valueOf(i+1));
					}
					else
						successfulImports++;
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error occurred building the agency import results page", e);
//			e.printStackTrace(System.out);
			throw new DisplayBeanException("Error occurred building the agency import results page", e);
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
	public String getParticipantsFlag()
	{
		if (participantsFlag == null)
			return "";
		return participantsFlag;
	}

	public int getSuccessfulImports()
	{
		return successfulImports;
	}

	public int getTotalImports()
	{
		return totalImports;
	}

	public int getErrorCount()
	{
		if (errorResults == null)
			return 0;
		return errorResults.size();
	}

	public String getErrorData(int index)
	{
		if (errorResults == null || index >= errorResults.size())
			return "";
		
		ImportResult result = (ImportResult) errorResults.get(index);
		return Escape.forXML(result.getData());
	}

	public String getErrorMessage(int index)
	{
		if (errorResults == null || index >= errorResults.size())
			return "";
		
		ImportResult result = (ImportResult) errorResults.get(index);
		return Escape.forHtml(result.getMessage());
	}

	public String getErrorDataLineNumber(int index)
	{
		if (errorIndices == null || index >= errorIndices.size())
			return "";
		return (String) errorIndices.get(index);
	}

	public String getImportType()
	{
		String type = "";
		if (getAgentsFlag().equals("Y"))
			type = "agent";
		else if (getParticipantsFlag().equals(""))
			type = "participant codes/sub-agent";
		return type;
	}

	public String getNewAgentsMsg()
	{
		if (newAgentsMsg == null)
			return "";
		return newAgentsMsg;
	}

	public boolean isCarProcess()
	{
		return carProcess;
	}

	public String getCarFlag()
	{
		String ret = "N";
		if (isCarProcess())
			ret = "Y";
		return ret;
	}

	public String getCarGroupName()
	{
		return carGroupName;
	}

}
