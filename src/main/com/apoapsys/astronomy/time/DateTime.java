package com.apoapsys.astronomy.time;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.apoapsys.astronomy.geo.Coordinate;

public class DateTime {
	
	private EpochEnum epoch;
	private double julianDay;
	private Calendar calendar;
	
	public DateTime() {
		this(JulianUtil.julianNow(), EpochEnum.J2000);
	}
	
	public DateTime(double julianDay) {
		this(julianDay, EpochEnum.J2000);
	}
	
	public DateTime(double julianDay, EpochEnum epoch) {
		this.julianDay = julianDay;
		this.epoch = epoch;
		this.calendar = JulianUtil.julianToCalendar(julianDay);
	}
	
	public DateTime(long millisSince1970) {
		this(millisSince1970, EpochEnum.J2000);
	}
	
	public DateTime(long millisSince1970, EpochEnum epoch) {
		this(JulianUtil.millisToJulian(millisSince1970), epoch);
	}
	
	public EpochEnum getEpoch() {
		return epoch;
	}
	
	public double getJulianDay() {
		return julianDay;
	}
	
	public double getJulianDaySinceEpoch() {
		return getJulianDaySinceEpoch(epoch.julianDay());
	}
	
	public double getJulianDaySinceEpoch(EpochEnum epoch) {
		return getJulianDaySinceEpoch(epoch.julianDay());
	}
	
	public double getJulianDaySinceEpoch(double epoch) {
		return julianDay - epoch;
	}
	
	public double getJulianMinutes() {
		double tod = getHourOfDay() * 60.0 + getMinute() + (getSecond() / 60.0);
		return tod;
	}
	
	public long getMillis() {
		return JulianUtil.julianToMillis(julianDay);
	}
	
	public double getJulianCentury() {
		return (julianDay - epoch.julianDay()) / 36525.0;
	}
	

	
	public double getDayNumber() {
		return DateTimeUtil.getDayNumber(this);
	}
	
	public double getGMST() {
		return getGMST(360);
	}
	
	public double getGMST(double clampTo) {
		return DateTimeUtil.getGMST(this, clampTo);
	}
	
	public double getLMST(Coordinate lon) {
		return DateTimeUtil.getLMST(this, lon);
	}
	
	public double getGMST2() {
		return getGMST2(null);
	}
	
	public double getGMST2(Coordinate lon) {
		return DateTimeUtil.getGMST2(this, lon);
	}
	
	
	
	
	
	public int getYear() {
		return calendar.get(Calendar.YEAR);
	}
	
	public int getMonth() {
		return calendar.get(Calendar.MONTH + 1);
	}
	
	public int getDayOfMonth() {
		return calendar.get(Calendar.DAY_OF_MONTH);
	}
	
	public int getHourOfDay() {
		return calendar.get(Calendar.HOUR_OF_DAY);
	}
	
	public int getMinute() {
		return calendar.get(Calendar.MINUTE);
	}
	
	public int getSecond() {
		return calendar.get(Calendar.SECOND);
	}
	
	public int getMillisecond() {
		return calendar.get(Calendar.MILLISECOND);
	}
	
	public String format(String fmt) {
		SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		Date dt = new Date(calendar.getTimeInMillis());
		return sdf.format(dt);
	}
	
	@Override
	public String toString() {
		Date dt = new Date(getMillis());
		return dt.toString();
	}
}
