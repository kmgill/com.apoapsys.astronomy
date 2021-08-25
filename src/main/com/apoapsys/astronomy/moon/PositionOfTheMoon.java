package com.apoapsys.astronomy.moon;

import com.apoapsys.astronomy.time.DateTime;

public class PositionOfTheMoon extends MoonUtil {

	private double meanLongitudeOfTheSun; // L
	private double meanLongitudeOfTheMoon; // L_
	private double meanElongationOfTheMoon; // D
	private double meanAnomalyOfTheSun; // M
	private double meanAnomalyOfTheMoon; // M_
	private double moonsArgumentOfLatitude; // F
	private double longitudeOfTheMeanAscendingNode; // omega
	private double geocentricLongitudeOfTheCenterOfTheMoon; // lambda
	private double geocentricLatitudeOfTheCenterOfTheMoon; // beta
	private double distanceBetweenTheCentersOfTheEarthAndMoon; // Delta
	private double equatorialHorizontalParallax; // pi
	private double nutationInLongitude; // Delta psi (pitchfork)
	private double nutationInObliquity; // delta epsilon
	private double apparentGeocentricLongitudeOfTheCenterOfTheMoon; // apparentLambda
	private double meanObliquityOfTheEcliptic; // epsilon0
	private double trueObliquityOfTheEcliptic; // epsilon
	private double apparentRightAscension; // alpha
	private double apparentDeclination; // delta
	private double meanPerigeeOfTheLunarOrbit; // Pi

	protected PositionOfTheMoon(DateTime dt,
			NutationAndObliquity nutationAndObliquity) {

		double T = dt.getJulianCentury();

		// Mean Longitude of the Sun (ch. 22)
		double L = 280.4665 + 36000.7698 * T;

		// Mean Longitude of the Moon (ch. 47.1), or Mean Equinox of the Date,
		// including the constant term of the
		// effect of the light time (-0.70)
		double L_ = 218.3164477 + 481267.88123421 * T - 0.0015786 * (T * T)
				+ (T * T * T) / 538841 - (T * T * T * T) / 65194000;

		// Mean Elongation of the Moon (ch. 47.2)
		double D = 297.8501921 + 445267.1114034 * T - 0.0018819 * (T * T)
				+ (T * T * T) / 545868 - (T * T * T * T) / 113065000;

		// Mean Anomaly of the Sun (ch. 47.3)
		double M = 357.5291092 + 35999.0502909 * T - 0.0001536 * (T * T)
				+ (T * T * T) / 24490000;

		// Mean Anomaly of the Moon (ch. 47.4)
		double M_ = 134.9633964 + 477198.8675005 * T + 0.0087414 * (T * T)
				+ (T * T * T) / 69699 - (T * T * T * T) / 14712000;

		// Moon's Argument of Latitude (mean distance of the Moon from it's
		// ascending node) (ch. 47.5)
		double F = 93.2720950 + 483202.0175233 * T - 0.0036539 * (T * T)
				- (T * T * T) / 3526000 + (T * T * T * T) / 863310000;

		// Longitude of the Mean Ascending Node (ch. 47.7)
		double omega = 125.0445479 - 1934.1362891 * T + 0.0020754 * (T * T)
				+ (T * T * T) / 467441 - (T * T * T * T) / 60616000;

		double A1 = 119.75 + 131.849 * T;
		double A2 = 53.09 + 479264.290 * T;
		double A3 = 313.45 + 481266.484 * T;

		// Eccentricity of the Earth's orbit around the Sun
		double E = 1 - 0.002516 * T - 0.0000074 * (T * T);

		L = deg(L);
		L_ = deg(L_);
		D = deg(D);
		M = deg(M);
		M_ = deg(M_);
		F = deg(F);
		A1 = deg(A1);
		A2 = deg(A2);
		A3 = deg(A3);

		// Longitude of the Moon, unit is 0.000001 degrees
		double sigmal = 0;
		double sigmar = 0;
		double sigmab = 0;

		for (int i = 0; i < table47A.length; i++) {
			double[] row = table47A[i];
			double e;
			if (row[1] == 1 || row[1] == -1) {
				e = E;
			} else if (row[1] == 2 || row[1] == -2) {
				e = E * E;
			} else {
				e = 1;
			}

			sigmal += row[4] * e
					* sin(row[0] * D + row[1] * M + row[2] * M_ + row[3] * F);
			sigmar += row[5] * e
					* cos(row[0] * D + row[1] * M + row[2] * M_ + row[3] * F);

		}

		sigmal += 3958 * sin(A1) + 1962 * sin(L_ - F) + 318 * sin(A2);

		for (int i = 0; i < table47B.length; i++) {
			double[] row = table47B[i];
			double e;
			if (row[1] == 1 || row[1] == -1) {
				e = E;
			} else if (row[1] == 2 || row[1] == -2) {
				e = E * E;
			} else {
				e = 1;
			}

			sigmab += row[4] * e
					* sin(row[0] * D + row[1] * M + row[2] * M_ + row[3] * F);
		}

		sigmab += -2235 * sin(L_) + 382 * sin(A3) + 175 * sin(A1 - F) + 175
				* sin(A1 + F) + 127 * sin(L_ - M_) - 115 * sin(L_ + M_);

		// Geocentric longitude of the center of the Moon
		double lambda = L_ + sigmal / 1000000;

		// Geocentric latitude of the center of the Moon
		double beta = sigmab / 1000000;

		// Distance in kilometers between the centers of the Earth and Moon.
		double Delta = 385000.56 + sigmar / 1000;

		// Equatorial horizontal parallax
		double pi = asin(6378.14 / Delta);

		// Nutation in Longitude
		double deltaPsi = nutationAndObliquity.getNutationInLongitude() / 60 / 60;

		// Nutation in Obliquity
		double deltaEpsilon = nutationAndObliquity.getNutationInObliquity();

		// Time measured in units of 10000 Julian years since J2000.0
		double U = T / 100;

		double apparentLambda = lambda + deltaPsi;

		// Mean obliquity of the ecliptic
		double epsilon0 = nutationAndObliquity.getMeanObliquityOfTheEcliptic();

		// True obliquity of the ecliptic
		double epsilon = nutationAndObliquity.getTrueObliquityOfTheEcliptic();

		// Apparent right ascension (ch. 13.3)
		double X = cos(lambda);
		double Y = (sin(lambda) * cos(epsilon) - tan(beta) * sin(epsilon));
		double alpha = deg(atan2(Y, X));
		// double alpha = atan((sin(lamba) * cos(epsilon) - tan(beta) *
		// sin(epsilon)) / cos(lamba));

		// Apparent declination (ch. 13.4)
		double delta = asin(sin(beta) * cos(epsilon) + cos(beta) * sin(epsilon)
				* sin(lambda));

		// Mean perigee of the lunar orbit
		double Pi = 83.3532465 + 4096.0137287 * T - 0.0103200 * pow(T, 2)
				- pow(T, 3) / 80053 + pow(T, 4) / 18999000;

		meanLongitudeOfTheSun = L; // L
		meanLongitudeOfTheMoon = L_; // L_
		meanElongationOfTheMoon = D; // D
		meanAnomalyOfTheSun = M; // M
		meanAnomalyOfTheMoon = M_; // M_
		moonsArgumentOfLatitude = F; // F
		longitudeOfTheMeanAscendingNode = omega; // omega
		geocentricLongitudeOfTheCenterOfTheMoon = lambda; // lambda
		geocentricLatitudeOfTheCenterOfTheMoon = beta; // beta
		distanceBetweenTheCentersOfTheEarthAndMoon = Delta; // Delta
		equatorialHorizontalParallax = pi; // pi
		nutationInLongitude = deltaPsi; // Delta psi (pitchfork)
		nutationInObliquity = deltaEpsilon; // delta epsilon
		apparentGeocentricLongitudeOfTheCenterOfTheMoon = apparentLambda; // apparentLambda
		meanObliquityOfTheEcliptic = epsilon0; // epsilon0
		trueObliquityOfTheEcliptic = epsilon; // epsilon
		apparentRightAscension = alpha; // alpha
		apparentDeclination = delta; // delta
		meanPerigeeOfTheLunarOrbit = Pi; // Pi
	}

