package com.apoapsys.astronomy.utilities.iphasdr2;

import com.apoapsys.astronomy.coords.EquatorialRightAscension;
import com.apoapsys.astronomy.math.Angle;

public class IphasDr2RowLight implements IphasDr2Row {
	private String name;
	private double rightAscension;
	private double declination;
	private float rMagnitude;
	private float rMagnitudeSigma;
	private float iMagnitude;
	private float iMagnitudeSigma;
	private float haMagnitude;
	private float haMagnitudeSigma;
	private int mergedClass;
	private int errBits;
	private EquatorialRightAscension equatorialCoordinates;
	
	public IphasDr2RowLight(Object[] row) {
		
		this.name = (String) row[0];
		this.rightAscension = ((double[]) row[1])[0];
		this.declination = ((double[]) row[2])[0];
		
		this.rMagnitude = ((float[]) row[3])[0];
		this.rMagnitudeSigma = ((float[]) row[4])[0];
		
		this.iMagnitude = ((float[]) row[5])[0];
		this.iMagnitudeSigma = ((float[]) row[6])[0];
		
		this.haMagnitude = ((float[]) row[7])[0];
		this.haMagnitudeSigma = ((float[]) row[8])[0];
		
		this.mergedClass = ((short[]) row[9])[0];
		this.errBits = ((int[]) row[10])[0];
		
		this.equatorialCoordinates = new EquatorialRightAscension(Angle.fromDegrees(this.rightAscension), Angle.fromDegrees(this.declination));
	}

	public String getName() {
		return name;
	}

	public double getRightAscension() {
		return rightAscension;
	}

	public double getDeclination() {
		return declination;
	}

	public float getrMagnitude() {
		return rMagnitude;
	}

	public float getrMagnitudeSigma() {
		return rMagnitudeSigma;
	}

	public float getiMagnitude() {
		return iMagnitude;
	}

	public float getiMagnitudeSigma() {
		return iMagnitudeSigma;
	}

	public float getHaMagnitude() {
		return haMagnitude;
	}

	public float getHaMagnitudeSigma() {
		return haMagnitudeSigma;
	}

	public int getMergedClass() {
		return mergedClass;
	}

	public int getErrBits() {
		return errBits;
	}
	
	public float getRmi() {
		return 0.0f;
	}

	public EquatorialRightAscension getEquatorialCoordinates() {
		return equatorialCoordinates;
	}
	
	
	public boolean isA10() {
		
		return (this.rMagnitudeSigma < 0.1 &&
				this.iMagnitudeSigma < 0.1 &&
				this.haMagnitudeSigma < 0.1);
				// not saturated &&
	}
	
	public boolean isSimplifiedA10() {
		return isA10();
	}
	
}
