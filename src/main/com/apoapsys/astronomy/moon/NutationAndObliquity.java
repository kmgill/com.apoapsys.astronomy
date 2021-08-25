package com.apoapsys.astronomy.moon;

import com.apoapsys.astronomy.time.DateTime;

public class NutationAndObliquity extends MoonUtil {

	private DateTime dt;

	private double meanElongationOfTheMoonFromTheSun; // D
	private double meanAnomalyOfTheSun; // (Earth) // M
	private double meanAnomalyOfTheMoon; // M_
	private double moonsArgumentOfLatitude; // F, ch. 22

	/**
	 * Longitude of teh ascending node of the Moon's mean orbit on the ecliptic
	 * Measured from the mean equinox of the date (ch. 22)
	 */
	private double longitudeOfTheAscendingNodeOfTheMoonsMeanOrbit; // Omega

	private double meanLongitudeOfTheSun; // L
	private double meanLongitudeOfTheMoon; // L_

	/**
	 * Time measured in units of 10000 Julian years since J2000.0
	 * 
	 */
	private double U;

	private double meanObliquityOfTheEcliptic; // epsilon0

	private double nutationInLongitude; // deltaPsi (pitchfork)
	private double nutationInObliquity; // deltaEpsilon
	private double trueObliquityOfTheEcliptic; // epsilon

	protected NutationAndObliquity(DateTime dt) {
		this.dt = dt;

		double T = dt.getJulianCentury();

		// Mean Elongation Of The Moon From The Sun
		double D = 297.85036 + 445267.111480 * T - 0.0019142 * (T * T)
				+ (T * T * T) / 189474;

		// Mean Anomaly of the Sun (Earth)
		double M = 357.52772 + 35999.050340 * T - 0.0001603 * (T * T)
				- (T * T * T) / 300000;

		// Mean Anomaly of the Moon
		double M_ = 134.96298 + 477198.867398 * T + 0.0086972 * (T * T)
				+ (T * T * T) / 56250;

		// Moon's Argument of Latitude (ch. 22)
		double F = 93.27191 + 483202.017538 * T - 0.0036825 * (T * T)
				+ (T * T * T) / 327270;

		// Longitude of teh ascending node of the Moon's mean orbit on the
		// ecliptic
		// Measured from the mean equinox of the date (ch. 22)
		double omega = 125.04452 - 1934.136261 * T + 0.0020708 * (T * T)
				+ (T * T * T) / 450000;

		// Mean Longitude of the Sun
		double L = 280.4665 + 36000.7698 * T;

		// Mean Longitude of the Moon
		double L_ = 218.3165 + 481267.8813 * T;

		// Time measured in units of 10000 Julian years since J2000.0
		double U = T / 100;

		// Mean obliquity of the ecliptic
		double epsilon0 = degToDecimal(23, 26, 21.448)
				- degToDecimal(0, 0, 46.8150) * T - 1.55 * pow(U, 2) + 1999.25
				* pow(U, 3) - 51.38 * pow(U, 4) - 249.67 * pow(U, 5) - 39.05
				* pow(U, 6) + 7.12 * pow(U, 7) + 27.87 * pow(U, 8) + 5.79
				* pow(U, 9) + 2.45 * pow(U, 10);

		D = deg(D);
		M = deg(M);
		M_ = deg(M_);
		F = deg(F);
		omega = deg(omega);

		// Nutation in Longitude Limited accuracy:
		double deltaPsi = -17.20 * sin(omega) - 1.32 * sin(2 * L) - 0.23
				* sin(2 * L_) + 0.21 * sin(2 * omega);

		// Nutation in Obliquity Limited accuracy:
		double deltaEpsilon = 9.20 * cos(omega) + 0.57 * cos(2 * L) + 0.10
				* cos(2 * L_) - 0.09 * cos(2 * omega);

		// True obliquity of the ecliptic
		double epsilon = epsilon0 + deltaEpsilon / 60 / 60;
		
		
		meanElongationOfTheMoonFromTheSun = D;
		meanAnomalyOfTheSun = M;
		meanAnomalyOfTheMoon = M_;
		moonsArgumentOfLatitude = F;
		longitudeOfTheAscendingNodeOfTheMoonsMeanOrbit = omega;
		meanLongitudeOfTheSun = L;
		meanLongitudeOfTheMoon = L_;
		this.U = U;

		meanObliquityOfTheEcliptic = epsilon0;
		nutationInLongitude = deltaPsi;
		nutationInObliquity = deltaEpsilon;
		trueObliquityOfTheEcliptic = epsilon;

	}

	public DateTime getDt() {
		return dt;
	}

	public double getMeanElongationOfTheMoonFromTheSun() {
		return meanElongationOfTheMoonFromTheSun;
	}

	public double getMeanAnomalyOfTheSun() {
		return meanAnomalyOfTheSun;
	}

	public double getMeanAnomalyOfTheMoon() {
		return meanAnomalyOfTheMoon;
	}

	public double getMoonsArgumentOfLatitude() {
		return moonsArgumentOfLatitude;
	}

	/**
	 * Longitude of teh ascending node of the Moon's mean orbit on the ecliptic
	 * Measured from the mean equinox of the date (ch. 22)
	 */
	public double getLongitudeOfTheAscendingNodeOfTheMoonsMeanOrbit() {
		return longitudeOfTheAscendingNodeOfTheMoonsMeanOrbit;
	}

	public double getMeanLongitudeOfTheSun() {
		return meanLongitudeOfTheSun;
	}

	public double getMeanLongitudeOfTheMoon() {
		return meanLongitudeOfTheMoon;
	}

	/**
	 * Time measured in units of 10000 Julian years since J2000.0
	 * 
	 */
	public double getU() {
		return U;
	}

	public double getMeanObliquityOfTheEcliptic() {
		return meanObliquityOfTheEcliptic;
	}

	public double getNutationInLongitude() {
		return nutationInLongitude;
	}

	public double getNutationInObliquity() {
		return nutationInObliquity;
	}

	public double getTrueObliquityOfTheEcliptic() {
		return trueObliquityOfTheEcliptic;
	}

	public static NutationAndObliquity calculate(DateTime dt) {
		return new NutationAndObliquity(dt);
	}

}
