package com.apoapsys.astronomy.orbits;

import com.apoapsys.astronomy.math.MathExt;

public class KeplerLaguerreConwaySolver extends IterativeSolver {
	
	public KeplerLaguerreConwaySolver(double ecc, double M) {
		super(ecc, M);
	}
	
	protected double solve(double x) {
		double s = ecc * Math.sin(x);
		double c = ecc * Math.cos(x);
		double f = x - s - M;
		double f1 = 1 - c;
		double f2 = s;

		x += -5 * f / (f1 + MathExt.sign(f1) * MathExt.sqrt(Math.abs(16 * f1 * f1 - 20 * f * f2)));
		return x;
	}
}