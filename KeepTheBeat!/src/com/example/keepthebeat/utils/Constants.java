package com.example.keepthebeat.utils;

public class Constants {

	/*
	 * Show and hide timers for GameShapes
	 */
	public static long showTimer = 1100;
	public static long hideTimer = 1500;
	
	/*
	 * Percents for GameShapes : shape size and timer percent while its the good moment to touch the shape
	 */
	public static int shapeSizePercent = 15;
	public static int timerGoodPercent = 20;
	
	/*
	 * Scoring constants
	 */
	public static int baseScore = 1000;
	public static int bonusChance = 50; //bonusChance = X ; there is 1 chance on X that the Shape is a bonus !
	public static int tooLatePercent = 10;
	public static int missPercent = 50;
	
	/**
	 * Mode
	 */
	public enum Mode {CREATE, PLAY};
	public static Mode mode;
	
}
