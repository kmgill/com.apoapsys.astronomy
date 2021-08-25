package com.apoapsys.astronomy.perlin;

public interface PerlinNoiseModifier {
	public double onNoise(double x, double y, double noise, double frequency, double amplitude, double octaveNum);
	public double onTotal(double total);
}
