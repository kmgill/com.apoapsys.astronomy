package com.apoapsys.astronomy.orbits.custom;

import com.apoapsys.astronomy.math.MathExt;
import com.apoapsys.astronomy.orbits.EclipticOrbitPosition;
import com.apoapsys.astronomy.orbits.Orbit;
import com.apoapsys.astronomy.time.DateTime;

public abstract class CustomJovianOrbit implements Orbit<EclipticOrbitPosition> {

	protected static final double RADIUS = 71398.0;
	
	protected class JovianElements {

		public double[] l = new double[4];
		public double[] p = new double[4];
		public double[] w = new double[4];

		public double gamma;
		public double phi;
		public double psi;
		public double G;
		public double G_;
		public double pi;
	}
	
	@Override
	public double getPeriod() {
		return 0.0;
	}
	
	@Override
	public DateTime getEpoch()
	{
		return new DateTime();
	}
	
	protected JovianElements computeElements(DateTime dt) {

		double t = dt.getJulianDaySinceEpoch(2443000.5);

		JovianElements e = new JovianElements();

		e.l[0] = 106.07719 + 203.488955790 * t;
		e.l[1] = 175.73161 + 101.374724735 * t;
		e.l[2] = 120.55883 + 50.317609207 * t;
		e.l[3] = 84.44459 + 21.571071177 * t;

		e.p[0] = 97.0881 + 0.16138586 * t;
		e.p[1] = 154.8663 + 0.04726307 * t;
		e.p[2] = 188.1840 + 0.00712734 * t;
		e.p[3] = 335.2868 + 0.00184000 * t;

		e.w[0] = 312.3346 - 0.13279386 * t;
		e.w[1] = 100.4411 - 0.03263064 * t;
		e.w[2] = 119.1942 - 0.00717703 * t;
		e.w[3] = 322.6186 - 0.00175934 * t;

		// Principle inequality in the longitude of Jupiter
		e.gamma = 0.33033 * MathExt.sin_d(163.679 + 0.0010512 * t) + 0.03439
				* MathExt.sin_d(34.486 - 0.0161731 * t);

		// Phase of free libration
		e.phi = 199.6766 + 0.17379190 * t;

		// Longitude of the node of the equator of Jupiter on the ecliptic
		e.psi = 316.5182 - 0.00000208 * t;

		// Mean anomalies of Jupiter and Saturn
		e.G = 30.23756 + 0.0830925701 * t + e.gamma;
		e.G_ = 31.97853 + 0.0334597339 * t;

		// Longitude of the perihelion of Jupiter
		e.pi = 13.469942;
		return e;

	}

}
