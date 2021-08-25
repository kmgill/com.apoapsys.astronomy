package com.apoapsys.astronomy.rotation;

import com.apoapsys.astronomy.coords.EquatorialRightAscension;
import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.math.MathExt;
import com.apoapsys.astronomy.time.DateTime;

public class IAUNeptuneRotation extends IAURotation {

	
	private double computeN(DateTime dt) {
		return 357.85 + 52.316 * dt.getJulianCentury();
	}
	
	@Override
	protected Angle calculateMeridian(DateTime dt) {
		double jd = (dt.getJulianDay() - 2451545.0);
		double N = computeN(dt);
		double meridian = 253.18 + 536.3128492 * jd - 0.48 * MathExt.sin_d(N);
		return Angle.fromDegrees(meridian);
	}

	@Override
	protected EquatorialRightAscension calculateOrientation(DateTime dt) {
		double N = computeN(dt);

		double ra = 299.36 + 0.70 * MathExt.sin_d(N);
		double dec = 43.46 - 0.51 * MathExt.cos_d(N);
		
		return new EquatorialRightAscension(Angle.fromDegrees(ra), Angle.fromDegrees(dec));
	}

}
