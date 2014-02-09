package com.example.keepthebeat.utils;

import android.util.Log;

public class Tools {
	
	public static void log(Object origin, Object message) {
		Log.d(origin.getClass().getName(), "" + message);
	}
	
	public static int distanceBetweenPoints(Pair<Integer,Integer> p1, Pair<Integer,Integer> p2) {
		return distanceBetweenPosition(p1.getFirst(), p1.getSecond(), p2.getFirst(), p2.getSecond());
	}
	
	public static int distanceBetweenPosition(int x1, int y1, int x2, int y2) {
		return (int) Math.sqrt(square(x1 - x2) + square(y1 - y2));
	}
	
	public static int square(int n) {
		return n * n;
	}
}
