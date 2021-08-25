package com.apoapsys.astronomy.utilities.simulations;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

import com.apoapsys.astronomy.math.MathExt;
import com.apoapsys.astronomy.simulations.nbody.Particle;
import com.apoapsys.astronomy.simulations.nbody.leapfrog.Collision;
import com.apoapsys.astronomy.simulations.nbody.leapfrog.LeapFrogSimulator;
import com.apoapsys.astronomy.time.DateTime;
import com.google.common.collect.Lists;

public class SimpleSimulationEngineWorker extends Thread implements SimulationWorker, SimulationThread {
	
	protected LeapFrogSimulator simulator;
	protected List<TrackedBody> tracked = Lists.newArrayList();
	
	protected int iterStep = 0;
	protected double deltaT = 1.0;
	protected double timePassed = 0;
	protected double startTime = 0;
	
	protected boolean paused = false;
	protected boolean stop = false;
	
	protected long writeDelayMillis = 1000;
	
	protected long lastWrite = System.currentTimeMillis();
	
	private List<SimulationEventHandler> eventHandlers = Lists.newArrayList();
	
	public SimpleSimulationEngineWorker(LeapFrogSimulator simulator) {
		this(simulator, null);
	}
	
	public SimpleSimulationEngineWorker(LeapFrogSimulator simulator, List<TrackedBody> tracked) {
		this.simulator = simulator;
		
		if (tracked != null) {
			this.tracked = Collections.synchronizedList(tracked);
		} else {
			this.tracked = Collections.synchronizedList(this.tracked);
		}
	}
	
	@Override
	public void beginSimulation() {
		startTime = System.currentTimeMillis();
		this.start();
	}
	
	@Override
	public void run() {
		
		while(!stop) {
			
			if (!isPaused()) {
				step();
			} else {
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	private void step() {
		timePassed += deltaT;
		List<Collision> collisions = simulator.step(deltaT);
		
		double currentTime = startTime + (timePassed * 1000);
		long currentTimeMillis = (long)MathExt.round(currentTime);
		DateTime dt = new DateTime(currentTimeMillis);
		
		if (collisions != null) {
			for (Collision collision : collisions) {
				TrackedBody body0 = getTrackedBodyByParticle(collision.particle0);
				TrackedBody body1 = getTrackedBodyByParticle(collision.particle1);
				
				
				fireCollisionHandlers(collision, dt);
				
				tracked.remove(body0);
				tracked.remove(body1);
				
				TrackedBody trackedBody = new TrackedBody(collision.merged, Color.WHITE, 0);
				trackedBody.isHypothetical = (body0.isHypothetical && body1.isHypothetical);
	
				
				tracked.add(trackedBody);

				
			}
		}
		
		
		iterStep ++;
		
		fireOnStepHandlers(deltaT, dt);
	}
	
	@Override
	public void stopSimulation() {
		stop = true;
	}
	
	@Override
	public boolean isPaused() {
		return paused;
	}
	
	@Override
	public void setPaused(boolean p) {
		paused = p;
	}
	
	@Override
	public TrackedBody getTrackedBodyByName(String name) {
		for (TrackedBody trackedBody : tracked) {
			if (trackedBody.particle.body.getName().equalsIgnoreCase(name)) {
				return trackedBody;
			}
		}
		return null;
	}
	
	@Override
	public TrackedBody getTrackedBodyByParticle(Particle particle) {
		for (TrackedBody trackedBody : tracked) {
			if (trackedBody.particle.equals(particle)) {
				return trackedBody;
			}
		}
		return null;
	}
	
	protected void fireCollisionHandlers(Collision collision, DateTime date) {
		for (SimulationEventHandler handler : eventHandlers) {
			handler.onCollision(collision, date);
		}
	}
	
	protected void fireOnStepHandlers(double deltaT, DateTime dt) {
		for (SimulationEventHandler handler : eventHandlers) {
			handler.onStep(deltaT, dt);
		}
	}
	
	@Override
	public SimulationThread getThread() {
		return this;
	}

	public LeapFrogSimulator getSimulator() {
		return simulator;
	}

	public void setSimulator(LeapFrogSimulator simulator) {
		this.simulator = simulator;
	}

	public List<TrackedBody> getTracked() {
		return tracked;
	}

	public void setTracked(List<TrackedBody> tracked) {
		this.tracked = tracked;
	}

	public double getDeltaT() {
		return deltaT;
	}

	public void setDeltaT(double deltaT) {
		this.deltaT = deltaT;
	}

	public long getWriteDelayMillis() {
		return writeDelayMillis;
	}

	public void setWriteDelayMillis(long writeDelayMillis) {
		this.writeDelayMillis = writeDelayMillis;
	}

	@Override
	public void addSimulationEventHandler(SimulationEventHandler handler) {
		eventHandlers.add(handler);
	}

	@Override
	public boolean removeSimulationEventHandler(SimulationEventHandler handler) {
		return eventHandlers.remove(handler);
	}


	
	
	
}
