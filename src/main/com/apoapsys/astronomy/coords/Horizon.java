package com.apoapsys.astronomy.coords;

import com.apoapsys.astronomy.math.Angle;

public class Horizon implements CoordinateSystem {
	
	private Angle azimuth;
	private Angle altitude;
	
	public Horizon() {
		
	}
	
	public Horizon(Angle azimuth, Angle altitude) {
		this.azimuth = azimuth;
		this.altitude = altitude;
	}

	public Angle getAzimuth() {
		return azimuth;
	}

	public void setAzimuth(Angle azimuth) {
		this.azimuth = azimuth;
	}

	public Angle getAltitude() {
		return altitude;
	}

	public void setAltitude(Angle altitude) {
		this.altitude = altitude;
	}
	
	public EquatorialHourAngle toEquatorial() {
		return null;
	}
	
	public Ecliptic toEcliptic() {
		return null;
	}
	
	public Galactic toGalactic() {
		return null;
	}
	
}
