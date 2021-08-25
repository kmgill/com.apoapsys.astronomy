package com.apoapsys.astronomy.bodies;

import com.apoapsys.astronomy.math.Vector;

public class Ring {
	
	private double innerRadius;
	private double outterRadius;
	private Vector orientation;
	
	public Ring() {
		
	}

	public double getInnerRadius() {
		return innerRadius;
	}

	public void setInnerRadius(double innerRadius) {
		this.innerRadius = innerRadius;
	}

	public double getOutterRadius() {
		return outterRadius;
	}

	public void setOutterRadius(double outterRadius) {
		this.outterRadius = outterRadius;
	}

	public Vector getOrientation() {
		return orientation;
	}

	public void setOrientation(Vector orientation) {
		this.orientation = orientation;
	}
	
	
	
}
