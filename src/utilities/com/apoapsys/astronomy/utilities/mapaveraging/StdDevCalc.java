package com.apoapsys.astronomy.utilities.mapaveraging;

import com.apoapsys.astronomy.math.MathExt;

public class StdDevCalc {
	private int n;
	private double sum;
	private double sumsq;
	
	public void reset() {
		n = 0;
		sum = sumsq = 0.0;
	}
	
	public void add(double x) {
		++this.n;
		this.sum += x;
		this.sumsq += x * x;
	}
	
	public double getMean() {
		double mean = 0.0;
		if (n > 0) {
			mean = sum / n;
		}
		return mean;
	}
	
	public double getVariance() {
		return MathExt.sqr(getStandardDeviation());
	}
	
	public double getStandardDeviation() {
		double deviation = 0.0;
		if (n > 1) {
			deviation = MathExt.sqrt((sumsq - sum * sum / n) / (n - 1));
		}
		return deviation;
	}
	
	public double getZForTestValue(double v) {
		return (v - getMean()) / getStandardDeviation();
	}
}
