package connective.teamup.download.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.CarrierInfo;
import connective.teamup.download.DisplayBean;
import connective.teamup.download.DisplayBeanException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.DatabaseOperation;
import connective.teamup.download.db.LogInfo;


/**
 * @author Adeel Raza, Kyle McCreary
 *
 * Display bean to build the Consolidated Activity Report for all trading partners.
 */
public class ActivityReportDisplayBean implements Serializable, DisplayBean
{
	private static final Logger LOGGER = Logger.getLogger(ActivityReportDisplayBean.class);
	
	private LogInfo[] logs = null;
	private Hashtable agentHash = null;
	
	private CarrierInfo carrierInfo = null;
	
	private String eventType = null;
	private String strStartDT = "";
	private String strEndDT = "";
	private String strOrderBy = "";
	private String startDateFormatted = "";
	private String endDateFormatted = "";
	

	/**
	 * Constructor for LogInfoDisplayBean.
	 */
	public ActivityReportDisplayBean()
	{
		super();
	}

	/**
	 * @see connective.teamup.download.DisplayBean#init(HttpServletRequest, HttpServletResponse, ServerInfo, FileItem[])
	 */
	public void init(HttpServletRequest req, HttpServletResponse resp, ServerInfo serverInfo, DatabaseOperation op, FileItem[] items) throws DisplayBeanException
	{
		try
		{
			// Load the company info
			carrierInfo = serverInfo.getCarrierInfo();
			
			// Parse the report parameters from the request
			strStartDT = req.getParameter("startdt");
			strEndDT = req.getParameter("enddt");
			strOrderBy = req.getParameter("orderby");
			if (strOrderBy == null || strOrderBy.equals(""))
				strOrderBy = "AGENTID";
			eventType = req.getParameter("activity");
			if (eventType == null)
				eventType = "";

			startDateFormatted = strStartDT.substring(5, 7) + "/" + strStartDT.substring(8) + "/" + strStartDT.substring(0, 4);
			endDateFormatted = strEndDT.substring(5, 7) + "/" + strEndDT.substring(8) + "/" + strEndDT.substring(0, 4);

			// use calendars to get the correct date value for logs
			int startYear = Integer.parseInt(strStartDT.substring(0, 4));
			int startMonth =  Integer.parseInt(strStartDT.substring(5, 7));
			int startDay = Integer.parseInt(strStartDT.substring(8)); 
			GregorianCalendar calStart = new GregorianCalendar(startYear, startMonth - 1, startDay, 0, 0, 0);
			
			int endYear = Integer.parseInt(strEndDT.substring(0, 4));
			int endMonth =  Integer.parseInt(strEndDT.substring(5, 7));
			int endDay = Integer.parseInt(strEndDT.substring(8)); 
			GregorianCalendar calEnd = new GregorianCalendar(endYear, endMonth - 1, endDay, 23, 59, 59);
			
			logs = op.getLogsByEventType(null, calStart.getTime().getTime(), calEnd.getTime().getTime(), strOrderBy, eventType);
			
			// Load the hashtable of agent IDs and names
			agentHash = op.getAgentHashtable();
		}
		catch (SQLException e)
		{
			LOGGER.error("Error building consolidated activity report", e);
			throw new DisplayBeanException("Error building consolidated activity report", e);
		}
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
	 * Returns the agency ID.
	 * @return String
	 */
	public String getAgentID(int index) 
	{
		return logs[index].getAgentID();
	}
	
	/** Returns the agency name.
	 * @return String
	 */
	public String getAgentName(int index)
	{
		String name = (String) agentHash.get(getAgentID(index));
		if (name == null)
			return "";
		return name;
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

	public int getAppCount()
	{
		return logs.length;
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

}
