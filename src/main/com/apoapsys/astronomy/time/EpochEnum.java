package com.apoapsys.astronomy.time;

public enum EpochEnum {
	
	J2000(2451545.0),
	J1900(2415020.0);
	
	private final double julianDay;
	
	EpochEnum(double julianDay)
	{
		this.julianDay = julianDay;
	}
	
	public double julianDay() { return julianDay; }
	
	
}
