package com.apoapsys.astronomy.utilities.iphasdr2;

import com.apoapsys.astronomy.coords.EquatorialRightAscension;
import com.apoapsys.astronomy.coords.Galactic;
import com.apoapsys.astronomy.math.Angle;

public class IphasDr2RowFull implements IphasDr2Row {
	private String name;
	private double rightAscension;
	private double declination;
	private double galacticLongitude;
	private double galacticLatitude;
	private String sourceId;
	
	private float rMagnitude;
	private float rMagnitudeSigma;
	private float rPeakMag;
	private float rPeakMagErr;
	private float rAperMag1;
	private float rAperMag1Err;
	private float rAperMag3;
	private float rAperMag3Err;
	private float rGauSig;
	private float rEll;
	private float rPA;
	private short fClass;
	private float rClassStat;
	private boolean rDeblend;
	private boolean rSaturated;
	private double rMJD;
	private float rSeeing;
	private String rDetectionID;
	private float rX;
	private float rY;
	
	private float iMagnitude;
	private float iMagnitudeSigma;
	private float iPeakMag;
	private float iPeakMagErr;
	private float iAperMag1;
	private float iAperMag1Err;
	private float iAperMag3;
	private float iAperMag3Err;
	private float iGauSig;
	private float iEll;
	private float iPA;
	private short iClass;
	private float iClassStat;
	private boolean iDeblend;
	private boolean iSaturated;
	private double iMJD;
	private float iSeeing;
	private String iDetectionID;
	private float iX;
	private float iY;
	private float iXi;
	private float iEta;
	
	private float haMagnitude;
	private float haMagnitudeSigma;
	private float haPeakMag;
	private float haPeakMagErr;
	private float haAperMag1;
	private float haAperMag1Err;
	private float haAperMag3;
	private float haAperMag3Err;
	private float haGauSig;
	private float haEll;
	private float haPA;
	private short haClass;
	private float haClassStat;
	private boolean haDeblend;
	private boolean haSaturated;
	private double haMJD;
	private float haSeeing;
	private String haDetectionID;
	private float haX;
	private float haY;
	private float haXi;
	private float haEta;
	
	private boolean brightNeighborhood;
	private boolean deblend;
	private boolean saturated;
	private short nBands;
	private boolean a10;
	private boolean a10Point;
	
	
	private String fieldId;
	private String fieldGrade;
	private int night;
	private float seeing;
	private short ccd;
	private short nObs;
	private String sourceId2;
	private String fieldId2;
	private float r2;
	private float rErr2;
	private float i2;
	private float iErr2;
	private float ha2;
	private float haErr2;
	private int errBits;
	
	private float starProbability;
	private float galaxyProbability;
	private float noiseProbability;
	private int mergedClass;
	
	private float rmi;
	private float rmiHa;
	
	private Galactic galacticCoordinates;
	private EquatorialRightAscension equatorialCoordinates;
	
