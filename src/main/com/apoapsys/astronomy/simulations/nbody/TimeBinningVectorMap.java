package com.apoapsys.astronomy.simulations.nbody;

import com.apoapsys.astronomy.math.MathExt;

public class TimeBinningVectorMap implements VectorTimeMap {
	
	private TimeBinningMap<VectorMap> gMap;
	private TimeBinningSuperMap<VectorMap> fMap;
	private TimeBinningSuperMap<TimeBinningMap<VectorMap>> eMap;
	private TimeBinningSuperMap<TimeBinningMap<TimeBinningMap<VectorMap>>> dMap;
	
	public TimeBinningVectorMap() {
		
		
		
		
		gMap = new TimeBinningMap<VectorMap>(new BaseTimeBinner() {
			@Override
			public int getTimeBin(double time) {
				return getG((int) MathExt.floor(time));
			}
		},
		new CreateNewCallback<VectorMap>() {
			@Override
			public VectorMap create() {
				return new VectorMap();
			}
		},
		100);
		
		
		fMap = new TimeBinningSuperMap<VectorMap>(new BaseTimeBinner() {
			@Override
			public int getTimeBin(double time) {
				return getF((int) MathExt.floor(time));
			}
		}, 100, gMap);
		
		eMap = new TimeBinningSuperMap<TimeBinningMap<VectorMap>>(new BaseTimeBinner() {
			@Override
			public int getTimeBin(double time) {
				return getE((int) MathExt.floor(time));
			}
		}, 10, fMap);
	
		dMap = new TimeBinningSuperMap<TimeBinningMap<TimeBinningMap<VectorMap>>>(new BaseTimeBinner() {
			@Override
			public int getTimeBin(double time) {
				return getD((int) MathExt.floor(time));
			}
		}, 10, eMap);
		
		
	}
	
	
	protected VectorMap getTimeMapForTime(double time) {
		TimeBinningMap<TimeBinningMap<TimeBinningMap<VectorMap>>> e = dMap.get(time);
		TimeBinningMap<TimeBinningMap<VectorMap>> f = e.get(time);
		TimeBinningMap<VectorMap> g = f.get(time);
		VectorMap map = g.get(time);
		return map;
	}
	
	@Override
	public void put(VectorState state) {
		VectorMap map = getTimeMapForTime(state.getTime());
		map.put(state);
	}
	
	@Override
	public VectorState get(String bodyId, double time) {
		VectorMap map = getTimeMapForTime(time);
		return map.get(bodyId);
	}
	
	abstract class BaseTimeBinner implements TimeBinner {
		
		int getD(int time) {
			return (int) MathExt.floor(time / 1000000);
		}
		
		int getE(int time) {
			return (int) MathExt.floor(time / 10000) - (getD(time) * 100);
		}
		
		int getF(int time) {
			return (int) MathExt.floor(time / 100) - (getD(time)*10000) - (getE(time) * 100);
		}
		
		int getG(int time) {
			return (int) time - (getD(time) * 1000000) - (getE(time) * 10000) - (getF(time) * 100);
		}
	}
	
}
