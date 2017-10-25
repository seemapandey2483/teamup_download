package connective.teamup.download.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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
import connective.teamup.download.db.DatabaseObject;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.ParticipantSummaryInfo;

/**
 * @author Kyle McCreary
 *
 */
public class TradingPartnerSummaryDisplayBean implements Serializable, DisplayBean
{
	private static final Logger LOGGER = Logger.getLogger(TradingPartnerSummaryDisplayBean.class);
	
	private int totalPartners = 0;
	private int livePartners = 0;
	private int registeredPartners = 0;
	private int unregisteredPartners = 0;
	private int disabledPartners = 0;
	
	private int totalParticipants = 0;
	private int liveParticipants = 0;
	private int registeredParticipants = 0;
	private int unregisteredParticipants = 0;
	private int disabledParticipants = 0;
	
	private int totalIDs = 0;
	
	private boolean clientUsers = false;
	private boolean browserUsers = false;
	
	private String tpSortOrder = "";
	private ArrayList vendorNameList = new ArrayList();
	private Hashtable vendorCountHash = new Hashtable();
	private Hashtable vendorClientCountHash = new Hashtable();
	private Hashtable vendorPartHash = new Hashtable();
	
	private CarrierInfo carrierInfo = null;
	

	/**
	 * Constructor for TradingPartnerSummaryDisplayBean.
	 */
	public TradingPartnerSummaryDisplayBean()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.DisplayBean#init(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public void init(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws DisplayBeanException
	{
		// Load the carrier info
		carrierInfo = serverInfo.getCarrierInfo();
		
		Hashtable vendorHash = new Hashtable();
		String vendorId = "";
		String vendorName = "";
		int vendorCount = 0;
		int clientAppCount = 0;
		try
		{
			// Get the trading partner summary info
			AgentInfo[] agents = op.getAllAgentsByVendor(false);
			for (int i=0; i < agents.length; i++)
			{
				totalPartners++;
				int status = agents[i].getStatus();
				if (status == AgentInfo.STATUS_LIVE)
					livePartners++;
				else if (status == AgentInfo.STATUS_REGISTERED)
					registeredPartners++;
				else if (status == AgentInfo.STATUS_DISABLED)
					disabledPartners++;
				else
					unregisteredPartners++;
				
				if (agents[i].getAms() != null && agents[i].getAms().getId() != null)
				{
					// update the client/browser app counts
					if (vendorId.equals(""))
					{
						// ignore
					}
					else if (agents[i].isClientAppRegistered())
					{
						clientUsers = true;
						clientAppCount++;
					}
					else
					{
						browserUsers = true;
					}
					
					// update the agency counts by vendor system
					if (agents[i].getAms().getId().equals(vendorId))
						vendorCount++;
					else
					{
						if (!vendorId.equals(""))
						{
							vendorNameList.add(vendorName);
							vendorCountHash.put(vendorName, String.valueOf(vendorCount));
							vendorClientCountHash.put(vendorName, String.valueOf(clientAppCount));
						}
						
						// Reset
						vendorId = agents[i].getAms().getId();
						vendorName = agents[i].getAms().getDisplayName();
						vendorHash.put(vendorId, vendorName);
						vendorCount = 1;
						clientAppCount = 0;
					}
				}
			}
			if (!vendorId.equals(""))
			{
				vendorNameList.add(vendorName);
				vendorCountHash.put(vendorName, String.valueOf(vendorCount));
				vendorClientCountHash.put(vendorName, String.valueOf(clientAppCount));
			}
			
			// Sort vendor systems alphabetically
			Collections.sort(vendorNameList);
			
			// Get the participant code summary info, sorted by agent vendor list
			ParticipantSummaryInfo[] participants = op.getParticipantSummaryList(false, DatabaseObject.colAgentAmsId);
			totalParticipants = participants.length;
			vendorId = "";
			vendorName = "";
			vendorCount = 0;
			for (int i=0; i < participants.length; i++)
			{
				int status = participants[i].getStatus();
				if (status == AgentInfo.STATUS_LIVE)
					liveParticipants++;
				else if (status == AgentInfo.STATUS_REGISTERED)
					registeredParticipants++;
				else if (status == AgentInfo.STATUS_DISABLED)
					disabledParticipants++;
				else
					unregisteredParticipants++;
				
				if (participants[i].getVendorId() != null && !participants[i].getVendorId().equals(""))
				{
					if (participants[i].getVendorId().equals(vendorId))
						vendorCount++;
					else
					{
						if (!vendorId.equals(""))
							vendorPartHash.put(vendorName, String.valueOf(vendorCount));
						
						// Reset
						vendorId = participants[i].getVendorId();
						vendorName = (String) vendorHash.get(vendorId);
						if (vendorName == null)
							vendorName = vendorId;
						vendorCount = 1;
					}
				}
			}
			if (!vendorId.equals(""))
			{
				vendorPartHash.put(vendorName, String.valueOf(vendorCount));
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error occurred building the agent summary report", e);
			throw new DisplayBeanException("Error occurred building the agent summary report", e);
		}
			
		// Calculate the total number of trading partners
		totalIDs = totalPartners + totalParticipants;
		
			
		// Get the Trading Partner List sort order from the request (if applicable)
		tpSortOrder = req.getParameter("current_sort");
		if (tpSortOrder == null)
			tpSortOrder = "";
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
	 * Returns the carrier info bean.
	 * @return CarrierInfo
	 */
	public CarrierInfo getCarrierInfo()
	{
		return carrierInfo;
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
	 * Returns the total number of disabled trading partners.
	 * @return int
	 */
	public int getDisabledPartners()
	{
		return disabledPartners;
	}

	/**
	 * Returns the number of inactive and unregistered trading partners.
	 * @return int
	 */
	public int getUnregisteredPartners()
	{
		return unregisteredPartners;
	}

	/**
	 * Returns the number of live trading partners.
	 * @return int
	 */
	public int getLivePartners()
	{
		return livePartners;
	}

	/**
	 * Returns the number of registered (but not live) trading partners.
	 * @return int
	 */
	public int getRegisteredPartners()
	{
		return registeredPartners;
	}

	/**
	 * Returns the total number of trading partners.
	 * @return int
	 */
	public int getTotalPartners()
	{
		return totalPartners;
	}

	/**
	 * Returns the current Trading Partner List sort order.
	 * @return String
	 */
	public String getTpSortOrder()
	{
		return tpSortOrder;
	}

	/**
	 * Returns the number of registered (but not live) participants (other than primary participant codes).
	 * @return int
	 */
	public int getRegisteredParticipants()
	{
		return registeredParticipants;
	}

	/**
	 * Returns the number of disabled participants (other than primary participant codes).
	 * @return int
	 */
	public int getDisabledParticipants()
	{
		return disabledParticipants;
	}

	/**
	 * Returns the number of inactive and unregistered participants (other than primary participant codes).
	 * @return int
	 */
	public int getUnregisteredParticipants()
	{
		return unregisteredParticipants;
	}

	/**
	 * Returns the number of live participants (other than primary participant codes).
	 * @return int
	 */
	public int getLiveParticipants()
	{
		return liveParticipants;
	}

	/**
	 * Returns the total number of participants (non-primary participant codes).
	 * @return int
	 */
	public int getTotalParticipants()
	{
		return totalParticipants;
	}

	/**
	 * Returns the total number of IDs (both primary agents and participant codes).
	 * @return int
	 */
	public int getTotalIDs()
	{
		return totalIDs;
	}

	/**
	 * Returns the total number of vendor systems currently used by download agents.
	 * @return int
	 */
	public int getVendorCount()
	{
		return vendorNameList.size();
	}

	/**
	 * Returns the specified vendor system name.
	 * @return String
	 */
	public String getVendorName(int index)
	{
		if (index >= vendorNameList.size())
			return "";
		return (String) vendorNameList.get(index);
	}

	/**
	 * Returns the number of agents using the specified vendor system.
	 * @return String
	 */
	public String getVendorAgentCount(int index)
	{
		if (index >= vendorNameList.size())
			return "";
		String str = (String) vendorCountHash.get(getVendorName(index));
		if (str == null)
			str = "";
		return str;
	}

	/**
	 * Returns the number of agents using the specified vendor system who have NOT registered with the client app.
	 * @return String
	 */
	public String getVendorBrowserAppCount(int index)
	{
		int agents = 0;
		int clientReg = 0;
		try
		{
			agents = Integer.parseInt(getVendorAgentCount(index));
		} catch (Exception e) {
			LOGGER.error(e);
		}
		try
		{
			clientReg = Integer.parseInt(getVendorClientAppCount(index));
		} catch (Exception e) {
			LOGGER.error(e);
		}
		
		int ret = agents - clientReg;
		if (ret < 0)
			return "0";
		return String.valueOf(ret);
	}

	/**
	 * Returns the number of agents using the specified vendor system who have registered with the client app.
	 * @return String
	 */
	public String getVendorClientAppCount(int index)
	{
		if (index >= vendorNameList.size())
			return "";
		String str = (String) vendorClientCountHash.get(getVendorName(index));
		if (str == null)
			str = "0";
		return str;
	}

	/**
	 * Returns the number of (non-primary) participants using the specified vendor system.
	 * @return String
	 */
	public String getVendorParticipantCount(int index)
	{
		if (index >= vendorNameList.size())
			return "";
		String str = (String) vendorPartHash.get(getVendorName(index));
		if (str == null)
			str = "0";
		return str;
	}

	/**
	 * Returns the total number of agents and (non-primary) participants using the specified vendor system.
	 * @return int
	 */
	public int getVendorTotalCount(int index)
	{
		int count = 0;
		try
		{
			count += Integer.parseInt(getVendorAgentCount(index));
		} catch (Exception e) {
			LOGGER.error(e);
		}
		try
		{
			count += Integer.parseInt(getVendorParticipantCount(index));
		} catch (Exception e) {
			LOGGER.error(e);
		}
		
		return count;
	}

	/**
	 * Returns 'true' if any agents are still configured to use the browser app.
	 * @return boolean
	 */
	public boolean isBrowserUsers()
	{
		return browserUsers;
	}

	/**
	 * Returns 'true' if any agents are configured to use the client app.
	 * @return boolean
	 */
	public boolean isClientUsers()
	{
		return clientUsers;
	}

}
