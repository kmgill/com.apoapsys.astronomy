package com.apoapsys.astronomy.math;

import org.apache.commons.math3.util.FastMath;

import com.apoapsys.astronomy.Constants;

public class MathExt
{
	public static final double PI = Math.PI;
	public static final double TWOPI = MathExt.PI * 2.0; 
	public static final double HALFPI = (MathExt.PI / 2.0);
	public static final double FORTPI = (MathExt.PI / 4.0);
	public static final double PIOVER180 = (MathExt.PI / 180.0);
	public static final double EPS10 = 1.e-10;
	
	protected final static double C00 = 1.0;
	protected final static double C02 = .25;
	protected final static double C04 = .046875;
	protected final static double C06 = .01953125;
	protected final static double C08 = .01068115234375;
	protected final static double C22 = .75;
	protected final static double C44 = .46875;
	protected final static double C46 = .01302083333333333333;
	protected final static double C48 = .00712076822916666666;
	protected final static double C66 = .36458333333333333333;
	protected final static double C68 = .00569661458333333333;
	protected final static double C88 = .3076171875;
	
	public static double sign(double v) {
		return (v >= 0) ? 1 : -1;
	}
	
	public static double clamp(double v) {
		return clamp(v, 360);
	}
	
	public static double clamp(double v, double within) {
		if (within == 0) {
			within = 360;
		}
		return v - within * Math.floor(v / within);
	};
	
	public static double div(double a, double b) {
		return ((a-a%b)/b);
	}
	
	public static double clamp(double v, double min, double max) {
		if (v < min) {
			return min;
		} else if (v > max) {
			return max;
		} else {
			return v;
		}
	}
	
	public static double log(double a)
	{
		return FastMath.log(a);
	}
	
	public static double log10(double a)
	{
		return FastMath.log10(a);
	}
	
	public static double log1p(double a)
	{
		return FastMath.log1p(a);
	}
	
	public static double abs(double a)
	{
		return FastMath.abs(a);
	}
	
	public static double tan(double a)
    {
    	return FastMath.tan(a);
    }
	
	public static double tan_d(double a)
    {
    	return FastMath.tan(MathExt.radians(a));
    }
    
	public static double tanh(double a) {
		return FastMath.tanh(a);
	}
	
	public static double tanh_d(double a) {
		return FastMath.tanh(MathExt.radians(a));
	}
	
	public static double atan(double a)
    {
    	return FastMath.atan(a);
    }
	
	public static double atan_d(double a) 
	{
		return MathExt.asin(MathExt.radians(a / MathExt.sqrt(Math.pow(a, 2) + 1)));
	}
	
    public static double sin(double a)
    {
    	return FastMath.sin(a);
    }
    
    public static double sin_d(double a)
    {
    	return MathExt.sin(MathExt.radians(a));
    }
    

    
    
    public static double cos(double a)
    {
    	return FastMath.cos(a);
    }
    
    public static double cos_d(double a)
    {
    	return MathExt.cos(MathExt.radians(a));
    }
    
    public static double sec(double a)
    {
    	return 1.0 / MathExt.cos(a);
    }
    
    public static double sec_d(double a)
    {
    	return MathExt.sec(MathExt.radians(a));
    }
    
    public static double sech(double a)
    {
    	return 1.0 / MathExt.cosh(a);
    }
    
    public static double sech_d(double a)
    {
    	return sech(MathExt.radians(a));
    }

    public static double sinh(double a) {
    	return (Math.exp(a) - Math.exp(-a)) / 2.0;
    }

    public static double cosh(double a) {
    	return (Math.pow(Math.E, a) + Math.pow(Math.E, -a)) / 2;
    }
    
    public static double radians(double a)
    {
    	return FastMath.toRadians(a);
    }
    
    public static double degrees(double a)
    {
    	return FastMath.toDegrees(a);
    }
    
    public static double atan2(double y, double x)
    {
    	return FastMath.atan2(y, x);
    }
    
    public static double atan2_d(double y, double x)
    {
    	return FastMath.atan2(y, x) * Constants._180_BY_PI;
    }
    
    
    public static double pow(double a, double b)
    {
    	return FastMath.pow(a, b);
    }
    
    /** Compute the square of a value. (a^2)
     * 
     * @param a
     * @return
     */
    public static double sqr(double a)
    {
    	return a * a;//MathExt.pow(a, 2);
    }
    
    /** Compute the cube of a value. (a^3)
     * 
     * @param a
     * @return
     */
    public static double cube(double a)
    {
    	//return MathExt.pow(a, 3);
    	return FastMath.pow(a, 3);
    }
    
    public static final long SQRT_MAGIC_D = 0x5fe6eb50c7b537a9L;
    public static final long SQRT_MAGIC_F = 0x5f3759dfL;
    
    public static double sqrt(double x)
    {
    	//return FastMath.sqrt(x);
    	
    	double orig = x;
    	double xhalf = 0.5f*x;
    	
        long bitValue = Double.doubleToRawLongBits(x);
        bitValue = SQRT_MAGIC_D - (bitValue >> 1);
        x = Double.longBitsToDouble(bitValue);
        x = x*(1.5d - xhalf*x*x);
        x = x*(1.5d - xhalf*x*x);
        // iterate for more accuratcy or not.
          // inlined for speed-speed
       //   x = x*(1.5f - xhalf*x*x);
        //x = x*(1.5f - xhalf*x*x);
        //x = x*(1.5f - xhalf*x*x);
        return x * orig;
        
    }
    
    
    
    

