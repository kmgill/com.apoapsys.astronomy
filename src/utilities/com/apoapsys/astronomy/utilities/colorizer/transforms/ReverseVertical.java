package com.apoapsys.astronomy.utilities.colorizer.transforms;

import com.apoapsys.astronomy.utilities.colorizer.Point;
import com.apoapsys.astronomy.utilities.colorizer.Transform;

public class ReverseVertical implements Transform {
	
	@Override
	public void transform(Point p) {
		p.y = 1023 - p.y;
	}
	
}
