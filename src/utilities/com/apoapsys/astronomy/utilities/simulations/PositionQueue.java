package com.apoapsys.astronomy.utilities.simulations;

import java.util.LinkedList;
import java.util.List;

public class PositionQueue<T> {
	private LinkedList<T> queue = new LinkedList<T>();
	//private Queue<Vector> queue = Queues.newArrayDeque();
	private int maxLength;
	
	public PositionQueue(int maxLength) {
		this.maxLength = maxLength;
	}
	
	
	public void push(T v) {
		queue.add(v);
		if (queue.size() > maxLength) {
			queue.pop();
		}
	}
	
	public void clear() {
		queue.clear();
	}
	
	public List<T> getList() {
		return queue;
	}
	
}
