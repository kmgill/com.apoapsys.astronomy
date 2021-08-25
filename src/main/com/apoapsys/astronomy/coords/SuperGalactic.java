package com.apoapsys.astronomy.coords;

import com.apoapsys.astronomy.math.Angle;

/**
 * A supergalactic system of coordinates (SGL), as defined by de Vaucouleurs, et al with the North Pole (SGB)
 * in the direction of galactic coordinates l=47.37 deg, b=+6.32 deg (Courtois et al.).
 * 
 * 
 * Courtois, H., et al (2013, June 14). "Cosmography of the Local Universe". http://arxiv.org/abs/1306.0091v4
 * de Vaucouleurs, G., et al. 1991, "Third Reference Catalogue of Bright Galaxies". Volume I: Explanations
 * and references.
 * 
 * @author kgill
 *
 */
public class SuperGalactic extends AbstractLatLonCoordinateSystem implements CoordinateSystem {
	
	public SuperGalactic() {

	}

	public SuperGalactic(Angle latitude, Angle longitude) {
		super(latitude, longitude);
	}

}
