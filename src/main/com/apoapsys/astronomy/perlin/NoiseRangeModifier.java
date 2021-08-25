package com.apoapsys.astronomy.perlin;

public class NoiseRangeModifier implements PerlinNoiseModifier {
	
	private double minimum;
	private double maximum;
	
	private double possibleMinimum = -1.0;
	private double possibleMaximum = 1.0;
	
	public NoiseRangeModifier(double minimum, double maximum) {
		this(minimum, maximum, -1.0, 1.0);
	}
	
	public NoiseRangeModifier(double minimum, double maximum, double possibleMinimum, double possibleMaximum) {
		validate(minimum, maximum);
		
		this.minimum = minimum;
		this.maximum = maximum;
		this.possibleMinimum = possibleMinimum;
		this.possibleMaximum = possibleMaximum;
	}

	private void validate(double minimum, double maximum) {
		if (maximum <= minimum) {
			throw new IllegalArgumentException("Maximum cannot be less than the minimum");
		}
	}
	
	public double getMinimum() {
		return minimum;
	}

	public void setMinimum(double minimum) {
		validate(minimum, maximum);
		this.minimum = minimum;
	}

	public double getMaximum() {
		return maximum;
	}

	public void setMaximum(double maximum) {
		validate(minimum, maximum);
		this.maximum = maximum;
	}

	@Override
	public double onNoise(double x, double y, double noise, double frequency, double amplitude, double octaveNum) {
		return noise;
	}

	@Override
	public double onTotal(double total) {
		total = minimum + ((total - possibleMinimum) / (possibleMaximum - possibleMinimum)) * (maximum - minimum);
		return total;
	}
	
	
	
}
