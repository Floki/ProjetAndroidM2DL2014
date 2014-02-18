package com.example.keepthebeat.game;

import com.example.keepthebeat.game.shape.GameShape;

public class Score {

	private int score = 0;
	
	private static final int MAX_GOOD_COMBO = 15;
	private static final int MAX_LATE_COMBO = 10;
	private static final int MAX_MISS_COMBO = 5;
	
	private int currentGoodCombo = 0;
	private int currentGoodSpree = 0;
	private int currentLateCombo = 0;
	private int currentLateSpree = 0;
	private int currentMissCombo = 0;
	private int currentMissSpree = 0;
	
	public int getScore() {
		return this.score;
	}
	
	/**
	 * Compute the score from a gameShape
	 * This manage score compute and displayed text
	 * @param shape the Game shape with which we will compute the score
	 */
	public void computeScoreFromShape( GameShape shape  ) {
		if( shape.stillUse() ) {
			//the user touch the shape...
			if( shape.isGoodMoment() || shape.isBonus() ) { //a bonus is always a bonus because we are nice developers :)
				this.score += shape.getScore();
				if( shape.isBonus() ) {
					//this is a bonus
					shape.setExplodingText( "BONUS + " + shape.getScore() );
				}
				else {
					//that was the good moment !
					comboGood( shape );
				}
			}
			else {
				//that was before the good moment
				this.score -= shape.getScore() * Game.level.getTooLatePercent()/100;
				comboLate( shape );
			}
		}
		else {
			if( !shape.isExploding() ) {
				if( !shape.isBonus()) { //a bonus is definitively not a malus because we are nice developers !
					//here the user have miss the shape
					this.score -= shape.getScore() * Game.level.getMissPercent()/100;
					comboMiss( shape );
				}
			}
		}
	}
	
	public void comboGood( GameShape shape ) {
		currentGoodCombo++;
		currentLateCombo = 0;
		currentLateSpree = 0;
		currentMissCombo = 0;
		currentMissSpree = 0;
		
		if( currentGoodCombo >= MAX_GOOD_COMBO ) {
			currentGoodSpree++;
			currentGoodCombo = 0;
			
			int bonus = 4 * Game.level.getTimeGoodPercent() * shape.getScore();
			score += bonus * currentGoodSpree;
			shape.setExplodingText( "COMBO ! +" + bonus + " x " + currentGoodSpree );
		}
	}
	
	public void comboLate( GameShape shape ) {
		currentLateCombo++;
		currentGoodCombo = 0;
		currentGoodSpree = 0;
		currentMissCombo = 0;
		currentMissSpree = 0;
		
		if( currentLateCombo >= MAX_LATE_COMBO ) {
			currentLateSpree++;
			currentLateCombo = 0;
			
			int bonus = 2 * Game.level.getTooLatePercent() * shape.getScore();
			score += bonus * currentLateSpree;
			shape.setExplodingText( "COMBO ! +" + bonus + " x " + currentLateSpree );
		}
	}
	
	public void comboMiss( GameShape shape ) {
		currentMissCombo++;
		currentGoodCombo = 0;
		currentGoodSpree = 0;
		currentLateCombo = 0;
		currentLateSpree = 0;
		
		if( currentMissCombo >= MAX_LATE_COMBO ) {
			currentMissSpree++;
			currentMissCombo = 0;
			
			int bonus = Game.level.getMissPercent() * shape.getScore();
			score += bonus * currentMissSpree;
			shape.setExplodingText( "COMBO ! +" + bonus + " x " + currentMissSpree );
			shape.hideAndExplode();
		}
	}
}
