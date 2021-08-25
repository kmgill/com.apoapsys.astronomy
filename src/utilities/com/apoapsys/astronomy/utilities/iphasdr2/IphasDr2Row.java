package com.apoapsys.astronomy.utilities.iphasdr2;

import com.apoapsys.astronomy.coords.EquatorialRightAscension;

public interface IphasDr2Row {
	public String getName();
	public double getRightAscension();
	public double getDeclination();
	public float getrMagnitude();
	public float getrMagnitudeSigma();
	public float getiMagnitude();
	public float getiMagnitudeSigma();
	public float getHaMagnitude();
	public float getHaMagnitudeSigma();
	public int getMergedClass();
	public int getErrBits();
	public float getRmi();
	public EquatorialRightAscension getEquatorialCoordinates();
	public boolean isSimplifiedA10();
	public boolean isA10();
}
