package com.apoapsys.astronomy.rotation;

import com.apoapsys.astronomy.coords.EquatorialRightAscension;
import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.time.DateTime;

public class IAUUranusRotation extends IAURotation {

	@Override
	protected Angle calculateMeridian(DateTime dt) {
		double jd = (dt.getJulianDay() - 2451545.0);
		double meridian = 203.81 - 501.1600928 * jd;
		return Angle.fromDegrees(meridian);
	}

	@Override
	protected EquatorialRightAscension calculateOrientation(DateTime dt) {
		double ra = 257.311;
		double dec = -15.175;

		return new EquatorialRightAscension(Angle.fromDegrees(ra), Angle.fromDegrees(dec));
	}

}
