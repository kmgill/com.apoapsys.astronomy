package com.apoapsys.astronomy.simulations.nbody;

import java.util.List;

import com.google.common.collect.Lists;

public class TimeBinningMap<T> {
	
	private List<T> binnedList;
	private TimeBinner binner;
	private int initialCapacity;
	private CreateNewCallback<T> creator;
	
	public TimeBinningMap(TimeBinner binner, CreateNewCallback<T> creator, int initialCapacity) {
		this.binner = binner;
		this.initialCapacity = initialCapacity;
		this.binnedList = Lists.newArrayListWithCapacity(initialCapacity);
		this.creator = creator;
		
		createUpToCapacity(initialCapacity);
	}
	
	protected void createUpToCapacity(int initialCapacity) {
		for (int i = binnedList.size(); i < initialCapacity; i++) {
			binnedList.add(i, creator.create());
		}
	}
	
	
	public T get(double time) {
		return binnedList.get(binner.getTimeBin(time));
	}
	
	public void set(double time, T item) {
		binnedList.set(binner.getTimeBin(time), item);
	}
	
	protected List<T> getList() {
		return binnedList;
	}
	
	protected int getInitialCapacity() {
		return initialCapacity;
	}
	
	protected TimeBinner getBinner() {
		return binner;
	}
	
	protected CreateNewCallback<T> getCreator() {
		return creator;
	}
	
	public TimeBinningMap<T> cloneEmpty() {
		TimeBinningMap<T> clone = new TimeBinningMap<T>(this.binner, this.creator, this.initialCapacity);
		return clone;
	}
}
