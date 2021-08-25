package com.apoapsys.astronomy.rotation;

import com.apoapsys.astronomy.coords.EquatorialRightAscension;
import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.math.MathExt;
import com.apoapsys.astronomy.time.DateTime;

public class IAUJupiterRotation extends IAURotation {
	
	
	
	private class JovianElements {
		double Ja;
		double Jb;
		double Jc;
		double Jd;
		double Je;
		
		
		public JovianElements(DateTime dt) {
			
			double t = dt.getJulianCentury();
			Ja = 99.360714 + 4850.4046 * t;
			Jb = 175.895369 + 1191.9605 * t;
			Jc = 300.323162 + 262.5475 * t;
			Jd = 114.012305 + 6070.2476 * t;
			Je = 49.511251 + 64.3000 * t;
		}
		
	}

	@Override
	protected Angle calculateMeridian(DateTime dt) {
		double jd = (dt.getJulianDay() - 2451545.0);
		double meridian = 284.95 + 870.5360000 * jd;
		return Angle.fromDegrees(meridian);
	}

	@Override
	protected EquatorialRightAscension calculateOrientation(DateTime dt) {
		JovianElements e = new JovianElements(dt);
		double t= dt.getJulianCentury();
		
		double ra = 268.056595 - 0.006499 * t 
				+ 0.000117 * MathExt.sin_d(e.Ja) 
				+ 0.000938 * MathExt.sin_d(e.Jb)
				+ 0.001432 * MathExt.sin_d(e.Jc)
				+ 0.000030 * MathExt.sin_d(e.Jd) 
				+ 0.002150 * MathExt.sin_d(e.Je);
				
		double dec = 64.495303 + 0.002413 * t 
				+ 0.000050 * MathExt.cos_d(e.Ja)
				+ 0.000404 * MathExt.cos_d(e.Jb)
				+ 0.000617 * MathExt.cos_d(e.Jc)
				- 0.000013 * MathExt.cos_d(e.Jd) 
				+ 0.000926 * MathExt.cos_d(e.Je);
		
		dec = 86.87;
		
		return new EquatorialRightAscension(Angle.fromDegrees(ra), Angle.fromDegrees(dec));
	}
}
