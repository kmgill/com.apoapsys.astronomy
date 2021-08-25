package com.apoapsys.astronomy.satellites;

import com.apoapsys.astronomy.coords.Horizon;
import com.apoapsys.astronomy.math.Angle;

public class SatelliteHorizonPosition extends Horizon {
	
	private Angle hourAngle;
	private double range;
	private double greenwichMeanSiderealTime;
	private double localSiderealTime;
	private SatellitePosition equatorialPosition;
	
	
	public SatelliteHorizonPosition(Angle azimuth, Angle altitude, Angle hourAngle, double range, double greenwichMeanSiderealTime, double localSiderealTime, SatellitePosition equatorialPosition) {
		super(azimuth, altitude);
		this.hourAngle = hourAngle;
		this.range = range;
		this.greenwichMeanSiderealTime = greenwichMeanSiderealTime;
		this.localSiderealTime = localSiderealTime;
		this.equatorialPosition = equatorialPosition;
	}

	public Angle getHourAngle() {
		return hourAngle;
	}

	public double getRange() {
		return range;
	}

	public double getGreenwichMeanSiderealTime() {
		return greenwichMeanSiderealTime;
	}

	public double getLocalSiderealTime() {
		return localSiderealTime;
	}

	public SatellitePosition getEquatorialPosition() {
		return equatorialPosition;
	}
	
	
}
