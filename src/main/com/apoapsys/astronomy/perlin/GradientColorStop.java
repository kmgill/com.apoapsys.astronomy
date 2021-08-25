package com.apoapsys.astronomy.perlin;


public class GradientColorStop {
	
	private double stop;
	private double red;
	private double green;
	private double blue;
	private double alpha;
	
	public GradientColorStop(double stop, double red, double green, double blue, double alpha) {
		this.stop = stop;
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}

	public double getStop() {
		return stop;
	}

	public void setStop(double stop) {
		this.stop = stop;
	}

	public double getRed() {
		return red;
	}

	public void setRed(double red) {
		this.red = red;
	}

	public double getGreen() {
		return green;
	}

	public void setGreen(double green) {
		this.green = green;
	}

	public double getBlue() {
		return blue;
	}

	public void setBlue(double blue) {
		this.blue = blue;
	}

	public double getAlpha() {
		return alpha;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}
	
	
	
}
