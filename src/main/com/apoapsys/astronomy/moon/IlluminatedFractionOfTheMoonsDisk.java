package com.apoapsys.astronomy.moon;

import com.apoapsys.astronomy.time.DateTime;

public class IlluminatedFractionOfTheMoonsDisk extends MoonUtil {

	private DateTime dt;
	private RelativePositionOfTheSun positionOfTheSun;

	private double illuminatedFractionOfTheMoonsDisk; // k
	private double positionAngleOfTheMoonsBrightLimb; // chi
	private double phaseAngle; // i - Phase angle (Selenocentric elongation of
								// the Earth from the Sun)

	protected IlluminatedFractionOfTheMoonsDisk(DateTime dt, PositionOfTheMoon positionOfTheMoon, RelativePositionOfTheSun positionOfTheSun) {
		this.dt = dt;
		this.positionOfTheSun = positionOfTheSun;

		// Geocentric right ascension of the Sun
		double alpha0 = positionOfTheSun.getApparentRightAscensionOfTheSun();

		// Geocentric declination of the Sun
		double delta0 = positionOfTheSun.getApparentDeclinationOfTheSun();

		// Geocentric longitude of Sun
		double lambda0 = positionOfTheSun.getTrueLongitudeOfTheSun();

		// Geocentric right ascension of the Moon
		double alpha = positionOfTheMoon.getApparentRightAscension();

		// Geocentric declination of the Moon
		double delta = positionOfTheMoon.getApparentDeclination();

		// Geocentric longitude of Moon
		double lambda = positionOfTheMoon.getGeocentricLongitudeOfTheCenterOfTheMoon();

		// Geocentric elongation of the Moon from the Sun
		double psi = acos(sin(delta0) * sin(delta) + cos(delta0) * cos(delta) * cos(alpha0 - alpha));

		// Distance from the Earth to the Sun
		double R = positionOfTheSun.getSunsRadiusVector() * 149597870.700;

		// Distance from the Earth to the Moon
		double Delta = positionOfTheMoon.getDistanceBetweenTheCentersOfTheEarthAndMoon();

		double Y = R * sin(psi);
		double X = Delta - R * cos(psi);

		// Phase angle (Selenocentric elongation of the Earth from the Sun)
		double i = atan2(Y, X);

		double D = positionOfTheMoon.getMeanElongationOfTheMoon();

		double M = positionOfTheMoon.getMeanAnomalyOfTheSun();

		double M_ = positionOfTheMoon.getMeanAnomalyOfTheMoon();

		// Phase angle (Selenocentric elongation of the Earth from the Sun)
		// Less accurate method
		/*
		 * double i = 180 - D - 6.289 * sin(M_) + 2.100 * sin(M) - 1.274 * sin(2
		 * * D - M_) - 0.658 * sin(2 * D) - 0.214 * sin(2 * M_) - 0.110 *
		 * sin(D);
		 */

		// Illuminated fraction of the Moon's disk
		double k = (1 + cos(i)) / 2;

		Y = cos(delta0) * sin(alpha0 - alpha);
		X = sin(delta0) * cos(delta) - cos(delta0) * sin(delta) * cos(alpha0 - alpha);

		// Position angle of the Moon's bright limb
		double chi = atan2(Y, X);

		illuminatedFractionOfTheMoonsDisk = k; // k
		positionAngleOfTheMoonsBrightLimb = chi; // chi
		phaseAngle = i; // i - Phase angle (Selenocentric elongation of the
						// Earth from the Sun)
	}

	public DateTime getDt() {
		return dt;
	}

	public RelativePositionOfTheSun getPositionOfTheSun() {
		return positionOfTheSun;
	}

	public double getIlluminatedFractionOfTheMoonsDisk() {
		return illuminatedFractionOfTheMoonsDisk;
	}

	public double getPositionAngleOfTheMoonsBrightLimb() {
		return positionAngleOfTheMoonsBrightLimb;
	}

	public double getPhaseAngle() {
		return phaseAngle;
	}

	public static IlluminatedFractionOfTheMoonsDisk calculate(DateTime dt) {
		NutationAndObliquity nutationAndObliquity = NutationAndObliquity.calculate(dt);
		return calculate(dt, nutationAndObliquity);
	}

	public static IlluminatedFractionOfTheMoonsDisk calculate(DateTime dt, NutationAndObliquity nutationAndObliquity) {
		PositionOfTheMoon positionOfTheMoon = PositionOfTheMoon.calculate(dt, nutationAndObliquity);
		return calculate(dt, nutationAndObliquity, positionOfTheMoon);
	}

	public static IlluminatedFractionOfTheMoonsDisk calculate(DateTime dt, NutationAndObliquity nutationAndObliquity, PositionOfTheMoon positionOfTheMoon) {
		RelativePositionOfTheSun positionOfTheSun = RelativePositionOfTheSun.calculate(dt, nutationAndObliquity);
		return calculate(dt, positionOfTheMoon, positionOfTheSun);
	}

	public static IlluminatedFractionOfTheMoonsDisk calculate(DateTime dt, PositionOfTheMoon positionOfTheMoon, RelativePositionOfTheSun positionOfTheSun) {
		return new IlluminatedFractionOfTheMoonsDisk(dt, positionOfTheMoon, positionOfTheSun);
	}

}
