package com.apoapsys.astronomy.rotation;

import com.apoapsys.astronomy.coords.EquatorialRightAscension;
import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.math.MathExt;
import com.apoapsys.astronomy.time.DateTime;

public class IAUEuropaRotation extends IAUJovianMoonRotation {

	@Override
	protected Angle calculateMeridian(DateTime dt) {
		double[] J = this.calculateJovianMoonElements(dt);
		double jd = (dt.getJulianDay() - 2451545.0);
		
		double meridian = 36.022 + 101.3747235 * jd 
							- 0.980 * MathExt.sin_d(J[4]) 
							- 0.054 * MathExt.sin_d(J[5])
							- 0.014 * MathExt.sin_d(J[6])
							- 0.008 * MathExt.sin_d(J[7]);
		return Angle.fromDegrees(meridian);
	}

	@Override
	protected EquatorialRightAscension calculateOrientation(DateTime dt) {
		double[] J = this.calculateJovianMoonElements(dt);
		double t = dt.getJulianCentury();
		
		double ra = 268.08 - 0.009 * t
							+ 1.086 * MathExt.sin_d(J[4])
							+ 0.060 * MathExt.sin_d(J[5])
							+ 0.015 * MathExt.sin_d(J[6])
							+ 0.009 * MathExt.sin_d(J[7]);



		double dec = 64.51 + 0.003 * t 
							+ 0.468 * MathExt.cos_d(J[4])
							+ 0.026 * MathExt.cos_d(J[5])
							+ 0.007 * MathExt.cos_d(J[6])
							+ 0.002 * MathExt.cos_d(J[7]);
		
		return new EquatorialRightAscension(Angle.fromDegrees(ra), Angle.fromDegrees(dec));
	}

}
