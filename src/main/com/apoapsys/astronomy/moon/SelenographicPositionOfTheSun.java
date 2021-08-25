package com.apoapsys.astronomy.moon;

import com.apoapsys.astronomy.time.DateTime;

public class SelenographicPositionOfTheSun extends MoonUtil {

	private DateTime dt;
	private NutationAndObliquity nutationAndObliquity;
	private PositionOfTheMoon positionOfTheMoon;
	private PhysicalLibrationsOfTheMoon physicalLibrationsOfTheMoon;
	private RelativePositionOfTheSun sunPosition;

	private double selenographicLongitudeOfTheSun; // l__0
	private double selenographicLatitudeOfTheSun; // b__0
	private double selenographicColongitudeOfTheSun; // c0
	private double opticalLibrationInLatitude;
	private double opticalLibrationInLongitude;
	
	protected SelenographicPositionOfTheSun(DateTime dt,
			NutationAndObliquity nutationAndObliquity,
			PositionOfTheMoon positionOfTheMoon,
			PhysicalLibrationsOfTheMoon physicalLibrationsOfTheMoon,
			RelativePositionOfTheSun sunPosition) {
		this.dt = dt;
		this.nutationAndObliquity = nutationAndObliquity;
		this.positionOfTheMoon = positionOfTheMoon;
		this.physicalLibrationsOfTheMoon = physicalLibrationsOfTheMoon;
		this.sunPosition = sunPosition;



		// Geocentric right ascension of the Sun
		double alpha0 = sunPosition.getRightAscensionOfTheSun();

		// Geocentric declination of the Sun
		double delta0 = sunPosition.getDeclinationOfTheSun();

		// Geocentric longitude of Sun
		double lambda0 = sunPosition.getTrueLongitudeOfTheSun();

		// Geocentric right ascension of the Moon
		double alpha = positionOfTheMoon.getApparentRightAscension();

		// Geocentric declination of the Moon
		double delta = positionOfTheMoon.getApparentDeclination();

		// Geocentric longitude of Moon
		double lambda = positionOfTheMoon
				.getGeocentricLongitudeOfTheCenterOfTheMoon();

		// Distance from the Earth to the Sun
		double R = sunPosition.getSunsRadiusVector() * 149597870.700;

		// Distance from the Earth to the Moon
		double Delta = positionOfTheMoon
				.getDistanceBetweenTheCentersOfTheEarthAndMoon();

		// Geocentric latitude of the center of the Moon
		double beta = positionOfTheMoon
				.getGeocentricLatitudeOfTheCenterOfTheMoon();

		double lambdaH = lambda0 + 180 + (Delta / R) * 57.296 * cos(beta)
				* sin(lambda0 - lambda);

		double betaH = Delta / R * beta;

		// Inclination of the mean lunar equator to the ecliptic
		double I = 1.54242;

		double W = lambdaH
				- secToDecimal(nutationAndObliquity.getNutationInLongitude())
				- nutationAndObliquity
						.getLongitudeOfTheAscendingNodeOfTheMoonsMeanOrbit();
		W = deg(W);

		double X = (sin(W) * cos(betaH) * cos(I) - sin(betaH) * sin(I));
		double Y = cos(W) * cos(betaH);
		double A = cramp(atan2(X, Y), 360.0);

		// Optical libration in longitude
		double l_0 = A - positionOfTheMoon.getMoonsArgumentOfLatitude();

		// Optical libration in latitude
		double b_0 = asin(-sin(W) * cos(betaH) * sin(I) - sin(betaH) * cos(I));

		double l__0 = -physicalLibrationsOfTheMoon.getTau()
				+ (physicalLibrationsOfTheMoon.getRho() * cos(A) + physicalLibrationsOfTheMoon
						.getSigma() * sin(A)) * tan(b_0);
		double b__0 = physicalLibrationsOfTheMoon.getSigma() * cos(A)
				- physicalLibrationsOfTheMoon.getRho() * sin(A);

		double l0 = l_0 + l__0;
		double b0 = b_0 + b__0;
		;

		// Selenographic colongitude of the Sun
		double c0;

		if (l0 < 90) {
			c0 = 90 - l0;
		} else {
			c0 = 450 - l0;
		}
		
		opticalLibrationInLatitude = b0;
		opticalLibrationInLongitude = l0;
		selenographicLongitudeOfTheSun = l__0; // l__0
		selenographicLatitudeOfTheSun = b__0; // b__0
		selenographicColongitudeOfTheSun = c0; // c0
	}

