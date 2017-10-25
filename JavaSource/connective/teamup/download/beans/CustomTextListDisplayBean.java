package connective.teamup.download.beans;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.CarrierInfo;
import connective.teamup.download.CustomTextFactory;
import connective.teamup.download.DisplayBean;
import connective.teamup.download.DisplayBeanException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.DatabaseOperation;

/**
 * Display bean for the Customizable Emails List page.
 * 
 * @author kmccreary
 */
public class CustomTextListDisplayBean implements Serializable, DisplayBean
{
	private static final Logger LOGGER = Logger.getLogger(CustomTextListDisplayBean.class);
	
	private CarrierInfo carrierInfo = null;

	private ArrayList agentFileList = null;
	private ArrayList agentDescList = null;
	private ArrayList agentPlatformList = null;
	private ArrayList agentStatusList = null;

	private ArrayList carrierFileList = null;
	private ArrayList carrierDescList = null;
	private ArrayList carrierStatusList = null;


	/**
	 * Constructor for CustomTextListDisplayBean.
	 */
	public CustomTextListDisplayBean()
	{
		super();
	}

	/* (non-Javadoc)
	 * @see connective.teamup.download.DisplayBean#init(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, connective.teamup.download.ServerInfo, connective.teamup.download.db.DatabaseOperation, org.apache.commons.fileupload.FileItem[])
	 */
	public void init(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws DisplayBeanException
	{
		// Load the company info
		carrierInfo = serverInfo.getCarrierInfo();
		
		// initialize agent email file info
		agentFileList = new ArrayList();
		agentDescList = new ArrayList();
		agentPlatformList = new ArrayList();
		agentStatusList = new ArrayList();
		addAgentFile(op, CustomTextFactory.TEXT_DL_STALE_CLIENT_APP, "Agent Workstation");
//		addAgentFile(op, CustomTextFactory.TEXT_DL_STALE, "Browser");
//		addAgentFile(op, CustomTextFactory.TEXT_AGENT_MIGRATION, "Browser");
//		addAgentFile(op, CustomTextFactory.TEXT_APPLIED_EDITS, "All");
		addAgentFile(op, CustomTextFactory.TEXT_DL_URL_CHANGE, "Browser");
		addAgentFile(op, CustomTextFactory.TEXT_NEW_AGENT_CLIENT_APP, "Agent Workstation");
//		addAgentFile(op, CustomTextFactory.TEXT_NEW_AGENT, "Browser");
		
		// initialize carrier email file info
		carrierFileList = new ArrayList();
		carrierDescList = new ArrayList();
		carrierStatusList = new ArrayList();
//		addCarrierFile(op, CustomTextFactory.TEXT_MIGRATED);
		addCarrierFile(op, CustomTextFactory.TEXT_REGISTERED);
		addCarrierFile(op, CustomTextFactory.TEXT_STATUS_CHANGE);
		addCarrierFile(op, CustomTextFactory.TEXT_AGENT_VENDOR_CHANGE);
	}

	private void addAgentFile(DatabaseOperation op, String textFile, String platform)
	{
		agentFileList.add(textFile);
		agentDescList.add(CustomTextFactory.getFileDescription(textFile));
		agentPlatformList.add(platform);
		
		String status = "Default";
		if (isCustomText(textFile, op))
			status = "Custom";
		else if (isCustomText(CustomTextFactory.getHtmlTextName(textFile), op))
			status = "Custom";
		agentStatusList.add(status);
	}

	private void addCarrierFile(DatabaseOperation op, String textFile)
	{
		carrierFileList.add(textFile);
		carrierDescList.add(CustomTextFactory.getFileDescription(textFile));
		
		String status = "Default";
		if (isCustomText(textFile, op))
			status = "Custom";
		else if (isCustomText(CustomTextFactory.getHtmlTextName(textFile), op))
			status = "Custom";
		carrierStatusList.add(status);
	}

	public CarrierInfo getCarrierInfo()
	{
		return carrierInfo;
	}

	private boolean isCustomText(String filename, DatabaseOperation op)
	{
		boolean custom = false;
		try
		{
			// Attempt to retrieve the text data from the resource table
			InputStream textData = op.getResource(filename);
			if (textData != null)
			{
				byte[] buf = new byte[256];
				int read; 
				if ((read = textData.read(buf, 0, 256)) != -1)
				{
					String text = new String(buf, 0, read);
					if (text != null && !text.trim().equals(""))
						custom = true;
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error(e);
			custom = false;
		}
		
		return custom;
	}

	/**
	 * Returns the number of agent files in the list.
	 */
	public int getAgentFileCount()
	{
		if (agentFileList == null)
			return 0;
		return agentFileList.size();
	}

	/**
	 * Returns the specified agent file description.
	 */
	public String getAgentFileDesc(int index)
	{
		if (agentDescList == null || index >= agentDescList.size())
			return "";
		return (String) agentDescList.get(index);
	}

	/**
	 * Returns the specified agent file name.
	 */
	public String getAgentFileName(int index)
	{
		if (agentFileList == null || index >= agentFileList.size())
			return "";
		return (String) agentFileList.get(index);
	}

	/**
	 * Returns the specified agent file platform.
	 */
	public String getAgentFilePlatform(int index)
	{
		if (agentPlatformList == null || index >= agentPlatformList.size())
			return "";
		return (String) agentPlatformList.get(index);
	}

	/**
	 * Returns the specified agent file status (custom or default).
	 */
	public String getAgentFileStatus(int index)
	{
		if (agentStatusList == null || index >= agentStatusList.size())
			return "";
		return (String) agentStatusList.get(index);
	}

	/**
	 * Returns the number of carrier files in the list.
	 */
	public int getCarrierFileCount()
	{
		if (carrierFileList == null)
			return 0;
		return carrierFileList.size();
	}

	/**
	 * Returns the specified carrier file description.
	 */
	public String getCarrierFileDesc(int index)
	{
		if (carrierDescList == null || index >= carrierDescList.size())
			return "";
		return (String) carrierDescList.get(index);
	}

	/**
	 * Returns the specified carrier file name.
	 */
	public String getCarrierFileName(int index)
	{
		if (carrierFileList == null || index >= carrierFileList.size())
			return "";
		return (String) carrierFileList.get(index);
	}

	/**
	 * Returns the specified carrier status (custom or default).
	 */
	public String getCarrierFileStatus(int index)
	{
		if (carrierStatusList == null || index >= carrierStatusList.size())
			return "";
		return (String) carrierStatusList.get(index);
	}
}
