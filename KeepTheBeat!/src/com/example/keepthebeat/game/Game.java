package com.example.keepthebeat.game;

import java.io.File;

import com.example.keepthebeat.R;
import com.example.keepthebeat.game.engine.GameEngine;
import com.example.keepthebeat.game.engine.SoundEngine;
import com.example.keepthebeat.utils.Constants;
import com.example.keepthebeat.utils.FileAccess;
import com.example.keepthebeat.utils.Tools;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;

public class Game extends Activity implements SurfaceHolder.Callback {

	public static int screenHeight;
	public static int screenWidth;
	
	// Taille virtuelle
	public final static int virtualSize = 1000;
	public static int screenYToVirtualY(int screenPosition)  {return (screenPosition * virtualSize) / screenHeight; }
	public static int screenXToVirtualX(int screenPosition)  {return (screenPosition * virtualSize) / screenWidth; }
	public static int virtualYToScreenY(int virtualPosition) {return (virtualPosition * screenHeight) / virtualSize; }
	public static int virtualXToScreenX(int virtualPosition) {return (virtualPosition * screenWidth) / virtualSize ; }
	
	// Attributs priv�s
	// Vue du jeu
	private GameView gameView;
	// Moteur du son
	private SoundEngine soundEngine;
	// Moteur du jeu
	private GameEngine gameEngine;
	// Thread principal
	private static GameThread gameThread;
	
	// Pattern file name
	private String fileName;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Retrieve parameters
		Bundle extras = getIntent().getExtras();
		String[] fileInformation;
		String filePath = null;
		if (extras != null) {
		    fileInformation = extras.getStringArray("SELECTED_MUSIC");
		    fileName = fileInformation[0] + ".vlf";
		    filePath = fileInformation[1];
		}
		File storage = getApplication().getExternalFilesDir(null);
		FileAccess.keepTheBeatFolder = storage.getPath();
		Tools.log("", "File : " + FileAccess.keepTheBeatFolder );
		if(Constants.mode == Constants.Mode.CREATE) {
			// Delete file pattern
			FileAccess.deleteFile(fileName);		
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //recuperation de la taille du device
        screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        
		// On cr�e la vue
		setContentView(R.layout.activity_game);
		gameView = (GameView)findViewById(R.id.gameView);
		gameView.getHolder().addCallback(this);

		// On cr�� le moteur de son
		if(filePath == null) {
			soundEngine = new SoundEngine(Game.this);
		}
		else {
			soundEngine = new SoundEngine(Game.this, filePath);
		}
		// On cr�e le moteur du jeu
		gameEngine = new GameEngine( soundEngine );
		if(FileAccess.fileExist("test.vlf") && Constants.mode == Constants.Mode.PLAY) {
			//gameEngine.setPatternFromString(FileAccess.readFileAsString(fileName));
			gameEngine.loadPattern(fileName);
		}
		// On envoie la position touch� par l'utilisateur
		gameView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				final int action = event.getAction();
				switch(action) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_MOVE:
					gameEngine.isTouching(true);
					gameEngine.setUserTouchPosition(event.getX(), event.getY());
					if(Constants.mode == Constants.Mode.CREATE) {
						gameEngine.saveShape(soundEngine.getCurrentMusicTime() , event.getX(), event.getY());
//						FileAccess.writeToFile(fileName, virtualX + " " + virtualY + " " + soundEngine.getCurrentMusicTime() + "\n");
					}
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
		gameThread = new GameThread(gameView, gameEngine);
	}

	@Override
	public void onDestroy() {
		//soundEngine.onDestroy();//sound engine destroyed by gameThread.setRunning(false);
		gameThread.setRunning(false);
		gameThread.interrupt();
		gameThread = null;
		gameEngine = null;
		gameView = null;
		super.onDestroy();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        if(Constants.mode == Constants.Mode.CREATE) {
	    		gameEngine.savePattern(fileName);
	        }
	    }

	    return super.onKeyDown(keyCode, event);
	}
	
	public static void pauseThread(boolean pause) {
		gameThread.setRunning(!pause);
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
