package com.apoapsys.astronomy.perlin;

public abstract class Perlin implements NoiseGenerator {
	
	private double persistence = .75;
	private double numberOfOctaves = 16;
	private double resolution  = 60.0;

	public Perlin() {
		
	}
	
	public Perlin(double persistence, double numberOfOctaves, double resolution) {
		setPersistence(persistence);
		setNumberOfOctaves(numberOfOctaves);
		setResolution(resolution);
	}
	
	public double noise(double x, double y, PerlinNoiseModifier modifier) {
		
		double noise = 0.0;
		
		noise = PerlinNoiseUtil.perlinNoise2D(x / getResolution(), y / getResolution(), getPersistence(), getNumberOfOctaves(), modifier);
		
		return noise;
	}
	
	public double getPersistence() {
		return persistence;
	}

	public void setPersistence(double persistence) {
		if (persistence > 1.0)
			persistence = 1.0;
		if (persistence < 0.0) 
			persistence = 0.0;
		this.persistence = persistence;
	}

	public double getNumberOfOctaves() {
		return numberOfOctaves;
	}

	public void setNumberOfOctaves(double numberOfOctaves) {
		if (numberOfOctaves < 1)
			numberOfOctaves = 1;
		this.numberOfOctaves = numberOfOctaves;
	}

	public double getResolution() {
		return resolution;
	}

	public void setResolution(double resolution) {
		if (resolution <= 0.0)  
			resolution = 0.1;
		this.resolution = resolution;
	}
	
	
	
}
