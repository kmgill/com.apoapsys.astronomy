package com.apoapsys.astronomy.perlin;

import java.util.List;

import com.google.common.collect.Lists;

public class PerlinCompositor implements NoiseGenerator {
	
	private List<PerlinEntry> entries = Lists.newArrayList();
	private List<PerlinNoiseModifier> modifiers = Lists.newArrayList();
	
	private double totalWeight = 0;
	
	public PerlinCompositor() {
		
	}

	public void addPerlin(NoiseGenerator perlin, double weight, PerlinNoiseModifier ... modifiers) {
		List<PerlinNoiseModifier> modifierList = null;
		if (modifiers != null) {
			modifierList = Lists.newArrayList(modifiers);
		}
		
		entries.add(new PerlinEntry(perlin, weight, modifierList));
		recalculateTotalWeight();
	}
	
	public void addModifier(PerlinNoiseModifier modifier) {
		this.modifiers.add(modifier);
	}
	
	public boolean removeModifier(PerlinNoiseModifier modifier) {
		return this.modifiers.remove(modifier);
	}
	
	public double noise(double x, double y) {
		double totalNoise = 0.0;
		
		for (PerlinEntry entry : entries) {
			
			double noise = entry.perlin.noise(x, y);
			
			if (entry.modifiers != null) {
				for (PerlinNoiseModifier modifier : entry.modifiers) {
					noise = modifier.onTotal(noise);
				}
			}
			
			double relativeWeight = entry.weight / totalWeight;
			totalNoise += (noise * relativeWeight);
			
		}
		
		for (PerlinNoiseModifier modifier : modifiers) {
			totalNoise = modifier.onTotal(totalNoise);
		}
		
		return totalNoise;
	}
	
	protected void recalculateTotalWeight() {
		double total = 0;
		for (PerlinEntry entry : entries) {
			total += entry.weight;
		}
		totalWeight = total;
	}
	
	
	class PerlinEntry {
		public NoiseGenerator perlin;
		public double weight;
		public List<PerlinNoiseModifier> modifiers;
		
		public PerlinEntry(NoiseGenerator perlin, double weight, List<PerlinNoiseModifier> modifiers) {
			this.perlin = perlin;
			this.weight = weight;
			this.modifiers = modifiers;
		}
		
		public boolean equals(Object o) {
			if (o == null || !(o instanceof PerlinEntry)) {
				return false;
			}
			PerlinEntry entry = (PerlinEntry) o;
			return this.perlin == entry.perlin && this.weight == entry.weight;
		}
	}
	
}
