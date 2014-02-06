package com.example.keepthebeat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import android.R.drawable;
import android.content.Context;
import android.graphics.Canvas;
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

public class GameView extends View implements GameListener{

	private List<ShapeDrawable> drawables;
	public GameView(Context context) {
		super(context);
		drawables = new ArrayList<ShapeDrawable>();
	}
	public GameView(Context context, AttributeSet attr) {
		super(context, attr);
		drawables = new ArrayList<ShapeDrawable>();
	}

	protected void onDraw(Canvas canvas) {
		for(ShapeDrawable drawable : drawables) {
			Game.log(this, "Tell to draw");
			BeatShape beat = (BeatShape)drawable;
			beat.draw(canvas);
		}	
	}

	@Override
	public void doSomethingCorrespondingToTheString(String action) {
		
	}
	
	@Override
	public void doSomethingCorrespondingToTheStringAndParam(String action,
			Object param) {
		if(action.equals("redraw") && param instanceof List<?>) {
			drawables = (List<ShapeDrawable>) param;
			invalidate();
		}
	}
}