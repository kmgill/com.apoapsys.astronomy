package com.apoapsys.astronomy.orbits;

public class KeplerFunc1Solver extends IterativeSolver {
	
	public KeplerFunc1Solver(double ecc, double M) {
		super(ecc, M);
	}
	
	protected double solve(double x) {
		return M + ecc * Math.sin(x);
	}
}
