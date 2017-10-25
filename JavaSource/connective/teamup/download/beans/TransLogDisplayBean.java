package connective.teamup.download.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import connective.teamup.download.ws.objects.DownloadFileInfo;


/**
 * @author Kyle McCreary
 */
public class TransLogDisplayBean implements Serializable, DisplayBean
{
	private static final Logger LOGGER = Logger.getLogger(TransLogDisplayBean.class);
	
	private CarrierInfo carrierInfo = null;
	private String carrierName = "";
	private String agentId = "";
	private String agencyName = "";
	private String dateRange = "";
	private String sortOrder = "";
	private String reportAction = "";
	private String startDate = "";
	private String endDate = "";
	private boolean participantUsed = false;
	private DownloadFileInfo[] files = null;
	
	private SimpleDateFormat df = null;
	private SimpleDateFormat dfShort = null;
	private SimpleDateFormat dfLong = null;


	/**
	 * Constructor for TransLogDisplayBean.
	 */
	public TransLogDisplayBean()
	{
		super();
		
		// create the date formatters
		df = (SimpleDateFormat) SimpleDateFormat.getInstance();
		df.applyPattern("MM/dd/yyyy HH:mm");
		
		dfShort = (SimpleDateFormat) SimpleDateFormat.getInstance();
		dfShort.applyPattern("MM/dd/yyyy");

		dfLong = (SimpleDateFormat) SimpleDateFormat.getInstance();
		dfLong.applyPattern("MM/dd/yyyy HH:mm:ss.SSS");
	}

	/**
	 * @see connective.teamup.download.DisplayBean#init(HttpServletRequest, HttpServletResponse, ServerInfo)
	 */
	public void init(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws DisplayBeanException
	{
		try
		{
			// Load the company info
			carrierInfo = CarrierInfo.getInstance();
			carrierName = CarrierInfo.getInstance().getName();
			
			// Load the agent info
			agentId = req.getParameter("agentId");
			AgentInfo agent = op.getAgentInfo(agentId);
			if (agent != null)
				agencyName = agent.getName();
			
			reportAction = req.getParameter("action");
			startDate = req.getParameter("startdt");
			if (startDate == null)
				startDate = "";
			endDate = req.getParameter("enddt");
			if (endDate == null)
				endDate = "";
		}
		catch (SQLException e)
		{
			LOGGER.error("Error occurred retrieving the agent info", e);
			throw new DisplayBeanException("Error occurred retrieving the agent info", e);
		}
	}

	/**
	 * @return
	 */
	public String getAgencyName()
	{
		return agencyName;
	}

	/**
	 * @return
	 */
	public String getAgentId()
	{
		return agentId;
	}

	/**
	 * @return
	 */
	public String getCarrierName()
	{
		return carrierName;
	}

	/**
	 * @return
	 */
	public String getDateRange()
	{
		return dateRange;
	}

	/**
	 * @return
	 */
	public DownloadFileInfo getFile(int index)
	{
		return files[index];
	}

	public int getFileCount()
	{
		if (files == null)
			return 0;
		return files.length;
	}

	/**
	 * @param string
	 */
	public void setDateRange(String string)
	{
		dateRange = string;
	}

	/**
	 * @param files
	 */
	public void setFiles(DownloadFileInfo[] files)
	{
		this.files = files;
	}

	/**
	 * @return
	 */
	public boolean isParticipantUsed()
	{
		return participantUsed;
	}

	/**
	 * @param b
	 */
	public void setParticipantUsed(boolean b)
	{
		participantUsed = b;
	}

	public String formatDate(long date)
	{
		Date d = null;
		try
		{
			d = new Date(date);
		} catch (Exception e) {
			LOGGER.error(e);
		}
		
		if (d == null)
			return "";
		return df.format(d);
	}

	public String formatDateStrLong(long date)
	{
		if (date == 0)
			return "";
		return dfLong.format(new Date(date));
	}

	public String formatDateStrShort(long date)
	{
		if (date == 0)
			return "";
		return dfShort.format(new Date(date));
	}

	public CarrierInfo getCarrierInfo()
	{
		return carrierInfo;
	}

	/**
	 * @return
	 */
	public String getSortOrder()
	{
		if (sortOrder == null)
			return "";
		return sortOrder;
	}

	/**
	 * @param string
	 */
	public void setSortOrder(String string)
	{
		sortOrder = string;
	}

	/**
	 * @return
	 */
	public String getReportAction()
	{
		return reportAction;
	}

	/**
	 * @param string
	 */
	public void setReportAction(String string)
	{
		reportAction = string;
	}

	/**
	 * @return
	 */
	public String getEndDate()
	{
		return endDate;
	}

	/**
	 * @return
	 */
	public String getStartDate()
	{
		return startDate;
	}

	/**
	 * @param string
	 */
	public void setEndDate(String string)
	{
		endDate = string;
	}

	/**
	 * @param string
	 */
	public void setStartDate(String string)
	{
		startDate = string;
	}

}
