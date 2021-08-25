package com.apoapsys.astronomy.orbits;

import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.math.Vector;

public class OrbitPosition {
	
	private Vector position;
	private Angle eccentricAnomaly;
	private Angle meanAnomaly;
	private Angle trueAnomaly;
	
	public OrbitPosition(Vector position) {
		this.position = position;
	}
	
	public OrbitPosition(Vector position, Angle eccentricAnomaly, Angle meanAnomaly, Angle trueAnomaly) {
		this.position = position;
		this.eccentricAnomaly = eccentricAnomaly;
		this.meanAnomaly = meanAnomaly;
		this.trueAnomaly = trueAnomaly;
	}

	public Vector getPosition() {
		return position;
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
	
	
	
}
