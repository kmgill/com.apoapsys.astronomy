package com.apoapsys.astronomy.sun;

import com.apoapsys.astronomy.geo.Coordinate;
import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.math.Euler;
import com.apoapsys.astronomy.math.Vector;
import com.apoapsys.astronomy.time.DateTime;

public class SunPosition {
	private DateTime dateTime;
	private Angle rightAscension;
	private Angle hourAngle;
	private double trueSolarTime;
	private double equationOfTime;
	private Angle declination;
	private Angle zenithAngle;
	private Angle elevationAngle;
	private Angle apparentElevationAngle;
	private Angle azimuthAngle;
	private double sunriseUTC;
	private double sunsetUTC;
	private double distance;
	private Coordinate viewerLatitude;
	private Coordinate viewerLongitude;
	
	private Vector geocentricPositionVector;
	private Euler geocentricPositionEuler;
	
	public SunPosition(DateTime dateTime,
						Angle rightAscension,
						Angle hourAngle,
						double trueSolarTime,
						double equationOfTime,
						Angle declination,
						Angle zenithAngle,
						Angle elevationAngle,
						Angle apparentElevationAngle,
						Angle azimuthAngle,
						double sunriseUTC,
						double sunsetUTC,
						double distance,
						Coordinate viewerLatitude,
						Coordinate viewerLongitude,
						Vector geocentricPositionVector,
						Euler geocentricPositionEuler) {
		this.dateTime = dateTime;
		this.rightAscension = rightAscension;
		this.hourAngle = hourAngle;
		this.trueSolarTime = trueSolarTime;
		this.equationOfTime = equationOfTime;
		this.declination = declination;
		this.zenithAngle = zenithAngle;
		this.elevationAngle = elevationAngle;
		this.apparentElevationAngle = apparentElevationAngle;
		this.azimuthAngle = azimuthAngle;
		this.sunriseUTC = sunriseUTC;
		this.sunsetUTC = sunsetUTC;
		this.distance = distance;
		this.viewerLatitude = viewerLatitude;
		this.viewerLongitude = viewerLongitude;
		this.geocentricPositionVector = geocentricPositionVector;
		this.geocentricPositionEuler = geocentricPositionEuler;
	}

	public DateTime getDateTime() {
		return dateTime;
	}

	public Angle getRightAscension() {
		return rightAscension;
	}

	public Angle getHourAngle() {
		return hourAngle;
	}

	public double getTrueSolarTime() {
		return trueSolarTime;
	}

	public double getEquationOfTime() {
		return equationOfTime;
	}

	public Angle getDeclination() {
		return declination;
	}

	public Angle getZenithAngle() {
		return zenithAngle;
	}

	public Angle getElevationAngle() {
		return elevationAngle;
	}

	public Angle getApparentElevationAngle() {
		return apparentElevationAngle;
	}

	public Angle getAzimuthAngle() {
		return azimuthAngle;
	}

	public double getSunriseUTC() {
		return sunriseUTC;
	}

	public double getSunsetUTC() {
		return sunsetUTC;
	}

	public double getDistance() {
		return distance;
	}

	public Coordinate getViewerLatitude() {
		return viewerLatitude;
	}

	public Coordinate getViewerLongitude() {
		return viewerLongitude;
	}

	public Vector getGeocentricPositionVector() {
		return geocentricPositionVector;
	}

	public Euler getGeocentricPositionEuler() {
		return geocentricPositionEuler;
	}
	
	
	
}
