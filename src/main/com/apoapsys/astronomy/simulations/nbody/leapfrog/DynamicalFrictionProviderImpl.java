package com.apoapsys.astronomy.simulations.nbody.leapfrog;

import com.apoapsys.astronomy.Constants;
import com.apoapsys.astronomy.math.MathExt;
import com.apoapsys.astronomy.math.Vector;
import com.apoapsys.astronomy.simulations.nbody.Particle;

/** Simplified formula for the force from dynamical friction
 * 
 * @author kgill
 *
 */
public class DynamicalFrictionProviderImpl implements SimulationForceProvider {

	@Override
	public Vector onParticle(double deltaT, Particle particle, Particle[] allParticles) {
		
		
		
		
		return new Vector();
	}
	
	
	
	protected double getForce(Particle particle, double mediumDensity) {
		
		double G2 = MathExt.sqr(Constants.G);
		double M2 = MathExt.sqr(particle.body.getMass());
		if (M2 == 0.0) {
			return 0.0;
		}
		
		double v2 = MathExt.sqr(particle.velocity.length());
		
		double p = mediumDensity;
		
		double C = 0.0; // ????
		
		double f = C * ((G2 * M2 * p) / v2);
		
		return f;
	}
}
