package com.apoapsys.astronomy.orbits;

public class KeplerFunc2Solver extends IterativeSolver {
	
	public KeplerFunc2Solver(double ecc, double M) {
		super(ecc, M);
	}
	
	protected double solve(double x)
	{
		return x + (M + ecc * Math.sin(x) - x) / (1 - ecc * Math.cos(x));
	}
}
