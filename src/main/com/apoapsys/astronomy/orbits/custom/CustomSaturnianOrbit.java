package com.apoapsys.astronomy.orbits.custom;

import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.orbits.Orbit;
import com.apoapsys.astronomy.orbits.OrbitPosition;
import com.apoapsys.astronomy.time.DateTime;

public abstract class CustomSaturnianOrbit<T extends OrbitPosition> implements Orbit<T> {
	
	protected static final double RADIUS = 60330.0;
	protected static final Angle ASCENDING_NODE = new Angle(168.8112);
	protected static final Angle TILT = new Angle(28.0817);
	
	
	@Override
	public DateTime getEpoch() {
		return new DateTime();
	}
}
