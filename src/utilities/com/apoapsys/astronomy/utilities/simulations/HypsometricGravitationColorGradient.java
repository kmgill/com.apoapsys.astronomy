package com.apoapsys.astronomy.utilities.simulations;

import com.apoapsys.astronomy.math.MathExt;

public class HypsometricGravitationColorGradient implements GravitationColorGradient {
	
	private static double[][] gradient = {{0.000000, 0.000000, 0.388235, 0.274510},
									{0.179381, 0.145098, 0.513725, 0.207843},
									{0.301031, 0.894118, 0.811765, 0.494118},
									{0.400000, 0.603922, 0.274510, 0.000000},
									{0.550515, 0.486275, 0.133333, 0.141176},
									{0.779381, 0.474510, 0.474510, 0.474510},
									{0.929897, 0.7000, 0.7000, 0.7000},
									{1.000000, 0.9000, 0.9000, 0.9000}};
	
	private static GradientStop[] stops;

	public HypsometricGravitationColorGradient() {
		stops = new GradientStop[gradient.length];
		
		int i = 0;
		for (double[] stopGradient : gradient) {
			stops[i] = new GradientStop(stopGradient);
			i++;
		}
	}
	
	
	@Override
	public double[] getColor(double ratio, double[] color) {
		
		if (color == null) {
			color = new double[4];
		}
		
		color[0] = color[1] = color[2] = 0.0;
		color[3] = 255.0;
		
		ratio = MathExt.clamp(ratio, 0.0, 1.0);
		
		GradientStop lower = null;
		GradientStop upper = null;
		
		for (GradientStop stop : stops) {
			
			if (ratio >= stop.stop) {
				lower = stop;
			}
			if (ratio <= stop.stop) {
				upper = stop;
				break;
			}
			
		}
		
		if (upper == null && lower == null) {
			return color;
		} else if (upper == null) { // lower != null is implied by the first condition
			upper = lower;
		} else if (lower == null) { // upper != null is implied by the first condition
			lower = upper;
		}
		
		double color_ratio = (ratio - lower.stop) / (upper.stop - lower.stop);
		if (Double.isNaN(color_ratio))
			color_ratio = 1.0; 

		
		color[0] = ((lower.r * (1.0 - color_ratio)) + (upper.r * color_ratio)) * 255.0;
		color[1] = ((lower.g * (1.0 - color_ratio)) + (upper.g * color_ratio)) * 255.0;
		color[2] = ((lower.b * (1.0 - color_ratio)) + (upper.b * color_ratio)) * 255.0;

		return color;
		

	}
	
	
	static class GradientStop {
		double stop = 0;
		double r;
		double g;
		double b;
		
		public GradientStop(double[] gradient) {
			this(gradient[0], gradient[1], gradient[2], gradient[3]);
		}
		
		public GradientStop(double stop, double r, double g, double b) {
			this.stop = stop;
			this.r = r;
			this.g = g;
			this.b = b;
		}
		
	}
	
}
