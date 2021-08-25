package com.apoapsys.astronomy.simulations.nbody.leapfrog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.apoapsys.astronomy.Constants;
import com.apoapsys.astronomy.math.MathExt;
import com.apoapsys.astronomy.math.Vector;
import com.apoapsys.astronomy.simulations.nbody.Particle;
import com.google.common.collect.Lists;

/** Simple and extensible time-shifting simulation engine.
 * 
 * @author kgill
 *
 */
public class LeapFrogSimulator {

	
	private boolean mergesOnCollision = false;
	
	private List<Particle> particles = Collections.synchronizedList(new ArrayList<Particle>());
	
	private List<SimulationForceProvider> forceProviders = Collections.synchronizedList(new ArrayList<SimulationForceProvider>());
	private List<CollisionDetectionProvider> collisionProviders = Collections.synchronizedList(new ArrayList<CollisionDetectionProvider>());
	private List<GravitationalBindingProvider> bindingProviders = Collections.synchronizedList(new ArrayList<GravitationalBindingProvider>());
	
	private Particle[] particlesArray;
	private SimulationForceProvider[] forceProvidersArray;
	private CollisionDetectionProvider[] collisionProvidersArray;
	private GravitationalBindingProvider[] bindingProvidersArray;
	
	
	private boolean checkForCollisions = true;
	private boolean checkForGravitationalBinding = true;
	
	public LeapFrogSimulator() {
		this.addCollisionDetectionProvider(new DefaultCollisionDetectionProviderImpl());
		this.addGravitationalBindingProvider(new DefaultGravitationalBindingProviderImpl());

	}
	
	/** Add a particle with it's initial state
	 * 
	 * @param particle
	 */
	public void addParticle(Particle particle) {
		this.particles.add(particle);
		updateArrays();
	}
	
	public Particle checkForCollision(Particle particle) {

		if (particle.body.getRadius() == 0.0) {
			return null;
		}
		
		for (Particle other : particlesArray) {

			for (CollisionDetectionProvider provider : collisionProvidersArray) {
				if (provider.checkCollision(particle, other)) {
					return other;
				}
			}

		}
		return null;
	}
	
	
	
	
	public List<Particle> getParticles() {
		return Lists.newArrayList(particles);
	}

	

	Vector acceleration = new Vector();
	protected void stepVelocityVectors(double deltaT) {
		
		synchronized(particlesArray) {
			for (Particle particle : particlesArray) {
				
				acceleration.reset();
				for (SimulationForceProvider provider : forceProvidersArray) {
					Vector accelSegment = provider.onParticle(deltaT, particle, particlesArray);
					acceleration.add(accelSegment);
				}
				
				for (SimulationForceProvider provider : particle.ownForces) {
					Vector accelSegment = provider.onParticle(deltaT, particle, particlesArray);
					acceleration.add(accelSegment);
				}
				
				particle.previousVelocities.add(particle.velocity.clone());
				
				particle.velocity.x += deltaT * acceleration.x;
				particle.velocity.y += deltaT * acceleration.y;
				particle.velocity.z += deltaT * acceleration.z;
				
				particle.velocity.x = MathExt.min(particle.velocity.x, Constants.C);
				particle.velocity.y = MathExt.min(particle.velocity.y, Constants.C);
				particle.velocity.z = MathExt.min(particle.velocity.z, Constants.C);
			}
		}
	}
	
	protected void stepPositionVectors(double deltaT) {
		for (Particle particle : particles) {
			
			particle.previousPositions.add(particle.position.clone());
			particle.position.x += deltaT * particle.velocity.x;
			particle.position.y += deltaT * particle.velocity.y;
			particle.position.z += deltaT * particle.velocity.z;
		}
	}
	
