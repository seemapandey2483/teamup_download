package com.teamup.agencyportal.api;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	
	public static String replaceWithPattern(String str,String replace,String  ptr){
		Pattern ptn = Pattern.compile(ptr);
		Matcher mtch = ptn.matcher(str);
		return mtch.replaceFirst(replace);
	}
}
