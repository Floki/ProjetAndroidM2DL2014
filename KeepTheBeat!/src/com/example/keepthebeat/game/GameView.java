package com.example.keepthebeat.game;

import java.util.ArrayList;
import java.util.List;

import com.example.keepthebeat.game.engine.GameEngine;
import com.example.keepthebeat.game.shape.GameShape;
import com.example.keepthebeat.utils.Constants;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView {
	private SurfaceHolder holder;
	
	public GameView(Context context) {
		super(context);
		init();
	}
	public GameView(Context context, AttributeSet attr) {
		super(context, attr);
		init();
	}
	
	@SuppressLint("WrongCall")
	private void init() {
		//setBackgroundColor(Color.BLUE);
	}

	protected void onDraw(Canvas canvas) {
		if(canvas == null) {
			return;
		}
		canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
		if(Constants.pattern != null) {
			List<GameShape> drawables = new ArrayList<GameShape>(Constants.pattern);
			for(GameShape drawable : drawables) {
				if(drawable != null) {
					drawable.draw(canvas);
				}
			}	
		}
		
		if( GameEngine.score != null && GameEngine.score.getScore() != 0 ) {
			Paint scorePaint = new Paint();
			scorePaint.setTextSize(Game.virtualXToScreenX(50));
			scorePaint.setColor(Color.WHITE);
			canvas.drawText(""+GameEngine.score.getScore(), Game.virtualXToScreenX(50), Game.virtualYToScreenY(50), scorePaint);
			Paint lifePaint = new Paint();
			lifePaint.setColor(Color.BLUE);
			canvas.drawRect(Game.virtualXToScreenX(50), 
							Game.virtualYToScreenY(50), 
						   (Game.virtualXToScreenX(Game.virtualSize-50) * GameEngine.life) / GameEngine.maxLife, 
							Game.virtualYToScreenY(75), lifePaint);
			Paint durationPaint = new Paint();
			durationPaint.setColor(Color.YELLOW);
			canvas.drawRect(Game.virtualXToScreenX(50), 
							Game.virtualYToScreenY(Game.virtualSize - 75), 
						   (Game.virtualXToScreenX(Game.virtualSize - 50) * GameEngine.timeBeforeChangeMusic) / GameEngine.durationOfASample, 
						    Game.virtualYToScreenY(Game.virtualSize - 50), durationPaint);
		}
		
	}	
}