package com.apoapsys.astronomy.perlin;


public class TurbulentPerlin extends Perlin {
	
	private final PerlinNoiseModifier modifier = new TurbulentPerlinModifier();
	
	public TurbulentPerlin(double persistence, double numberOfOctaves, double resolution) {
		super(persistence, numberOfOctaves, resolution);
	}
	
	@Override
	public double noise(double x, double y) {
		return super.noise(x, y, modifier);
	}
	
	class TurbulentPerlinModifier implements PerlinNoiseModifier {

		@Override
		public double onNoise(double x, double y, double noise,
				double frequency, double amplitude, double octaveNum) {
			noise = (1.0 / (octaveNum + 1)) * noise;
			return noise;
		}

		@Override
		public double onTotal(double total) {
			return (total * 2.0) - 1.0;
		}
		
	}
}
