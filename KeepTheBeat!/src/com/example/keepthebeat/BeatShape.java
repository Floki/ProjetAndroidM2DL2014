package com.example.keepthebeat;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Handler;
import android.util.Log;

public class BeatShape extends GameShape {

	private int width = 75;
	private int height = 75;
	private TriangleShape shape;
	
	public BeatShape() {
		super();
		
		int x[] = {0,0,0};
		int y[] = {0,0,0};
		int angle;
		for(int i = 0; i < 3; i++) {
			angle = 120 * i + (int)(Math.random() * 120);
			//Log.d("","Angle : " + angle);
			x[i] = (int)(Math.cos(Math.toRadians(angle)) * this.width);
			y[i] = (int)(Math.sin(Math.toRadians(angle)) * this.height);
		}
		shape = new TriangleShape(x[0], y[0], x[1], y[1], x[2], y[2]);
		this.getPaint().setColor(Color.rgb(1,1,1));
		this.setAlpha(255);
		
	}
	
	public void draw(Canvas canvas) {
		shape.draw(canvas, this.getPaint());
	}
	
	public void setPosition(int x, int y) {
		Game.log(this,"setPosition( " + x + " , " + y + ")");
		if(shape != null) {
			Game.log(this, "Position : " + shape.getCenterX() + " " + shape.getCenterY()
						 + "[" + shape.getX()[0] + "," + shape.getX()[1] + "," + shape.getX()[2] + "]"
						 + "[" + shape.getY()[0] + "," + shape.getY()[1] + "," + shape.getY()[2] + "]");
			shape.setPosition(x, y);
		}
	}
}
