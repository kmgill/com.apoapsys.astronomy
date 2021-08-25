package com.apoapsys.astronomy.simulations.nbody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.apoapsys.astronomy.Constants;
import com.apoapsys.astronomy.bodies.SingleBody;
import com.apoapsys.astronomy.containers.LimitedLengthLinkedListQueue;
import com.apoapsys.astronomy.math.MathExt;
import com.apoapsys.astronomy.math.Vector;
import com.apoapsys.astronomy.simulations.nbody.leapfrog.SimulationForceProvider;

public class Particle {
	
	public String identifier;
	public SingleBody body;
	public Vector position;
	public Vector velocity;
	
	public LimitedLengthLinkedListQueue<Vector> previousPositions = new LimitedLengthLinkedListQueue<>(3);
	public LimitedLengthLinkedListQueue<Vector> previousVelocities = new LimitedLengthLinkedListQueue<>(3);

	public Particle boundTo = null;
	public boolean blackHole = false;
	public boolean enabled = true;
	
	public List<SimulationForceProvider> ownForces = new ArrayList<>();
	
	public Map<String, Object> extendedProperties = new HashMap<>();
	
	public Particle() {
		this(null);
	}
	
	public Particle(SingleBody body) {
		this(body, new Vector(), new Vector());
	}
	
	public Particle(SingleBody body, Vector position, Vector velocity) {
		this.body = body;
		this.identifier = (body != null) ? body.getName() : null;
		this.position = position;
		this.velocity = velocity;
	}
	
	public Vector getPreviousPosition() {
		return previousPositions.peekLast();
	}
	
	public Vector getPreviousVelocity() {
		return previousVelocities.peekLast();
	}
	
	public double getKineticEnergy() {
		double e = .5 * body.getMass() * MathExt.sqr(velocity.length());
		return e;
	}
	
	public double getPotentialEnergy(Particle other) {
		double m = body.getMass();
		double M = body.getMass();
		double r = position.getDistanceTo(other.position);
		double U =  ((-Constants.G * m * M) / r) + Constants.K;
		return U;
	}
	
	
	public double getForceOnParticle(Particle other) {
		double d = other.position.getDistanceTo(position);
		double F = (Constants.G * body.getMass() * other.body.getMass()) / (d * d);
		return F;
	}
	
	public double getTotalForceOnParticle(Particle[] particles) {
		
		Vector F = new Vector();
		for (Particle particle : particles) {
			if (!particle.equals(this)) {
				F.add(getForceVectorOnParticle(particle));
			}
		}
		

		double g = 1.0 / body.getMass() * F.length();
		return g;
	}
	
	public double getTotal2DForceOnParticle(Particle[] particles, Particle center) {
		
		Vector F = new Vector();
		//double F = 0.0;
		
		
		for (Particle particle : particles) {
			if (!particle.equals(this)) {
				Vector f = getForceVectorOnParticle(particle);
				F.add(f);
			}
		}
		
		//F.divideScalar(particles.length - 1);
		//Vector Fc = getForceVectorOnParticle(center);
		//double centrifugalForce = MathExt.sqrt(MathExt.sqr(Fc.x) + MathExt.sqr(Fc.z));
		//double centrifugalForce = getCentrifugalForce(center);
		//double Ueff = this.getEffectivePotentialEnergy(center);
		//F.multiplyScalar(- Constants.G);
		//double E = getGravitationalPotential(center);
		//double mag = MathExt.sqrt(MathExt.sqr(F.x) + MathExt.sqr(F.z));
		double mag = F.length();
		//System.err.println(Ueff);
		//double g = centrifugalForce - mag ;//mag;// + centrifugalForce;
		//double g = mag - 1.0 / body.getMass() * centrifugalForce;
		return mag;
	}
	
	
	public double getLagrangianForceThreeBody(Particle center, Particle outter) {

		
		Vector F = new Vector();
		
		F.add(getForceVectorOnParticle(center));
		F.add(getForceVectorOnParticle(outter));
		
		
		double w = getAngularVelocity(center);
		
		double m = body.getMass();
		double r = position.getDistanceTo(center.position);
		//double Fc = m * r * MathExt.sqr(w);
		
		//double Fc = getCentrifugalForce(center);
		//double Fc = outter.getCentrifugalForce(center);
		Vector FcV = getForceVectorOnParticle(center);
		//double Fc = MathExt.sqrt(FcV.x * FcV.x + FcV.z * FcV.z);
		//double Fg = MathExt.sqrt(F.x * F.x + F.z * F.z);
		
		
		double Fc = FcV.length();
		double Fg = F.length();
		
		return Fg - Fc;
		
		
		
	}
	
	
	public double getGravitationalPotential(Particle center) {
		double m = body.getMass();
		double M = center.body.getMass();
		double r0 = getPreviousPosition().getDistanceTo(center.getPreviousPosition());
		double r = position.getDistanceTo(center.position);
		double tr = r - r0;
		double phi = this.getAngularVelocity(center);
		
		double E = .5 * m + ((tr * tr) + (r * r) * (phi * phi)) - ((Constants.G * m * M) / r);
		return E;
	}
	
