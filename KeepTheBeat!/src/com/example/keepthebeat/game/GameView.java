package com.example.keepthebeat.game;

import java.util.ArrayList;
import java.util.List;

import com.example.keepthebeat.game.engine.GameEngine;
import com.example.keepthebeat.game.shape.DancingGuyShape;
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
	private GameEngine gameEngine;
	
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
			Paint durationPaint = new Paint();
			durationPaint.setColor(Color.YELLOW);
			
			int xLeft, xRight, yTop, yBottom;
			xLeft = Game.virtualXToScreenX(50);
			xRight = (int) (xLeft + (((float)Game.virtualXToScreenX(Game.virtualSize - 100)) * GameEngine.getPatternPercent() / 100));
			xRight = Math.max(xLeft, xRight);
			yTop = Game.virtualYToScreenY(Game.virtualSize - 50);
			yBottom = Game.virtualYToScreenY(Game.virtualSize - 75);
			canvas.drawRect(xLeft, yTop, xRight, yBottom, durationPaint);
			
			if(gameEngine != null) {
				for(DancingGuyShape dancingGuy : gameEngine.getDancingGuy()) {
					dancingGuy.draw(canvas);
				}
			}
		}
	}

	public void setGameEngine(GameEngine gameEngine) {
		this.gameEngine = gameEngine;
  	}
}