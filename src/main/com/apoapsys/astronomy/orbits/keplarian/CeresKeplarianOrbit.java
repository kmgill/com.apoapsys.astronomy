package com.apoapsys.astronomy.orbits.keplarian;

import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.orbits.EllipticalOrbit;
import com.apoapsys.astronomy.orbits.Ephemeris;
import com.apoapsys.astronomy.orbits.Orbit;
import com.apoapsys.astronomy.orbits.OrbitPosition;
import com.apoapsys.astronomy.time.EpochEnum;

public class CeresKeplarianOrbit extends EllipticalOrbit implements Orbit<OrbitPosition> {
	
	public CeresKeplarianOrbit() {
		super(new Ephemeris(2.7654,
							0.0,
							0.079138,
							new Angle(10.587),
							new Angle(80.3932),
							new Angle(72.5898),
							113.410,
							4.60 * 365.25,
							EpochEnum.J2000.julianDay()));
		
		
	}
	
	/*
	 * double semiMajorAxis,
					double longitudeOfPerihelion,
					double eccentricity,
					Angle inclination,
					Angle ascendingNode,
					Angle argOfPeriapsis,
					double meanAnomalyAtEpoch,
					double period,
					double epoch)
	 */
	
	
}
