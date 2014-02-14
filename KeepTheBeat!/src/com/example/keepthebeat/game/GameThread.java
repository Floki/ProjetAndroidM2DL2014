package com.example.keepthebeat.game;

import com.example.keepthebeat.game.engine.GameEngine;
import com.example.keepthebeat.game.engine.SoundEngine;
import com.example.keepthebeat.utils.Tools;

import android.annotation.SuppressLint;
import android.graphics.Canvas;

public class GameThread extends Thread {
	static final long FPS = 60;
	private GameView view;
	private GameEngine gameEngine;
	private SoundEngine soundEngine;
	private boolean running = false;

	public GameThread(GameView view, GameEngine gameEngine, SoundEngine soundEngine) {
		this.view = view;
		this.gameEngine = gameEngine;
		this.soundEngine = soundEngine;
	}

	public void setRunning(boolean run) {
		running = run;
		soundEngine.playIfNeedToPlay(run);
		if(run) {
			start();
		}
		else {
			interrupt();
		}
	}

	@SuppressLint("WrongCall")
	@Override
	public void run() {
		long ticksPS = 1000 / FPS;
		long startTime;
		long sleepTime;
		while (running) {
			Canvas c = null;
			gameEngine.engineLoop();
			startTime = System.currentTimeMillis();
			try {
				c = view.getHolder().lockCanvas();
				if(c != null) {
					synchronized (view.getHolder()) {
						Tools.log(this, "Draw");
						view.onDraw(c);
					}
				}
			} finally {
				if (c != null) {
					Tools.log(this, "Free the canvas");
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
} 

