package connective.teamup.download.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.GregorianCalendar;

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
import connective.teamup.download.db.LogInfo;


/**
 * @author Adeel Raza
 */
public class LogInfoDisplayBean implements Serializable, DisplayBean
{
	private static final Logger LOGGER = Logger.getLogger(LogInfoDisplayBean.class);
	
	private LogInfo[] logs = null;
	
	private CarrierInfo carrierInfo = null;
	
	private String eventType = null;
	private String strAgentID = "";
	private String strAgencyName = "";
	private String strStartDT = "";
	private String strEndDT = "";
	private String strOrderBy = "";
	private String agentId = "";
	private String startDateFormatted = "";
	private String endDateFormatted = "";
	private String tpSortOrder = "";
	

	/**
	 * Constructor for LogInfoDisplayBean.
	 */
	public LogInfoDisplayBean()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.DisplayBean#init(HttpServletRequest, HttpServletResponse, ServerInfo)
	 */
	public void init(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws DisplayBeanException
	{
		try
		{
			// Load the company info
			carrierInfo = serverInfo.getCarrierInfo();
			
			// Load the agent info
			String sourcePage = req.getParameter("src_page");
			if (sourcePage != null && sourcePage.equalsIgnoreCase("agytrans_param"))
			{
				strAgentID = req.getParameter("agentID");
				if (strAgentID == null || strAgentID.equals(""))
					strAgentID = req.getParameter("search_str");
			}
			else
				strAgentID = req.getParameter("agyName");
			AgentInfo agent = op.getAgentInfo(strAgentID);
			if (agent != null)
				strAgentID = agent.getAgentId();
			strAgencyName = agent.getName();
			
			// Parse the report parameters from the request
			setStartDate(req.getParameter("startdt"));
			setEndDate(req.getParameter("enddt"));
			setOrderBy(req.getParameter("orderby"));
			
			// Retrieve the transaction log entries
			loadTransactionLog(op);
			
			// Get the Trading Partner List sort order from the request (if applicable)
			tpSortOrder = req.getParameter("current_sort");
			if (tpSortOrder == null)
				tpSortOrder = "";
		}
		catch (SQLException e)
		{
			LOGGER.error("Error occurred building the transaction log report", e);
			throw new DisplayBeanException("Error occurred building the transaction log report", e);
		}
	}
	
	public void setStartDate(String startDate)
	{
		if (startDate != null)
		{
			strStartDT = startDate;
			startDateFormatted = startDate.substring(5, 7) + "/" + startDate.substring(8) + "/" + startDate.substring(0, 4);
		}
	}
	
	public void setEndDate(String endDate)
	{
		if (endDate != null)
		{
			strEndDT = endDate;
			endDateFormatted = endDate.substring(5, 7) + "/" + endDate.substring(8) + "/" + endDate.substring(0, 4);
		}
	}
	
	public void setOrderBy(String orderBy)
	{
		strOrderBy = orderBy;
	}

	/**
	 * Loads the relevent transactions from the log. <p>
	 * <b>NOTE:</b> The start and end date parameters <b>must</b> be set before calling
	 * this method.
	 */
	public void loadTransactionLog(DatabaseOperation op) throws SQLException
	{
		// use calendars to get the correct date value for logs
		int startYear = Integer.parseInt(strStartDT.substring(0, 4));
		int startMonth =  Integer.parseInt(strStartDT.substring(5, 7));
		int startDay = Integer.parseInt(strStartDT.substring(8)); 
		GregorianCalendar calStart = new GregorianCalendar(startYear, startMonth - 1, startDay, 0, 0, 0);
			
		int endYear = Integer.parseInt(strEndDT.substring(0, 4));
		int endMonth =  Integer.parseInt(strEndDT.substring(5, 7));
		int endDay = Integer.parseInt(strEndDT.substring(8)); 
		GregorianCalendar calEnd = new GregorianCalendar(endYear, endMonth - 1, endDay, 23, 59, 59);

		logs = op.getLogsByEventType(strAgentID, calStart.getTime().getTime(), calEnd.getTime().getTime(), strOrderBy, eventType);
	}

	/**
	 * Returns the carrierEmail.
	 * @return String
	 */
	public String getCarrierEmail()
	{
		return carrierInfo.getContactEmail();
	}

	/**
	 * Returns the carrierName.
	 * @return String
	 */
	public String getCarrierName()
	{
		return carrierInfo.getName();
	}

	/**
	 * Returns the carrierShortName.
	 * @return String
	 */
	public String getCarrierShortName()
	{
		return carrierInfo.getShortName();
	}
	
	/**
	 * Returns the agencyName.
	 * @return String
	 */
	public String getAgentID(int index) 
	{
		return logs[index].getAgentID();
	}
	
	/**
	 * Returns the batchnum.
	 * @return String
	 */
	public String getBatchnum(int index)
	{
		if (logs[index].getBatchnum() == null || logs[index].getBatchnum().equals("0"))
			return "";
		else
			return logs[index].getBatchnum();
	}

	/**
	 * Returns the created_date.
	 * @return String
	 */
	public String getCreated_date(int index) 
	{
		return logs[index].getCreatedDateStrLong();
	}
	
	/**
	 * Returns the created_date.
	 * @return String
	 */
	public String getCreated_dateDisplay(int index) 
	{
		return logs[index].getCreatedDateStrShort();
	}
	
	/**
	 * Returns the log description.
	 * @return String
	 */
	public String getDescription(int index)
	{
		return logs[index].getDescription();
	}

	/**
	 * Returns the event_type.
	 * @return String
	 */
	public String getEvent_type(int index) 
	{
//		return logs[index].getEvent_type();
		return logs[index].getEvent_typeDesc();
	}

	/**
	 * Returns the fileName.
	 * @return String
	 */
	public String getFileName(int index) 
	{
		return logs[index].getFilenameForDisplay();
	}

	/**
	 * Returns the imported_date.
	 * @return String
	 */
	public String getImported_date(int index) 
	{
		return logs[index].getImportedDateStrLong();
	}

	/**
	 * Returns the imported_date.
	 * @return String
	 */
	public String getImported_dateDisplay(int index) 
	{
		return logs[index].getImportedDateStrShort();
	}

	/**
	 * Returns the log_date.
	 * @return String
	 */
	public String getLog_date(int index) 
	{
		return logs[index].getLogDateStrLong();
	}

	/**
	 * Returns the log_date.
	 * @return String
	 */
	public String getLog_dateDisplay(int index) 
	{
		return logs[index].getLogDateStrShort();
	}

	/**
	 * Returns the origFileName.
	 * @return String
	 */
	public String getOrigFileName(int index) 
	{
		return logs[index].getOrigFileName();
	}

	/**
	 * Returns the status.
	 * @return String
	 */
	public String getStatus(int index) 
	{
		return logs[index].getEventStatus().getDescription();
	}

	public boolean isFailed(int index)
	{
//		String statusCode = logs[index].getEventStatus().getSimpleStatusFlag();
//		return (statusCode != null && statusCode.equals("F"));
		return !logs[index].getEventStatus().isSuccessful();
	}

	public int getAppCount()
	{
		return logs.length;
	}
	

	/**
	 * Returns the strAgentID.
	 * @return String
	 */
	public String getStrAgentID() {
		return strAgentID;
	}

	/**
	 * Returns the strEndDT.
	 * @return String
	 */
	public String getStrEndDT() {
		return strEndDT;
	}

	/**
	 * Returns the strOrderBy.
	 * @return String
	 */
	public String getStrOrderBy() {
		return strOrderBy;
	}

	/**
	 * Returns the strStartDT.
	 * @return String
	 */
	public String getStrStartDT() {
		return strStartDT;
	}

	/**
	 * Returns the strAgencyName.
	 * @return String
	 */
	public String getStrAgencyName() {
		return strAgencyName;
	}

	/**
	 * Returns the agentId.
	 * @return String
	 */
	public String getAgentId() {
		return agentId;
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
	 * Returns the end date formatted as "mm/dd/yyyy".
	 * @return String
	 */
	public String getEndDateFormatted()
	{
		return endDateFormatted;
	}

	/**
	 * Returns the start date formatted as "mm/dd/yyyy".
	 * @return String
	 */
	public String getStartDateFormatted()
	 {
		return startDateFormatted;
	}

	/**
	 * Returns the event type.
	 * @return String
	 */
	public String getEventType()
	{
		return eventType;
	}

	/**
	 * Sets the event type.
	 * @param eventType The event type to set
	 */
	public void setEventType(String eventType)
	{
		this.eventType = eventType;
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
	 * @param string
	 */
	public void setAgentId(String string)
	{
		agentId = string;
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
	public void setStrAgencyName(String string)
	{
		strAgencyName = string;
	}

	/**
	 * @param string
	 */
	public void setStrAgentID(String string)
	{
		strAgentID = string;
	}

}
