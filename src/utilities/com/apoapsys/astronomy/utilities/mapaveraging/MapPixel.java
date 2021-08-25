package com.apoapsys.astronomy.utilities.mapaveraging;

import com.apoapsys.astronomy.math.MathExt;


public class MapPixel {
	public double[] red;
	public double[] green;
	public double[] blue;
	
	public MapPixel(int sampleCount) {
		red = new double[sampleCount];
		green = new double[sampleCount];
		blue = new double[sampleCount];
	}
	
	public void set(int index, double r, double g, double b) {
		red[index] = r;
		green[index] = g;
		blue[index] = b;
	}
	
	public PixelRGB getWithinStdDev(double stdDevLimit) 
	{
		PixelRGB averaged = new PixelRGB(0, 0, 0);
		StdDevCalc calcR = new StdDevCalc();
		StdDevCalc calcG = new StdDevCalc();
		StdDevCalc calcB = new StdDevCalc();
		
		for (int i = 0; i < red.length; i++) {
			calcR.add(red[i]);
			calcG.add(green[i]);
			calcB.add(blue[i]);
		}
		
		double rCount = 0;
		double gCount = 0;
		double bCount = 0;
		
		for (int i = 0; i < red.length; i++) {
			
			double zRed = calcR.getZForTestValue(red[i]);
			if (MathExt.abs(zRed) <= stdDevLimit) {
				averaged.red += red[i];
				rCount++;
			}
			
			double zGreen = calcG.getZForTestValue(green[i]);
			if (MathExt.abs(zGreen) <= stdDevLimit) {
				averaged.green += green[i];
				gCount++;
			}
			
			double zBlue = calcB.getZForTestValue(blue[i]);
			if (MathExt.abs(zBlue) <= stdDevLimit) {
				averaged.blue += blue[i];
				bCount++;
			}
		}
		
		averaged.red /= rCount;
		averaged.green /= gCount; 
		averaged.blue /= bCount;
		
		
		return averaged;
	}
	
	public PixelRGB getAveragedPixelRGB() {
		PixelRGB averaged = new PixelRGB(0, 0, 0);
		
		for (int i = 0; i < red.length; i++) {
			averaged.red += red[i];
			averaged.green += green[i];
			averaged.blue += blue[i];
		}
		
		averaged.red /= red.length;
		averaged.green /= red.length; 
		averaged.blue /= red.length;
		
		return averaged;
	}
	
}
