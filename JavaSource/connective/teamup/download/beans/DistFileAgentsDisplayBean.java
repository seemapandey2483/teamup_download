/*
 * 03/10/2005 - Created
 */
package connective.teamup.download.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.CarrierInfo;
import connective.teamup.download.DisplayBean;
import connective.teamup.download.DisplayBeanException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentGroupInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.AmsInfo;
import connective.teamup.download.db.BatchInfo;
import connective.teamup.download.db.DatabaseFactory;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author Kyle McCreary
 *
 * Display bean used for displaying the search results for agent recipients
 * of a batch of distributed files (Applied edits or email).
 */
public class DistFileAgentsDisplayBean implements DisplayBean, Serializable
{
	private static final Logger LOGGER = Logger.getLogger(DistFileAgentsDisplayBean.class);
	
	private CarrierInfo carrierInfo = null;
	private AgentInfo[] agents = null;
	private ArrayList included = null;
	
	private String agyGroup = "";
	private String amsId = "";
	private String amsVersion = "";
	private String batchDesc = "";
	private String distBatchNumber = "";
	private String queryText = "";
	private String actionPrefix = "";
	private String notify = "";


	/* (non-Javadoc)
	 * @see connective.teamup.download.DisplayBean#init(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, connective.teamup.download.ServerInfo, connective.teamup.download.db.DatabaseOperation, FileItem[])
	 */
	public void init(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items)
		throws DisplayBeanException
	{
		try
		{
			// Load the carrier info
			carrierInfo = serverInfo.getCarrierInfo();
			
			// Parse the action prefix and distribution parameters from the request
			String action = req.getParameter("action");
			if (action.length() > 10)
				actionPrefix = action.substring(0, 10);
			agyGroup = req.getParameter("agyGroup");
			amsId = req.getParameter("amsId");
			amsVersion = req.getParameter("amsVersion");
			notify = req.getParameter("notify");
			distBatchNumber = req.getParameter("distBatchNumber");
			batchDesc = req.getParameter("batchDesc");
				
			if (distBatchNumber == null || distBatchNumber.equals(""))
			{
				// Get the next batch number (if any), increment and save
				int batchNumber = 1;
				String str = op.getPropertyValue(DatabaseFactory.PROP_BATCHNUM);
				if (str != null && !str.equals(""))
					batchNumber = Integer.parseInt(str);
				Hashtable props = new Hashtable();
				props.put(DatabaseFactory.PROP_BATCHNUM, String.valueOf(batchNumber + 1));
				op.setProperties(props);
				
				// Create the batch import info object
				if (batchNumber > 0)
				{
					BatchInfo batchInfo = op.createBatchInfo(batchNumber);
					batchInfo.setDescription(batchDesc);
					batchInfo.save();
					distBatchNumber = String.valueOf(batchNumber);
				}
			}
			else
			{
				try
				{
					BatchInfo batchInfo = op.getBatchInfo(Integer.parseInt(distBatchNumber));
					if (!batchDesc.equals(batchInfo.getDescription()))
					{
						batchInfo.setDescription(batchDesc);
						batchInfo.save();
					}
				}
				catch (Exception e) {
					LOGGER.error(e);
				}
			}

			// Build the search query
			if (agyGroup == null || agyGroup.trim().equals(""))
				queryText = "All download agents";
			else if (agyGroup.equals(AgentGroupInfo.GROUP_ALL_LIVE))
				queryText = "All live agents";
			else if (agyGroup.equals(AgentGroupInfo.GROUP_ALL_REGISTERED))
				queryText = "All registered agents";
			else if (agyGroup.equals(AgentGroupInfo.GROUP_ALL_UNREGISTERED))
				queryText = "All new (unregistered) agents";
			else
				queryText = "All members of the group '" + agyGroup + "'";
			
			if (amsId != null && !amsId.equals(""))
			{
				AmsInfo amsInfo = op.getAmsInfo(amsId);
				if (amsInfo != null)
					queryText += " with vendor system '" + amsInfo.getDisplayName() + "'";
			}
			
			if (amsVersion != null && !amsVersion.trim().equals(""))
			{
				queryText += "; only agents with version equal to or starting with '";
				queryText += amsVersion + "' have been selected by default";
			}
			queryText += ".";
			
			// Build the agent list
			agents = op.getDistributionAgentList(amsId, agyGroup);
			if (agents != null && agents.length > 0)
			{
				if (amsVersion != null && amsVersion.equals(""))
					amsVersion = null;
				
				included = new ArrayList();
				for (int i=0; i < agents.length; i++)
				{
					String flag = "N";
					if (amsVersion == null)
						flag = "Y";
					else if (agents[i].getAmsVer() == null)
						flag = "N";
					else if (agents[i].getAmsVer().equals(amsVersion))
						flag = "Y";
					else if (agents[i].getAmsVer().length() > amsVersion.length() &&
							 agents[i].getAmsVer().substring(0, amsVersion.length()).equals(amsVersion))
						flag = "Y";
					included.add(flag);
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error(e);
			e.printStackTrace(System.out);
		}
	}

	/**
	 * @return
	 */
	public String getActionPrefix()
	{
		return actionPrefix;
	}
	
	public int getAgentCount()
	{
		if (agents == null)
			return 0;
		return agents.length;
	}

	/**
	 * @return
	 */
	public AgentInfo getAgentInfo(int index)
	{
		if (agents == null || index >= agents.length)
			return null;
		return agents[index];
	}

	/**
	 * @return
	 */
	public String getAgyGroup()
	{
		if (agyGroup == null)
			return "";
		return agyGroup;
	}

	/**
	 * @return
	 */
	public String getAmsId()
	{
		if (amsId == null)
			return "";
		return amsId;
	}

	/**
	 * @return
	 */
	public String getAmsVersion()
	{
		if (amsVersion == null)
			return "";
		return amsVersion;
	}

	/**
	 * @return
	 */
	public String getBatchDesc()
	{
		if (batchDesc == null)
			return "";
		return batchDesc;
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
	public String getDistBatchNumber()
	{
		if (distBatchNumber == null)
			return "";
		return distBatchNumber;
	}

	/**
	 * @return
	 */
	public boolean isIncluded(int index)
	{
		if (included == null || index >= included.size())
			return false;
		
		String flag = (String) included.get(index);
		return flag.equals("Y");
	}

	/**
	 * @return
	 */
	public String getNotify()
	{
		if (notify == null)
			return "";
		return notify;
	}

	/**
	 * @return
	 */
	public String getQueryText()
	{
		return queryText;
	}

}
