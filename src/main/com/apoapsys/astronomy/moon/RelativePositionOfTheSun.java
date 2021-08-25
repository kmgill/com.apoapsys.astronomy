package com.apoapsys.astronomy.moon;

import com.apoapsys.astronomy.time.DateTime;

public class RelativePositionOfTheSun extends MoonUtil {

	private DateTime dt;
	private NutationAndObliquity nutationAndObliquity;

	private double geometricMeanLongitudeOfTheSun; // L0
	private double meanAnomalyOfTheSun; // M
	private double eccentricityOfTheEarthsOrbit; // e
	private double sunsEquationOfTheCenter; // C
	private double trueLongitudeOfTheSun; // O
	private double trueAnomalyOfTheSun; // v
	private double sunsRadiusVector; // R
	private double omega;
	private double apparentLongitudeOfTheSun; // lambda
	private double rightAscensionOfTheSun; // alpha
	private double declinationOfTheSun; // delta (little)
	private double apparentRightAscensionOfTheSun; // alphaApp
	private double apparentDeclinationOfTheSun; // deltaApp (little delta)

	protected RelativePositionOfTheSun(DateTime dt,
			NutationAndObliquity nutationAndObliquity) {
		this.dt = dt;
		this.nutationAndObliquity = nutationAndObliquity;

		double T = dt.getJulianCentury();

		// Geometric mean longitude of the Sun
		double L0 = 280.46646 + 36000.76983 * T + 0.0003032 * pow(T, 2);
		L0 = deg(L0);

		// Mean anomaly of the Sun
		double M = 357.52911 + 35999.05029 * T - 0.0001537 * pow(T, 2);
		M = deg(M);

		// Eccentricity of the Earth's orbit
		double e = 0.016708634 - 0.000042037 * T - 0.0000001267 * pow(T, 2);

		// Sun's equation of the center
		double C = (1.914602 - 0.004817 * T - 0.000014 * pow(T, 2)) * sin(M)
				+ (0.019993 - 0.000101 * T) * sin(2 * M) + 0.000289
				* sin(3 * M);

		// True longitude of the Sun
		double O = L0 + C;

		// True anomaly of the Sun
		double v = M + C;

		// Sun's radius vector (distance between the centers of
		// the Sun and the Earth, in AU)
		double R = (1.000001018 * (1 - pow(e, 2))) / (1 + e * cos(v));

		// Something important...
		double omega = 125.04 - 1934.136 * T;

		// Apparent longitude of the Sun
		double lambda = O - 0.00569 - 0.00478 * sin(omega);

		double epsilon = nutationAndObliquity.getTrueObliquityOfTheEcliptic();

		double X = cos(epsilon) * sin(O);
		double Y = cos(O);

		// Right Ascension of the Sun
		double alpha = atan2(Y, X);
		alpha = deg(alpha);

		// Declination of the Sun
		double delta = asin(sin(epsilon) * sin(O));

		Y = cos(epsilon) * sin(lambda);
		X = cos(lambda);

		// Apparent Right Ascension of the Sun
		double alphaApp = atan2(Y, X);
		alphaApp = deg(alphaApp);

		// Apparent Declination of the Sun
		double deltaApp = asin(sin(epsilon + 0.00256 * cos(omega))
				* sin(lambda));
		
		
		geometricMeanLongitudeOfTheSun = L0;
		meanAnomalyOfTheSun = M;
		eccentricityOfTheEarthsOrbit = e;
		sunsEquationOfTheCenter = C;
		trueLongitudeOfTheSun = O;
		trueAnomalyOfTheSun = v;
		sunsRadiusVector = R;
		this.omega = omega;
		apparentLongitudeOfTheSun = lambda;
		rightAscensionOfTheSun = alpha;
		declinationOfTheSun = delta; // delta (little)
		apparentRightAscensionOfTheSun = alphaApp; // alphaApp
		apparentDeclinationOfTheSun = deltaApp; // deltaApp (little delta)

	}

	public DateTime getDt() {
		return dt;
	}

	public NutationAndObliquity getNutationAndObliquity() {
		return nutationAndObliquity;
	}

	public double getGeometricMeanLongitudeOfTheSun() {
		return geometricMeanLongitudeOfTheSun;
	}

	public double getMeanAnomalyOfTheSun() {
		return meanAnomalyOfTheSun;
	}

	public double getEccentricityOfTheEarthsOrbit() {
		return eccentricityOfTheEarthsOrbit;
	}

	public double getSunsEquationOfTheCenter() {
		return sunsEquationOfTheCenter;
	}

	public double getTrueLongitudeOfTheSun() {
		return trueLongitudeOfTheSun;
	}

	public double getTrueAnomalyOfTheSun() {
		return trueAnomalyOfTheSun;
	}

	public double getSunsRadiusVector() {
		return sunsRadiusVector;
	}

	public double getOmega() {
		return omega;
	}

	public double getApparentLongitudeOfTheSun() {
		return apparentLongitudeOfTheSun;
	}

	public double getRightAscensionOfTheSun() {
		return rightAscensionOfTheSun;
	}

	public double getDeclinationOfTheSun() {
		return declinationOfTheSun;
	}

	public double getApparentRightAscensionOfTheSun() {
		return apparentRightAscensionOfTheSun;
	}

	public double getApparentDeclinationOfTheSun() {
		return apparentDeclinationOfTheSun;
	}

	public static RelativePositionOfTheSun calculate(DateTime dt) {
		NutationAndObliquity nutationAndObliquity = NutationAndObliquity
				.calculate(dt);
		return calculate(dt, nutationAndObliquity);
	}

	public static RelativePositionOfTheSun calculate(DateTime dt,
			NutationAndObliquity nutationAndObliquity) {
		return new RelativePositionOfTheSun(dt, nutationAndObliquity);
	}
}
