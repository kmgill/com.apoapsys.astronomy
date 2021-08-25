package com.apoapsys.astronomy.perlin;

public class StandardPerlin extends Perlin {
	
	private final PerlinNoiseModifier modifier = new StandardPerlinModifier();
	
	public StandardPerlin(double persistence, double numberOfOctaves, double resolution) {
		super(persistence, numberOfOctaves, resolution);
	}
	
	@Override
	public double noise(double x, double y) {
		return super.noise(x, y, modifier);
	}
	
	class StandardPerlinModifier implements PerlinNoiseModifier {

		@Override
		public double onNoise(double x, double y, double noise,
				double frequency, double amplitude, double octaveNum) {
			return noise;
		}

		@Override
		public double onTotal(double total) {
			return total;
		}
		
	}
}
