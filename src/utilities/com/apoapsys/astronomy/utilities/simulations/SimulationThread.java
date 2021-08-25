package com.apoapsys.astronomy.utilities.simulations;

public interface SimulationThread {
	public void setPaused(boolean paused);
	public boolean isPaused();
	public void stopSimulation();
}
