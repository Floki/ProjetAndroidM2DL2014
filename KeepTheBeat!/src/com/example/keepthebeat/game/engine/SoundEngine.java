package com.example.keepthebeat.game.engine;

import java.io.IOException;
import com.example.keepthebeat.R;
import com.example.keepthebeat.utils.Tools;
import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

/**
 * Contient tout le code pour la lecture et analyse de sons
 * @author Florian
 *
 */
public class SoundEngine {

	// Lit la musique
	private MediaPlayer mRealPlayer; 
	
	// Chemin vers le media
	private String mediaPath;

	// Lecture en cour
	private boolean isPlaying;
	
	@SuppressLint("NewApi")
	public SoundEngine(Context context) {
		// Initialisation des lecteur 
		int mediaToOpen = 0;
		mediaToOpen = R.raw.defaultsound;
		mRealPlayer = MediaPlayer.create(context, mediaToOpen);
	}
	
	@SuppressLint("NewApi")
	public SoundEngine(Context context, String filePath) {
		// Initialisation des lecteur 
		Uri mediaToOpen = Uri.parse(filePath);
		mRealPlayer = MediaPlayer.create(context, mediaToOpen);
		mediaPath = filePath;
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
		return mRealPlayer.getCurrentPosition()/10;
	}
	
	public void changeMediaPlayed(String mediaPath) {
		try {
			Tools.log(this, "Want to play " + mediaPath);
			mRealPlayer.stop();
			mRealPlayer.reset();
			mRealPlayer.setDataSource(mediaPath);
			this.mediaPath = mediaPath;
			mRealPlayer.prepare();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getMediaFileName() {
		return mediaPath.substring(mediaPath.lastIndexOf("/") + 1);
	}
	
	public String getMediaPath() {
		return mediaPath;
	}
	
	public String getMediaFolder() {
		return mediaPath.substring(0, mediaPath.lastIndexOf("/"));
	}
}
