/*
 * 03/02/2005 - Created
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
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentGroupInfo;
import connective.teamup.download.db.AmsInfo;
import connective.teamup.download.db.BatchInfo;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author Kyle McCreary
 *
 * Display bean used for displaying the search parameters for agent recipients
 * of a batch of distributed files (Applied edits or email).
 */
public class DistFileParamsDisplayBean implements DisplayBean, Serializable
{
	private static final Logger LOGGER = Logger.getLogger(DistFileParamsDisplayBean.class);
	
	private CarrierInfo carrierInfo = null;
	private ArrayList groupLabels = null;
	private ArrayList groupValues = null;
	private ArrayList vendorLabels = null;
	private ArrayList vendorValues = null;
	
	private String agyGroup = AgentGroupInfo.GROUP_ALL_LIVE;
	private String amsId = "";
	private String amsVersion = "";
	private String batchDesc = "";
	private String distBatchNumber = "";
	private String preview = "";
	private String notify = "";


	/* (non-Javadoc)
	 * @see connective.teamup.download.DisplayBean#init(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, connective.teamup.download.ServerInfo, connective.teamup.download.db.DatabaseOperation, FileItem[])
	 */
	public void init(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items)
		throws DisplayBeanException
	{
		// Load the carrier info
		carrierInfo = serverInfo.getCarrierInfo();

		// Initialize the agent group lists, add the default groups
		groupLabels = new ArrayList();
		groupValues = new ArrayList();
		
		groupLabels.add("All live agents");
		groupValues.add(AgentGroupInfo.GROUP_ALL_LIVE);
		
		groupLabels.add("All registered agents");
		groupValues.add(AgentGroupInfo.GROUP_ALL_REGISTERED);
		
		if (req.getParameter("action").equals("dist_email_params"))
		{
			groupLabels.add("All new (unregistered) agents");
			groupValues.add(AgentGroupInfo.GROUP_ALL_UNREGISTERED);
		}
		
		// Get a list of all saved agent groups
		try
		{
			AgentGroupInfo[] groups = op.getAllAgentGroups();
			if (groups != null && groups.length > 0)
			{
				groupLabels.add("----------------------");
				groupValues.add("");
				
				for (int i=0; i < groups.length; i++)
				{
					groupLabels.add(groups[i].getName());
					groupValues.add(groups[i].getName());
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error(e);
			e.printStackTrace(System.out);
		}
		
		// Parse existing values from the request
		agyGroup = req.getParameter("agyGroup");
		if (agyGroup == null || agyGroup.equals(""))
			agyGroup = AgentGroupInfo.GROUP_ALL_LIVE;	// default to "All live agents"
		amsId = req.getParameter("amsId");
		amsVersion = req.getParameter("amsVersion");
		preview = req.getParameter("preview");
		if (preview == null || preview.equals(""))
			preview = "Y";
		notify = req.getParameter("notify");
		if (notify == null || notify.equals(""))
			notify = "Y";
		distBatchNumber = req.getParameter("distBatchNumber");
		if (distBatchNumber != null && !distBatchNumber.equals(""))
		{
			try
			{
				BatchInfo batchInfo = op.getBatchInfo(Integer.parseInt(distBatchNumber));
				batchDesc = batchInfo.getDescription();
			}
			catch (Exception e) {
				LOGGER.error(e);
			}
		}
		
		// Build the list of agency vendor systems (not for Applied edit actions)
		String action = req.getParameter("action");
		if (action != null && action.indexOf("edits") < 0)
		{
			vendorLabels = new ArrayList();
			vendorValues = new ArrayList();
			vendorLabels.add(" (do not limit by vendor system) ");
			vendorValues.add("");
			
			try
			{
				AmsInfo[] vendor = op.getAmsInfoList();
				if (vendor != null && vendor.length > 0)
				{
					for (int i=0; i < vendor.length; i++)
					{
						vendorLabels.add(vendor[i].getDisplayName());
						vendorValues.add(vendor[i].getId());
					}
				}
			}
			catch (Exception e)
			{
				LOGGER.error(e);
				e.printStackTrace(System.out);
			}
		}
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
	public String getBatchDesc(String defaultDesc)
	{
		if (batchDesc == null || batchDesc.equals(""))
			batchDesc = defaultDesc;
		return getBatchDesc();
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

	public int getGroupCount()
	{
		return groupLabels.size();
	}

	/**
	 * @return
	 */
	public String getGroupLabel(int index)
	{
		return (String) groupLabels.get(index);
	}

	/**
	 * @return
	 */
	public String getGroupValue(int index)
	{
		return (String) groupValues.get(index);
	}

	public boolean isGroupSelected(int index)
	{
		boolean ret = false;
		if (agyGroup != null && getGroupValue(index).equals(agyGroup))
			ret = true;
		return ret;
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
	public String getPreview()
	{
		if (preview == null)
			return "";
		return preview;
	}

	public int getVendorCount()
	{
		if (vendorLabels == null)
			return 0;
		return vendorLabels.size();
	}

	/**
	 * @return
	 */
	public String getVendorLabel(int index)
	{
		if (vendorLabels == null || vendorLabels.size() <= index)
			return "";
		return (String) vendorLabels.get(index);
	}

	/**
	 * @return
	 */
	public String getVendorValue(int index)
	{
		if (vendorValues == null || vendorValues.size() <= index)
			return "";
		return (String) vendorValues.get(index);
	}

	public boolean isVendorSelected(int index)
	{
		boolean ret = false;
		if (amsId != null && getVendorValue(index).equals(amsId))
			ret = true;
		return ret;
	}

}
