package com.apoapsys.astronomy.simulations.nbody.leapfrog;

import com.apoapsys.astronomy.simulations.nbody.Particle;

public interface CollisionDetectionProvider {
	public boolean checkCollision(Particle p0, Particle p1);
}
