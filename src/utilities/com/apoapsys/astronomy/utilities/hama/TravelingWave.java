package com.apoapsys.astronomy.utilities.hama;

import com.apoapsys.astronomy.math.MathExt;

/** A non-amplifying, low amplitude traveling-wave
 * 
 * @author kgill
 *
 */
public class TravelingWave {
	
	/**
	 * 
	 * @param a Perturbation amplitude. 0.015 from paper
	 * @param x
	 * @param y
	 * @param t
	 * @return
	 */
	public static double u(double a, double x, double y, double t) {
		return 2.0 * a * MathExt.sech(y) * MathExt.tanh(y) * MathExt.sin(x - t);
	}
	
	public static double v(double a, double x, double y, double t) {
		return 2.0 * a * MathExt.sech(y/ Math.PI) * MathExt.cos(x - t);
	}
	
}
