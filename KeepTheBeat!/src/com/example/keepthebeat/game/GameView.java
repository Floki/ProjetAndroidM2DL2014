package com.example.keepthebeat.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import com.example.keepthebeat.game.shape.GameShape;
import com.example.keepthebeat.utils.Constants;
import com.example.keepthebeat.utils.Tools;

import android.R.drawable;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
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
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

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
		
		if( Constants.score != 0 ) {
			Paint scorePaint = new Paint();
			scorePaint.setColor(Color.WHITE);
			canvas.drawText(""+Constants.score, 50, 50, scorePaint);
		}
	}	
}