	public double getEstimatedOrbitalVelocity(Particle center) {
		
		double m = body.getMass();
		double M = center.body.getMass();
		double r = position.getDistanceTo(center.position);
		
		double Y = Constants.G * (m + M);
		double X = r;
		double v = MathExt.sqrt(Y / X);
		
		return v;
	}

	public double getCentrifugalForce(Particle center) {
		double r = position.getDistanceTo(center.position);
		
		//double v = velocity.length();
		double v = MathExt.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);
		//double v= getAngularVelocity(center);
		//if (v == 0.0) {
		//	v = getEstimatedOrbitalVelocity(center);
		//}
		//double v = getAngularVelocity(center);
		
		
		// Rotational speed
		//double w = v / r;
		
		double w = r / v;
			
		//w *= Constants._180_BY_PI;
		
		double m = body.getMass();
		
		double f = (m * MathExt.sqr(w)) / r;
		//double f = m * r * MathExt.sqr(w);
		return f;
	}
	
	public double getLegrangianFunction(Particle center) {
		double T = this.getKineticEnergy();
		double V = this.getPotentialEnergy(center);
		double L = T - V;
		return L;
	}
	
	public Vector getForceVectorOnParticle(Particle other) {
		return getForceVectorOnParticle(other, null);
	}
	
	public Vector getForceVectorOnParticle(Particle other, Vector into) {
		if (into == null) {
			into = new Vector();
		}
		
		double dX = other.position.x - position.x;
		double dY = other.position.y - position.y;
		double dZ = other.position.z - position.z;
		double d = other.position.getDistanceTo(position);

		double F = (Constants.G * body.getMass() * other.body.getMass()) / (d * d);
		double fX0 = F * dX / d;
		double fY0 = F * dY / d;
		double fZ0 = F * dZ / d;
		
		into.x = fX0;
		into.y = fY0;
		into.z = fZ0;
		
		
		return into;
	}
	
	Vector forceFromOther = new Vector();
	
	public Vector getForceVectorOnParticle(Particle[] particles) {
		if (!enabled) {
			return new Vector();
		}
		
		return getForceVectorOnParticle(particles, null);
	}
	
	public Vector getForceVectorOnParticle(Particle[] particles, Vector into) {
		
		if (into == null) {
			into = new Vector();
		} else {
			into.x = into.y = into.z = 0.0;
		}
		
		if (!enabled) {
			return into;
		}
		
		for (Particle other : particles) {
			
			if (this.equals(other) || other.enabled == false) {
				continue;
			}
			
			double d = this.position.getDistanceTo(other.position);
			//if (d / 1000.0 < (this.body.getRadius() + other.body.getRadius())) {
			//	continue;
			//}

			getForceVectorOnParticle(other, forceFromOther);

			into.x += forceFromOther.x;
			into.y += forceFromOther.y;
			into.z += forceFromOther.z;
		}
		
		return into;
	}
	
	/*
	Vector forceOnParticles = new Vector();
	public double getTotalForceOnParticle(Particle[] particles) {
		getForceVectorOnParticle(particles, forceOnParticles);
		double f = MathExt.sqrt(MathExt.sqr(forceOnParticles.x) + MathExt.sqr(forceOnParticles.y) + MathExt.sqr(forceOnParticles.z));
		
		double g = 1.0 / body.getMass() * f;
		
		return g;
	}
	*/

	public Vector getAccelerationAtCurrentTime(Vector force) {
		return getAccelerationAtCurrentTime(force, null);
	}
	
	public Vector getAccelerationAtCurrentTime(Vector force, Vector into) {
		if (into == null) {
			into = new Vector();
		}
		
		double aX = force.x / body.getMass();
		double aY = force.y / body.getMass();
		double aZ = force.z / body.getMass();
		
		if (Double.isNaN(aX)) {
			aX = 0.0;
		}
		
		if (Double.isNaN(aY)) {
			aY = 0.0;
		}
		
		if (Double.isNaN(aZ)) {
			aZ = 0.0;
		}
		
		into.x = aX;
		into.y = aY;
		into.z = aZ;
		
		return into;
	}
	
	public Vector getAccelerationAtCurrentTime(Particle[] particles) {
		return getAccelerationAtCurrentTime(particles, null);
	}
	
	Vector forceOnParticles = new Vector();
	public Vector getAccelerationAtCurrentTime(Particle[] particles, Vector into) {	
		if (into == null) {
			into = new Vector();
		}
		
		getForceVectorOnParticle(particles, forceOnParticles);
		getAccelerationAtCurrentTime(forceOnParticles, into);
		
		return into;
	}
	

	public double getAngularMomentumSpinningBody() {
		double m = body.getMass();
		double r = body.getRadius();
		double L = (2.0 / 5.0) * m * r * r * (2.0 * Math.PI / 86400.0);
		return L;
	}
	
	
	public double getAngularMomentumOrbitingBody(Particle centerParticle) {
		double m = body.getMass();
		double R = position.getDistanceTo(centerParticle.position);
		double l = m * MathExt.sqr(R);
		double T = 0.1;//365.25 * 24 * 60 * 60;
		double w = 2.0 * Math.PI / T;
		double L = l * w;
		return L;
	}
	
	public double getAngularVelocity(Particle centerParticle) {
		//double R = position.getDistanceTo(centerParticle.position);
		//double m = body.getMass();
		//double I = m * MathExt.sqr(R);
		
		//double L = getAngularMomentumOrbitingBody(centerParticle);
		
		//double w = L / I;
		double v = MathExt.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);
		double r = position.getDistanceTo(centerParticle.position);
		double w = v / r;
		return w;
	}
	

	public double getEffectivePotentialEnergy(Particle other) {
		
		double r = this.position.getDistanceTo(other.position);
		
		double L = this.getAngularMomentumOrbitingBody(other);
		
		double m = this.body.getMass();
		double M = other.body.getMass();

		double effectivePotential = ((L * L) / (2.0 * m * (r * r))) - ((Constants.G * m * M) / r);

		return effectivePotential;
	}
	
	
	/** Not sure if this is correct
	 * 
	 * @param particles
	 * @param deltaT
	 * @return
	 */
	public double getEffectivePotentialEnergy(Particle[] particles) {
		double ttlEnergy = 0.0;
		
		for (Particle other : particles) {
			if (!other.equals(this)) {
				ttlEnergy += getEffectivePotentialEnergy(other);
			}
			//double d = this.position.getDistanceTo(other.position);
			//if (d / 1000.0 <= (this.body.getRadius() + other.body.getRadius())) {
				// We are inside a body, this force does not apply here
			//	return Double.NaN;
			//}

			
			
		}
		
		
		return 1.0 / body.getMass() * ttlEnergy;// / particles.length;
	}

	
	public double getEscapeVelocity(Particle other) {
		double r = other.position.getDistanceTo(position);
		double v = MathExt.sqrt((2 * Constants.G * other.body.getMass()) / r);
		return v;
	}
	
	public double getEscapeVelocity() {
		double ev = MathExt.sqrt((2.0 * Constants.G * body.getMass()) / body.getRadius());
		return ev;
	}
	
	
	public void determineGravitationBinding(List<Particle> particles) {
		
		Particle boundTo = null;
		double minR = Double.MAX_VALUE;
		
		for (Particle particle : particles) {
			if (this.equals(particle)) {
				continue;
			}
			
			double k = getKineticEnergy();
			double p = getPotentialEnergy(particle);
			double r = k + p;
			if (r < minR) {
				boundTo = particle;
				minR = r;
			}
		}
		this.boundTo = boundTo;
	}

	
	public Particle getMergedParticle(Particle other) {
		
		Particle merged = new Particle();
		
		Vector position = this.position.clone().add(other.position).divideScalar(2.0);
		
		double ttlMass = this.body.getMass() + other.body.getMass();
		Vector thisVelocity = this.velocity.clone().multiplyScalar(this.body.getMass() / ttlMass);
		Vector otherVelocity = other.velocity.clone().multiplyScalar(other.body.getMass() / ttlMass);
		
		Vector velocity = thisVelocity.add(otherVelocity);
//		/Vector velocity = this.velocity.clone().add(other.velocity);
		
		merged.position = position;
		merged.velocity = velocity;
		
		merged.body = new SingleBody();
		merged.body.setName(this.body.getName() + " + " + other.body.getName());
		merged.body.setMass(this.body.getMass() + other.body.getMass());
		merged.body.setId(this.body.getId());
		merged.blackHole = (blackHole || other.blackHole);
		double radius = (merged.blackHole) ? 1.0 : MathExt.pow((merged.body.getMass() / 5.97219E+24), 0.3) * 6378.135;
		
		merged.body.setRadius(radius);
		return merged;
	}
	
	
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		
		if (other == null) {
			return false;
		}
		
		if (!(other instanceof Particle)) {
			return false;
		}
		
		Particle o = (Particle) other;
		return body.equals(o.body);
		
	}
	
}
