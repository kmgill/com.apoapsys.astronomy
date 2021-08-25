package com.apoapsys.astronomy.simulations.nbody;

public class TimeBinningSuperMap<T> extends TimeBinningMap<TimeBinningMap<T>> {
	
	private TimeBinningMap<T> subMapPrototype;
	
	public TimeBinningSuperMap(TimeBinner binner, int initialCapacity, TimeBinningMap<T> subMapPrototype) {
		super(binner, new SubMapCreator<T>(subMapPrototype), initialCapacity);
		
		this.subMapPrototype = subMapPrototype;

	}

	@Override
	public TimeBinningMap<T> get(double time) {
		int bin = getBinner().getTimeBin(time);
		
		if (getList().size() <= bin) {
			createUpToCapacity(bin+1);
		}
		
		TimeBinningMap<T> subMap = getList().get(bin);
		return subMap;
	}
	

	@Override
	public TimeBinningMap<TimeBinningMap<T>> cloneEmpty() {
		TimeBinningMap<TimeBinningMap<T>> clone = new TimeBinningSuperMap<T>(getBinner(), getInitialCapacity(), subMapPrototype);
		return clone;
	}
	
	
	static class SubMapCreator<T> implements CreateNewCallback<TimeBinningMap<T>> {
		
		private TimeBinningMap<T> subMapPrototype;
		
		public SubMapCreator(TimeBinningMap<T> subMapPrototype) {
			this.subMapPrototype = subMapPrototype;
		}
		
		@Override
		public TimeBinningMap<T> create() {
			return subMapPrototype.cloneEmpty();
		}
		
	}
}
