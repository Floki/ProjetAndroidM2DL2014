package com.example.keepthebeat.shape;

import com.example.keepthebeat.Game;

import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;

public class GameShape extends ShapeDrawable{
	private int width = 175;
	private int height = 175;
	
	private boolean stillUse = true;
	private long timeToFullDisplay = 0;
	
	public GameShape() {
		super(new OvalShape());
		this.getPaint().setColor(Color.rgb(1,1,1));
		this.setAlpha(100);
		setPosition(0,0);
		
		timeToFullDisplay = (System.currentTimeMillis() + Game.time);
	}
	
	public GameShape(Shape shape) {
		super(shape);
		this.getPaint().setColor(Color.rgb(1,1,1));
		this.setAlpha(100);
		setPosition(0,0);
	}
	
	public void hideMore() {
		// TURN OFF YOUR BRAIN
		int absTimeDifference = (int) Math.abs(timeToFullDisplay - System.currentTimeMillis());
		int timeForOneColorChange = (int) (Game.time / 255);
		int computeColor = 255 - absTimeDifference / timeForOneColorChange;
		computeColor = Math.max(Math.min(computeColor, 255), 0);
		this.getPaint().setColor(Color.rgb(computeColor, computeColor, computeColor));
		if(computeColor == 0 && System.currentTimeMillis() > timeToFullDisplay) {
			this.getPaint().setAlpha(0);
			stillUse = false;
		}
		// THANK YOU, YOU CAN TURN IT ON NOW
	}
	
	public boolean stillUse() {
		return stillUse;
	}
	
	public void setPosition(int x, int y, int displayWidth, int displayHeight) {
		int newX = Math.min(Math.max(x, width), displayWidth - width);
		int newY = Math.min(Math.max(y, height), displayHeight - height);
		this.setPosition(newX, newY);
	}
	
	public void setPosition(int x, int y) {
		this.setBounds(x - width  / 2
					  ,y - height / 2
					  ,x + width  / 2
					  ,y + height / 2);
	}
}
