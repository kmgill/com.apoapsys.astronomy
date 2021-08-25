package com.apoapsys.astronomy.simulations.nbody.leapfrog;

import com.apoapsys.astronomy.math.Vector;
import com.apoapsys.astronomy.simulations.nbody.Particle;

public class TidalAccelerationProviderImpl implements SimulationForceProvider {

	@Override
	public Vector onParticle(double deltaT, Particle particle, Particle[] allParticles) {
		
		return new Vector();
	}

}
