package com.apoapsys.astronomy.rotation;

import com.apoapsys.astronomy.coords.EquatorialRightAscension;
import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.time.DateTime;

public class IAUTitanRotation extends IAURotation {

	@Override
	protected Angle calculateMeridian(DateTime dt) {
		double jd = (dt.getJulianDay() - 2451545.0);
		double meridian = 186.5855 + 22.5769768 * jd;
		return Angle.fromDegrees(meridian);
	}

	@Override
	protected EquatorialRightAscension calculateOrientation(DateTime dt) {
		double ra = 39.4827;
		double dec = 83.4279;
		
		dec = 63.27;
		
		return new EquatorialRightAscension(Angle.fromDegrees(ra), Angle.fromDegrees(dec));
	}

}
