package com.apoapsys.astronomy.orbits.keplarian;

import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.orbits.EllipticalOrbit;
import com.apoapsys.astronomy.orbits.Ephemeris;
import com.apoapsys.astronomy.orbits.Orbit;
import com.apoapsys.astronomy.orbits.OrbitPosition;
import com.apoapsys.astronomy.time.EpochEnum;

public class ErisKeplarianOrbit extends EllipticalOrbit implements Orbit<OrbitPosition> {
	
	public ErisKeplarianOrbit() {
		super(new Ephemeris(6.814528383022676E+01,
							0.0,
							4.324547411204651E-01,
							new Angle(4.374037864588535E+01),
							new Angle(3.612861006165989E+01),
							new Angle(1.508355993252542E+02),
							1.943095415904719E+02,
							2.054717566990577E+05,
							EpochEnum.J2000.julianDay()));
		
		
	}
	
}
