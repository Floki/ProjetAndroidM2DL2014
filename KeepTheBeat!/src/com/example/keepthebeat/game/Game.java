package com.example.keepthebeat.game;

import java.io.File;

import com.example.keepthebeat.CustomActivity;
import com.example.keepthebeat.R;
import com.example.keepthebeat.game.engine.GameEngine;
import com.example.keepthebeat.game.engine.SoundEngine;
import com.example.keepthebeat.music.MusicSelection;
import com.example.keepthebeat.title.Title;
import com.example.keepthebeat.utils.Constants;
import com.example.keepthebeat.utils.FileAccess;
import com.example.keepthebeat.utils.Tools;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;

public class Game extends CustomActivity implements SurfaceHolder.Callback {

	public static int screenHeight;
	public static int screenWidth;
	
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
	private static GameThread gameThread;
	
	// Pattern file name
	private String patternFolder;
	private String patternFilePath;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Retrieve parameters
		Bundle extras = getIntent().getExtras();
		String[] patternInformation;
		String musicFilePath = null;
		String patternName = null;
		if (extras != null) {
			Tools.log(this, "In extra if");
			if(extras.getStringArray("SELECTED_MUSIC") != null && Constants.mode == Constants.Mode.CREATE) {
				patternInformation = extras.getStringArray("SELECTED_MUSIC");
				// Music name for folder
			    patternFolder = patternInformation[0];
			    // Music full path
			    musicFilePath = patternInformation[1];
			    // Pattern file name
			    patternName = patternInformation[2] + ".vlf";
			    Tools.log(this, patternFilePath);
				// On retrouve le chemin du fichier pattern, la méthode utilisée permet de se foutre si il y a un / en fin de chemin
				patternFilePath = FileAccess.computeFullFilePathFromPathAndName(Pattern.patternPath(), patternFolder);
				patternFilePath = FileAccess.computeFullFilePathFromPathAndName(patternFilePath, patternName);
			}
			else if(extras.getString("SELECTED_PATTERN") != null && Constants.mode == Constants.Mode.PLAY) {
				Tools.log(this, "In SELECTED_PATTERN");
				// Music name for folder
				patternFilePath = extras.getString("SELECTED_PATTERN");
			}
			else {
				backToTitle("Erreur lors du lancement de la partie.");
			}
		}
		else {
			backToTitle("Erreur lors du lancement de la partie.");
		}
		Tools.log(this, Constants.mode + " " + patternFilePath);
		if(Constants.mode == Constants.Mode.CREATE) {
			// Delete file pattern
			FileAccess.deleteFile(patternFilePath);		
		}
		
		// Full Screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Récuperation de la taille du device
        screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        
		// On cree la vue
		setContentView(R.layout.activity_game);
		gameView = (GameView)findViewById(R.id.gameView);
		gameView.getHolder().addCallback(this);

		// On créé le moteur de son
		if(musicFilePath == null) {
			soundEngine = new SoundEngine(Game.this);
		}
		else {
			soundEngine = new SoundEngine(Game.this, musicFilePath);
		}
		
		// On crée le moteur du jeu
		gameEngine = new GameEngine( soundEngine );
		Tools.log(this, patternFilePath);
		if(FileAccess.fileExist(patternFilePath) && Constants.mode == Constants.Mode.PLAY) {
			Tools.log(this, "File Exist");
			gameEngine.loadPattern(patternFilePath);
		}
		else if(Constants.mode == Constants.Mode.CREATE) {
			gameEngine.writeInPattern(patternFilePath);
		}
		else {
			backToTitle("Impossible de récupérer la piste");
		}
		
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
	    		gameEngine.savePattern(patternFilePath);
	        }
	        backToTitle();
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
