package com.example.keepthebeat.game;

import com.example.keepthebeat.game.shape.GameShape;
import com.example.keepthebeat.utils.Constants;

public class Score {

	private int score = 0;
	
	public int getScore() {
		return this.score;
	}
	
	public void computeScoreFromShape( GameShape shape  ) {
		if( shape.stillUse() ) {
			if( shape.isGoodMoment() || shape.isBonus() ) { //a bonus is always a bonus because we are nice developers :)
				this.score += shape.getScore();
				if( shape.isBonus() ) {
					shape.setExplodingText( "BONUS + " + shape.getScore() );
				}
				else {
					shape.setExplodingText( "+ " + shape.getScore() );
				}
			}
			else {
				this.score -= shape.getScore() * Game.level.getTooLatePercent()/100;
				shape.setExplodingText( "- " + shape.getScore() * Game.level.getTooLatePercent()/100 );
			}
		}
		else {
			if( !shape.isExploding() ) {
				if( !shape.isBonus()) //a bonus is definitively not a malus because we are nice developers !
					this.score -= shape.getScore() * Game.level.getMissPercent()/100;
			}
		}
	}
}
