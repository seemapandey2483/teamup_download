package connective.teamup.download.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
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
import connective.teamup.download.db.AgentSummaryInfo;
import connective.teamup.download.db.DatabaseOperation;


/**
 * @author Adeel Raza
 */
public class LogParamDisplayBean implements Serializable, DisplayBean
{
	private static final Logger LOGGER = Logger.getLogger(LogParamDisplayBean.class);
	
	private AgentSummaryInfo[] agentsList = null;
	private ArrayList years = new ArrayList();
	
	private CarrierInfo carrierInfo = null;
	private int implementationYear = 0;
	
	private String agentId = "";
	private String searchStr = "";
	private String searchMethod = "agt_id";
	private String errorMsg = null;
	private String currentDate = "";
	private int listSelection = -1;
	private int currentYear = 0;
	private int currentMonth = 0;
	private int currentDay = 0;
	private int startYear = 0;
	private int startMonth = 0;
	private int startDay = 0;
	
	private String tpSortOrder = "";
	

	/**
	 * Constructor for LogParamDisplayBean.
	 */
	public LogParamDisplayBean()
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
			// Load the carrier info
			carrierInfo = serverInfo.getCarrierInfo();
			implementationYear = carrierInfo.getImplementationYear();
			
			Date today = new Date(System.currentTimeMillis());
			currentDate = today.toString().trim();
			
			// Get current system date
			Calendar cal = Calendar.getInstance();
			currentMonth = cal.get(Calendar.MONTH) + 1;
			currentDay = cal.get(Calendar.DAY_OF_MONTH);
			currentYear = cal.get(Calendar.YEAR);
			
			// Get yesterday's date
			cal.add(Calendar.DATE, -1);
			startMonth = cal.get(Calendar.MONTH) + 1;
			startDay = cal.get(Calendar.DAY_OF_MONTH);
			startYear = cal.get(Calendar.YEAR);
	
			for (int i = implementationYear; i <= currentYear; i++)
			{
				if (i > currentYear)
					break;
				else
					years.add(Integer.toString(i));
			}
			
			agentId = req.getParameter("agentID");
			if (agentId == null || agentId.equals(""))
			{
				AgentInfo agentBean = serverInfo.getAgentInfo(req.getSession(), op);
				if (agentBean != null)
					agentId = agentBean.getAgentId();
				else
					agentId = "";
			}
			
			String sourcePage = req.getParameter("src_page");
			if (sourcePage != null && sourcePage.equals("agytrans_param"))
			{
				// Retrieve a list of agents most closely matching the search string entered
				searchStr = req.getParameter("search_str");
				if (searchStr == null)
					searchStr = "";
				
				String searchType = req.getParameter("search_by");
				if (searchType != null && searchType.equalsIgnoreCase("agt_name"))
				{
					// search by agent name
					searchMethod = searchType;
					String firstLetter = "";
					if (searchStr.length() > 0)
						firstLetter = String.valueOf(searchStr.charAt(0)).toUpperCase();
					agentsList = op.getAgentsByAlpha(firstLetter, 0, -1);
					if (agentsList != null && agentsList.length > 0)
					{
						for (int i=0; i < agentsList.length; i++)
						{
							if (searchStr.equalsIgnoreCase(agentsList[i].getAgentName()))
								listSelection = i;
							else if (i < 0 && searchStr.length() < agentsList[i].getAgentName().length() &&
									 searchStr.equalsIgnoreCase(agentsList[i].getAgentName().substring(0, searchStr.length())))
							{
								listSelection = i;
							}
						}
					}
					else
					{
						errorMsg = "No trading partners were found for the specified search parameters";
					}
				}
				else
				{
					// search by agent ID
					agentsList = op.getAgentsByParticipant(searchStr, 50);
				}
				if (listSelection < 0)
					listSelection = 0;
			}
			else if ((searchStr == null || searchStr.equals("")) && agentId != null)
			{
				searchStr = agentId;
			}
			
			// Get the Trading Partner List sort order from the request (if applicable)
			tpSortOrder = req.getParameter("current_sort");
			if (tpSortOrder == null)
				tpSortOrder = "";
		}
		catch (Exception e)
		{
			LOGGER.error("Error occurred building the report parameters page", e);
			throw new DisplayBeanException("Error occurred building the report parameters page", e);
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
	 * Returns the number of agents retrieved for the select box.
	 */
	public int getAgencyCount()
	{
		if (agentsList == null)
			return 0;
		return agentsList.length;
	}

	/**
	 * Returns the specified agent's ID and name.
	 */
	public String getAgencyNameDisplay(int index) 
	{
		String ret = "";
		
		if (agentsList != null && index < agentsList.length)
			ret = agentsList[index].getParticipantCode() + " - " + agentsList[index].getAgentName();
		return ret;
	}

	/**
	 * Returns the specified agent ID.
	 * @return String
	 */
	public String getAgencyId(int index) 
	{
		if (agentsList == null || index >= agentsList.length)
			return "";
		return agentsList[index].getAgentId();

	}

	/**
	 * Returns the previously specified agent ID.
	 */
	public String getAgentId()
	{
		return agentId;
	}

	/**
	 * Returns the current date.
	 */
	public String getCurrentDate()
	{
		return currentDate;
	}

	/**
	 * Returns the current year.
	 */
	public int getCurrentYear()
	{
		return currentYear;
	}

	/**
	 * Returns the list of years to be displayed in the select box.
	 */
	public ArrayList getYears()
	{
		return years;
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
	 * Returns the startDay.
	 * @return String
	 */
	public String getStartDay()
	{
		String day = String.valueOf(startDay).trim();
		
		if (startDay == 0)
			day = "";
		else if (startDay < 10)
			day = "0" + day;
			
		return day;
	}

	/**
	 * Returns the startMonth.
	 * @return String
	 */
	public String getStartMonth()
	{
		String month = String.valueOf(startMonth).trim();
		
		if (startMonth == 0)
			month = "";
		else if (startMonth < 10)
			month = "0" + month;
			
		return month;
	}

	/**
	 * Returns the startYear.
	 * @return int
	 */
	public int getStartYear() {
		return startYear;
	}

	/**
	 * Returns the currentDay.
	 * @return String
	 */
	public String getCurrentDay()
	{
		String day = String.valueOf(currentDay).trim();
		
		if (currentDay == 0)
			day = "";
		else if (currentDay < 10)
			day = "0" + day;
			
		return day;
	}

	/**
	 * Returns the currentMonth.
	 * @return String
	 */
	public String getCurrentMonth()
	{
		String month = String.valueOf(currentMonth).trim();
		
		if (currentMonth  == 0)
			month = "";
		else if (currentMonth < 10)
			month = "0" + month;
			
		return month;
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
	 * @return Returns the error message (if applicable).
	 */
	public String getErrorMsg()
	{
		return errorMsg;
	}

	/**
	 * @return Returns the index of the list item that should be selected at load time (or -1 for none).
	 */
	public int getListSelection()
	{
		return listSelection;
	}

	/**
	 * @return Returns the current search string.
	 */
	public String getSearchStr()
	{
		String ret = searchStr;
		if (searchStr == null)
			ret = "";
		else if (searchStr.length() > 10)
			ret = searchStr.substring(0, 10);
		return ret;
	}

	/**
	 * @return Returns the current search method.
	 */
	public String getSearchMethod()
	{
		return searchMethod;
	}
}