	public DateTime getDt() {
		return dt;
	}

	public NutationAndObliquity getNutationAndObliquity() {
		return nutationAndObliquity;
	}

	public PositionOfTheMoon getPositionOfTheMoon() {
		return positionOfTheMoon;
	}

	public PhysicalLibrationsOfTheMoon getPhysicalLibrationsOfTheMoon() {
		return physicalLibrationsOfTheMoon;
	}

	public RelativePositionOfTheSun getSunPosition() {
		return sunPosition;
	}

	public double getSelenographicLongitudeOfTheSun() {
		return selenographicLongitudeOfTheSun;
	}

	public double getSelenographicLatitudeOfTheSun() {
		return selenographicLatitudeOfTheSun;
	}

	public double getSelenographicColongitudeOfTheSun() {
		return selenographicColongitudeOfTheSun;
	}

	public double getOpticalLibrationInLatitude() {
		return opticalLibrationInLatitude;
	}

	public double getOpticalLibrationInLongitude() {
		return opticalLibrationInLongitude;
	}

	public static SelenographicPositionOfTheSun calculate(DateTime dt) {
		NutationAndObliquity nutationAndObliquity = NutationAndObliquity
				.calculate(dt);
		return calculate(dt, nutationAndObliquity);
	}

	public static SelenographicPositionOfTheSun calculate(DateTime dt,
			NutationAndObliquity nutationAndObliquity) {
		PositionOfTheMoon positionOfTheMoon = PositionOfTheMoon.calculate(dt,
				nutationAndObliquity);
		return calculate(dt, nutationAndObliquity, positionOfTheMoon);
	}

	public static SelenographicPositionOfTheSun calculate(DateTime dt,
			NutationAndObliquity nutationAndObliquity,
			PositionOfTheMoon positionOfTheMoon) {
		OpticalLibrationsOfTheMoon opticalLibrations = OpticalLibrationsOfTheMoon
				.calculate(dt, nutationAndObliquity, positionOfTheMoon);
		return calculate(dt, nutationAndObliquity, positionOfTheMoon,
				opticalLibrations);
	}

	public static SelenographicPositionOfTheSun calculate(DateTime dt,
			NutationAndObliquity nutationAndObliquity,
			PositionOfTheMoon positionOfTheMoon,
			OpticalLibrationsOfTheMoon opticalLibrations) {
		PhysicalLibrationsOfTheMoon physicalLibrationsOfTheMoon = PhysicalLibrationsOfTheMoon
				.calculate(dt, nutationAndObliquity, positionOfTheMoon,
						opticalLibrations);
		return calculate(dt, nutationAndObliquity, positionOfTheMoon,
				physicalLibrationsOfTheMoon);
	}

	public static SelenographicPositionOfTheSun calculate(DateTime dt,
			NutationAndObliquity nutationAndObliquity,
			PositionOfTheMoon positionOfTheMoon,
			PhysicalLibrationsOfTheMoon physicalLibrationsOfTheMoon) {
		RelativePositionOfTheSun sunPosition = RelativePositionOfTheSun
				.calculate(dt, nutationAndObliquity);
		return calculate(dt, nutationAndObliquity, positionOfTheMoon,
				physicalLibrationsOfTheMoon, sunPosition);
	}

	public static SelenographicPositionOfTheSun calculate(DateTime dt,
			NutationAndObliquity nutationAndObliquity,
			PositionOfTheMoon positionOfTheMoon,
			PhysicalLibrationsOfTheMoon physicalLibrationsOfTheMoon,
			RelativePositionOfTheSun sunPosition) {
		return new SelenographicPositionOfTheSun(dt, nutationAndObliquity,
				positionOfTheMoon, physicalLibrationsOfTheMoon, sunPosition);
	}

}
