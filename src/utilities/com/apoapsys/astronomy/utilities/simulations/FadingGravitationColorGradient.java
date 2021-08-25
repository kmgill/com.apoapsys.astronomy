package com.apoapsys.astronomy.utilities.simulations;

import java.awt.Color;

public class FadingGravitationColorGradient implements GravitationColorGradient {
	
	double[] minColor;
	double[] maxColor;
	
	public FadingGravitationColorGradient(int minColor, int maxColor) {
		
		this.minColor = colorToArray(minColor);
		this.maxColor = colorToArray(maxColor);
		
	}

	private double[] colorToArray(int rgb) {
		Color c = new Color(rgb);
		double[] a = new double[3];
		a[0] = (double)c.getRed() / 255.0;
		a[1] = (double)c.getGreen() / 255.0;
		a[2] = (double)c.getBlue() / 255.0;
		return a;
	}
		
	
	@Override
	public double[] getColor(double ratio, double[] color) {
		
		if (color == null) {
			color = new double[4];
		}

		color[0] = ((minColor[0] * (1.0 - ratio)) + (maxColor[0] * ratio)) * 255.0;
		color[1] = ((minColor[1] * (1.0 - ratio)) + (maxColor[1] * ratio)) * 255.0;
		color[2] = ((minColor[2] * (1.0 - ratio)) + (maxColor[2] * ratio)) * 255.0;
		color[3] = 255.0;
		
		return color;
	}

}
