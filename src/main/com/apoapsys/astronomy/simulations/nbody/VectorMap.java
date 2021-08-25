package com.apoapsys.astronomy.simulations.nbody;

import java.util.Map;

import com.google.common.collect.Maps;

public class VectorMap {
	
	private Map<String, VectorState> map = Maps.newHashMap();
	
	public VectorMap() {
		
	}
	
	public void put(VectorState vectorState) {
		map.put(vectorState.getBodyId(), vectorState);
	}
	
	public VectorState get(String bodyId) {
		return map.get(bodyId);
	}
	
}
