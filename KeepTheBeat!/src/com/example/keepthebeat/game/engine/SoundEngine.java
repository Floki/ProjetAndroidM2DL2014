package com.example.keepthebeat.game.engine;

import com.example.keepthebeat.R;
import com.example.keepthebeat.R.raw;
import com.example.keepthebeat.game.GameNotifier;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
//import android.media.audiofx.Equalizer;
//import android.media.audiofx.Visualizer;
//import android.media.audiofx.Visualizer.OnDataCaptureListener;
import android.widget.TextView;

/**
 * Contient tout le code pour la lecture et analyse de sons
 * @author Florian
 *
 */
public class SoundEngine extends GameNotifier{

	// Lit la musique
	private MediaPlayer mRealPlayer; 

	// Lecture en cour
	private boolean isPlaying;
	
	@SuppressLint("NewApi")
	public SoundEngine(Context context) {
		// Initialisation des lecteur 
		int mediaToOpen = 0;
		mediaToOpen = R.raw.testsound1;
		mRealPlayer = MediaPlayer.create(context, mediaToOpen);
	}
	
	@SuppressLint("NewApi")
	public SoundEngine(Context context, String filePath) {
		// Initialisation des lecteur 
		Uri mediaToOpen = Uri.parse(filePath);
		mRealPlayer = MediaPlayer.create(context, mediaToOpen);
	}
	
	/**
	 * Demarre ou stoppe la musique suivant au choix
	 * @param needToPlay T = Play, F = Pause
	 */
	public void playIfNeedToPlay(boolean needToPlay) {
		if(needToPlay) {
			mRealPlayer.start();
		}
		else {
			mRealPlayer.pause();
		}
		isPlaying = needToPlay;
	}
	
	/**
	 * Demarre ou stoppe la musique
	 */
	public void PlayOrPause() {
		playIfNeedToPlay(!isPlaying);
	}
	
	/**
	 * Destructeur
	 */
	public void onDestroy() {
		mRealPlayer.stop();
		mRealPlayer.release();
	}

	public int getCurrentMusicTime() {
		System.err.println( mRealPlayer.getCurrentPosition() );
		return mRealPlayer.getCurrentPosition()/10;
	}
}
