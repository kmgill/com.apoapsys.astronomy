package com.apoapsys.astronomy.perlin;

import java.util.Collections;
import java.util.List;

import com.apoapsys.astronomy.math.MathExt;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Doubles;

public class ColorGradientBuilder {
	
	private NoiseGenerator noiseGenerator;
	private List<GradientColorStop> colorStops = Lists.newArrayList();
	
	private double maxStop = 1.0;
	private double minStop = 0.0;
	
	public ColorGradientBuilder(NoiseGenerator noiseGenerator) {
		this.noiseGenerator = noiseGenerator;
	}
	
	public void clear() {
		colorStops.clear();
	}
	
	public void addColorStop(GradientColorStop colorStop) {
		colorStops.add(colorStop);
		sortStops();
	}
	
	public boolean removeColorStop(GradientColorStop colorStop) {
		return colorStops.remove(colorStop);
	}
	
	private void sortStops() {
		Ordering<GradientColorStop> ordering = new Ordering<GradientColorStop>() {
			public int compare(GradientColorStop left, GradientColorStop right) {
				return Doubles.compare(left.getStop(), right.getStop());
			}
		};
		Collections.sort(colorStops, ordering);
		if (colorStops.size() > 0) {
			minStop = colorStops.get(0).getStop();
			maxStop = colorStops.get(colorStops.size() - 1).getStop();
		}
	}
	
	
	public double[] color(double x, double y, double[] rgba) {
		
		double noise = noiseGenerator.noise(x, y);
		
		double ratio = (noise - minStop) / (maxStop - minStop);
		ratio = MathExt.clamp(ratio, 0.0, 1.0);
		
		if (rgba == null) {
			rgba = new double[4];
		}
		
		rgba[0] = rgba[1] = rgba[2] = rgba[3] = 0.0;
		
		GradientColorStop lower = null;
		GradientColorStop upper = null;
		
		for (GradientColorStop stop : colorStops) {
			
			if (ratio >= stop.getStop()) {
				lower = stop;
			}
			if (ratio <= stop.getStop()) {
				upper = stop;
				break;
			}
			
		}
		
		if (upper == null && lower == null) {
			return rgba;
		} else if (upper == null) { // lower != null is implied by the first condition
			upper = lower;
		} else if (lower == null) { // upper != null is implied by the first condition
			lower = upper;
		}
		
		
		double colorRatio = (ratio - lower.getStop()) / (upper.getStop() - lower.getStop());
		if (Double.isNaN(colorRatio))
			colorRatio = 1.0; 

		rgba[0] = ((lower.getRed() * (1.0 - colorRatio)) + (upper.getRed() * colorRatio));
		rgba[1] = ((lower.getGreen() * (1.0 - colorRatio)) + (upper.getGreen() * colorRatio));
		rgba[2] = ((lower.getBlue() * (1.0 - colorRatio)) + (upper.getBlue() * colorRatio));
		rgba[3] = ((lower.getAlpha() * (1.0 - colorRatio)) + (upper.getAlpha() * colorRatio));
		
		return rgba;
	}
	
}
