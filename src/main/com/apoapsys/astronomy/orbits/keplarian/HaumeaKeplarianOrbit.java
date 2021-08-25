package com.apoapsys.astronomy.orbits.keplarian;

import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.orbits.EllipticalOrbit;
import com.apoapsys.astronomy.orbits.Ephemeris;
import com.apoapsys.astronomy.orbits.Orbit;
import com.apoapsys.astronomy.orbits.OrbitPosition;
import com.apoapsys.astronomy.time.EpochEnum;

public class HaumeaKeplarianOrbit extends EllipticalOrbit implements Orbit<OrbitPosition> {
	
	public HaumeaKeplarianOrbit() {
		super(new Ephemeris(4.290900504570640E+01,
							0.0,
							1.999240087754753E-01,
							new Angle(2.820613695665376E+01),
							new Angle(1.219331357048411E+02),
							new Angle(2.405918328387456E+02),
							1.895938765545494E+02,
							1.026646884377227E+05,
							EpochEnum.J2000.julianDay()));
		
		
	}
}
