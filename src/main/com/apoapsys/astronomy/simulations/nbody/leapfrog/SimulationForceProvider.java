package com.apoapsys.astronomy.simulations.nbody.leapfrog;

import com.apoapsys.astronomy.math.Vector;
import com.apoapsys.astronomy.simulations.nbody.Particle;

public interface SimulationForceProvider {
	
	/** Provides a force algorithm and returns an acceleration vector
	 * 
	 * @param deltaT
	 * @param particle
	 * @param allParticles
	 * @return
	 */
	public Vector onParticle(double deltaT, Particle particle, Particle[] allParticles);
	
}
