package connective.teamup.download.beans;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.CarrierInfo;
import connective.teamup.download.DisplayBean;
import connective.teamup.download.DisplayBeanException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author Kyle McCreary
 */
public class CarGroupNameDisplayBean implements Serializable, DisplayBean
{
	private static final Logger LOGGER = Logger.getLogger(CarGroupNameDisplayBean.class);
	
	private CarrierInfo carrierInfo = null;
	private String groupName = null;
	private String groupDesc = null;


	/* (non-Javadoc)
	 * @see connective.teamup.download.DisplayBean#init(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, connective.teamup.download.ServerInfo, connective.teamup.download.db.DatabaseOperation, org.apache.commons.fileupload.FileItem[])
	 */
	public void init(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws DisplayBeanException
	{
		try
		{
			// Load the carrier info
			this.carrierInfo = serverInfo.getCarrierInfo();
			
			// Load the rollout group info
			this.groupName = req.getParameter("groupName");
			this.groupDesc = req.getParameter("groupDesc");
		}
		catch (Exception e)
		{
			LOGGER.error("Error occurred retrieving application properties", e);
			throw new DisplayBeanException("Error occurred retrieving application properties", e);
		}
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
	public String getGroupDesc()
	{
		return groupDesc;
	}

	/**
	 * @return
	 */
	public String getGroupName()
	{
		return groupName;
	}

	/**
	 * @param info
	 */
	public void setCarrierInfo(CarrierInfo info)
	{
		carrierInfo = info;
	}

	/**
	 * @param string
	 */
	public void setGroupDesc(String string)
	{
		groupDesc = string;
	}

	/**
	 * @param string
	 */
	public void setGroupName(String string)
	{
		groupName = string;
	}

}
