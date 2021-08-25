package com.apoapsys.astronomy.simulations.nbody.leapfrog;

import com.apoapsys.astronomy.simulations.nbody.Particle;

/** Simple check for the object's distance and whether that is shorter than their combined radius
 * 
 * @author kgill
 *
 */
public class DefaultCollisionDetectionProviderImpl implements CollisionDetectionProvider {

	@Override
	public boolean checkCollision(Particle p0, Particle p1) {
		
		if (p0 == null || p1 == null) {
			return false;
		}
		
		if (p0.enabled == false || p1.enabled == false) {
			return false;
		}
		
		if (p0.equals(p1)) {
			return false; // Cannot collide with one's self
		}
		
		if (p0.body.getRadius() == 0.0 || p1.body.getRadius() == 0.0) {
			return false; // Cannot collide with something that has no volume
		}

		double d = p0.position.getDistanceTo(p1.position);
		if ((p0.body.getRadius() + p1.body.getRadius()) > d ) {
			return true;
		}
		
		return false;
	}

}
