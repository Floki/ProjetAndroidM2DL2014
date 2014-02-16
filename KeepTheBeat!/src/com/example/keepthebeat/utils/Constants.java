package com.example.keepthebeat.utils;

import java.util.Collection;

import com.example.keepthebeat.game.Pattern;
import com.example.keepthebeat.game.shape.GameShape;

public class Constants {
	// SCARY variables :D Feel free to use :)
	// See : http://stackoverflow.com/questions/11774099/legal-identifiers-in-java
	public static int ௹, _, $, ¢, £, ¤, ¥, ؋, ৲, ৳, ૱, ฿, ៛,   ₠, ₡, ₢, ₣, ₤, ₥, ₦, ₧, ₨, ₩, ₪,
	₫,d, €, ₭,K, ₮,T, ₯, ₰, ₱, P, ₲, G,₳,A, ₴;
	
	
	/*
	 * Show and hide timers for GameShapes
	 */
	public static final long SHOW_TIMER = 1100;
	public static final long HIDE_TIMER = 1500;
	
	/*
	 * Percents for GameShapes : shape size and timer percent while its the good moment to touch the shape
	 */
	public static final int SHAPE_SIZE_PERCENT = 15;
	public static final int TIME_GOOD_PERCENT = 20;
	
	/*
	 * Scoring constants
	 */
	public static final int BASE_SCORE = 1000;
	public static final int BONUS_CHANCE = 50; //bonusChance = X ; there is 1 chance on X that the Shape is a bonus !
	public static final int TOO_LATE_PERCENT = 10; //pourcentage de perte sur le score du gameShape si on ne le touche pas dans le good moment
	public static final int MISS_PERCENT = 50; //pourcentage de perte sur le score du gameShape si on ne le touche pas du tout ; aka il a disparu :o


	public static final String EASY = "EASY";
	public static final String NORMAL = "NORMAL";
	public static final String HARD = "HARD";
	
	/**
	 * Mode
	 */
	public enum Mode {CREATE, PLAY};
	public static Mode mode;
	
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

	/*
	 * Default pattern
	 */
	public static Pattern defaultPattern;

}
