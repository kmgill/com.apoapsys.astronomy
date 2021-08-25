package com.apoapsys.astronomy.orbits;

import com.apoapsys.astronomy.coords.Ecliptic;
import com.apoapsys.astronomy.math.Vector;

public class EclipticOrbitPosition extends OrbitPosition {

	private Ecliptic ecliptic;
	
	public EclipticOrbitPosition(Vector position, Ecliptic e) {
		super(position);
		this.ecliptic = e;
	}
	
	public Ecliptic getEclipticCoordinates() {
		return ecliptic;
	}

}
