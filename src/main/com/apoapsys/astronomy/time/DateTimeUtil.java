package com.apoapsys.astronomy.time;

import com.apoapsys.astronomy.geo.Coordinate;
import com.apoapsys.astronomy.geo.CoordinateTypeEnum;
import com.apoapsys.astronomy.math.MathExt;

public class DateTimeUtil {

	public static double getDayNumber(DateTime dt) {

		double dd = dt.getDayOfMonth();
		double mm = dt.getMonth();
		double yyyy = dt.getYear();
		double hh = dt.getHourOfDay();
		double min = dt.getMinute();
		double sec = dt.getSecond();
		double ms = dt.getMillisecond();

		double d = 367.0 * yyyy
				- div((7.0 * (yyyy + (div((mm + 9.0), 12.0)))), 4.0)
				+ div((275.0 * mm), 9.0) + dd - 730530.0;
		d = d + hh / 24.0 + min / (60.0 * 24.0) + sec / (24.0 * 60.0 * 60.0);

		return d;
	}

	public static double getDayNumberNow() {
		return getDayNumber(new DateTime());
	}

	public static double getGMST(DateTime dt) {
		return getGMST(dt, 24.0);
	}

	public static double getGMST(DateTime dt, double clampTo) {
		double jd = dt.getJulianDay();
		double jd0 = Math.floor(jd + 0.5) - .5;
		double H = (jd - jd0) * 24.0;
		double D = jd - 2451545.0;
		double D0 = jd0 - 2451545.0;
		double T = D / 36525.0;
		double gmst = (6.697374558 + 0.06570982441908 * D0 + 1.00273790935 * H + 0.000026 * Math
				.pow(T, 2));
		gmst = MathExt.clamp(gmst, clampTo);
		return gmst;
	}

	public static double getLMST(DateTime dt, Coordinate lon) {
		double gst = getGMST(dt);
		double lmst = gst + (lon.getDecimal() / 15.0);
		return lmst;
	}

	public static double getGMST2() {
		return getGMST2(null, null);
	}
	
	public static double getGMST2(DateTime dt) {
		return getGMST2(dt, null);
	}
	
	// http://www.satellite-calculations.com/TLETracker/scripts/tletracker.online.sat.calc
	public static double getGMST2(DateTime dt, Coordinate lon) {

		if (dt == null) {
			dt = new DateTime();
		}

		if (lon == null) {
			lon = new Coordinate(0.0, CoordinateTypeEnum.LONGITUDE);
		}

		double day = dt.getDayOfMonth();
		double month = dt.getMonth();
		double year = dt.getYear();
		double hour = dt.getHourOfDay();
		double minute = dt.getMinute();
		double second = dt.getSecond();
		double ms = dt.getMillisecond();

		if (month == 1 || month == 2) {
			year = year - 1;
			month = month + 12;
		}

		double a = Math.floor(year / 100);
		double b = 2 - a + Math.floor(a / 4);

		double c = Math.floor(365.25 * year);
		double d = Math.floor(30.6001 * (month + 1));

		// days since J2000.0
		double jd = b + c + d - 730550.5 + day
				+ (hour + minute / 60.0 + second / 3600.0) / 24.0;

		double jt = (jd) / 36525.0; // julian centuries since J2000.0
		double GMST = 280.46061837 + 360.98564736629 * jd + 0.000387933 * jt
				* jt - jt * jt * jt / 38710000 + lon.getDecimal();
		
		if (GMST > 0.0) {
			while (GMST > 360.0)
				GMST -= 360.0;
		} else {
			while (GMST < 0.0)
				GMST += 360.0;
		}

		return GMST;
	};

	private static double div(double a, double b) {
		return ((a - a % b) / b);
	}
}
