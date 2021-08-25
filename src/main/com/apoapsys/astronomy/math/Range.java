package com.apoapsys.astronomy.math;

public class Range {
	
	private final double minimum;
	private final double maximum;
	private final UnitsEnum units;
	private final boolean inclusive;
	
	public Range(double minimum, double maximum) {
		this(minimum, maximum, true);
	}
	
	public Range(double minimum, double maximum, boolean inclusive) {
		this(minimum, maximum, UnitsEnum.UNDEFINED, inclusive);
	}
	
	public Range(double minimum, double maximum, UnitsEnum units) {
		this(minimum, maximum, units, true);
	}
	
	public Range(double minimum, double maximum, UnitsEnum units, boolean inclusive) {
		this.minimum = minimum;
		this.maximum = maximum;
		this.units = units;
		this.inclusive = inclusive;
	}

	public double getMinimum() {
		return minimum;
	}

	public double getMaximum() {
		return maximum;
	}

	public UnitsEnum getUnits() {
		return units;
	}
	
	public boolean isInclusive() {
		return inclusive;
	}
}
