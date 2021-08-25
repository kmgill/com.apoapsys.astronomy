package com.apoapsys.astronomy.satellites;

import com.apoapsys.astronomy.coords.EquatorialRightAscension;
import com.apoapsys.astronomy.geo.Coordinate;
import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.math.Vector;

public class SatellitePosition {

	private EquatorialRightAscension position;
	private Coordinate latitude;
	private Coordinate longitude;
	private double altitude;
	private double angularRange;
	private double surfaceRange;
	private double visibilityTimeMinutes;
	private Angle eccentricAnomaly;
	private Angle meanAnomaly;
	private Angle trueAnomaly;
	private Vector eciVector;

	public SatellitePosition(EquatorialRightAscension position,
								Coordinate latitude, 
								Coordinate longitude, 
								double altitude, 
								double angularRange, 
								double surfaceRange, 
								double visibilityTimeMinutes,
								Angle eccentricAnomaly, 
								Angle meanAnomaly, 
								Angle trueAnomaly, 
								Vector eciVector) {
		this.position = position;
		this.latitude = latitude;
		this.longitude = longitude;
		this.altitude = altitude;
		this.angularRange = angularRange;
		this.surfaceRange = surfaceRange;
		this.visibilityTimeMinutes = visibilityTimeMinutes;
		this.eccentricAnomaly = eccentricAnomaly;
		this.meanAnomaly = meanAnomaly;
		this.trueAnomaly = trueAnomaly;
		this.eciVector = eciVector;
	}

	public EquatorialRightAscension getPosition() {
		return position;
	}

	public Coordinate getLatitude() {
		return latitude;
	}

	public Coordinate getLongitude() {
		return longitude;
	}

	public double getAltitude() {
		return altitude;
	}

	public double getAngularRange() {
		return angularRange;
	}

	public double getSurfaceRange() {
		return surfaceRange;
	}

	public double getVisibilityTimeMinutes() {
		return visibilityTimeMinutes;
	}

	public Angle getEccentricAnomaly() {
		return eccentricAnomaly;
	}

	public Angle getMeanAnomaly() {
		return meanAnomaly;
	}

	public Angle getTrueAnomaly() {
		return trueAnomaly;
	}

	public Vector getEciVector() {
		return eciVector;
	}
	
	

}
