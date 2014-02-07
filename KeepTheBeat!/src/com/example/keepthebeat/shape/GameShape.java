package com.example.keepthebeat.shape;

import com.example.keepthebeat.Game;

import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;

public class GameShape extends ShapeDrawable{
	private int width;
	private int height;
	
	private boolean stillUse = true;
	private long timeToFullDisplay = 0;
	private long timeToHide = 0;
	private long hideTimer = 0;
	private long showTimer = 0;
	
	public GameShape(long showTimer, long hideTimer) {
		super(new OvalShape());
		height = Game.screenHeight * 15/100;
		width = height;
		this.getPaint().setColor(Color.rgb(1,1,1));
		this.setAlpha(0);
		setPosition(0,0);
		
		this.showTimer = showTimer;
		this.hideTimer = hideTimer;
		
		timeToFullDisplay = (System.currentTimeMillis() + showTimer);
		timeToHide = timeToFullDisplay + hideTimer;
	}
	
	public GameShape(Shape shape) {
		super(shape);
		this.getPaint().setColor(Color.rgb(1,1,1));
		this.setAlpha(0);
		setPosition(0,0);
	}
	
	public void hideMore() {
		if(stillUse) {
			int absTimeDifference;
			int timeForOneColorChange;
			int computeColor;
			if( System.currentTimeMillis() > timeToFullDisplay ){
				//hide the shape
				absTimeDifference = (int)(timeToHide - System.currentTimeMillis());
				timeForOneColorChange = (int) (hideTimer / 255);
				computeColor =  absTimeDifference / timeForOneColorChange - 255;

			}
			else {
				//show the shape
				absTimeDifference = (int)(timeToFullDisplay - System.currentTimeMillis());
				timeForOneColorChange = (int) (showTimer / 255);
				computeColor = 255 - absTimeDifference / timeForOneColorChange;
			}
			
			// TURN OFF YOUR BRAIN
	
			computeColor = Math.max(Math.min(computeColor, 255), 0);
			this.getPaint().setColor(Color.rgb(computeColor, computeColor, computeColor));
			this.setAlpha(computeColor);
			if(computeColor <= 0 && System.currentTimeMillis() > timeToHide) {
				this.getPaint().setAlpha(0);
				stillUse = false;
			}
		}
		// THANK YOU, YOU CAN TURN IT ON NOW
	}
	
	public boolean stillUse() {
		return stillUse;
	}
	
	public void setPosition(int x, int y) {
		int newX = Math.min(Math.max(x, width/2), Game.screenWidth - width/2);
		int newY = Math.min(Math.max(y, height/2), Game.screenHeight - height/2);
		this.setBounds(newX - width  / 2
				  ,newY - height / 2
				  ,newX + width  / 2
				  ,newY + height / 2);
	}
	
}