	protected void determineGravitationBinding() {
		
		for (Particle particle : particlesArray) {
			
			for (GravitationalBindingProvider provider : bindingProvidersArray) {
				Particle boundTo = provider.findParticleGravitationalBinding(particle, particlesArray);
				
				if (boundTo != null) {
					particle.boundTo = boundTo;
				}
			}
			
		}
	}
	
	
	public List<Collision> step(double deltaT) {
		
		
		stepVelocityVectors(deltaT);
		stepPositionVectors(deltaT);

		
		if (this.checkForGravitationalBinding) {
			determineGravitationBinding();
		}
		 
		List<Collision> collisions = null;//Lists.newArrayList();
		List<Collision> reducedCollisions = collisions;// = Lists.newArrayList();
		
		if (this.checkForCollisions) {
			for (Particle particle : particlesArray) {
				Particle collidesWith = checkForCollision(particle);
				if (collidesWith != null) {
					
					if (collisions == null) {
						collisions = Lists.newArrayList();
					}
					
					collisions.add(new Collision(particle, collidesWith));
				}
			}

			if(this.mergesOnCollision && collisions != null) {
				reducedCollisions = Lists.newArrayList();
				for (Collision collision : collisions) {
					if (particles.contains(collision.particle0) && particles.contains(collision.particle1)) {
						particles.remove(collision.particle0);
						particles.remove(collision.particle1);
		
						particles.add(collision.merged);
						reducedCollisions.add(collision);
					}
				}
			}
		} 
		
		return reducedCollisions;
	}

	public boolean isMergingOnCollision() {
		return mergesOnCollision;
	}

	public void setMergingOnCollision(boolean mergesOnCollision) {
		this.mergesOnCollision = mergesOnCollision;
	}
	
	public boolean isCheckingForCollisions() {
		return checkForCollisions;
	}

	public void setCheckingForCollisions(boolean checkForCollisions) {
		this.checkForCollisions = checkForCollisions;
	}

	public boolean isCheckForGravitationalBinding() {
		return checkForGravitationalBinding;
	}

	public void setCheckForGravitationalBinding(boolean checkForGravitationalBinding) {
		this.checkForGravitationalBinding = checkForGravitationalBinding;
	}

	public void addSimulationForceProvider(SimulationForceProvider provider) {
		this.forceProviders.add(provider);
		updateArrays();
	}
	
	public boolean removeSimulationForceProvider(SimulationForceProvider provider) {
		boolean r = this.forceProviders.remove(provider);
		updateArrays();
		return r;
	}
	
	public List<SimulationForceProvider> getSimulationForceProviders() {
		return forceProviders;
	}
	
	public void addCollisionDetectionProvider(CollisionDetectionProvider provider) {
		this.collisionProviders.add(provider);
		updateArrays();
	}
	
	public boolean removeCollisionDetectionProvider(CollisionDetectionProvider provider) {
		boolean r = this.collisionProviders.remove(provider);
		updateArrays();
		return r;
	}

	
	public List<CollisionDetectionProvider> getCollisionDetectionProviders() {
		return collisionProviders;
	}
	
	public void addGravitationalBindingProvider(GravitationalBindingProvider provider) {
		bindingProviders.add(provider);
		updateArrays();
	}
	
	public boolean removeGravitationalBindingProvider(GravitationalBindingProvider provider) {
		boolean r = bindingProviders.remove(provider);
		updateArrays();
		return r;
	}
	
	public List<GravitationalBindingProvider> getGravitationalBindingProviders() {
		return bindingProviders;
	}
	
	protected void updateArrays() {
		collisionProvidersArray = new CollisionDetectionProvider[collisionProviders.size()];
		collisionProviders.toArray(collisionProvidersArray);
		
		bindingProvidersArray = new GravitationalBindingProvider[bindingProviders.size()];
		bindingProviders.toArray(bindingProvidersArray);
		
		forceProvidersArray = new SimulationForceProvider[forceProviders.size()];
		forceProviders.toArray(forceProvidersArray);
		
		particlesArray = new Particle[particles.size()];
		particles.toArray(particlesArray);
	}
	
}
