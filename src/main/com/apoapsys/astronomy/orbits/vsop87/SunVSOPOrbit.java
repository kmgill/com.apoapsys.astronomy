package com.apoapsys.astronomy.orbits.vsop87;

public class SunVSOPOrbit extends VSOP87OrbitRect {
	
	public SunVSOPOrbit()
	{
		super(SunVSOPSeries.X, SunVSOPSeries.Y, SunVSOPSeries.Z, 0);
	}
}
