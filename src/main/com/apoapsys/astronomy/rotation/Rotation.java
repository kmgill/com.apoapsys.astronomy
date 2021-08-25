package com.apoapsys.astronomy.rotation;

import com.apoapsys.astronomy.math.Angle;
import com.apoapsys.astronomy.time.DateTime;

public interface Rotation {
	
	RotationalOrientation getOrientation(DateTime dt);
	Angle getSiderealRotation(DateTime dt);
	
	RotationalQuaternion computeRotationalQuaternion(DateTime dt);
	RotationalQuaternion computeRotationalQuaternion(DateTime dt, boolean ignoreMeridian);
}
