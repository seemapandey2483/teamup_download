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
public class TradingPartnerSubListAgentIdDisplayBean implements Serializable, DisplayBean
{
	private static final Logger LOGGER = Logger.getLogger(TradingPartnerSubListAgentIdDisplayBean.class);
	
	private AgentSummaryInfo[] agents = null;
	private String nextAgentID = null;
	private int maxRows = 0;

	private CarrierInfo carrierInfo = null;


	/**
	 * Constructor for TradingPartnerSubListAgentIdDisplayBean.
	 */
	public TradingPartnerSubListAgentIdDisplayBean()
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
			String agentid = req.getParameter("agentSearch");
			if (agentid == null || agentid.equals(""))
				agentid = serverInfo.getTPListViewSearchParameter(req.getSession());
			if (agentid == null || agentid.equals(""))
				agentid = req.getParameter("agentID");
			if (agentid == null)
				agentid = "";
			
			maxRows = -1;
			try {
				maxRows = Integer.parseInt(req.getParameter("maxRows"));
			}
			catch (Exception e)
			{
				LOGGER.error(e);
				maxRows = 10;
			}
			agents = op.getAgentsByParticipant(agentid, maxRows + 1);
			if (agents != null && agents.length > maxRows)
				nextAgentID = agents[maxRows].getParticipantCode();
			
			// Save the current view as the user's default TP list view
			serverInfo.setTPListView(req.getSession(), ServerInfo.TPLIST_AGENT_ID, agentid);
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
		int ret = 0;
		if (agents == null)
			ret = 0;
		else if (agents.length > maxRows)
			ret = maxRows;
		else
			ret = agents.length;
		
		return ret;
	}

	/**
	 * Returns the specified agent info.
	 * @return AgentInfo
	 */
	public AgentSummaryInfo getAgent(int index)
	{
		if (agents == null || index > agents.length)
			return null;
		
		return agents[index];
	}

	/**
	 * @return Returns the maxRows.
	 */
	public int getMaxRows()
	{
		return maxRows;
	}

	/**
	 * @return Returns the agent ID to start the "Next" page.
	 */
	public String getNextAgentID()
	{
		return nextAgentID;
	}

}
