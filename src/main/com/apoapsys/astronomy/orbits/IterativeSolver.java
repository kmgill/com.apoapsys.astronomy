package com.apoapsys.astronomy.orbits;

public abstract class IterativeSolver {
	
	protected double ecc;
	protected double M;
	
	public IterativeSolver(double ecc, double M) {
		this.ecc = ecc;
		this.M = M;
	}
	
	protected abstract double solve(double x);
	
	public double[] solveIterationFixed(double x0, int maxIter) {
		
		double x = 0;
		double x2 = x0;
		
		for (double i = 0; i < maxIter; i++) {
			x = x2;
			x2 = solve(x);
		}
		
		return new double[]{x2, x2 - x};
	}
	
	
	/*
	 * KMG.SolveKeplerFunc2 = function(ecc, M) {
	this.solve = function(x) {
		return x + (M + ecc * Math.sin(x) - x) / (1 - ecc * Math.cos(x));
	};
};
	 
	
	function solveIterationFixed(f, x0, maxIter) {
		
		var x = 0;
		var x2 = x0;
		
		for (var i = 0; i < maxIter; i++) {
			x = x2;
			x2 = f.solve(x);
		}
		
		return [x2, x2 - x];
	}
	
	*/
}
