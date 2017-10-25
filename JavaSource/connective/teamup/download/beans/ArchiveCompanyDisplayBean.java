package connective.teamup.download.beans;

import java.io.Serializable;
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
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.DownloadStatus;
import connective.teamup.download.db.FileInfo;

/**
 * @author Kyle McCreary
 *
 * Display bean used for displaying the list of current and archived download
 * files for a specified trading partner.  This display bean is used solely for
 * the version of the archive page used in the Company Admin application.
 */
public class ArchiveCompanyDisplayBean implements Serializable, DisplayBean
{
	private static final Logger LOGGER = Logger.getLogger(ArchiveCompanyDisplayBean.class);
	
	private String agentId = null;
	private String agentName = null;
	private boolean transShown = false;
	private boolean participantUsed = false;
	
	private String tpSortOrder = "";
	private String originatingPage = "";
	private CarrierInfo carrierInfo = null;
	
	private Vector archiveFiles = null;
	
	private String statusArchive = DownloadStatus.ARCHIVED.getCode();
	private String statusCurrent = DownloadStatus.CURRENT.getCode();
	private String statusDBArchive = DownloadStatus.DB_ARCHIVED.getCode();
	private String statusDBCurrent = DownloadStatus.DB_CURRENT.getCode();
	private String statusDownload = DownloadStatus.POLICY_DOWNLOAD.getCode();
	private String statusResend = DownloadStatus.RESEND.getCode();
	

	/**
	 * Constructor for ArchiveCompanyDisplayBean.
	 */
	public ArchiveCompanyDisplayBean()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.DisplayBean#init(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public void init(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws DisplayBeanException
	{
		// Load carrier info
		this.carrierInfo = serverInfo.getCarrierInfo();

		archiveFiles = new Vector();
		
		try
		{
			// Get the selected trading partner info
			AgentInfo agentBean = serverInfo.getAgentInfo(req.getSession(), op);
			if (agentBean == null)
			{
				String agentID = req.getParameter("agentID");
				agentBean = op.getAgentInfo(agentID);
				req.getSession().setAttribute("agentId", agentID);
			}
			agentId = agentBean.getAgentId();
			agentName = agentBean.getName();
			
			// Get the list of archived files from the database
		/*	FileInfo[] files = op.getAgentFiles(agentId);
			for (int i=0; i < files.length; i++)
			{
				if (!files[i].getDownloadStatus().equals(DownloadStatus.TEST))
				{
					if (transShown)
						files[i].loadTransFromDb();
						
					archiveFiles.addElement(files[i]);
				}
				
				String partCode = files[i].getParticipantCode();
				if (partCode != null && !partCode.equals("") && !partCode.equals(files[i].getAgentId()))
					participantUsed = true;
			}*/
				
			// Get the Trading Partner List sort order from the request (if applicable)
			tpSortOrder = req.getParameter("current_sort");
			if (tpSortOrder == null)
				tpSortOrder = "";
		}
		catch (Exception e)
		{
			LOGGER.error("Error occurred loading the file archive display bean", e);
			throw new DisplayBeanException("Error occurred loading the file archive display bean", e);
		}
	}

	/**
	 * Returns the agentId.
	 * @return String
	 */
	public String getAgentId()
	{
		return agentId;
	}

	/**
	 * Returns the agentName.
	 * @return String
	 */
	public String getAgentName()
	{
		return agentName;
	}
	
	/**
	 * Returns the number of archived files.
	 * @return int
	 */
	public int getArchiveFilesCount()
	{
		if (archiveFiles == null)
			return 0;
		else
			return archiveFiles.size();
	}
	
	/**
	 * Returns the specified file info.
	 * @return FileInfo
	 */
	public FileInfo getArchiveFile(int index)
	{
		return (FileInfo) archiveFiles.elementAt(index);
	}

	/**
	 * Returns true if the files individual transactions should be shown.
	 * @return boolean
	 */
	public boolean isTransShown()
	{
		return transShown;
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
	 * Returns the carrier's full name.
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
	 * Returns the originating page.
	 * @return String
	 */
	public String getOriginatingPage()
	{
		return originatingPage;
	}

	/**
	 * Sets the originating page.
	 * @param originatingPage The page to set
	 */
	public void setOriginatingPage(String originatingPage)
	{
		if (originatingPage == null)
			this.originatingPage = "";
		else
			this.originatingPage = originatingPage;
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
	 * Returns the carrier info bean.
	 * @return CarrierInfo
	 */
	public CarrierInfo getCarrierInfo()
	{
		return carrierInfo;
	}
	
	/**
	 * Sets the 'show transactions' flag.
	 * @param showTrans True to show transactions, false to hide
	 */
	public void showTransactions(boolean showTrans)
	{
		this.transShown = showTrans;
	}

	/**
	 * Returns the "archive" status code.
	 * @return String
	 */
	public String getStatusArchive()
	{
		return statusArchive;
	}

	/**
	 * Returns the "current" status code.
	 * @return String
	 */
	public String getStatusCurrent()
	{
		return statusCurrent;
	}

	/**
	 * Returns the "download" status code.
	 * @return String
	 */
	public String getStatusDownload()
	{
		return statusDownload;
	}

	/**
	 * Returns the "resend" status code.
	 * @return String
	 */
	public String getStatusResend()
	{
		return statusResend;
	}

	/**
	 * Returns the "direct bill archive" status code.
	 * @return String
	 */
	public String getStatusDBArchive()
	{
		return statusDBArchive;
	}

	/**
	 * Returns the "direct bill current" status code.
	 */
	public String getStatusDBCurrent()
	{
		return statusDBCurrent;
	}

	/**
	 * Returns true if file or transaction status is not "archived".
	 * @return boolean
	 */
	public boolean isDownloadable(DownloadStatus status)
	{
		return (!status.equals(DownloadStatus.ARCHIVED) &&
				!status.equals(DownloadStatus.DB_ARCHIVED));
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

}
