package connective.teamup.download.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import connective.teamup.download.CarrierInfo;
import connective.teamup.download.DisplayBean;
import connective.teamup.download.DisplayBeanException;
import connective.teamup.download.ServerInfo;
import connective.teamup.download.db.DatabaseOperation;


/**
 * @author Adeel Raza, Kyle McCreary
 *
 * Display bean for building the parameters page of the Consolidated Activity Report.
 */
public class ActivityParamDisplayBean implements Serializable, DisplayBean
{
	private static final Logger LOGGER = Logger.getLogger(ActivityParamDisplayBean.class);
	
	private Vector years = new Vector();
	
	private CarrierInfo carrierInfo = null;
	private int implementationYear = 0;
	private boolean detailedReport = false;
	
	private String currentDate = "";
	private int currentYear = 0;
	private int currentMonth = 0;
	private int currentDay = 0;
	private int startYear = 0;
	private int startMonth = 0;
	private int startDay = 0;
	

	/**
	 * Constructor for ActivityParamDisplayBean.
	 */
	public ActivityParamDisplayBean()
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
					years.addElement(Integer.toString(i));
			}
			
			// Check for which version of the report to display: regular or detailed
			String action = req.getParameter("action");
			if (action != null && action.endsWith("_det"))
				detailedReport = true;
		}
		catch (Exception e)
		{
			LOGGER.error("Error initializing report parameter options", e);
			throw new DisplayBeanException("Error initializing report parameter options", e);
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
	 * Returns the currentDate.
	 * @return Date
	 */
	public String getCurrentDate() {
		return currentDate;
	}

	/**
	 * Returns the currentYear.
	 * @return int
	 */
	public int getCurrentYear() {
		return currentYear;
	}

	/**
	 * Returns the years.
	 * @return Vector
	 */
	public Vector getYears() {
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
	 * Returns TRUE to display the detailed version of the report, otherwise displays the original
	 * report format.
	 */
	public boolean isDetailedReport()
	{
		return detailedReport;
	}
}
