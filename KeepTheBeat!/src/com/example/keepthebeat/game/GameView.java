package com.example.keepthebeat.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import com.example.keepthebeat.game.shape.GameShape;
import com.example.keepthebeat.utils.Tools;

import android.R.drawable;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class GameView extends View implements GameListener{

	private List<ShapeDrawable> drawables;
	private String scoreLabel = "";
	
	public GameView(Context context) {
		super(context);
		init();
	}
	public GameView(Context context, AttributeSet attr) {
		super(context, attr);
		init();
	}
	
	private void init() {
		drawables = new ArrayList<ShapeDrawable>();
		setBackgroundColor(Color.BLUE);
	}

	protected void onDraw(Canvas canvas) {
		for(ShapeDrawable drawable : drawables) {
			GameShape beat = (GameShape)drawable;
			beat.draw(canvas);
		}	
		Paint scorePaint = new Paint();
		scorePaint.setColor(Color.WHITE);
		scorePaint.setAlpha(255);
		scorePaint.setTextSize(20);
		canvas.drawText(scoreLabel, 50, 50, scorePaint);
	}

	@Override
	public void doSomethingCorrespondingToTheString(String action) {
		if(action.equals("redraw")) {
			invalidate();
		}
	}
	
	@Override
	public void doSomethingCorrespondingToTheStringAndParam(String action,
			Object param) {
		if(action.equals("redraw") && param instanceof List<?>) {
			drawables = (List<ShapeDrawable>) param;
		}
		if(action.equals("score") && param instanceof Integer) {
			scoreLabel = "" + ((Integer)param).intValue();
		}
		invalidate();
	}
	
}