package com.example.keepthebeat.game;

import com.example.keepthebeat.CustomActivity;
import com.example.keepthebeat.db.ScoreContract.ScoreEntry;
import com.example.keepthebeat.db.ScoreDbHelper;
import com.example.keepthebeat.game.engine.GameEngine;
import com.example.keepthebeat.game.engine.SoundEngine;
import com.example.keepthebeat.utils.Constants;
import com.example.keepthebeat.utils.Tools;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;

public class GameThread extends Thread {
	
	private GameView view;
	private GameEngine gameEngine;
	private CustomActivity gameActivity;
	private SoundEngine soundEngine;
	private boolean running = false;

	public GameThread(CustomActivity activity, GameView view, GameEngine gameEngine) {
		this.view = view;
		this.gameEngine = gameEngine;
		this.soundEngine = gameEngine.getSoundEngine();
		this.gameActivity = activity;
	}

	public void setRunning(boolean run) {
		running = run;
		soundEngine.playIfNeedToPlay(run);
		if(run) {
			start();
		}
		else {
			soundEngine.onDestroy();
			interrupt();
		}
	}

	@SuppressLint("WrongCall")
	@Override
	public void run() {
		long ticksPS = 1000 / Constants.FPS;
		long startTime;
		long sleepTime;
		while (running) {
			if(gameEngine.isEnded()) {
				running = false;
				//saveScore( p.getMusicFile().getTitle(), p.getPatternName(), gameEngine.getScore() );
				gameActivity.backToTitle("Fin de partie" , "Score : " + gameEngine.getScore());
			}
			Canvas c = null;
			gameEngine.engineLoop();
			startTime = System.currentTimeMillis();
			try {
				c = view.getHolder().lockCanvas();
				if(c != null) {
					synchronized (view.getHolder()) {
						//Tools.log(this, "Draw");
						view.onDraw(c);
					}
				}
			} finally {
				if (c != null) {
					//Tools.log(this, "Free the canvas");
					view.getHolder().unlockCanvasAndPost(c);
				}
			}
			sleepTime = ticksPS-(System.currentTimeMillis() - startTime);
			try {
				if (sleepTime > 0)
					sleep(sleepTime);
				else
					sleep(10);
			} catch (Exception e) {}
		}
	}

	private void saveScore(String musicTitle, String patternName, int score) {
		ScoreDbHelper scoreDbHelper = new ScoreDbHelper(gameActivity);
		// Gets the data repository in write mode
		SQLiteDatabase db = scoreDbHelper.getWritableDatabase();

		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(ScoreEntry.COLUMN_NAME_TRACK, musicTitle);
		values.put(ScoreEntry.COLUMN_NAME_PATTERN, patternName);
		values.put(ScoreEntry.COLUMN_NAME_SCORE, score);

		// Insert the new row, returning the primary key value of the new row
		long newRowId;
		newRowId = db.insert(ScoreEntry.TABLE_NAME, null, values);
		Tools.log(this, "Row inserted in DB : [ " + newRowId + " , " + musicTitle + " , " + patternName + " , " + score + " ]");
		//normaly score is inserted in DB
	}
} 

