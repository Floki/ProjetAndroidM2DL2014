package com.example.keepthebeat;

import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;

public class GameShape extends ShapeDrawable{
	private int width = 75;
	private int height = 75;
	
	public GameShape() {
		super(new OvalShape());
		this.getPaint().setColor(Color.GRAY);
		this.setAlpha(100);
		setPosition(0,0);
	}
	
	public GameShape(Shape shape) {
		super(shape);
		this.getPaint().setColor(Color.GRAY);
		this.setAlpha(100);
		setPosition(0,0);
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
