package com.wechat.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 日期工具类
 * 
 * @author GJ
 *
 */
public class DateUtil {
	
	/**
	 * yyyy/MM/dd
	 */
	public static final String DEFAULT_DATE_FMT1 = "yyyy/MM/dd";
	
	/**
	 * yyyy-MM-dd
	 */
	public static final String DEFAULT_DATE_FMT2 = "yyyy-MM-dd";
	
	/**
	 * yyyyMMdd
	 */
	public static final String DEFAULT_DATE_FMT3 = "yyyyMMdd";
	
	/**
	 * yyyy/MM/dd HH:mm:ss
	 */
	public static final String DEFAULT_TIME_FMT1 = "yyyy/MM/dd HH:mm:ss";
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static final String DEFAULT_TIME_FMT2 = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 日期类型转换字符串
	 * @param date
	 * @param format
	 * @return
	 */
	public static String Date2String(Date date, String format) {
		return Date2String(date, format, null);
	}
	
	/**
	 * 转换日期为指定格式字符串
	 * 
	 * @param date
	 * @param format
	 * @param timeZone
	 * @return
	 */
	public static String Date2String(Date date, String format, TimeZone timeZone) {
		if (date == null || format == null)
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		if (timeZone != null)
			sdf.setTimeZone(timeZone);
		return sdf.format(date);
	}
	
	public static void main(String[] args) {
		System.out.println(Date2String(new Date(), DEFAULT_DATE_FMT2));
	}
}
