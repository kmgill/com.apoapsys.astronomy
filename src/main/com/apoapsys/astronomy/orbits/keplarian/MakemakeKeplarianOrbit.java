package com.apoapsys.astronomy.orbits.keplarian;

import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.orbits.EllipticalOrbit;
import com.apoapsys.astronomy.orbits.Ephemeris;
import com.apoapsys.astronomy.orbits.Orbit;
import com.apoapsys.astronomy.orbits.OrbitPosition;
import com.apoapsys.astronomy.time.EpochEnum;

public class MakemakeKeplarianOrbit extends EllipticalOrbit implements Orbit<OrbitPosition> {
	
	public MakemakeKeplarianOrbit() {
		super(new Ephemeris(4.537149503754902E+01,
							0.0,
							1.645302661137667E-01,
							new Angle(2.900018092674307E+01),
							new Angle(7.927479351325880E+01),
							new Angle(2.962796702827131E+02),
							1.397247535166562E+02,
							1.116279789467439E+05,
							EpochEnum.J2000.julianDay()));
		
		
	}
	
}
