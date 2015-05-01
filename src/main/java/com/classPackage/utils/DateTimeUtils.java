package com.classPackage.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {
	
	public static final String formater = "yyyy/MM/dd HH:mm:ss";

	public static Timestamp getCurrentTimeStamp(String dateS) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat(formater);
		Date date = dateFormat.parse(dateS);
		return new Timestamp(date.getTime());
	}
	
	public static String getTimeStampAsString(Timestamp timestamp) {
		    SimpleDateFormat form = new SimpleDateFormat(formater);
		    return form.format(timestamp);
	}
	
	public static Date formatStringToDate(String str) {
		Date date = null;
		try {
			SimpleDateFormat form = new SimpleDateFormat(formater);
			date = form.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return date;
	}
}