	public double getMeanLongitudeOfTheSun() {
		return meanLongitudeOfTheSun;
	}

	public double getMeanLongitudeOfTheMoon() {
		return meanLongitudeOfTheMoon;
	}

	public double getMeanElongationOfTheMoon() {
		return meanElongationOfTheMoon;
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

	public double getLongitudeOfTheMeanAscendingNode() {
		return longitudeOfTheMeanAscendingNode;
	}

	public double getGeocentricLongitudeOfTheCenterOfTheMoon() {
		return geocentricLongitudeOfTheCenterOfTheMoon;
	}

	public double getGeocentricLatitudeOfTheCenterOfTheMoon() {
		return geocentricLatitudeOfTheCenterOfTheMoon;
	}

	public double getDistanceBetweenTheCentersOfTheEarthAndMoon() {
		return distanceBetweenTheCentersOfTheEarthAndMoon;
	}

	public double getEquatorialHorizontalParallax() {
		return equatorialHorizontalParallax;
	}

	public double getNutationInLongitude() {
		return nutationInLongitude;
	}

	public double getNutationInObliquity() {
		return nutationInObliquity;
	}

	public double getApparentGeocentricLongitudeOfTheCenterOfTheMoon() {
		return apparentGeocentricLongitudeOfTheCenterOfTheMoon;
	}

	public double getMeanObliquityOfTheEcliptic() {
		return meanObliquityOfTheEcliptic;
	}

	public double getTrueObliquityOfTheEcliptic() {
		return trueObliquityOfTheEcliptic;
	}

	public double getApparentRightAscension() {
		return apparentRightAscension;
	}

	public double getApparentDeclination() {
		return apparentDeclination;
	}

	public double getMeanPerigeeOfTheLunarOrbit() {
		return meanPerigeeOfTheLunarOrbit;
	}

	public static PositionOfTheMoon calculate(DateTime dt) {
		NutationAndObliquity nutationAndObliquity = NutationAndObliquity
				.calculate(dt);
		return new PositionOfTheMoon(dt, nutationAndObliquity);
	}

	public static PositionOfTheMoon calculate(DateTime dt,
			NutationAndObliquity nutationAndObliquity) {
		return new PositionOfTheMoon(dt, nutationAndObliquity);
	}

}
