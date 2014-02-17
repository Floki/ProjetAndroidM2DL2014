package com.example.keepthebeat.utils;

import java.util.Collection;

import com.example.keepthebeat.game.shape.GameShape;

public class Constants {

	/*
	 * Levels
	 */
	public static final String EASY = "EASY";
	public static final String NORMAL = "NORMAL";
	public static final String HARD = "HARD";
	
	/*
	 * Thread running and shared object
	 */
	public static boolean running;
	public static Collection<GameShape> pattern;
	public static int score;
	public static long FPS = 100;
	
	
	/*
	 *  Game folder
	 */
	public static String keepTheBeatFolder = "/sdcard/KeepTheBeat"; //should not be defined here, because of it's computed by Title.onCreate() - so it is hard to know real value of it... -_-' 

}
