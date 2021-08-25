package com.apoapsys.astronomy.simulations.nbody.leapfrog;

import com.apoapsys.astronomy.simulations.nbody.Particle;

public interface GravitationalBindingProvider {
	public Particle findParticleGravitationalBinding(Particle particle, Particle[] allParticles);
}
