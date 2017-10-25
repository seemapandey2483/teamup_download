/*
 * Created on Nov 6, 2007
 */
package connective.teamup.download.beans;

import java.io.Serializable;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.CarrierInfo;
import connective.teamup.download.DisplayBean;
import connective.teamup.download.DisplayBeanException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.AgentSummaryInfo;
import connective.teamup.download.db.DatabaseOperation;

/**
 * @author kmccreary
 */
public class TradingPartnerSubListAlphaDisplayBean implements Serializable, DisplayBean
{
	private static final Logger LOGGER = Logger.getLogger(TradingPartnerSubListAlphaDisplayBean.class);
	
	private AgentSummaryInfo[] agents = null;
	private String firstLetter = null;
	private int startingPosition = 0;
	private int maxRows = 0;

	private CarrierInfo carrierInfo = null;


	/**
	 * Constructor for TradingPartnerSubListAlphaDisplayBean.
	 */
	public TradingPartnerSubListAlphaDisplayBean()
	{
		super();
	}

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
			
			// Parse the search parameters from the request
			firstLetter = req.getParameter("alphaStart");
			if (firstLetter == null || firstLetter.equals(""))
			{
				firstLetter = serverInfo.getTPListViewSearchParameter(req.getSession());
				if (firstLetter == null || firstLetter.equals(""))
					firstLetter = "A";
			}
			else if (firstLetter.length() > 1)
				firstLetter = firstLetter.substring(0,1);
			
//			try {
//				startingPosition = Integer.parseInt(req.getParameter("startPos"));
//			}
//			catch (Exception e)
//			{
//				startingPosition = 0;
//			}
			
			try {
				maxRows = Integer.parseInt(req.getParameter("maxRows"));
			}
			catch (Exception e)
			{
				LOGGER.error(e);
				maxRows = -1;	// default to 'all' (no max rows)
			}
			
//			agents = op.getAgentsByAlpha(firstLetter, startingPosition, maxRows);
			agents = op.getAgentsByAlpha(firstLetter, 0, -1);
			
			// Save the current view as the user's default TP list view
			serverInfo.setTPListView(req.getSession(), ServerInfo.TPLIST_ALPHA, firstLetter);
		}
		catch (SQLException e)
		{
			LOGGER.error(e);
			//throw new DisplayBeanException("Error occurred building the list of download agents", e);
			agents = null;
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
	 * Returns the carrier info bean.
	 * @return CarrierInfo
	 */
	public CarrierInfo getCarrierInfo()
	{
		return carrierInfo;
	}

	/**
	 * Returns the number of agents.
	 * @return int
	 */
	public int getAgentCount()
	{
		if (agents == null)
			return 0;
		
		return agents.length;
	}

	/**
	 * Returns the specified agent info.
	 * @return AgentInfo
	 */
	public AgentSummaryInfo getAgent(int index)
	{
		if (index > agents.length)
			return null;
		
		return agents[index];
	}

	/**
	 * @return Returns the firstLetter.
	 */
	public String getFirstLetter()
	{
		return firstLetter;
	}

	/**
	 * @return Returns the maxRows.
	 */
	public int getMaxRows()
	{
		return maxRows;
	}

	/**
	 * @return Returns the startingPosition.
	 */
	public int getStartingPosition()
	{
		return startingPosition;
	}

}
