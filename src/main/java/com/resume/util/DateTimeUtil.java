package com.resume.util;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * 时间转换工具类
 * @author mac
 *
 */
public class DateTimeUtil {
	
	private final static String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	private final static String STANDARD = "yyyy";
	
	private final static String STANDARD_1 = "yyyy-MM-dd";
	
	
	public static Date strToDate(String dateTimeStr,String formatStr) {
		//formatStr字符串格式
		DateTimeFormatter dateTimeFormatter =  DateTimeFormat.forPattern(formatStr);
		DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
		return dateTime.toDate();
	}
	
	public static String dateToStr(Date date,String formatStr) {
		if(date == null) {
			return StringUtils.EMPTY;
		}
		DateTime dateTime = new DateTime(date);
		return dateTime.toString(formatStr);
	}
	
	public static Date strToDate(String dateTimeStr) {
		//formatStr字符串格式
		DateTimeFormatter dateTimeFormatter =  DateTimeFormat.forPattern(STANDARD_FORMAT);
		DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
		return dateTime.toDate();
	}
	
	public static Date strToDate_1(String dateTimeStr) {
		//formatStr字符串格式
		DateTimeFormatter dateTimeFormatter =  DateTimeFormat.forPattern(STANDARD_1);
		DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
		return dateTime.toDate();
	}
	
	public static Date strToDateYear(String dateTimeStr) {
		//formatStr字符串格式
		DateTimeFormatter dateTimeFormatter =  DateTimeFormat.forPattern(STANDARD);
		DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
		return dateTime.toDate();
	}
	
	public static String dateToStr(Date date) {
		if(date == null) {
			return StringUtils.EMPTY;
		}
		DateTime dateTime = new DateTime(date);
		return dateTime.toString(STANDARD_FORMAT);
	}
//	public static void main(String[] args) {
//		System.out.println(DateTimeUtil.dateToStr(new Date(), "yyyy-mm-dd HH:mm:ss"));
//		System.out.print(DateTimeUtil.strToDate("2010-01-02 12:12:12", "yyyy-mm-dd HH:mm:ss"));
//		/**
//		 * 2019-31-20 15:31:30
//		 * Sat Jan 02 12:12:12 CST 2010
//		 */
//	}
	//根据日期判断年龄函数
	public static  int getAge(Date birthDay) throws Exception {
        Calendar cal = Calendar.getInstance(); 
        if (cal.before(birthDay)) { //出生日期晚于当前时间，无法计算
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);  //当前年份
        int monthNow = cal.get(Calendar.MONTH);  //当前月份
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH); //当前日期
        cal.setTime(birthDay); 
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);  
        int age = yearNow - yearBirth;   //计算整岁数
            if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;//当前日期在生日之前，年龄减一
            }else{
                age--;//当前月份在生日之前，年龄减一

} } return age; }
	
	//返回年字所在下标
			public static  int getYear(String date) {
				return date.indexOf("年");
			}
			
			//返回日字所在下标
				public static  int getDay(String date) {
					return date.indexOf("日");
			}
				//返回月字所在下标
				public static  int getMonth(String date) {
					return date.indexOf("月");
			}
				
			//处理文字型日期 ：如1982年7月15日
			public static String get_date(String date) {
				int a = getYear(date);
				String  a1 = date.substring(0,a);
				System.out.println(a1);
				int b = getMonth(date);
				String b1 = date.substring(a+1,b);
				System.out.println(b1);
				int c = getDay(date);
				String c1 = date.substring(b+1,c);
				System.out.println(c1);
				date = a1+"-"+b1+"-"+c1;
				return date;
				
			}	
	
}












