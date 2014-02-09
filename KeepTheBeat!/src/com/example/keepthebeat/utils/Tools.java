package com.example.keepthebeat.utils;

import android.util.Log;

public class Tools {
	
	public static void log(Object origin, Object message) {
		Log.d(origin.getClass().getName(), "" + message);
	}
}
