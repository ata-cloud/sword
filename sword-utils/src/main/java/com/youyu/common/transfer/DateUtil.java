/*
 *    Copyright 2018-2019 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.youyu.common.transfer;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.ObjectUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 */

public class DateUtil {

	/** 时间格式:yyyyMMdd */
	public static final String YYYYMMDD = "yyyyMMdd";
	
	/** 时间格式:yyyyMMddHHmmss */
	public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

	/** 时间格式:yyyy-MM-dd */
	public static final String YYYY_MM_DD = "yyyy-MM-dd";

	/** 时间格式:yyyyMMdd HH:mm:ss */
	public static final String YYYYMMDD_HHMMSS = "yyyyMMdd HH:mm:ss";

	/** 时间格式:yyyy-MM-dd HH:mm:ss */
	public static final String YYYY_MM_DD_HHMMSS = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 日期格式转换
	 * 
	 * @param dateStr
	 * @param srcFormat
	 * @param destFormat
	 * @return
	 * @throws ParseException
	 */
	public static String convertDateFormat(String dateStr, String srcFormat, String destFormat) {
		try{
			SimpleDateFormat dateFormat = new SimpleDateFormat(srcFormat);
			Date date = dateFormat.parse(dateStr);
			SimpleDateFormat dateFormat2 = new SimpleDateFormat(destFormat);
			return dateFormat2.format(date);
		} catch(ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取系统当前日期
	 * 
	 * @param format
	 * @return 返回字符串
	 */
	public static String getCurrentDate(String format) {
		if (ObjectUtils.isEmpty(format)) {
			format = YYYYMMDD;
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(new Date());
	}

	/**
	 * 根据day，计算时间
	 * 
	 * @param date
	 *            日期
	 * @param day
	 *            正数为加的天数，负数为负的天数
	 * @return
	 */
	public static String calcuateDate(Date date, String format, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, day);
		Date time = calendar.getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(time);

	}
	
	/**
	 * 
	 * 根据天，计算时间
	 * 
	 * @param date String
	 * @param format
	 * @param day
	 * @return
	 */
	public static String calcuateDate(String date, String format, int day) {
		try {
			Calendar calendar = Calendar.getInstance();
			Date date2 = string2Date(date, format);
			calendar.setTime(date2);
			calendar.add(Calendar.DATE, day);
			Date time = calendar.getTime();
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			return dateFormat.format(time);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * String转换成date
	 * 
	 * @param date
	 *            格式：YYYYMMDD
	 * @return
	 */
	public static Date date2String(String date) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(YYYYMMDD);
			return dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 格式化日期，默认使用yyyyMMdd格式
	 * 
	 * @param date
	 * */
	public static String date2String(Date date) {
		return date2String(date, YYYYMMDD);
	}

	/**
	 * 格式化日期，使用指定的日期格式
	 * 
	 * @param date
	 * @param format
	 * */
	public static String date2String(Date date, String format) {
		if (date != null) {
			if (StringUtils.isBlank(format)) {
				format = YYYYMMDD;
			}
			return new SimpleDateFormat(format).format(date);
		}
		return null;
	}
	/**
	 * 格式化日期，使用指定的日期格式
	 * 
	 * @param dateStr
	 * @param format
	 * @throws ParseException 
	 * */
	public static Date string2Date(String dateStr, String format) throws ParseException {
		if (dateStr != null) {
			return new SimpleDateFormat(format).parse(dateStr);
		}
		return null;
	}
	
	/**
	 * 
	 * String转换成date
	 * 
	 * @param date
	 *            格式：YYYYMMDD
	 * @return
	 */
	public static Date date2String(String date, String format) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			return dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
