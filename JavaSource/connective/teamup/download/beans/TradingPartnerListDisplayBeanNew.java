package connective.teamup.download.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.CarrierInfo;
import connective.teamup.download.DisplayBean;
import connective.teamup.download.DisplayBeanException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentInfo;
import connective.teamup.download.db.AmsInfo;
import connective.teamup.download.db.DatabaseOperation;

public class TradingPartnerListDisplayBeanNew implements Serializable, DisplayBean{
	
	private static final Logger LOGGER = Logger.getLogger(TradingPartnerListDisplayBeanNew.class);

	private String lastSystem = "";
	private int sortOrder = 0;
	
	private AgentInfo[] agents = null;
	
	private CarrierInfo carrierInfo = null;
	
	/**
	 * Constructor for TradingPartnerListDisplayBean.
	 */
	public TradingPartnerListDisplayBeanNew()
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
		
			
			// Save the current view as the user's default TP list view
			serverInfo.setTPListView(req.getSession(), ServerInfo.TPLIST_ALL);
//			serverInfo.resetTPListView(req.getSession());
		}
		catch (Exception e)
		{
			LOGGER.error("Error occurred building the list of download agents", e);
			throw new DisplayBeanException("Error occurred building the list of download agents", e);
		}
	}

}
