package com.apoapsys.astronomy.rotation;

import com.apoapsys.astronomy.coords.EquatorialRightAscension;
import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.math.MathExt;
import com.apoapsys.astronomy.time.DateTime;

public class IAUMercuryRotation extends IAURotation {

	private double[] calculateElements(DateTime dt) {
		double[] M = new double[6];
		
		double jd = (dt.getJulianDay() - 2451545.0);
		
		M[0] = 0;
		M[1] = 174.791086 + 4.092335 * jd;
		M[2] = 349.582171 + 8.184670 * jd;
		M[3] = 164.373257 + 12.277005 * jd;
		M[4] = 339.164343 + 16.369340 * jd;
		M[5] = 153.955429 + 20.461675 * jd;
		
		return M;
	}
	
	@Override
	protected Angle calculateMeridian(DateTime dt) {
		
		double[] M = calculateElements(dt);
		
		double meridian = 329.5469 + 6.1385025 * (dt.getJulianDay() - 2451545.0)
				+ 0.00993822 * MathExt.sin_d(M[1])
				- 0.00104581 * MathExt.sin_d(M[2])
				- 0.00010280 * MathExt.sin_d(M[3])
				- 0.00002364 * MathExt.sin_d(M[4])
				- 0.00000532 * MathExt.sin_d(M[5]);
		return Angle.fromDegrees(meridian);
	}

	@Override
	protected EquatorialRightAscension calculateOrientation(DateTime dt) {
		double ra = 281.0097 - 0.0328 * dt.getJulianCentury();
		double dec = 61.4143 - 0.0049 * dt.getJulianCentury();
		
		return new EquatorialRightAscension(Angle.fromDegrees(ra), Angle.fromDegrees(dec));
	}

}