	public IphasDr2RowFull(Object[] row) {
		
		this.name = (String) row[0];
		this.rightAscension = ((double[]) row[1])[0];
		this.declination = ((double[]) row[2])[0];
		this.sourceId = (String) row[3];
		this.galacticLongitude = ((double[])row[5])[0];
		this.galacticLatitude = ((double[])row[6])[0];
		this.mergedClass = ((short[]) row[7])[0];
		
		this.starProbability = ((float[])row[9])[0];
		this.galaxyProbability = ((float[])row[10])[0];
		this.noiseProbability = ((float[])row[11])[0];
		
		this.rmi = ((float[])row[12])[0];
		this.rmiHa = ((float[])row[13])[0];
		
		int c = 14;
		this.rMagnitude = ((float[]) row[c++])[0];
		this.rMagnitudeSigma = ((float[]) row[c++])[0];
		this.rPeakMag = ((float[]) row[c++])[0];
		this.rPeakMagErr = ((float[]) row[c++])[0];
		this.rAperMag1 = ((float[]) row[c++])[0];
		this.rAperMag1Err = ((float[]) row[c++])[0];
		this.rAperMag3 = ((float[]) row[c++])[0];
		this.rAperMag3Err = ((float[]) row[c++])[0];
		this.rGauSig = ((float[]) row[c++])[0];
		this.rEll = ((float[]) row[c++])[0];
		this.rPA = ((float[]) row[c++])[0];
		this.fClass = ((short[]) row[c++])[0];
		this.rClassStat = ((float[]) row[c++])[0];
		this.rDeblend = ((boolean[]) row[c++])[0];
		this.rSaturated = ((boolean[]) row[c++])[0];
		this.rMJD = ((double[]) row[c++])[0];
		this.rSeeing = ((float[]) row[c++])[0];
		this.rDetectionID = (String) row[c++];
		this.rX = ((float[]) row[c++])[0];
		this.rY = ((float[]) row[c++])[0];

		this.iMagnitude = ((float[]) row[c++])[0];
		this.iMagnitudeSigma = ((float[]) row[c++])[0];
		this.iPeakMag = ((float[]) row[c++])[0];
		this.iPeakMagErr = ((float[]) row[c++])[0];
		this.iAperMag1 = ((float[]) row[c++])[0];
		this.iAperMag1Err = ((float[]) row[c++])[0];
		this.iAperMag3 = ((float[]) row[c++])[0];
		this.iAperMag3Err = ((float[]) row[c++])[0];
		this.iGauSig = ((float[]) row[c++])[0];
		this.iEll = ((float[]) row[c++])[0];
		this.iPA = ((float[]) row[c++])[0];
		this.iClass = ((short[]) row[c++])[0];
		this.iClassStat = ((float[]) row[c++])[0];
		this.iDeblend = ((boolean[]) row[c++])[0];
		this.iSaturated = ((boolean[]) row[c++])[0];
		this.iMJD = ((double[]) row[c++])[0];
		this.iSeeing = ((float[]) row[c++])[0];
		this.iDetectionID = (String) row[c++];
		this.iX = ((float[]) row[c++])[0];
		this.iY = ((float[]) row[c++])[0];
		this.iXi = ((float[]) row[c++])[0];
		this.iEta = ((float[]) row[c++])[0];
		
		
		this.haMagnitude = ((float[]) row[c++])[0];
		this.iMagnitudeSigma = ((float[]) row[c++])[0];
		this.haPeakMag = ((float[]) row[c++])[0];
		this.haPeakMagErr = ((float[]) row[c++])[0];
		this.haAperMag1 = ((float[]) row[c++])[0];
		this.haAperMag1Err = ((float[]) row[c++])[0];
		this.haAperMag3 = ((float[]) row[c++])[0];
		this.haAperMag3Err = ((float[]) row[c++])[0];
		this.haGauSig = ((float[]) row[c++])[0];
		this.haEll = ((float[]) row[c++])[0];
		this.haPA = ((float[]) row[c++])[0];
		this.haClass = ((short[]) row[c++])[0];
		this.haClassStat = ((float[]) row[c++])[0];
		this.haDeblend = ((boolean[]) row[c++])[0];
		this.haSaturated = ((boolean[]) row[c++])[0];
		this.haMJD = ((double[]) row[c++])[0];
		this.haSeeing = ((float[]) row[c++])[0];
		this.haDetectionID = (String) row[c++];
		this.haX = ((float[]) row[c++])[0];
		this.haY = ((float[]) row[c++])[0];
		this.haXi = ((float[]) row[c++])[0];
		this.haEta = ((float[]) row[c++])[0];
		
		this.brightNeighborhood = ((boolean[]) row[c++])[0];
		this.deblend = ((boolean[]) row[c++])[0];
		this.saturated = ((boolean[]) row[c++])[0];
		this.nBands = ((short[]) row[c++])[0];
		this.a10 = ((boolean[]) row[c++])[0];
		this.a10Point = ((boolean[]) row[c++])[0];
		
		
		this.fieldId = (String) row[c++];
		this.fieldGrade = (String) row[c++];
		this.night = ((int[]) row[c++])[0];
		this.seeing = ((float[]) row[c++])[0];
		this.ccd = ((short[]) row[c++])[0];
		this.nObs = ((short[]) row[c++])[0];
		this.sourceId2 = (String) row[c++];
		this.fieldId2 = (String) row[c++];
		this.r2 = ((float[]) row[c++])[0];
		this.rErr2 = ((float[]) row[c++])[0];
		this.i2 = ((float[]) row[c++])[0];
		this.iErr2 = ((float[]) row[c++])[0];
		this.ha2 = ((float[]) row[c++])[0];
		this.haErr2 = ((float[]) row[c++])[0];
		this.errBits = ((int[]) row[c++])[0];
		
		
		this.galacticCoordinates = new Galactic(Angle.fromDegrees(galacticLatitude), Angle.fromDegrees(this.galacticLongitude));
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


	public double getGalacticLongitude() {
		return galacticLongitude;
	}


	public double getGalacticLatitude() {
		return galacticLatitude;
	}


	public String getSourceId() {
		return sourceId;
	}


	public float getrMagnitude() {
		return rMagnitude;
	}


	public float getrMagnitudeSigma() {
		return rMagnitudeSigma;
	}


	public float getrPeakMag() {
		return rPeakMag;
	}


	public float getrPeakMagErr() {
		return rPeakMagErr;
	}


	public float getrAperMag1() {
		return rAperMag1;
	}


	public float getrAperMag1Err() {
		return rAperMag1Err;
	}


	public float getrAperMag3() {
		return rAperMag3;
	}


	public float getrAperMag3Err() {
		return rAperMag3Err;
	}


	public float getrGauSig() {
		return rGauSig;
	}


	public float getrEll() {
		return rEll;
	}


	public float getrPA() {
		return rPA;
	}


	public short getfClass() {
		return fClass;
	}


	public float getrClassStat() {
		return rClassStat;
	}


	public boolean isrDeblend() {
		return rDeblend;
	}


	public boolean isrSaturated() {
		return rSaturated;
	}


	public double getrMJD() {
		return rMJD;
	}


	public float getrSeeing() {
		return rSeeing;
	}


	public String getrDetectionID() {
		return rDetectionID;
	}


	public float getrX() {
		return rX;
	}


	public float getrY() {
		return rY;
	}


	public float getiMagnitude() {
		return iMagnitude;
	}


	public float getiMagnitudeSigma() {
		return iMagnitudeSigma;
	}


	public float getiPeakMag() {
		return iPeakMag;
	}


	public float getiPeakMagErr() {
		return iPeakMagErr;
	}


	public float getiAperMag1() {
		return iAperMag1;
	}


	public float getiAperMag1Err() {
		return iAperMag1Err;
	}


	public float getiAperMag3() {
		return iAperMag3;
	}


	public float getiAperMag3Err() {
		return iAperMag3Err;
	}


	public float getiGauSig() {
		return iGauSig;
	}


	public float getiEll() {
		return iEll;
	}


	public float getiPA() {
		return iPA;
	}


	public short getiClass() {
		return iClass;
	}


	public float getiClassStat() {
		return iClassStat;
	}


	public boolean isiDeblend() {
		return iDeblend;
	}


	public boolean isiSaturated() {
		return iSaturated;
	}


	public double getiMJD() {
		return iMJD;
	}


	public float getiSeeing() {
		return iSeeing;
	}


	public String getiDetectionID() {
		return iDetectionID;
	}


	public float getiX() {
		return iX;
	}


	public float getiY() {
		return iY;
	}


	public float getiXi() {
		return iXi;
	}


	public float getiEta() {
		return iEta;
	}


	public float getHaMagnitude() {
		return haMagnitude;
	}


	public float getHaMagnitudeSigma() {
		return haMagnitudeSigma;
	}


	public float getHaPeakMag() {
		return haPeakMag;
	}


	public float getHaPeakMagErr() {
		return haPeakMagErr;
	}


	public float getHaAperMag1() {
		return haAperMag1;
	}


	public float getHaAperMag1Err() {
		return haAperMag1Err;
	}


	public float getHaAperMag3() {
		return haAperMag3;
	}


	public float getHaAperMag3Err() {
		return haAperMag3Err;
	}


	public float getHaGauSig() {
		return haGauSig;
	}


	public float getHaEll() {
		return haEll;
	}


	public float getHaPA() {
		return haPA;
	}


	public short getHaClass() {
		return haClass;
	}


	public float getHaClassStat() {
		return haClassStat;
	}


	public boolean isHaDeblend() {
		return haDeblend;
	}


	public boolean isHaSaturated() {
		return haSaturated;
	}


	public double getHaMJD() {
		return haMJD;
	}


	public float getHaSeeing() {
		return haSeeing;
	}


	public String getHaDetectionID() {
		return haDetectionID;
	}


	public float getHaX() {
		return haX;
	}


	public float getHaY() {
		return haY;
	}


	public float getHaXi() {
		return haXi;
	}


	public float getHaEta() {
		return haEta;
	}


	public boolean isBrightNeighborhood() {
		return brightNeighborhood;
	}


	public boolean isDeblend() {
		return deblend;
	}


	public boolean isSaturated() {
		return saturated;
	}


	public short getnBands() {
		return nBands;
	}
	

	public boolean isA10() {
		return a10;
	}


	public boolean isA10Point() {
		return a10Point;
	}


	public String getFieldId() {
		return fieldId;
	}


	public String getFieldGrade() {
		return fieldGrade;
	}


	public int getNight() {
		return night;
	}


	public float getSeeing() {
		return seeing;
	}


	public short getCcd() {
		return ccd;
	}


	public short getnObs() {
		return nObs;
	}


	public String getSourceId2() {
		return sourceId2;
	}


	public String getFieldId2() {
		return fieldId2;
	}


	public float getR2() {
		return r2;
	}


	public float getrErr2() {
		return rErr2;
	}


	public float getI2() {
		return i2;
	}


	public float getiErr2() {
		return iErr2;
	}


	public float getHa2() {
		return ha2;
	}


	public float getHaErr2() {
		return haErr2;
	}


	public int getErrBits() {
		return errBits;
	}


	public float getStarProbability() {
		return starProbability;
	}


	public float getGalaxyProbability() {
		return galaxyProbability;
	}


	public float getNoiseProbability() {
		return noiseProbability;
	}


	public int getMergedClass() {
		return mergedClass;
	}


	public float getRmi() {
		return rmi;
	}


	public float getRmiHa() {
		return rmiHa;
	}


	public Galactic getGalacticCoordinates() {
		return galacticCoordinates;
	}


	public EquatorialRightAscension getEquatorialCoordinates() {
		return equatorialCoordinates;
	}


	public boolean isSimplifiedA10() {
		
		return (this.rMagnitudeSigma < 0.1 &&
				this.iMagnitudeSigma < 0.1 &&
				this.haMagnitudeSigma < 0.1);
				// not saturated &&
				
		
		
	}
	
}
