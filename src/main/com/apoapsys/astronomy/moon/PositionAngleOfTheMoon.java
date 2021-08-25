package com.apoapsys.astronomy.moon;

import com.apoapsys.astronomy.time.DateTime;

public class PositionAngleOfTheMoon extends MoonUtil {

	private DateTime dt;
	private NutationAndObliquity nutationAndObliquity;
	private PositionOfTheMoon positionOfTheMoon;
	private PhysicalLibrationsOfTheMoon physicalLibrationsOfTheMoon;
	private TotalLibrationsOfTheMoon totalLibrationsOfTheMoon;

	private double positionAngleOfTheMooon;

	protected PositionAngleOfTheMoon(DateTime dt, NutationAndObliquity nutationAndObliquity, PositionOfTheMoon positionOfTheMoon, PhysicalLibrationsOfTheMoon physicalLibrationsOfTheMoon,
			TotalLibrationsOfTheMoon totalLibrationsOfTheMoon) {
		this.dt = dt;
		this.nutationAndObliquity = nutationAndObliquity;
		this.positionOfTheMoon = positionOfTheMoon;
		this.physicalLibrationsOfTheMoon = physicalLibrationsOfTheMoon;
		this.totalLibrationsOfTheMoon = totalLibrationsOfTheMoon;

		double b = totalLibrationsOfTheMoon.getTotalLibrationsInLatitude();

		double Omega = nutationAndObliquity.getLongitudeOfTheAscendingNodeOfTheMoonsMeanOrbit();

		// Inclination of the mean lunar equator to the ecliptic
		double I = 1.54242;

		double deltaPsi = nutationAndObliquity.getNutationInLongitude();

		double rho = physicalLibrationsOfTheMoon.getRho();

		double epsilon = positionOfTheMoon.getTrueObliquityOfTheEcliptic();

		double alpha = positionOfTheMoon.getApparentRightAscension();

		double sigma = physicalLibrationsOfTheMoon.getSigma();

		double V = Omega + deltaPsi + sigma / sin(I);

		double Y = sin(I + rho) * sin(V);

		double X = sin(I + rho) * cos(V) * cos(epsilon) - cos(I + rho) * sin(epsilon);

		double omega = atan2(Y, X);

		double P = asin((sqrt(pow(X, 2) + pow(Y, 2)) * cos(alpha - omega)) / cos(b));

		positionAngleOfTheMooon = P;
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

	public TotalLibrationsOfTheMoon getTotalLibrationsOfTheMoon() {
		return totalLibrationsOfTheMoon;
	}

	public double getPositionAngleOfTheMooon() {
		return positionAngleOfTheMooon;
	}

	public static PositionAngleOfTheMoon calculate(DateTime dt) {
		NutationAndObliquity nutationAndObliquity = NutationAndObliquity.calculate(dt);
		return calculate(dt, nutationAndObliquity);
	}

	public static PositionAngleOfTheMoon calculate(DateTime dt, NutationAndObliquity nutationAndObliquity) {
		PositionOfTheMoon positionOfTheMoon = PositionOfTheMoon.calculate(dt, nutationAndObliquity);
		return calculate(dt, nutationAndObliquity, positionOfTheMoon);
	}

	public static PositionAngleOfTheMoon calculate(DateTime dt, NutationAndObliquity nutationAndObliquity, PositionOfTheMoon positionOfTheMoon) {
		OpticalLibrationsOfTheMoon opticalLibrations = OpticalLibrationsOfTheMoon.calculate(dt, nutationAndObliquity, positionOfTheMoon);
		return calculate(dt, nutationAndObliquity, positionOfTheMoon, opticalLibrations);
	}

	public static PositionAngleOfTheMoon calculate(DateTime dt, NutationAndObliquity nutationAndObliquity, PositionOfTheMoon positionOfTheMoon, OpticalLibrationsOfTheMoon opticalLibrations) {
		PhysicalLibrationsOfTheMoon physicalLibrationsOfTheMoon = PhysicalLibrationsOfTheMoon.calculate(dt, nutationAndObliquity, positionOfTheMoon, opticalLibrations);
		return calculate(dt, nutationAndObliquity, positionOfTheMoon, opticalLibrations, physicalLibrationsOfTheMoon);
	}

	public static PositionAngleOfTheMoon calculate(DateTime dt, NutationAndObliquity nutationAndObliquity, PositionOfTheMoon positionOfTheMoon, OpticalLibrationsOfTheMoon opticalLibrations,
			PhysicalLibrationsOfTheMoon physicalLibrationsOfTheMoon) {
		TotalLibrationsOfTheMoon totalLibrationsOfTheMoon = TotalLibrationsOfTheMoon.calculate(dt, opticalLibrations, physicalLibrationsOfTheMoon);
		return calculate(dt, nutationAndObliquity, positionOfTheMoon, physicalLibrationsOfTheMoon, totalLibrationsOfTheMoon);
	}

	public static PositionAngleOfTheMoon calculate(DateTime dt, NutationAndObliquity nutationAndObliquity, PositionOfTheMoon positionOfTheMoon,
			PhysicalLibrationsOfTheMoon physicalLibrationsOfTheMoon, TotalLibrationsOfTheMoon totalLibrationsOfTheMoon) {
		return new PositionAngleOfTheMoon(dt, nutationAndObliquity, positionOfTheMoon, physicalLibrationsOfTheMoon, totalLibrationsOfTheMoon);
	}

}
