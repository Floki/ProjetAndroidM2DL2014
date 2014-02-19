package com.nadt.keepthebeatsurvival.game.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.shapes.Shape;

public class TriangleShape extends Shape{

	private Path path;
	private int centerX;
	private int centerY;
	private int x[] = {0,0,0};
	private int y[] = {0,0,0};
	
	public TriangleShape(int centerX, int centerY, int x, int y, int x2, int y2, int x3, int y3) {
		this.init(centerX, centerY, x, y, x2, y2, x3, y3);
	}
	
	public TriangleShape(int x, int y, int x2, int y2, int x3, int y3) {
		this.init(0, 0, x, y, x2, y2, x3, y3);
	}
	
	private void init(int centerX, int centerY, int x, int y, int x2, int y2, int x3, int y3) {
		this.x[0] = x;
		this.x[1] = x2;
		this.x[2] = x3;
		this.y[0] = y;
		this.y[1] = y2;
		this.y[2] = y3;
		this.centerX = centerX;
		this.centerY = centerY;
	}
	
	public void setPosition(int newX, int newY) {
		for(int i = 0; i < 3; i++) {
			this.x[i] += newX - this.centerX;
			this.y[i] += newY - this.centerY;
		}
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		path = new Path();
		path.setFillType(Path.FillType.EVEN_ODD);
		path.moveTo(x[0],y[0]);
		path.lineTo(x[1],y[1]);
		path.lineTo(x[2],y[2]);
		path.lineTo(x[0],y[0]);
		path.close();
		canvas.drawPath(path, paint);
	}
	
	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

	public int getCenterX() {
		return centerX;
	}

	public void setCenterX(int centerX) {
		this.centerX = centerX;
	}

	public int getCenterY() {
		return centerY;
	}

	public void setCenterY(int centerY) {
		this.centerY = centerY;
	}

	public int[] getX() {
		return x;
	}

	public void setX(int[] x) {
		this.x = x;
	}

	public int[] getY() {
		return y;
	}

	public void setY(int[] y) {
		this.y = y;
	}

}
