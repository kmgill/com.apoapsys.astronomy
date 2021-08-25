package com.apoapsys.astronomy.rotation;

import com.apoapsys.astronomy.coords.EquatorialRightAscension;
import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.time.DateTime;


public class StaticRotation extends IAURotation {

	@Override
	protected Angle calculateMeridian(DateTime dt) {
		return Angle.fromDegrees(0);
	}

	@Override
	protected EquatorialRightAscension calculateOrientation(DateTime dt) {
		return new EquatorialRightAscension(Angle.fromDegrees(90), Angle.fromDegrees(90));
	}

}
