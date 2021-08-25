package com.apoapsys.astronomy.coords;

import com.apoapsys.astronomy.math.Angle;

/**
 * An abstract class to provide a skeleton for those coordinate systems which are
 * nothing more than a latitude/longitude pair.
 * @author kgill
 *
 */
public abstract class AbstractLatLonCoordinateSystem implements CoordinateSystem {
	private Angle latitude;
	private Angle longitude;
	
	public AbstractLatLonCoordinateSystem() {
		
	}
	
	public AbstractLatLonCoordinateSystem(Angle latitude, Angle longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Angle getLatitude() {
		return latitude;
	}

	public void setLatitude(Angle latitude) {
		this.latitude = latitude;
	}

	public Angle getLongitude() {
		return longitude;
	}

	public void setLongitude(Angle longitude) {
		this.longitude = longitude;
	}
	
}
