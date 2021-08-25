package com.apoapsys.astronomy.simulations.nbody.leapfrog;

import com.apoapsys.astronomy.simulations.nbody.Particle;

public class Collision {
	public Particle particle0;
	public Particle particle1;
	public Particle merged;
	
	public Collision(Particle particle0, Particle particle1) {
		this.particle0 = particle0;
		this.particle1 = particle1;
		this.merged = particle0.getMergedParticle(particle1);
	}
}
