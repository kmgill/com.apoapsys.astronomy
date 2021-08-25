package com.apoapsys.astronomy.orbits.vsop87;

public class EarthVSOPSeries {
	public static double[][][] L = {
		EarthVSOPTerms.L0,
		EarthVSOPTerms.L1,
		EarthVSOPTerms.L2,
		EarthVSOPTerms.L3,
		EarthVSOPTerms.L4,
		EarthVSOPTerms.L5,
	};
	
	public static double[][][] B = {
		EarthVSOPTerms.B0,
		EarthVSOPTerms.B1,
		EarthVSOPTerms.B2
	};
	
	public static double[][][] R = {
		EarthVSOPTerms.R0,
		EarthVSOPTerms.R1,
		EarthVSOPTerms.R2,
		EarthVSOPTerms.R3,
		EarthVSOPTerms.R4
	};
}
