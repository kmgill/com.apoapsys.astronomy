package com.apoapsys.astronomy.tle;

@SuppressWarnings("serial")
public class TLEParseException extends Exception {

	private String tle;
	
	public TLEParseException(String message, String tle) {
		super(message);
		this.tle = tle;
	}
	
	public TLEParseException(String message) {
		super(message);
	}
	
	public TLEParseException(String message, Throwable thrown, String tle) {
		super(message, thrown);
		this.tle = tle;
	}
	
	public TLEParseException(String message, Throwable thrown) {
		super(message, thrown);
	}
	
	public String getTLE() {
		return tle;
	}
}
