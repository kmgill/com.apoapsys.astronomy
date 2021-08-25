package com.apoapsys.astronomy.orbits.keplarian;

import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.orbits.EllipticalOrbit;
import com.apoapsys.astronomy.orbits.Ephemeris;
import com.apoapsys.astronomy.orbits.Orbit;
import com.apoapsys.astronomy.orbits.OrbitPosition;
import com.apoapsys.astronomy.time.EpochEnum;

public class SednaKeplarianOrbit extends EllipticalOrbit implements Orbit<OrbitPosition> {
	
	public SednaKeplarianOrbit() {
		super(new Ephemeris(518.57,
							0.0,
							0.8527,
							new Angle(11.927),
							new Angle(144.26),
							new Angle(311.02),
							358.01,
							11400 * 365.25,
							EpochEnum.J2000.julianDay()));
		
		
	}
	
}
