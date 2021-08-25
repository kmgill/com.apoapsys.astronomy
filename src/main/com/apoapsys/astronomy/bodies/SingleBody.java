package com.apoapsys.astronomy.bodies;

import com.apoapsys.astronomy.rotation.Rotation;


public class SingleBody extends Body {
	
	
	private int id;
	private double momentOfInertia;
	private double radius;
	private double meanDensity;
	private double surfaceGravity; // TODO: Implement equatorial AND polar gravity
	private double flattening;
	private double escapeVelocity;
	private Rotation rotation;
	private double mass;
	private double siderealRotationalPeriod;
	private double obliquityToTheOrbit;
	
	public SingleBody() {

	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getMomentOfInertia() {
		return momentOfInertia;
	}

	public void setMomentOfInertia(double momentOfInertia) {
		this.momentOfInertia = momentOfInertia;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public double getMeanDensity() {
		return meanDensity;
	}

	public void setMeanDensity(double meanDensity) {
		this.meanDensity = meanDensity;
	}

	public double getSurfaceGravity() {
		return surfaceGravity;
	}

	public void setSurfaceGravity(double surfaceGravity) {
		this.surfaceGravity = surfaceGravity;
	}

	public double getFlattening() {
		return flattening;
	}

	public void setFlattening(double flattening) {
		this.flattening = flattening;
	}

	public double getEscapeVelocity() {
		return escapeVelocity;
	}

	public void setEscapeVelocity(double escapeVelocity) {
		this.escapeVelocity = escapeVelocity;
	}

	public Rotation getRotation() {
		return rotation;
	}

	public void setRotation(Rotation rotation) {
		this.rotation = rotation;
	}

	public double getSiderealRotationalPeriod() {
		return siderealRotationalPeriod;
	}

	public void setSiderealRotationalPeriod(double siderealRotationalPeriod) {
		this.siderealRotationalPeriod = siderealRotationalPeriod;
	}

	public double getObliquityToTheOrbit() {
		return obliquityToTheOrbit;
	}

	public void setObliquityToTheOrbit(double obliquityToTheOrbit) {
		this.obliquityToTheOrbit = obliquityToTheOrbit;
	}

	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}

	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		
		if (!(other instanceof SingleBody)) {
			return false;
		}
		
		SingleBody o = (SingleBody) other;
		return this.id == o.id;
	}
	
}
