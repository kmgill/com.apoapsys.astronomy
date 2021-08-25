package com.apoapsys.astronomy.rotation;

import com.apoapsys.astronomy.time.DateTime;

public abstract class IAUJovianMoonRotation extends IAURotation {
	
	protected double[] calculateJovianMoonElements(DateTime dt) {
		double t = dt.getJulianCentury();
		double[] J = new double[9];
		
		J[0] = 0;
		J[1] = 73.32 + 91472.9 * t;
		J[2] = 24.62 + 45137.2 * t;
		J[3] = 283.90 + 4850.7 * t;
		J[4] = 355.80 + 1191.3 * t;
		J[5] = 119.90 + 262.1 * t;
		J[6] = 229.80 + 64.3 * t;
		J[7] = 352.25 + 2382.6 * t;
		J[8] = 113.35 + 6070.0 * t;
		
		return J;
	}
	
	
	
	
}
