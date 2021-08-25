package com.apoapsys.astronomy.containers;

import java.util.Iterator;
import java.util.LinkedList;

public class LimitedLengthLinkedListQueue<T> {
	
	private final int maxSize;
	private LinkedList<T> list = new LinkedList<>();
	
	public LimitedLengthLinkedListQueue(int maxSize) {
		this.maxSize = maxSize;
	}

	public int getMaxSize() {
		return maxSize;
	}
	
	public int size() {
		return list.size();
	}
	
	public void add(T object) {
		while (list.size() >= maxSize) {
			poll();
		}
		list.push(object);
		
	}
	
	public T get(int index) {
		return list.get(index);
	}
	
	public T peek() {
		return peekFirst();
	}
	
	public T peekFirst() {
		return list.get(0);
	}
	
	public T peekLast() {
		return list.get(list.size() - 1);
	}
	
	public T poll() {
		return list.remove(list.size() - 1);
	}
	
	public Iterator<T> iterator() {
		return list.iterator();
	}
	
}
