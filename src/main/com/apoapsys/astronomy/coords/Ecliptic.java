package com.apoapsys.astronomy.coords;

import com.apoapsys.astronomy.math.Angle;

public class Ecliptic extends AbstractLatLonCoordinateSystem implements CoordinateSystem {

	private double range;
	
	public Ecliptic() {
		
	}
	
	public Ecliptic(Angle latitude, Angle longitude) {
		this(latitude, longitude, 0);
	}
	
	public Ecliptic(Angle latitude, Angle longitude, double range) {
		super(latitude, longitude);
		this.range = range;
	}

	public double getRange() {
		return range;
	}

	public void setRange(double range) {
		this.range = range;
	}

}
