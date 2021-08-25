package com.apoapsys.astronomy.orbits;

import com.apoapsys.astronomy.math.MathExt;

public class KeplerLaguerreConwayHypSolver extends IterativeSolver {
	
	public KeplerLaguerreConwayHypSolver(double ecc, double M) {
		super(ecc, M);
	}
	
	protected double solve(double x) {
		double s = ecc * MathExt.sinh(x);
		double c = ecc * MathExt.cosh(x);
		double f = x - s - M;
		double f1 = c - 1;
		double f2 = s;

		x += -5 * f / (f1 + MathExt.sign(f1) * MathExt.sqrt(MathExt.abs(16 * f1 * f1 - 20 * f * f2)));
		return x;
	}
}
