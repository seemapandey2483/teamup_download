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

public class ReportListDisplayBean implements Serializable, DisplayBean{
	
	private static final Logger LOGGER = Logger.getLogger(ReportListDisplayBean.class);
	
	private CarrierInfo carrierInfo = null;
	/**
	 * Constructor for TradingPartnerListDisplayBean.
	 */
	public ReportListDisplayBean()
	{
		super();
		
	}

	/**
	 * @see connective.teamup.download.DisplayBean#init(HttpServletRequest, HttpServletResponse, ServerInfo, DatabaseOperation, FileItem[])
	 */
	public void init(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws DisplayBeanException 
	{
		try
		{
			// Load the carrier info
			carrierInfo = serverInfo.getCarrierInfo();
			
			
		}
		catch (Exception e)
		{
			LOGGER.error("Error occurred building the list of download agents", e);
			throw new DisplayBeanException("Error occurred building the list of download agents", e);
		}
	}

	/**
	 * Returns the banner graphic filename.
	 * @return String
	 */
	public String getBannerGraphic()
	{
		return carrierInfo.getBannerGraphicFile();
	}

	/**
	 * Returns the carrier's contact email address.
	 * @return String
	 */
	public String getCarrierEmail()
	{
		return carrierInfo.getContactEmail();
	}

	/**
	 * Returns the carrier's display name.
	 * @return String
	 */
	public String getCarrierName()
	{
		return carrierInfo.getName();
	}

	/**
	 * Returns the carrier's short name.
	 * @return String
	 */
	public String getCarrierShortName()
	{
		return carrierInfo.getShortName();
	}

	/**
	 * Returns the number of agents.
	 * @return int
	 */
	
	/**
	 * Returns the carrier info bean.
	 * @return CarrierInfo
	 */
	public CarrierInfo getCarrierInfo()
	{
		return carrierInfo;
	}
	
	
}
