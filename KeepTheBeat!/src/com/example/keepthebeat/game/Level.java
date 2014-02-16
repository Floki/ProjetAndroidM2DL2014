package com.example.keepthebeat.game;

import com.example.keepthebeat.utils.Constants;
import com.example.keepthebeat.utils.FileAccess;

public class Level {
	
	/*
	 * Easy and Hard multiplier
	 */
	private final int easyPercent = 20;
	private final int hardPercent = 20;
	
	/*
	 * Show and hide timers for GameShapes
	 */
	private long showTimer = 1100;
	private long hideTimer = 1500;
	
	/*
	 * Percents for GameShapes : shape size and timer percent while its the good moment to touch the shape
	 */
	private int shapeSizePercent = 15;
	private int timeGoodPercent = 20;
	
	/*
	 * Scoring constants
	 */
	private int baseScore = 1000;
	private int bonusChance = 50; //bonusChance = X ; there is 1 chance on X that the Shape is a bonus !
	private int tooLatePercent = 10; //pourcentage de perte sur le score du gameShape si on ne le touche pas dans le good moment
	private int missPercent = 50; //pourcentage de perte sur le score du gameShape si on ne le touche pas du tout ; aka il a disparu :o

	
	public Level() {
		String level = FileAccess.readFileAsString(Constants.keepTheBeatFolder, "level");
		if( level.equals("") ) {
			level = Constants.NORMAL;
		}
		
		//default is Normal, so modify just for Easy and Hard
		if( level.equals( Constants.EASY ) ) {
			showTimer += showTimer * easyPercent/100;
			hideTimer += hideTimer * easyPercent/100;
			timeGoodPercent += timeGoodPercent * easyPercent/100;
			
			tooLatePercent -= tooLatePercent * easyPercent/100;
			missPercent -= missPercent * easyPercent/100;
			bonusChance -= bonusChance * easyPercent/100;
		}
		else if ( level.equals( Constants.HARD ) ) {
			showTimer -= showTimer * hardPercent/100;
			hideTimer -= hideTimer * hardPercent/100;
			timeGoodPercent -= timeGoodPercent * hardPercent/100;
			
			tooLatePercent += tooLatePercent * hardPercent/100;
			missPercent += missPercent * hardPercent/100;
			bonusChance += bonusChance * hardPercent/100;
		}
	}


	public long getShowTimer() {
		return showTimer;
	}


	public long getHideTimer() {
		return hideTimer;
	}


	public int getShapeSizePercent() {
		return shapeSizePercent;
	}


	public int getTimeGoodPercent() {
		return timeGoodPercent;
	}


	public int getBaseScore() {
		return baseScore;
	}


	public int getBonusChance() {
		return bonusChance;
	}


	public int getTooLatePercent() {
		return tooLatePercent;
	}


	public int getMissPercent() {
		return missPercent;
	}
	
}
