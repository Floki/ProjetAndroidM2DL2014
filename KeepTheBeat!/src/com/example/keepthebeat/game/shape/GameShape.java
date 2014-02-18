package com.example.keepthebeat.game.shape;

import com.example.keepthebeat.game.Game;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;

public class GameShape extends ShapeDrawable{
	private int width;
	private int height;
	
	private boolean stillUse = true;
	private long hideTimer = 0;
	private long showTimer = 0;
	private long currentTimeAtAdd;
	private int xPosition;
	private int yPosition;
	
	private int score;
	
	private String explodeString = "";
	
	private boolean goodMoment = false;
	private boolean exploding = false;
	private int maxExplodeTic;
	
	public GameShape(long showTimer, long hideTimer) {
		super(new OvalShape());
		height = (Game.screenHeight * Game.level.getShapeSizePercent() / 100);
		width = height;
		
		maxExplodeTic = (int) ((Game.screenHeight/1.8) * (Game.screenHeight/1.8));
		
		this.getPaint().setColor(Color.rgb(1,1,1));
		this.setAlpha(0);
		setPosition(0,0);
		
		this.showTimer = showTimer;
		this.hideTimer = hideTimer;
		
		currentTimeAtAdd = System.currentTimeMillis();
		
		/*
		 * compute Shape's score
		 */
		//is this Shape a bonus ?
		int chance = (int)((Math.random() * Game.level.getBonusChance()) + 1);
		if( chance == (int)((Math.random() * Game.level.getBonusChance()) + 1) ) {
			score = Game.level.getBaseScore() + Game.level.getBaseScore() * chance / Game.level.getBonusChance() *2;
		}
		else {
			score = Game.level.getBaseScore();
		}

	}
	
	public GameShape(Shape shape) {
		super(shape);
		this.getPaint().setColor(Color.rgb(1,1,1));
		this.setAlpha(0);
		setPosition(0,0);
	}
	
	private long getTimeToFullDisplay() {
		return currentTimeAtAdd + showTimer;
	}
	
	private long getTimeToFullHide() {
		return currentTimeAtAdd + showTimer + hideTimer;
	}
	
