package com.example.keepthebeat;

import com.example.keepthebeat.engine.GameEngine;
import com.example.keepthebeat.engine.SoundEngine;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class Game extends Activity {

	// Variables globales du jeu
	public static long time = 750;
	public static int screenHeight;
	public static int screenWidth;
	
	// Attributs priv�s
	// Vue du jeu
	private GameView gameView;
	// Moteur du son
	private SoundEngine soundEngine;
	// Moteur du jeu
	private GameEngine gameEngine;
	//TODO Remove when not use
	private boolean fileExist;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fileExist = FileAccess.fileExist("test.vlf");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //recuperation de la taille du device
        screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        
		// On cr�e la vue
		setContentView(R.layout.activity_game);
		gameView = (GameView)findViewById(R.id.gameView);
		
		// On cr�� le moteur de son
		soundEngine = new SoundEngine(Game.this);
		// On cr�e le moteur du jeu
		gameEngine = new GameEngine();
		soundEngine.addToTheListnersTheListener(gameEngine);
		gameEngine.addToTheListnersTheListener(gameView);

		// On envoie la position touch� par l'utilisateur
		gameView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				final int action = event.getAction();
				switch(action) {
				case MotionEvent.ACTION_DOWN:
					gameEngine.addGameShape( event.getX(), event.getY());
					break;
				case MotionEvent.ACTION_MOVE:
					gameEngine.setUserTouchPosition(event.getX(), event.getY());
					gameEngine.addGameShape( event.getX(), event.getY());
					if(!fileExist) {
						FileAccess.writeToFile("test.vlf", event.getX() + " " + event.getY() + " " + soundEngine.getCurrentMusicTime() + "\n");
					}
					break;
				case MotionEvent.ACTION_UP:
					break;
				case MotionEvent.ACTION_CANCEL:
					break;
				}
				return true;
			}
		});
		
		soundEngine.PlayOrPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.layout.menu, menu);
		return true;
	}

	@Override
	public void onDestroy() {
		soundEngine.onDestroy();
		soundEngine = null;
		super.onDestroy();
	}
	
	public static void log(Object origin, Object message) {
		Log.d(origin.getClass().getName(), "" + message);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		// Doc menus : http://developer.android.com/guide/topics/ui/menus.html
        switch (item.getItemId()) {
          case R.id.option:
              Toast.makeText(Game.this, "Option", Toast.LENGTH_SHORT).show();
              return true;
          case R.id.returnToTitle:
              // TODO : return to main menu
              return true; 
          case R.id.stats:
              // TODO : stats view
              return true; 
          case R.id.quit:
              finish();
              return true;
        }
        return false;
    }
}
