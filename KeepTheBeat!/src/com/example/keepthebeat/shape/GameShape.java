package com.example.keepthebeat.shape;

import com.example.keepthebeat.Game;
import com.example.keepthebeat.utils.Constants;

import android.graphics.Color;
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
	
	private boolean goodMoment = false;
	private boolean exploding = false;
	
	public GameShape(long showTimer, long hideTimer) {
		super(new OvalShape());
		height = (Game.screenHeight * Constants.shapeSizePercent/100);
		width = height;
		this.getPaint().setColor(Color.rgb(1,1,1));
		this.setAlpha(0);
		setPosition(0,0);
		
		this.showTimer = showTimer;
		this.hideTimer = hideTimer;
		
		currentTimeAtAdd = System.currentTimeMillis();

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
						this.getPaint().setColor(Color.rgb(255,193,37));
					}
					else {
						//show that moment what not good, ie user is a looser
						this.getPaint().setColor(Color.rgb(255, 0, 0));
					}
					height = (Game.screenHeight * (255-computeColor)/600);
					width = height;
					setPosition(getX(), getY());
				}
				else {
					if( System.currentTimeMillis() < ( getTimeToFullDisplay() + ( ((int)hideTimer) * Constants.timerGoodPercent/100) ) ) {
						this.getPaint().setColor(Color.rgb(0, 255, 0));
						goodMoment = true;
					}
					else {
						this.getPaint().setColor(Color.rgb(computeColor, computeColor, computeColor));
						goodMoment = false;
					}
				}
				this.setAlpha(computeColor);
				if( computeColor == 0 ) {
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
				
				if( absTimeDifference < ( ((int)showTimer) * Constants.timerGoodPercent/100) ) {
					this.getPaint().setColor(Color.rgb(0, 255, 0));
					goodMoment = true;
				}
				else {
					this.getPaint().setColor(Color.rgb(computeColor, computeColor, computeColor));
					goodMoment = false;
				}
				
				this.setAlpha(computeColor);
			}
		}
	}
	
	public void hideAndExplode() {
		showTimer = 1;
		hideTimer = 250;
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
}
