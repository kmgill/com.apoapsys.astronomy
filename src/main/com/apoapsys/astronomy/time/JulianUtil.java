package com.apoapsys.astronomy.time;

import java.util.Calendar;
import java.util.Date;

public class JulianUtil {
	
	// http://www.csgnetwork.com/juliangregcalconv.html
	public static Calendar julianToCalendar(double jd) {

		jd += 0.5;
		double z = Math.floor(jd);
		double f = jd - z;
		double A;
		if (z < 2299161) {
			A = z;
		} else {
			double omega = Math.floor((z-1867216.25)/36524.25);
			A = z + 1 + omega - Math.floor(omega/4);
		}
		double B = A + 1524;
		double C = Math.floor((B-122.1)/365.25);
		double D = Math.floor(365.25*C);
		double Epsilon = Math.floor((B-D)/30.6001);
		double dayGreg = B - D - Math.floor(30.6001*Epsilon) + f;
		double monthGreg, yearGreg;
		if (Epsilon < 14) {
			monthGreg = Epsilon - 1;
		} else {
			monthGreg = Epsilon - 13;
		}
		if (monthGreg > 2) {
			yearGreg = C - 4716;
		} else {
			yearGreg = C - 4715;
		}
		
		double year = yearGreg;
		double month = monthGreg;
		double day = Math.floor(dayGreg);
		
		double dayMinutes = ((dayGreg - day) * 1440.0);
		double hour = Math.floor(dayMinutes / 60.0);
		double minute = Math.floor(dayMinutes - (hour * 60.0));
		double second = Math.floor(60.0 * (dayMinutes - (hour * 60.0) -minute));
		double millisecond =  (1000.0 * (60.0 * (dayMinutes - (hour * 60.0) -minute)- second) );
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, (int)year);
		cal.set(Calendar.MONTH, (int)month - 1);
		cal.set(Calendar.DAY_OF_MONTH, (int) day);
		cal.set(Calendar.HOUR_OF_DAY, (int) hour);
		cal.set(Calendar.MINUTE, (int) minute);
		cal.set(Calendar.SECOND, (int) second);
		cal.set(Calendar.MILLISECOND, (int) millisecond);
		return cal;
	}
	
	public static long julianToMillis(double jd) {
		Calendar cal = julianToCalendar(jd);
		return cal.getTimeInMillis();
	}


	// http://www.csgnetwork.com/juliandatetime.html
	public static double dateToJulian(int year, int month, int day, int hour, int minute, int second, int millisecond, int tz) {
		return dateToJulian((double)year, (double)month, (double)day, (double)hour, (double)minute, (double)second, (double)millisecond, (double)tz);
	}
	
	public static double dateToJulian(double year, double month, double day, double hour, double minute, double second, double millisecond, double tz) {
		double day_decimal, julian_day, a;

		day_decimal = day + (hour - tz + (minute + second / 60.0 + millisecond / 1000 / 60) / 60.0) / 24.0;

		if (month < 3) {
			month += 12;
			year--;
		}

		julian_day = Math.floor(365.25 * (year + 4716.0)) + Math.floor(30.6001 * (month + 1)) + day_decimal - 1524.5;
		if (julian_day > 2299160.0) {
			a = Math.floor(year / 100);
			julian_day += (2 - a + Math.floor(a / 4));
		}
		
		return julian_day;
	}
	
	
	public static double millisToJulian(long millis) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		
		
		Date d = new Date(millis);
		double jd =  dateToJulian(cal.get(Calendar.YEAR),
								cal.get(Calendar.MONTH) + 1,
								cal.get(Calendar.DAY_OF_MONTH),
								cal.get(Calendar.HOUR_OF_DAY),
								cal.get(Calendar.MINUTE),
								cal.get(Calendar.SECOND),
								cal.get(Calendar.MILLISECOND),
								0);
		return jd;
	};


	public static double julianNow() {
		Calendar cal = Calendar.getInstance();
		//System.err.println(cal.get(Calendar.ZONE_OFFSET));
		
		return millisToJulian(System.currentTimeMillis()-cal.get(Calendar.ZONE_OFFSET));
	}

}
