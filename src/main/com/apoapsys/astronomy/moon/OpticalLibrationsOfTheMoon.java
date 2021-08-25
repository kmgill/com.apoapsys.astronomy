package com.apoapsys.astronomy.moon;

import com.apoapsys.astronomy.time.DateTime;

public class OpticalLibrationsOfTheMoon extends MoonUtil {

	private DateTime dt;
	private NutationAndObliquity nutationAndObliquity;
	private PositionOfTheMoon positionOfTheMoon;

	private double inclinationOfTheMeanLunarEquatorToTheEcliptic; // I
	private double opticalLibrationInLongitude; // l_
	private double opticalLibrationInLatitude; // b_
	private double A;
	
	protected OpticalLibrationsOfTheMoon(DateTime dt,
			NutationAndObliquity nutationAndObliquity,
			PositionOfTheMoon positionOfTheMoon) {
		this.dt = dt;
		this.nutationAndObliquity = nutationAndObliquity;
		this.positionOfTheMoon = positionOfTheMoon;

		// Inclination of the mean lunar equator to the ecliptic
		double I = 1.54242;

		

		double W = positionOfTheMoon
				.getGeocentricLongitudeOfTheCenterOfTheMoon()
				- secToDecimal(positionOfTheMoon.getNutationInLongitude())
				- positionOfTheMoon.getLongitudeOfTheMeanAscendingNode();
		W = deg(W);

		double X = (sin(W)
				* cos(positionOfTheMoon
						.getGeocentricLatitudeOfTheCenterOfTheMoon()) * cos(I) - sin(positionOfTheMoon
				.getGeocentricLatitudeOfTheCenterOfTheMoon()) * sin(I));
		double Y = cos(W)
				* cos(positionOfTheMoon
						.getGeocentricLatitudeOfTheCenterOfTheMoon());
		double A = cramp(atan2(X, Y), 360.0);

		// Optical libration in longitude
		double l_ = A - positionOfTheMoon.getMoonsArgumentOfLatitude();

		// Optical libration in latitude
		double b_ = asin(-sin(W)
				* cos(positionOfTheMoon
						.getGeocentricLatitudeOfTheCenterOfTheMoon())
				* sin(I)
				- sin(positionOfTheMoon
						.getGeocentricLatitudeOfTheCenterOfTheMoon()) * cos(I));

		this.A = A;
		inclinationOfTheMeanLunarEquatorToTheEcliptic = I; // I
		opticalLibrationInLongitude = l_; // l_
		opticalLibrationInLatitude = b_; // b_
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

	public double getA() {
		return A;
	}
	
	public double getInclinationOfTheMeanLunarEquatorToTheEcliptic() {
		return inclinationOfTheMeanLunarEquatorToTheEcliptic;
	}

	public double getOpticalLibrationInLongitude() {
		return opticalLibrationInLongitude;
	}

	public double getOpticalLibrationInLatitude() {
		return opticalLibrationInLatitude;
	}

	public static OpticalLibrationsOfTheMoon calculate(DateTime dt) {
		NutationAndObliquity nutationAndObliquity = NutationAndObliquity
				.calculate(dt);
		return calculate(dt, nutationAndObliquity);
	}

	public static OpticalLibrationsOfTheMoon calculate(DateTime dt,
			NutationAndObliquity nutationAndObliquity) {
		PositionOfTheMoon positionOfTheMoon = PositionOfTheMoon.calculate(dt,
				nutationAndObliquity);
		return calculate(dt, nutationAndObliquity, positionOfTheMoon);
	}

	public static OpticalLibrationsOfTheMoon calculate(DateTime dt,
			NutationAndObliquity nutationAndObliquity,
			PositionOfTheMoon positionOfTheMoon) {
		return new OpticalLibrationsOfTheMoon(dt, nutationAndObliquity,
				positionOfTheMoon);
	}

}