	public void hideMore() {
		if(stillUse) {
			int absTimeDifference;
			int timeForOneColorChange;
			int computeColor;
			if( System.currentTimeMillis() > getTimeToFullDisplay() ){
				//hide the shape
				absTimeDifference = (int)(getTimeToFullHide() - System.currentTimeMillis());
				timeForOneColorChange = Math.max((int) (hideTimer / 255),1);
				computeColor =  absTimeDifference / timeForOneColorChange;
				computeColor = Math.max(Math.min(computeColor, 255), 0);
				if( exploding ) {
					if( goodMoment ) {
						//show that moment what good, ie GG
						if( !isBonus() ) {
							this.getPaint().setColor(Color.rgb(255,193,37));
						}
						else {
							// it is a BONUS !!
							this.getPaint().setColor(Color.rgb(255,215,0));
						}
					}
					else {
						//show that moment what not good, ie user is a looser
						if( !isBonus() ) {
							this.getPaint().setColor(Color.rgb(255, 0, 0));
						}
						else {
							// it is a BONUS
							this.getPaint().setColor(Color.rgb(255,215,0));
						}
					}
					//height = (Game.screenHeight * Constants.SHAPE_SIZE_PERCENT / 100) + (Game.screenHeight * (255-computeColor)/600);
					//height += height/3.5;
					height = (int) ((Game.screenHeight * Game.level.getShapeSizePercent()/ 100)/4 + Math.sqrt( ( ( (255 - computeColor + 10 ) ) * maxExplodeTic ) / 255 ));
					width = height;
					setPosition(getX(), getY());
					computeColor = computeColor / 2; //on explode, shape will be more rapidely transparent
				}
				else {
					if( System.currentTimeMillis() < ( getTimeToFullDisplay() + ( ((int)hideTimer) * Game.level.getTimeGoodPercent()/100) ) ) {
						//is is good moment
						if( !isBonus() ) {
							this.getPaint().setColor(Color.rgb(0, 255, 0));
						}
						else {
							// it is a BONUS !!
							this.getPaint().setColor(Color.rgb(255,165,0));
						}
						goodMoment = true;
					}
					else {
						if( !isBonus() ) {
							this.getPaint().setColor(Color.rgb(computeColor, computeColor, computeColor));
						}
						else {
							// it is a BONUS !!
							this.getPaint().setColor(Color.rgb(255,165,0));
						}
						goodMoment = false;
					}
				}
				this.setAlpha(computeColor);
				if( System.currentTimeMillis() > getTimeToFullHide() ) {
					this.getPaint().setAlpha(0);
					stillUse = false;
				}
			}
			else {
				//show the shape
				absTimeDifference = (int)(getTimeToFullDisplay() - System.currentTimeMillis());
				timeForOneColorChange = Math.max((int) (showTimer / 255),1);
				computeColor = 255 - absTimeDifference / timeForOneColorChange;
				computeColor = Math.max(Math.min(computeColor, 255), 0);
				
				if( absTimeDifference < ( ((int)showTimer) * Game.level.getTimeGoodPercent()/100) ) {
					if( !isBonus() ) {
						this.getPaint().setColor(Color.rgb(0, 255, 0));
					}
					else {
						// it is a BONUS !!
						this.getPaint().setColor(Color.rgb(255,165,0));
					}
					goodMoment = true;
				}
				else {
					if( !isBonus() ) {
						this.getPaint().setColor(Color.rgb(computeColor, computeColor, computeColor));
					}
					else {
						// it is a BONUS !!
						this.getPaint().setColor(Color.rgb(255,165,0));
					}
					goodMoment = false;
					
					int timeForOneGrow = Math.max((int) ( (showTimer - ( ((int)showTimer) * Game.level.getTimeGoodPercent()/100)) / (Game.screenHeight * Game.level.getShapeSizePercent() / 100) ),1);
					height = (int) ( (Game.screenHeight * Game.level.getShapeSizePercent() / 100) - ( absTimeDifference - ( ((int)showTimer) * Game.level.getTimeGoodPercent()/100) ) / timeForOneGrow );
					width = height;
					setPosition(getX(), getY());
				}
				
				this.setAlpha(computeColor);
			}
		}
	}
	
	public void hideAndExplode() {
		showTimer = 1;
		hideTimer = 400;
		currentTimeAtAdd = System.currentTimeMillis();
		exploding  = true;
	}
	
	public boolean stillUse() {
		return stillUse;
	}
	
	public void setPosition(int x, int y) {
		xPosition = Math.min(Math.max(x, width/2), Game.screenWidth - width/2);
		yPosition = Math.min(Math.max(y, height/2), Game.screenHeight - height/2);
		this.setBounds(xPosition - width  / 2
				  ,yPosition - height / 2
				  ,xPosition + width  / 2
				  ,yPosition + height / 2);
	}
	
	public void draw(Canvas canvas) {
		super.draw(canvas);
		Paint paint = new Paint();
		if( goodMoment ) {
			paint.setColor(Color.rgb(0, 255, 0));
		}
		else {
			paint.setColor(Color.rgb(255, 0, 0));
		}
		if( isBonus() ) {
			paint.setColor( Color.rgb(255,215,0) );
		}
		paint.setAlpha( this.getPaint().getAlpha() );
		int baseHeight = (Game.screenHeight * Game.level.getShapeSizePercent() / 100);
		paint.setTextSize( baseHeight / 3 );
		paint.setTextAlign( Align.CENTER );
		if( isExploding() ) {
			canvas.drawText( explodeString, getX(), getY()-(int)(height/2), paint);
		}
	}
	
	public void setExplodingText( String text ) {
		this.explodeString = text;
	}
	
	public int getX() {
		return xPosition;
	}
	
	public int getY() {
		return yPosition;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public boolean isGoodMoment() {
		return this.goodMoment && !exploding;
	}
	
	public boolean isExploding() {
		return this.exploding;
	}
	
	public int getScore() {
		return this.score;
	}
	
	public boolean isBonus() {
		return getScore() > Game.level.getBaseScore();
	}
	
	public boolean notGoodMomentAfterDisplay() {
		return System.currentTimeMillis() > currentTimeAtAdd + showTimer && !goodMoment;
	}
}
