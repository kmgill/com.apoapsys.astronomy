package com.apoapsys.astronomy.perlin;

import java.util.Random;

import com.apoapsys.astronomy.math.MathExt;

/**
 * Based on Perlin Noise pseudocode from
 * http://freespace.virgin.net/hugo.elias/models/m_perlin.htm
 * 
 * @author Kevin M. Gill
 * 
 */
class PerlinNoiseUtil {

	private static Random random = new Random(System.currentTimeMillis());


	
	public static double noise(double x, double y) {
		int n = (int) (x + y * 57.0);
		n = (n << 13) ^ n;
		//return (random.nextDouble() * 2.0) - 1.0;
		return (1.0 - ((n * (n * n * 15731 + 789221) + 1376312589) & 0x7fffffff) / 1073741824.0);
	}

	protected static double smoothNoise(double x, double y) {
		double corners = (noise(x - 1, y - 1) + noise(x + 1, y - 1)
				+ noise(x - 1, y + 1) + noise(x + 1, y + 1)) / 16.0;
		double sides = (noise(x - 1, y) + noise(x + 1, y) + noise(x, y - 1) + noise(
				x, y + 1)) / 8.0;
		double center = noise(x, y) / 4.0;
		return corners + sides + center;
	}

	protected static double cosineInterpolate(double a, double b, double x) {
		double ft = x * Math.PI;
		double f = (1 - MathExt.cos(ft)) * .5;

		return a * (1 - f) + b * f;
	}

	protected static double interpolatedNoise(double x, double y) {

		int integer_X = (int) x;
		double fractional_X = x - integer_X;

		int integer_Y = (int) y;
		double fractional_Y = y - integer_Y;

		double v1 = smoothNoise(integer_X, integer_Y);
		double v2 = smoothNoise(integer_X + 1, integer_Y);
		double v3 = smoothNoise(integer_X, integer_Y + 1);
		double v4 = smoothNoise(integer_X + 1, integer_Y + 1);
		return MathExt.cosineInterpolate(v1, v2, v3, v4, fractional_X,
				fractional_Y);
	}

	//
	public static double perlinNoise2D(double x, double y, double persistence,
			double numberOfOctaves, PerlinNoiseModifier modifier) {
		
		if (modifier == null) {
			throw new IllegalArgumentException("Modifier interface must not be null");
		}
		
		double total = 0;
		double p = persistence;
		double n = numberOfOctaves - 1;

		for (int i = 0; i < n; i++) {
			double frequency = MathExt.pow(2, i);
			double amplitude = MathExt.pow(p, i);

			double noise = interpolatedNoise(x * frequency, y * frequency);
			
			noise = modifier.onNoise(x, y, noise, frequency, amplitude, i);
			
			total = total + noise * amplitude;

		}
		return modifier.onTotal(total);
		
	}
	
	public static double perlinNoise2D(double x, double y, double persistence,
			double numberOfOctaves) {

		double total = 0;
		double p = persistence;
		double n = numberOfOctaves - 1;

		for (int i = 0; i < n; i++) {
			double frequency = MathExt.pow(2, i);
			double amplitude = MathExt.pow(p, i);

			double noise = interpolatedNoise(x * frequency, y * frequency);
			
			total = total + noise * amplitude;

		}
		return total;
	}
	

	

	
	public static double perlinNoise2DRidged(double x, double y, double persistence,
			double numberOfOctaves) {

		double total = 0;
		double p = persistence;
		double n = numberOfOctaves - 1;

		for (int i = 0; i < n; i++) {
			double frequency = MathExt.pow(2, i);
			double amplitude = MathExt.pow(p, i);

			double noise = 1.0 - MathExt.abs(interpolatedNoise(x * frequency, y * frequency));
			
			total = total + noise * amplitude;

		}
		return total;
	}
	
	
	
	public static double undulate(double x) {
		if (x < -0.4) {
			return 0.15 + 2.857 * (x + 0.75) * (x + 0.75);
		} else if (x < 0.4) {
			return 0.95 - 2.8125 * (x * x);
		} else {
			return 0.26 + 2.666 * (x - 0.7) * (x - 0.7);
		}
	}

	public static double turbulence(double x, double y, double size) {
		double value = 0.0, initialSize = size;

		while (size >= 1) {
			value += smoothNoise(x / size, y / size) * size;
			size /= 2.0;
		}

		return (128.0 * value / initialSize);
	}

	protected static double turbulenceX(double posX, double posY, double pixel_size) {
		double x = 0;
		double scale = 1;
		while (scale > pixel_size) {
			posX = posX / scale;
			posY = posY / scale;
			x = x + interpolatedNoise(posX, posY) * scale;
			scale = scale / 2.0;
		}
		return x;
	}
}
