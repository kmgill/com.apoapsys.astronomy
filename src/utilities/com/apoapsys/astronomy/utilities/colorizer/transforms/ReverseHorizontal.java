package com.apoapsys.astronomy.utilities.colorizer.transforms;

import com.apoapsys.astronomy.utilities.colorizer.Point;
import com.apoapsys.astronomy.utilities.colorizer.Transform;

public class ReverseHorizontal implements Transform {
	
	@Override
	public void transform(Point p) {
		p.x = 1023 - p.x;
	}
	
}