    public static double cbrt(double a)
    {
    	return FastMath.cbrt(a);
    }
    
    public static double asin(double a)
    {
    	return FastMath.asin(a);
    }
    
    public static double asin_d(double a)
    {
    	return FastMath.asin(MathExt.radians(a));
    }
    
    public static double acos(double a)
    {
    	return FastMath.acos(a);
    }
    
    public static double floor(double a)
    {
    	return FastMath.floor(a);
    }
    
    public static double ceil(double a)
    {
    	return FastMath.ceil(a);
    }
    
    public static double round(double a)
    {
    	return FastMath.round(a);
    }
	/*
	public static double min(double a, double b)
	{
		return Math.min(a, b);
	}
	
	public static double max(double a, double b)
	{
		return Math.max(a, b);
	}
	*/
	
	public static double min(double...values)
	{
		double m = Double.NaN;
		for (int i = 0; i < values.length; i++) {
			if (!Double.isNaN(values[i])) {
				if (Double.isNaN(m)) {
					m = values[i];
				} else {
					m = FastMath.min(m, values[i]);
				}
			}
		}
		return m;
	}
	
	public static long min(long...values)
	{
		long m = Long.MAX_VALUE;
		for (int i = 0; i < values.length; i++) {
			if (values[i] < m) {
				m = values[i];
			}
		}
		return m;
	}
	
	public static double max(double...values)
	{
		double m = Double.NaN;
		for (int i = 0; i < values.length; i++) {
			
			if (!Double.isNaN(values[i])) {
				if (Double.isNaN(m)) {
					m = values[i];
				} else {
					m = FastMath.max(m, values[i]);
				}
			}
		}
		return m;
	}
	
	public static long max(long...values)
	{
		long m = Long.MIN_VALUE;
		for (int i = 0; i < values.length; i++) {
			if (values[i] > m) {
				m = values[i];
			}
		}
		return m;
	}
	
	public static int interpolate(int i00, int i01, int i10, int i11, double xFrac, double yFrac)
	{
		double s00 = (double) i00;
		double s01 = (double) i01;
		double s10 = (double) i10;
		double s11 = (double) i11;

        return (int) FastMath.round(interpolate(s00, s01, s10, s11, xFrac, yFrac));
	}
	
	public static double interpolate(double s00, double s01, double s10, double s11, double xFrac, double yFrac)
	{
		double s0 = (s01 - s00)*xFrac + s00;
        double s1 = (s11 - s10)*xFrac + s10;
        return (s1 - s0)*yFrac + s0;
	}
	
	
	public static double cosineInterpolate(double a, double b, double x) {
		double ft = x * Math.PI;
		double f = (1.0 - MathExt.cos(ft)) * .5;

		return a * (1.0 - f) + b * f;
	}
	
	public static double cosineInterpolate(double s00, double s01, double s10, double s11, double xFrac, double yFrac)
	{
		double y0 = cosineInterpolate(s00, s01, xFrac);
		double y1 = cosineInterpolate(s10, s11, xFrac);
		return cosineInterpolate(y0, y1, yFrac);
	}
	
	public static boolean isValidNumber(double n)
	{
		return ((!Double.isNaN(n)) && (!Double.isInfinite(n)));
	}
	
	/** Normalized sinc function
	 * 
	 * @param x
	 * @return
	 */
	public static double sinc(double x)
	{
		return (MathExt.sin(MathExt.PI * x) / (MathExt.PI * x));
	}
	
	/** Unnormalized sinc function
	 * 
	 * @param x
	 * @return
	 */
	public static double sincu(double x)
	{
		return (MathExt.sin(x) / x);
	}
	
	public static double tsfn(double phi, double sinphi, double e)
	{
		sinphi *= e;
		return (MathExt.tan (.5 * (HALFPI - phi)) /
				MathExt.pow((1. - sinphi) / (1. + sinphi), .5 * e));
	}
	
	public static double msfn(double sinphi, double cosphi, double es) 
	{
		return cosphi / MathExt.sqrt(1.0 - es * sinphi * sinphi);
	}
	
	public static double[] enfn(double es) 
	{
		double t;
		double[] en = new double[5];
		en[0] = C00 - es * (C02 + es * (C04 + es * (C06 + es * C08)));
		en[1] = es * (C22 - es * (C04 + es * (C06 + es * C08)));
		en[2] = (t = es * es) * (C44 - es * (C46 + es * C48));
		en[3] = (t *= es) * (C66 - es * C68);
		en[4] = t * es * C88;
		return en;
	}
	
	
	public static double mlfn(double phi, double sphi, double cphi, double[] en) 
	{
		cphi *= sphi;
		sphi *= sphi;
		return en[0] * phi - cphi * (en[1] + sphi*(en[2] + sphi*(en[3] + sphi*en[4])));
	}
}
