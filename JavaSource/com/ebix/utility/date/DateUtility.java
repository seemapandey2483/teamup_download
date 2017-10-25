package com.ebix.utility.date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtility {

	public static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	public static  DateFormat dateWithTimeFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	public static final String DAY ="D";
	public static final String WEEK ="W";
	public static final String MONTH ="M";
	public static final String YEAR ="Y";
	
	public static Long[] getDateRange(String type) {
		try{
			if(DAY.equals(type)){
				return getDayDateRange();
			}else if(WEEK.equals(type)){
				return getWeekDateRange();	
			}else if(MONTH.equals(type)){
				return getMonthDateRange();	
			}else if(YEAR.equals(type)){
				return getYearlyDateRange();
			}
		}catch(Exception e) {
			return null;
		}
	
		return null;
	}
	
	public static Long[] getWeekDateRange() throws Exception{
		Long [] dateRange = new Long[2];
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		String weekStart = dateFormat.format(cal.getTime()) +" 00:00:00";
	
		
		Date dateWeekStartTo = dateWithTimeFormat.parse(weekStart);
		Long toWeekStartMillSeconds = dateWeekStartTo.getTime();
		dateRange[0] = toWeekStartMillSeconds;
		
		 cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		 String weekEnd = dateFormat.format(cal.getTime()) +" 00:00:00";
	
		 
		Date dateWeekEndTo = dateWithTimeFormat.parse(weekEnd);
		Long toWeekEndMillSeconds = dateWeekEndTo.getTime();
		dateRange[1] = toWeekEndMillSeconds;
		return dateRange;
		
	}
	public static Long[] getMonthDateRange() throws Exception{
		Long [] dateRange = new Long[2];
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		String MonthStart = dateFormat.format(cal.getTime()) +" 00:00:00";
		
		Date dateMonthStartTo = dateWithTimeFormat.parse(MonthStart);
		Long toMonthStartMillSeconds = dateMonthStartTo.getTime();
		dateRange[0] = toMonthStartMillSeconds;
		
		int lastDate = cal.getActualMaximum(Calendar.DATE);
		cal.set(Calendar.DATE, lastDate);
		
		String MonthEnd = dateFormat.format(cal.getTime()) +" 00:00:00";
		
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
		
		Date dateYearStartTo = dateWithTimeFormat.parse(YearStart);
		Long toYearStartMillSeconds = dateYearStartTo.getTime();
		dateRange[0] = toYearStartMillSeconds;
		
		cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, 1);
		cal.add(Calendar.DAY_OF_YEAR, -1);
		
		String YearEnd = dateFormat.format(cal.getTime()) +" 00:00:00";
		
		Date dateYearEndTo = dateWithTimeFormat.parse(YearEnd);
		Long toYearEndMillSeconds = dateYearEndTo.getTime();
		dateRange[1] = toYearEndMillSeconds;
		return dateRange;
		
	}
	
	public static Long[] getDayDateRange() throws Exception{
		Long [] dateRange = new Long[2];
		Calendar cal = Calendar.getInstance();
		String dayStart = dateFormat.format(cal.getTime()) +" 00:00:00";
		
		Date dateStartTo = dateWithTimeFormat.parse(dayStart);
		Long toDateStartMillSeconds = dateStartTo.getTime();
		dateRange[0] = toDateStartMillSeconds;
		
	
		String DayEnd = dateFormat.format(cal.getTime()) +" 23:59:00";
		Date dateDayEndTo = dateWithTimeFormat.parse(DayEnd);
		Long toDateEndMillSeconds = dateDayEndTo.getTime();
		dateRange[1] = toDateEndMillSeconds;
		return dateRange;
		
	}
}
