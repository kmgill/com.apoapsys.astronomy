package com.apoapsys.astronomy.utilities.colorizer.transforms;

import com.apoapsys.astronomy.utilities.colorizer.Point;
import com.apoapsys.astronomy.utilities.colorizer.Transform;

public class RotateCounterClockwise implements Transform {

	@Override
	public void transform(Point p) {
		// Hard coded for 1024x1024 images for now...
		int half = 512;
		
		if (p.x < half && p.y < half) { // Top Left
			int x = p.x;
			p.x = p.y;
			p.y = 1023 - x;
		} else if (p.x >= half && p.y < half) { // Top Right
			int x = p.x;
			p.x = p.y;
			p.y = 1023 - x;
		} else if (p.x >= half && p.y >= half) { // Bottom Right
			int x = p.x;
			p.x = p.y;
			p.y = 1023 - x;
		} else { // Bottom Left
			int x = p.x;
			p.x = p.y;
			p.y = 1023 - x;
			
		}
	}

}
