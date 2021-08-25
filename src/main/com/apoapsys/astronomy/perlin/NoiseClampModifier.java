package com.apoapsys.astronomy.perlin;

import com.apoapsys.astronomy.math.MathExt;


public class NoiseClampModifier implements PerlinNoiseModifier  {

	public static final double NO_MINIMUM = Double.NaN;
	public static final double NO_MAXIMUM = Double.NaN;
	
	private double minimum;
	private double maximum;
	
	public NoiseClampModifier(double minimum, double maximum) {
		validate(minimum, maximum);
		this.minimum = minimum;
		this.maximum = maximum;
	}
	
	private void validate(double minimum, double maximum) {
		if (!Double.isNaN(maximum) && !Double.isNaN(minimum) && maximum <= minimum) {
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
		//total = MathExt.min(total, maximum);
		///total = MathExt.max(total, minimum);
		total = MathExt.clamp(total, minimum, maximum);
		return total;
	}

}
