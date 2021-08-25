package com.apoapsys.astronomy.math;

public class Angle {
	
	private double angleDegrees = 0;
	
	public Angle(double degrees, double minutes, double seconds) {
		double A = Math.abs(seconds) / 60;
		double B = (Math.abs(minutes) + A) / 60;
		double C = Math.abs(degrees) + B;
		angleDegrees = C;
	}
	
	public Angle(double degrees) {
		angleDegrees = degrees;
	}
	
	public Angle clampToDegrees(double d) {
		return new Angle(MathExt.clamp(angleDegrees, d));
	}
	
	public Angle clampToRadians(double r) {
		return Angle.fromRadians(MathExt.clamp(MathExt.radians(angleDegrees), r));
	}
	
	public double getDegrees() {
		return angleDegrees;
	}
	
	public double getRadians() {
		return angleDegrees * (Math.PI / 180);
	}
	
	public double getDMSDegrees() {
		return Math.floor(Math.abs(angleDegrees));
	}
	
	public double getDMSMinutes() {
		double m = Math.floor((Math.abs(angleDegrees) - getDMSDegrees()) * 60);
		return m;
	}
	
	public double getDMSSeconds() {
		double s = Math.abs(angleDegrees) * 3600;
		s = s % 60;
		return s;
	}
	
	public static Angle fromRadians(double a) {
		return new Angle(MathExt.degrees(a));
	}
	
	public static Angle fromDegrees(double d) {
		return new Angle(d);
	}
	
	
	public String formatDMS() {
		String dms = (int) this.getDMSDegrees() + "\u00B0 " + (int) this.getDMSMinutes() + "' " + (MathExt.round(this.getDMSSeconds() * 1000.0) / 1000.0)+ "\"";
		return dms;
	}
	
	public String formatHMS() {
		double h = MathExt.floor(MathExt.abs(angleDegrees));
		double m = MathExt.floor((MathExt.abs(angleDegrees) - h) * 60);
		double s = ((MathExt.abs(angleDegrees) - h) * 60 - m);
		
		String hms = String.format("%2.0fh %2.0fm %2.3fs", h, m, s);
		return hms;
	}
	
	public String formatDegrees(String ifPos, String ifNeg) {
		String fmt = String.format("%3.3f\u00B0%s", MathExt.abs(angleDegrees), (angleDegrees >= 0 ? ifPos : ifNeg));
		return fmt;
	}
	
}
