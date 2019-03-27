package com.resume.util;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * ʱ��ת��������
 * @author mac
 *
 */
public class DateTimeUtil {
	
	private final static String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	private final static String STANDARD = "yyyy";
	
	private final static String STANDARD_1 = "yyyy-MM-dd";
	
	
	public static Date strToDate(String dateTimeStr,String formatStr) {
		//formatStr�ַ�����ʽ
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
		//formatStr�ַ�����ʽ
		DateTimeFormatter dateTimeFormatter =  DateTimeFormat.forPattern(STANDARD_FORMAT);
		DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
		return dateTime.toDate();
	}
	
	public static Date strToDate_1(String dateTimeStr) {
		//formatStr�ַ�����ʽ
		DateTimeFormatter dateTimeFormatter =  DateTimeFormat.forPattern(STANDARD_1);
		DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
		return dateTime.toDate();
	}
	
	public static Date strToDateYear(String dateTimeStr) {
		//formatStr�ַ�����ʽ
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
	//���������ж����亯��
	public static  int getAge(Date birthDay) throws Exception {
        Calendar cal = Calendar.getInstance(); 
        if (cal.before(birthDay)) { //�����������ڵ�ǰʱ�䣬�޷�����
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);  //��ǰ���
        int monthNow = cal.get(Calendar.MONTH);  //��ǰ�·�
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH); //��ǰ����
        cal.setTime(birthDay); 
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);  
        int age = yearNow - yearBirth;   //����������
            if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;//��ǰ����������֮ǰ�������һ
            }else{
                age--;//��ǰ�·�������֮ǰ�������һ

} } return age; }
	
	//�������������±�
			public static  int getYear(String date) {
				return date.indexOf("��");
			}
			
			//�������������±�
				public static  int getDay(String date) {
					return date.indexOf("��");
			}
				//�������������±�
				public static  int getMonth(String date) {
					return date.indexOf("��");
			}
				
			//�������������� ����1982��7��15��
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












