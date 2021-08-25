package com.apoapsys.astronomy.orbits.keplarian;

import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.orbits.EllipticalOrbit;
import com.apoapsys.astronomy.orbits.Ephemeris;
import com.apoapsys.astronomy.orbits.Orbit;
import com.apoapsys.astronomy.orbits.OrbitPosition;
import com.apoapsys.astronomy.time.EpochEnum;

public class VestaKeplarianOrbit extends EllipticalOrbit implements Orbit<OrbitPosition> {
	
	public VestaKeplarianOrbit() {
		super(new Ephemeris(2.361534940452743E+00,
							149.84,
							9.002246842706077E-02,
							new Angle(7.133937445524650E+00),
							new Angle(1.039514249511780E+02),
							new Angle(1.495866622389732E+02),
							3.410238523604547E+02,
							1.325531309325364E+03,
							EpochEnum.J2000.julianDay()));
		
		
	}

}
