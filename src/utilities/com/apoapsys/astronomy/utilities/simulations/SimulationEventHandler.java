package com.apoapsys.astronomy.utilities.simulations;

import com.apoapsys.astronomy.simulations.nbody.leapfrog.Collision;
import com.apoapsys.astronomy.time.DateTime;

public interface SimulationEventHandler {
	
	public void onCollision(Collision collision, DateTime date);
	
	public void onStep(double deltaT, DateTime date);
	
}
