package connective.teamup.download.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;

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
import connective.teamup.download.db.DownloadStatus;
import connective.teamup.download.db.FileInfo;

/**
 * @author Kyle McCreary
 *
 */
public class TradingPartnerListDisplayBean implements Serializable, DisplayBean
{
	private static final Logger LOGGER = Logger.getLogger(TradingPartnerListDisplayBean.class);
	
	private String lastSystem = "";
	private int sortOrder = 0;
	
	private AgentInfo[] agents = null;
	private boolean transShown = false;
	private String agentId = null;
	private String agentName = null;
	private String originatingPage = "";
	private String tpSortOrder = "";
	private boolean participantUsed = false;
	
	private CarrierInfo carrierInfo = null;
	private Vector archiveFiles = null;
	private String statusArchive = DownloadStatus.ARCHIVED.getCode();
	private String statusDBCurrent = DownloadStatus.DB_CURRENT.getCode();
	private String statusDownload = DownloadStatus.POLICY_DOWNLOAD.getCode();
	private String statusDBArchive = DownloadStatus.DB_ARCHIVED.getCode();
	/**
	 * Constructor for TradingPartnerListDisplayBean.
	 */
	public TradingPartnerListDisplayBean()
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
			
			// Load a hashtable with all ams vendor info
			/*Hashtable amsHash = new Hashtable();
			AmsInfo[] amslist = op.getAmsInfoList();
			for (int i=0; i < amslist.length; i++)
				amsHash.put(amslist[i].getId(), amslist[i]);
			
			// Get the sort order from the request
			sortOrder = ServerInfo.TPSORT_AGENTID;		// default sort order
			try
			{
				String newSort = req.getParameter("sort_order");
				if (newSort == null || newSort.equals(""))
					newSort = req.getParameter("current_sort");
				sortOrder = Integer.parseInt(newSort);
			}
			catch (Exception e) {}
			
			// Load the trading partner info using the specified sort order
//			AgentInfo[] ais = null;
			if (sortOrder == ServerInfo.TPSORT_NAME)
				agents = op.getAllAgentsByName(true);
			else if (sortOrder == ServerInfo.TPSORT_STATUS)
				agents = op.getAllAgentsByStatus(true);
			else if (sortOrder == ServerInfo.TPSORT_VENDOR)
				agents = op.getAllAgentsByVendor(true);
			else if (sortOrder == ServerInfo.TPSORT_DL_APP)
				agents = op.getAllAgentsByDownloadApp(true);
			else*/
				//agents = op.getAllAgents(true);
			
			// Load the trading partner info
//			for (int i=0; i < ais.length; i++)
//				agents.addElement(ais[i]);
			
			// Save the current view as the user's default TP list view
			serverInfo.setTPListView(req.getSession(), ServerInfo.TPLIST_ALL);
//			serverInfo.resetTPListView(req.getSession());
			if (carrierInfo.getImportFileCreator().equalsIgnoreCase("KEYLINK"))
				req.setAttribute("keyLinkFile", "Y");
			else
				req.setAttribute("keyLinkFile", "N");
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
	public AgentInfo getAgent(int index)
	{
		if (index > agents.length)
			return null;
		
		return agents[index];
	}
	
	/**
	 * Returns the status of the specified agent.
	 * @return String
	 */
	public String getAgentStatus(int index)
	{
		if (index > agents.length)
			return "";
		
		String status = agents[index].getStatusDisplay();
		if (agents[index].isTestAgent())
			status = "TEST: " + status;
		return status;
	}
	
	/**
	 * Returns the specified agent's vendor system name and version.
	 * @return String
	 */
	public String getAgentVendorSystem(int index)
	{
		String vendor;
		
		if (index > agents.length || agents[index].getAms() == null ||
			agents[index].getAms().getName() == null)
		{
			vendor = "";
		}
		else
		{
			vendor = agents[index].getAms().getName();
			if (agents[index].getAmsVer() != null && !agents[index].getAmsVer().equals(""))
				vendor += " " + agents[index].getAmsVer();
			
			if (sortOrder == ServerInfo.TPSORT_VENDOR &&
				agents[index].getAms().getNote() != null && 
				!agents[index].getAms().getNote().trim().equals("") &&
				!agents[index].getAms().getDisplayName().equals(lastSystem))
			{
				vendor += " (" + agents[index].getAms().getName() + " " + agents[index].getAms().getNote() + ")";
				lastSystem = agents[index].getAms().getDisplayName();
			}
		}
		
		return vendor;
	}

	/**
	 * Returns the current sort order.
	 * @return int
	 */
	public int getSortOrder()
	{
		return sortOrder;
	}
	
	/**
	 * Returns the sort order for the specified column.
	 * @param column The column name
	 * @return int
	 */
	public int getColumnSort(String column)
	{
		int order = 0;
		
		if (column == null || column.equals(""))
			order = sortOrder;
		else if (column.equalsIgnoreCase("id"))
			order = ServerInfo.TPSORT_AGENTID;
		else if (column.equalsIgnoreCase("name"))
			order = ServerInfo.TPSORT_NAME;
		else if (column.equalsIgnoreCase("status"))
			order = ServerInfo.TPSORT_STATUS;
		else if (column.equalsIgnoreCase("dldate"))
			order = ServerInfo.TPSORT_LASTDL_DATE;
		else if (column.equalsIgnoreCase("vendor"))
			order = ServerInfo.TPSORT_VENDOR;
		else if (column.equalsIgnoreCase("dlapp"))
			order = ServerInfo.TPSORT_DL_APP;
		
		return order;
	}
	
	/**
	 * Returns true if the specified column is the current sort order.
	 * @param column The column name
	 * @return boolean
	 */
	public boolean isCurrentSort(String column)
	{
		return (getColumnSort(column) == sortOrder);
	}

	/**
	 * Returns the carrier info bean.
	 * @return CarrierInfo
	 */
	public CarrierInfo getCarrierInfo()
	{
		return carrierInfo;
	}
	
	public int getArchiveFilesCount()
	{
		if (archiveFiles == null)
			return 0;
		else
			return archiveFiles.size();
	}
	
	public String getStatusArchive()
	{
		return statusArchive;
	}
	
	public String getStatusDBCurrent()
	{
		return statusDBCurrent;
	}
	
	public String getStatusDownload()
	{
		return statusDownload;
	}
	
	public boolean isTransShown()
	{
		return transShown;
	}
	
	public FileInfo getArchiveFile(int index)
	{
		return (FileInfo) archiveFiles.elementAt(index);
	}
	
	public String getStatusDBArchive()
	{
		return statusDBArchive;
	}
	
	public String getAgentId()
	{
		return agentId;
	}
	
	public String getOriginatingPage()
	{
		return originatingPage;
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
	 * Returns true if participant codes are used for any files in this archive.
	 * @return boolean
	 */
	public boolean isParticipantUsed()
	{
		return participantUsed;
	}
	
	/**
	 * Returns the agentName.
	 * @return String
	 */
	public String getAgentName()
	{
		return agentName;
	}

}
