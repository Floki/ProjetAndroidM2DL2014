package com.example.keepthebeat.game;

import java.io.ObjectInputStream;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;

import com.example.keepthebeat.CustomActivity;
import com.example.keepthebeat.R;
import com.example.keepthebeat.game.engine.GameEngine;
import com.example.keepthebeat.game.engine.SoundEngine;
import com.example.keepthebeat.utils.Constants;
import com.example.keepthebeat.utils.FileAccess;
import com.example.keepthebeat.utils.Tools;

public class Game extends CustomActivity implements SurfaceHolder.Callback {

	public static int screenHeight;
	public static int screenWidth;
	
	public static Level level;
	
	// Taille virtuelle
	public final static int virtualSize = 1000;
	public static int screenYToVirtualY(int screenPosition)  {return (screenPosition * virtualSize) / screenHeight; }
	public static int screenXToVirtualX(int screenPosition)  {return (screenPosition * virtualSize) / screenWidth; }
	public static int virtualYToScreenY(int virtualPosition) {return (virtualPosition * screenHeight) / virtualSize; }
	public static int virtualXToScreenX(int virtualPosition) {return (virtualPosition * screenWidth) / virtualSize ; }
	
	// Attributs prives
	// Vue du jeu
	private GameView gameView;
	// Moteur du son
	private SoundEngine soundEngine;
	// Moteur du jeu
	private GameEngine gameEngine;
	// Thread principal
	private GameThread gameThread;
	
	// Pattern file name
	private String patternFolder;
	private String patternFilePath;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Tools.log(this, "Start Game");
		super.onCreate(savedInstanceState);
		level = new Level();
		// Full Screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Récuperation de la taille du device
        screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        
		// On cree la vue
        Tools.log(this, "Set View");
		setContentView(R.layout.activity_game);
		gameView = (GameView)findViewById(R.id.gameView);
		gameView.getHolder().addCallback(this);
		
		Tools.log(this, "Set SoundEngine");
		soundEngine = new SoundEngine(this, Game.this);
		
		// On crée le moteur du jeu
		Tools.log(this, "Start GameEngine");
		gameEngine = new GameEngine( this, soundEngine );
		
		// On envoie la position touché par l'utilisateur
		gameView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				final int action = event.getAction();
				switch(action) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_MOVE:
					gameEngine.isTouching(true);
					gameEngine.setUserTouchPosition(event.getX(), event.getY());
					break;
				case MotionEvent.ACTION_UP:
					gameEngine.isTouching(false);
					break;
				case MotionEvent.ACTION_CANCEL:
					gameEngine.isTouching(false);
					break;
				}
				return true;
			}
		});
		Tools.log(this, "Set GameThread");
		gameThread = new GameThread(this, gameView, gameEngine);
	}

	@Override
	public void onDestroy() {
		if(gameThread != null) {
			gameThread.setRunning(false);
			gameThread.interrupt();
		}
		if(soundEngine != null) {
			soundEngine.onDestroy();
		}
		if(gameView != null) {
			gameView.destroyDrawingCache();
		}
		gameThread = null;
		gameEngine = null;
		gameView = null;
		soundEngine = null;
		super.onDestroy();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	onDestroy();
	        backToTitle();
	        return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		gameThread.setRunning(true);
		gameView.setWillNotDraw(false);
	}
	@Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
