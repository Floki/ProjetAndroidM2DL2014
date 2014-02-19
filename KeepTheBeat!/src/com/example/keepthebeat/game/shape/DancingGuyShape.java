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
	private int animationBrasG;
	private int animationBrasD;
	private int animationJambeG;
	private int animationJambeD; 
	
	public DancingGuyShape(int x, int y, int poid, int taille, int mainColor, int secondColor, int visiereColor) {
		this.x = x;
		this.y = y;
		this.animationBrasG = 0;
		this.animationBrasD = 0;
		this.animationJambeG = 0;
		this.animationJambeD = 0;
		this.mainPaint = new Paint();
		this.mainPaint.setColor(mainColor);
		this.mainPaint.setAlpha(100);
		this.secondPaint = new Paint();
		this.secondPaint.setColor(secondColor);
		this.secondPaint.setAlpha(200);
		this.visierePaint = new Paint();
		this.visierePaint.setColor(visiereColor);
		this.poid = poid;
		this.taille = taille;
		this.tailleJambe = taille / 2 * 3;
		this.tailleBras = taille / 2 * 3;
		this.brasD = new Rect(x + poid/2 + 1 , y - taille/2, x + poid/2 + 1 + poid /3, y - taille/2 + tailleBras);
		this.brasG = new Rect(x - poid/2 - 1 - poid /3, y - taille/2, x - poid/2 - 1 , y - taille/2 + tailleBras);
		this.jambeD = new Rect(x + poid/2, y + taille/2 + 1, x + 1 , y + taille/2 + tailleJambe);
		this.jambeG = new Rect(x - 1, y + taille/2 + 1, x - poid/2 , y + taille/2 + tailleJambe);
		this.corp = new Rect(x - poid/2, y - taille/2, x + poid/2, y + taille/2);
		this.tete = new Rect(x + poid/4, y - taille/2 - 1, x - poid/4, y -taille/2- taille/3);
		this.visiere = new Rect(x + poid/6, y - taille/2 - 1, x - poid/6, y -taille/2- taille/4);
	}
	
	public void draw(Canvas canvas) {
		super.draw(canvas);
		// Dessine corp
		canvas.drawRect(corp, mainPaint);
		// Dessine bras
		Rect brasGDrawn = moveBrasOuJambe(brasG, animationBrasG, true);
		canvas.drawRect(brasGDrawn, mainPaint);
		Rect brasDDrawn = moveBrasOuJambe(brasD, animationBrasD, false);
		canvas.drawRect(brasDDrawn, mainPaint);
		// Dessine Jambe
		Rect jambeGDrawn = moveBrasOuJambe(jambeG, animationJambeG, true);
		canvas.drawRect(jambeGDrawn, mainPaint);
		Rect jambeDDrawn = moveBrasOuJambe(jambeD, animationJambeD, false);
		canvas.drawRect(jambeDDrawn, mainPaint);
		// Dessine tete
		canvas.drawRect(tete, 	 mainPaint);
		canvas.drawRect(visiere, visierePaint);
		// Dessiner les ligne de lumière
		// Corp
		canvas.drawPath(lightLine(corp, 0, true), secondPaint);
		canvas.drawPath(lightLine(corp, 0, false), secondPaint);
		canvas.drawPath(lightLine(corp, 1, false), secondPaint);
		// Bras
		canvas.drawPath(lightLine(brasGDrawn, 0, true), secondPaint);
		canvas.drawPath(lightLine(brasDDrawn, 0, true), secondPaint);
		canvas.drawPath(lightLine(brasDDrawn, 1, false), secondPaint);
		canvas.drawPath(lightLine(brasGDrawn, 1, false), secondPaint);

		// Jambe
		canvas.drawPath(lightLine(jambeGDrawn, 0, true), secondPaint);
		canvas.drawPath(lightLine(jambeDDrawn, 0, true), secondPaint);
		canvas.drawPath(lightLine(jambeDDrawn, 0, false), secondPaint);
		canvas.drawPath(lightLine(jambeGDrawn, 0, false), secondPaint);

		// Tete
		canvas.drawPath(lightLine(tete, 1, false), secondPaint);
	}
	
	public Path lightLine(Rect part, int pattern, boolean gaucheOrHaut) {
		Path lightLine = new Path();
		int invert = gaucheOrHaut ? 1 : -1;
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
	
	public Rect moveBrasOuJambe(Rect bras, int animation, boolean gauche) {
		int height = bras.height();
		int width = bras.width();
		int invert = gauche ? -1 : 1;
		switch(animation) { 
			// Levé
			case 2:
				return new Rect(bras.left, bras.top + width, bras.right, bras.top - height + width);
		// Horizontal
			case 1:
				return new Rect(bras.centerX() + invert * width/2, bras.top, bras.centerX() + invert * (height - invert * width / 2), bras.top + width);
		}
		return new Rect(bras);
	}
	
	public void setAnimation(int brasG, int brasD, int jambeG, int jambeD) {
		animationBrasG = brasG;
		animationBrasD = brasD;
		animationJambeG = jambeG;
		animationJambeD = jambeD;
	}
}
