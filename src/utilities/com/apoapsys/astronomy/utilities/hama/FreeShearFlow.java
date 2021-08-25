package com.apoapsys.astronomy.utilities.hama;

import com.apoapsys.astronomy.math.MathExt;

public class FreeShearFlow {
	
	public static double u(double y) {
		return 2.1 +  MathExt.sin(y * 0.5 * Math.PI) * 2.0;
		//return 1.0 + MathExt.tanh_d(y) * 20.0 / MathExt.sin(y);
	}
	
	public static double v() {
		return 0.0;
	}
}
