package com.example.keepthebeat.game.shape;

import com.example.keepthebeat.game.Game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;

public class DancingGuyShape extends ShapeDrawable{

	private int x;
	private int y;
	private int poid = 20;
	private int taille = 40;
	private int tailleBras;
	private int tailleJambe;
	private Rect brasG;
	private Rect brasD;
	private Rect jambeG;
	private Rect jambeD;
	private Rect corp;
	private Rect tete;
	private Rect visiere;
	private Paint mainPaint;
	private Paint secondPaint;
	private Paint visierePaint;
	
	public DancingGuyShape(int x, int y, int poid, int taille, int mainColor, int secondColor, int visiereColor) {
		this.x = x;
		this.y = y;
		this.mainPaint = new Paint();
		this.mainPaint.setColor(mainColor);
		this.secondPaint = new Paint();
		this.secondPaint.setColor(secondColor);
		this.visierePaint = new Paint();
		this.visierePaint.setColor(visiereColor);
		this.poid = poid;
		this.taille = taille;
		this.tailleJambe = taille / 2 * 3;
		this.tailleBras = taille / 2 * 3;
		this.brasD = new Rect(x + poid/2 + 1 + poid /3, y - taille/2, x + poid/2 + 1 , y - taille/2 + tailleBras);
		this.brasG = new Rect(x - poid/2 - 1 - poid /3, y - taille/2, x - poid/2 - 1 , y - taille/2 + tailleBras);
		this.jambeD = new Rect(x + poid/2, y + taille/2 + 1, x + 1 , y + taille/2 + tailleJambe);
		this.jambeG = new Rect(x - poid/2, y + taille/2 + 1, x - 1 , y + taille/2 + tailleJambe);
		this.corp = new Rect(x - poid/2, y - taille/2, x + poid/2, y + taille/2);
		this.tete = new Rect(x + poid/4, y - taille/2 - 1, x - poid/4, y -taille/2- taille/3);
		this.visiere = new Rect(x + poid/6, y - taille/2 - 1, x - poid/6, y -taille/2- taille/4);
	}
	
	public void draw(Canvas canvas) {
		super.draw(canvas);
		// Dessine corp
		canvas.drawRect(corp, mainPaint);
		// Dessine bras
		canvas.drawRect(brasG, mainPaint);
		canvas.drawRect(brasD, mainPaint);
		// Dessine Jambe
		canvas.drawRect(jambeG, mainPaint);
		canvas.drawRect(jambeD, mainPaint);
		// Dessine tete
		canvas.drawRect(tete, 	 mainPaint);
		canvas.drawRect(visiere, visierePaint);
		// Dessiner les ligne de lumière
		// Corp
		canvas.drawPath(lightLine(corp, 0, 1), secondPaint);
		canvas.drawPath(lightLine(corp, 0, -1), secondPaint);
		canvas.drawPath(lightLine(corp, 1, -1), secondPaint);
		// Bras
		canvas.drawPath(lightLine(brasG, 0, 1), secondPaint);
		canvas.drawPath(lightLine(brasD, 0, 1), secondPaint);
		canvas.drawPath(lightLine(brasD, 1, -1), secondPaint);
		canvas.drawPath(lightLine(brasG, 1, -1), secondPaint);

		// Jambe
		canvas.drawPath(lightLine(jambeG, 0, 1), secondPaint);
		canvas.drawPath(lightLine(jambeD, 0, 1), secondPaint);
		canvas.drawPath(lightLine(jambeD, 0, -1), secondPaint);
		canvas.drawPath(lightLine(jambeG, 0, -1), secondPaint);

		// Tete
		canvas.drawPath(lightLine(tete, 1, -1), secondPaint);
		// Bras
//		lightLine = new Path();
//		lightLine.moveTo(x - poid / 2 - 2, 			  y - taille / 2);
//		lightLine.lineTo(x - poid / 2 - 2, 			  y - taille / 2 + bras / 3);
//		lightLine.lineTo(x - poid / 2 - poid / 4 - 2, y - taille / 2 + bras / 3);
//		lightLine.lineTo(x - poid / 2 - poid / 4 - 2, y - taille / 2 + bras);
//		canvas.drawPath(lightLine, secondPaint);
//		lightLine = new Path();
//		lightLine.moveTo(x + poid / 2 + 2, y - taille / 2);
//		lightLine.lineTo(x + poid / 2 + 2, y - taille / 2 + bras / 3);
//		lightLine.lineTo(x + poid / 2 + poid / 4 + 2, y - taille / 2 + bras / 3);
//		lightLine.lineTo(x + poid / 2 + poid / 4 + 2, y - taille / 2 + bras);
//		canvas.drawPath(lightLine, secondPaint);
		// Jambes
	}
	
	public Path lightLine(Rect part, int pattern, int invert) {
		Path lightLine = new Path();
		switch(pattern) { 
			case 1:
				lightLine.moveTo(part.exactCenterX() - part.width() / 4, part.exactCenterY()- invert * part.height()/2);
				lightLine.lineTo(part.exactCenterX() - part.width() / 4, part.exactCenterY()- invert * part.height()/3);
				lightLine.lineTo(part.exactCenterX() + part.width() / 4, part.exactCenterY()- invert * part.height()/3);
				lightLine.lineTo(part.exactCenterX() + part.width() / 4, part.exactCenterY()- invert * part.height()/2);
			break;
			default:
				lightLine.moveTo(part.exactCenterX()- invert * part.width()/3, part.top);
				lightLine.lineTo(part.exactCenterX()- invert * part.width()/6, part.top + part.height() / 3);
				lightLine.lineTo(part.exactCenterX()- invert * part.width()/6, part.top + part.height() / 3);
				lightLine.lineTo(part.exactCenterX()- invert * part.width()/6, part.bottom);
			break;
		}
		return lightLine;
	}
}
