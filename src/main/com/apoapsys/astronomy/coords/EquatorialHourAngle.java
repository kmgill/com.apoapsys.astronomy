package com.apoapsys.astronomy.coords;

import com.apoapsys.astronomy.math.Angle;

public class EquatorialHourAngle implements CoordinateSystem {
	
	private Angle hourAngle;
	private Angle declination;
	
	public EquatorialHourAngle() {
		
	}
	
	public EquatorialHourAngle(Angle hourAngle, Angle declination) {
		this.hourAngle = hourAngle;
		this.declination = declination;
	}

	public Angle getHourAngle() {
		return hourAngle;
	}

	public void setHourAngle(Angle hourAngle) {
		this.hourAngle = hourAngle;
	}

	public Angle getDeclination() {
		return declination;
	}

	public void setDeclination(Angle declination) {
		this.declination = declination;
	}
	
	
}
