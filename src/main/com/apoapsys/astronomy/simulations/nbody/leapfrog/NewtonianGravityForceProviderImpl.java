package com.apoapsys.astronomy.simulations.nbody.leapfrog;

import com.apoapsys.astronomy.math.Vector;
import com.apoapsys.astronomy.simulations.nbody.Particle;

public class NewtonianGravityForceProviderImpl implements SimulationForceProvider {

	Vector acceleration = new Vector();
	
	private double multiplier = 1.0;
	private double brownian = 0.0;
	private double drag = 0.0;
	private double damp = 0.0;
	
	@Override
	public Vector onParticle(double deltaT, Particle particle, Particle[] allParticles) {
		particle.getAccelerationAtCurrentTime(allParticles, acceleration);
		acceleration.multiplyScalar(multiplier);
		return acceleration;
	}

	public double getBrownian() {
		return brownian;
	}

	public void setBrownian(double brownian) {
		this.brownian = brownian;
	}

	public double getDrag() {
		return drag;
	}

	public void setDrag(double drag) {
		this.drag = drag;
	}

	public double getDamp() {
		return damp;
	}

	public void setDamp(double damp) {
		this.damp = damp;
	}

	public double getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(double multiplier) {
		this.multiplier = multiplier;
	}
	
	
	
}
