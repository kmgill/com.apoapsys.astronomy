package com.apoapsys.astronomy.orbits.keplarian;

import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.orbits.EllipticalOrbit;
import com.apoapsys.astronomy.orbits.Ephemeris;
import com.apoapsys.astronomy.orbits.Orbit;
import com.apoapsys.astronomy.orbits.OrbitPosition;
import com.apoapsys.astronomy.time.EpochEnum;

public class TitanKeplarianOrbit extends EllipticalOrbit implements Orbit<OrbitPosition> {
	
	public TitanKeplarianOrbit() {
		super(new Ephemeris(8.168127483657287E-03,
							0.0,
							2.860074434716717E-02,
							new Angle(2.771833743784899E+01),
							new Angle(1.692391586820226E+02),
							new Angle(1.644078851778261E+02),
							1.643780911447081E+02,
							1.594734092046106E+01,
							EpochEnum.J2000.julianDay()));
		
		
	}
}
