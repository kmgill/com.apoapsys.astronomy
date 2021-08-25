package com.apoapsys.astronomy.geo.projection;


import com.apoapsys.astronomy.geo.exception.MapProjectionException;
import com.apoapsys.astronomy.logging.Log;
import com.apoapsys.astronomy.logging.Logging;
import com.apoapsys.astronomy.math.MathExt;

/** Implements the Winkel Tripel map projection (Winkel III), a modified azimuthal map projection.
 * 
 * @author Kevin M. Gill
 * @see http://en.wikipedia.org/wiki/Winkel_Tripel_projection
 */
public class WinkelTripelProjection extends AbstractBaseProjection
{
	@SuppressWarnings("unused")
	private static Log log = Logging.getLog(WinkelTripelProjection.class);


	
	public WinkelTripelProjection()
	{
		
	}
	
	public WinkelTripelProjection(double north, double south, double east, double west, double width, double height)
	{
		super(north, south, east, west, width, height);
	}
	
	
	@Override
	public void project(double latitude, double longitude, double elevation, MapPoint point) throws MapProjectionException
	{
		/*
		 * Borrowed from http://jmapprojlib.svn.sourceforge.net/viewvc/jmapprojlib/trunk/src/main/java/com/jhlabs/map/proj/WinkelTripelProjection.java?revision=29&view=markup
		 */
		double lpphi = latitude;
		double lplam = longitude;
		
		double c = 0.5 * lplam;
		double d = MathExt.acos(MathExt.cos(lpphi) * MathExt.cos(c));

		if (d != 0) {
			point.column = 2. * d * MathExt.cos(lpphi) * MathExt.sin(c) * (point.row = 1. / MathExt.sin(d));
			point.row *= d * MathExt.sin(lpphi);
		} else {
			point.column = point.row = 0.0;
		}
		point.column = (point.column + lplam * 0.636619772367581343) * 0.5;
		point.row = (point.row + lpphi) * 0.5;
		/*
		double lpphi = latitude;
		double lplam = longitude;

		double c = 0.5 * lplam;
		double cosO1 = 0.99993827224000145098735895662767;
		double d = Math.acos(Math.cos(lpphi) * Math.cos(c));
		
		double sinca = (d == 0) ? 0 : (Math.sin(d) / d);
		
		double x = 0;
		double y = 0;
		
		if (d != 0) {
			x = (2.0 * Math.cos(lpphi) * Math.sin(c)) / sinca;
			y = Math.sin(lpphi) / sinca;
		}
		
		x = (x + lplam * cosO1) / 2.0;
		y = (y + lpphi) / 2.0;
		

		point.column = x;
		point.row = y;
		*/
	}
	
	/*
	@Override
	public void getPoint(double latitude, double longitude, double elevation, MapPoint point) throws MapProjectionException
	{


		double lpphi = Math.toRadians(latitude);
		double lplam = Math.toRadians(longitude);

		double c = 0.5 * lplam;
		double cosO1 = 0.99993827224000145098735895662767;
		double d = Math.acos(Math.cos(lpphi) * Math.cos(c));
		
		double sinca = (d == 0) ? 0 : (Math.sin(d) / d);
		
		double x = 0;
		double y = 0;
		
		if (d != 0) {
			x = (2.0 * Math.cos(lpphi) * Math.sin(c)) / sinca;
			y = Math.sin(lpphi) / sinca;
		}
		
		x = (x + lplam * cosO1) / 2.0;
		y = (y + lpphi) / 2.0;
		

		point.column = Math.toDegrees(x);
		point.row = Math.toDegrees(y);

	}
	*/
	
	
}
