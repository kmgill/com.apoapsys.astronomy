package com.apoapsys.astronomy.utilities.simulations;

import java.util.List;

import com.apoapsys.astronomy.simulations.nbody.Particle;
import com.apoapsys.astronomy.simulations.nbody.leapfrog.LeapFrogSimulator;

public interface SimulationWorker {
	public void beginSimulation();
	
	public SimulationThread getThread();

	public LeapFrogSimulator getSimulator();

	public void setSimulator(LeapFrogSimulator simulator);

	public List<TrackedBody> getTracked();

	public void setTracked(List<TrackedBody> tracked);

	public double getDeltaT();

	public void setDeltaT(double deltaT);

	public long getWriteDelayMillis();

	public void setWriteDelayMillis(long writeDelayMillis);
	
	public void addSimulationEventHandler(SimulationEventHandler handler);
	public boolean removeSimulationEventHandler(SimulationEventHandler handler);
	
	public TrackedBody getTrackedBodyByName(String name);
	public TrackedBody getTrackedBodyByParticle(Particle particle);
}
