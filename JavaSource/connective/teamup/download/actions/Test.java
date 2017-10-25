package connective.teamup.download.actions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class Test {

	public static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	public static  DateFormat dateWithTimeFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
	
		//System.out.println(getDayDateRange()); 
		//System.out.println(getWeekDateRange()); 
		//System.out.println(getMonthDateRange()); 
		 Calendar cal = Calendar.getInstance();
		 int hour = cal.get(Calendar.HOUR);
		 int ampm = cal.get(Calendar.AM_PM);
			System.out.println(hour +"::" +ampm) ;
			cal.add(Calendar.HOUR, 14);
			
			 hour = cal.get(Calendar.HOUR);
			 ampm = cal.get(Calendar.AM_PM);
		System.out.println(hour +"::" +ampm) ;
		
		
	}
	public static Long[] getWeekDateRange() throws Exception{
		Long [] dateRange = new Long[2];
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		String weekStart = dateFormat.format(cal.getTime()) +" 00:00:00";
		System.out.println(weekStart);//Week start date Date
		
		Date dateWeekStartTo = dateWithTimeFormat.parse(weekStart);
		Long toWeekStartMillSeconds = dateWeekStartTo.getTime();
		dateRange[0] = toWeekStartMillSeconds;
		
		 cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		 String weekEnd = dateFormat.format(cal.getTime()) +" 00:00:00";
		 System.out.println(weekEnd);
		 
		Date dateWeekEndTo = dateWithTimeFormat.parse(weekStart);
		Long toWeekEndMillSeconds = dateWeekEndTo.getTime();
		dateRange[1] = toWeekEndMillSeconds;
		return dateRange;
		
	}
	public static Long[] getMonthDateRange() throws Exception{
		Long [] dateRange = new Long[2];
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		String MonthStart = dateFormat.format(cal.getTime()) +" 00:00:00";
		System.out.println(MonthStart);//Week start date Date
		
		Date dateMonthStartTo = dateWithTimeFormat.parse(MonthStart);
		Long toMonthStartMillSeconds = dateMonthStartTo.getTime();
		dateRange[0] = toMonthStartMillSeconds;
		
		int lastDate = cal.getActualMaximum(Calendar.DATE);
		cal.set(Calendar.DATE, lastDate);
		
		String MonthEnd = dateFormat.format(cal.getTime()) +" 00:00:00";
		System.out.println(MonthEnd);//Week start date Date
		
		Date dateMonthEndFrom = dateWithTimeFormat.parse(MonthEnd);
		Long toMonthEndMillSeconds = dateMonthEndFrom.getTime();
		dateRange[1] = toMonthEndMillSeconds;
		
		return dateRange;
		
	}
	public static Long[] getYearlyDateRange() throws Exception{
		Long [] dateRange = new Long[2];
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_YEAR, 1);
		String YearStart = dateFormat.format(cal.getTime()) +" 00:00:00";
		System.out.println(YearStart);//Week start date Date
	
		Date dateYearStartTo = dateWithTimeFormat.parse(YearStart);
		Long toYearStartMillSeconds = dateYearStartTo.getTime();
		dateRange[0] = toYearStartMillSeconds;
		
		//cal.set(Calendar.DAY_OF_YEAR, 12);
		//cal.set(Calendar.DAY_OF_MONTH, 31);	
		cal.add(Calendar.YEAR, 1);
		cal.add(Calendar.DAY_OF_YEAR, -1);
		String YearEnd = dateFormat.format(cal.getTime()) +" 00:00:00";
		System.out.println(YearEnd);//Week start date Date
		
		Date dateYearEndTo = dateWithTimeFormat.parse(YearEnd);
		Long toYearEndMillSeconds = dateYearEndTo.getTime();
		dateRange[1] = toYearEndMillSeconds;
		return dateRange;
		
	}
	
	public static Long[] getDayDateRange() throws Exception{
		Long [] dateRange = new Long[2];
		Calendar cal = Calendar.getInstance();
		String dayStart = dateFormat.format(cal.getTime()) +" 00:00:00";
		System.out.println(dayStart);//Week start date Date
		
		Date dateStartTo = dateWithTimeFormat.parse(dayStart);
		Long toDateStartMillSeconds = dateStartTo.getTime();
		dateRange[0] = toDateStartMillSeconds;
		
	
		String DayEnd = dateFormat.format(cal.getTime()) +" 23:59:00";
		System.out.println(DayEnd);//Week start date Date
		
		Date dateDayEndTo = dateWithTimeFormat.parse(DayEnd);
		Long toDateEndMillSeconds = dateDayEndTo.getTime();
		dateRange[1] = toDateEndMillSeconds;
		return dateRange;
		
	}

}
