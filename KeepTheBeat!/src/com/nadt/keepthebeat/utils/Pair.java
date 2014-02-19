package com.nadt.keepthebeat.utils;

import java.io.Serializable;

public class Pair<T,K> implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 761624867093180431L;
	public Pair(T first, K second) {
		super();
		this.first = first;
		this.second = second;
	}
	public T getFirst() {
		return first;
	}
	public void setFirst(T first) {
		this.first = first;
	}
	public K getSecond() {
		return second;
	}
	public void setSecond(K second) {
		this.second = second;
	}
	private T first;
	private K second;
}
