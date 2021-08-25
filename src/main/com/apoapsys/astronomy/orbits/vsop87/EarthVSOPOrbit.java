package com.apoapsys.astronomy.orbits.vsop87;

public class EarthVSOPOrbit extends VSOP87Orbit {
	
	public EarthVSOPOrbit() {
		super(EarthVSOPSeries.L, EarthVSOPSeries.B, EarthVSOPSeries.R, 365.25);
	}
	
}