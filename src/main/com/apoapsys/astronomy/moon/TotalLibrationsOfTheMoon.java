package com.apoapsys.astronomy.moon;

import com.apoapsys.astronomy.time.DateTime;

public class TotalLibrationsOfTheMoon extends MoonUtil {

	private DateTime dt;
	private OpticalLibrationsOfTheMoon opticalLibrations;
	private PhysicalLibrationsOfTheMoon physicalLibrationsOfTheMoon;

	private double totalLibrationsInLatitude;
	private double totalLibrationsInLongitude;

	protected TotalLibrationsOfTheMoon(DateTime dt,
			OpticalLibrationsOfTheMoon opticalLibrations,
			PhysicalLibrationsOfTheMoon physicalLibrationsOfTheMoon) {
		this.dt = dt;
		this.opticalLibrations = opticalLibrations;
		this.physicalLibrationsOfTheMoon = physicalLibrationsOfTheMoon;

		totalLibrationsInLatitude = opticalLibrations
				.getOpticalLibrationInLatitude()
				+ physicalLibrationsOfTheMoon.getPhysicalLibrationInLatitude();
		totalLibrationsInLongitude = opticalLibrations
				.getOpticalLibrationInLongitude()
				+ physicalLibrationsOfTheMoon.getPhysicalLibrationInLongitude();

	}

	public DateTime getDt() {
		return dt;
	}

	public OpticalLibrationsOfTheMoon getOpticalLibrations() {
		return opticalLibrations;
	}

	public PhysicalLibrationsOfTheMoon getPhysicalLibrationsOfTheMoon() {
		return physicalLibrationsOfTheMoon;
	}

	public double getTotalLibrationsInLatitude() {
		return totalLibrationsInLatitude;
	}

	public double getTotalLibrationsInLongitude() {
		return totalLibrationsInLongitude;
	}

	public static TotalLibrationsOfTheMoon calculate(DateTime dt) {
		NutationAndObliquity nutationAndObliquity = NutationAndObliquity
				.calculate(dt);
		PositionOfTheMoon positionOfTheMoon = PositionOfTheMoon.calculate(dt,
				nutationAndObliquity);
		OpticalLibrationsOfTheMoon opticalLibrations = OpticalLibrationsOfTheMoon
				.calculate(dt, nutationAndObliquity, positionOfTheMoon);
		PhysicalLibrationsOfTheMoon physicalLibrations = PhysicalLibrationsOfTheMoon
				.calculate(dt, nutationAndObliquity, positionOfTheMoon,
						opticalLibrations);
		return calculate(dt, opticalLibrations, physicalLibrations);
	}

	public static TotalLibrationsOfTheMoon calculate(DateTime dt,
			OpticalLibrationsOfTheMoon opticalLibrations,
			PhysicalLibrationsOfTheMoon physicalLibrationsOfTheMoon) {
		return new TotalLibrationsOfTheMoon(dt, opticalLibrations,
				physicalLibrationsOfTheMoon);
	}

}
