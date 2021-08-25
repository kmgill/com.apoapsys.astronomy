package com.apoapsys.astronomy.coords;

import com.apoapsys.astronomy.math.Angle;


public class Galactic extends AbstractLatLonCoordinateSystem implements CoordinateSystem {

	public Galactic() {
		
	}
	
	public Galactic(Angle latitude, Angle longitude) {
		super(latitude, longitude);
	}
	
}
