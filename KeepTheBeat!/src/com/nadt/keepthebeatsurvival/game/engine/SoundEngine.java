package com.nadt.keepthebeatsurvival.game.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import com.nadt.keepthebeatsurvival.R;
import com.nadt.keepthebeatsurvival.utils.Constants;
import com.nadt.keepthebeatsurvival.utils.Tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.media.audiofx.Visualizer.OnDataCaptureListener;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

/**
 * Contient tout le code pour la lecture et analyse de sons
 * @author Florian
 *
 */
public class SoundEngine {

	// Lit la musique silencieusement en avance
	private MediaPlayer mWitnessPlayer;
	private int witnessAdvance;
	// Lit la musique
	private MediaPlayer mRealPlayer; 
	
	// Chemin vers le media
	private String mediaPath;

	// Lecture en cour
	private boolean isPlaying;
	
	// Volume
	private float volume;
	
	// Amplitude
	private double amplitude;
	
	// Récupère les informations sur la musique
	private Visualizer mVisualizer;
	private Equalizer mEqualizer;
	
	private LinkedList<Double> lastAmplitudes;
	
	ArrayList<String> phoneMediasPath = new ArrayList<String>();  

	
	@SuppressLint("NewApi")
	public SoundEngine(Activity activity, Context context) {
		// Initialisation des lecteur 
		int mediaToOpen = 0;
		witnessAdvance = 0;
		lastAmplitudes = new LinkedList<Double>();
		mediaToOpen = R.raw.defaultsound;
		mRealPlayer = MediaPlayer.create(context, mediaToOpen);
		mWitnessPlayer = MediaPlayer.create(context, mediaToOpen);
		// Coupe le volume du lecteur témoin, sevira seulement à récupérer les infos
		mWitnessPlayer.setVolume(0, 0);
		if(Build.VERSION.RELEASE.contains("4.4")) {
			mWitnessPlayer.setVolume(0.1f, 0.1f);
		}
		volume = 1;
		mRealPlayer.setVolume(volume, volume);
		linkVisualizerAndEqualizer();
		String[] proj = { MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DURATION };  
		Cursor musicCursor = activity.managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,proj, null, null, null);  
		if(musicCursor == null) {
			return;
		}
		if(musicCursor.moveToFirst()) {  
			do {  
				if(musicCursor.getString(1) != null) {
					if(Integer.parseInt(musicCursor.getString(1)) > 60 * 1000) {
						phoneMediasPath.add(musicCursor.getString(0));
					}
				}
			}  
			while(musicCursor.moveToNext());  
		} 
	}
	
	/**
	 * Demarre ou stoppe la musique suivant au choix
	 * @param needToPlay T = Play, F = Pause
	 */
	public void playIfNeedToPlay(boolean needToPlay) {
		if(needToPlay) {
			mRealPlayer.start();
			setWitnessPlayerInFuture(witnessAdvance);
			if(!mWitnessPlayer.isPlaying() && mRealPlayer.isPlaying()) {
				mWitnessPlayer.start();
			}
		}
		else {
			mRealPlayer.pause();
		}
		isPlaying = needToPlay;
	}
	
	/**
	 * Place le lecteur temoin avec x milliseconde d'avance et le lance
	 */
	public void setWitnessPlayerInFuture(int timeInFuture) {
		if(!mWitnessPlayer.isPlaying() && mRealPlayer.isPlaying()) {
			mWitnessPlayer.start();
		}
		if(timeInFuture == witnessAdvance) {
			return;
		}
		mWitnessPlayer.seekTo(mRealPlayer.getCurrentPosition() + timeInFuture);
		witnessAdvance = timeInFuture;
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
	@SuppressLint("NewApi")
	public void onDestroy() {
		mRealPlayer.stop();
		mRealPlayer.release();
		mWitnessPlayer.stop();
		mWitnessPlayer.release();
		mVisualizer.release();
		mEqualizer.release();
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
			volume = 1;
			mRealPlayer.setVolume(volume, volume);
			mWitnessPlayer.stop();
			mWitnessPlayer.reset();
			mWitnessPlayer.setDataSource(mediaPath);
			this.mediaPath = mediaPath;
			mWitnessPlayer.prepare();
			mWitnessPlayer.setVolume(0, 0);
			if(Build.VERSION.RELEASE.contains("4.4")) {
				mWitnessPlayer.setVolume(0.1f, 0.1f);
			}		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public void playRandomMedia(Activity activity) {
		changeMediaPlayed(phoneMediasPath.get((int) (phoneMediasPath.size() * Math.random())));
		playIfNeedToPlay(true);
	}
	
	public void seekToRandomPosition() {
		int minimunPosition = Math.min(mRealPlayer.getDuration(), 30000);
		int maximunPosition = Math.max(mRealPlayer.getDuration() - 30000, 30000);
		mRealPlayer.seekTo((int) (minimunPosition + (Math.random() * (maximunPosition - minimunPosition))));
		setWitnessPlayerInFuture(witnessAdvance);
		
	}

	@SuppressLint("NewApi")
	public void linkVisualizerAndEqualizer() {
		// On initialise le nécessaire pour récupérer les informations 
		// sur le son joué
		mEqualizer = new Equalizer(0, mWitnessPlayer.getAudioSessionId());
		mEqualizer.setEnabled(true); // need to enable equalizer
		mVisualizer = new Visualizer(mWitnessPlayer.getAudioSessionId());
		mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
		// Lors de la lecture on récupère les informations
		// sur le fichier lu
		mVisualizer.setDataCaptureListener(new OnDataCaptureListener() {
			@Override
			public void onWaveFormDataCapture(Visualizer visualizer,
					byte[] bytes, int samplingRate) {}

			@Override
			public void onFftDataCapture(Visualizer visualizer, byte[] bytes,
					int samplingRate) {
				// On calcule l'amplitude (Merci Google)
				int mDivisions = 1;
				double amplitudeTmp = 0;
				for (int i = 0; i < (bytes.length - 1) / mDivisions; i++) {
					byte rfk = bytes[mDivisions * i];
					byte ifk = bytes[mDivisions * i + 1];
					float magnitude = (rfk * rfk + ifk * ifk);
					double dbValue = (int) (10 * Math.log10(magnitude));
					if (dbValue > 0) {
						amplitudeTmp += dbValue;
					}
				}
				amplitude = amplitudeTmp;
				// Et on la transfère au moteur du jeux
				LinkedList<Double> lastAmplitudesTmp = new LinkedList<Double>(lastAmplitudes);

				lastAmplitudesTmp.addLast(amplitude);
				if(lastAmplitudesTmp.size() > 20) {
					lastAmplitudesTmp.removeFirst();
				}
				lastAmplitudes = lastAmplitudesTmp;
			}

		}, Visualizer.getMaxCaptureRate(), true, true);
		mVisualizer.setEnabled(true);
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

	public void setVolume(float volume) {
		this.volume = Math.max(Math.min(1, volume),0);
		mRealPlayer.setVolume(volume, volume);
	}
	
	public float getVolume() {
		return this.volume;
	}
	
	public double getAmplitude() {
		LinkedList<Double> lastAmplitudesTmp = new LinkedList<Double>(lastAmplitudes);
		double lastAverageSongAmplitude = 0;
		for(double amplitudeTmp : lastAmplitudesTmp) {
			lastAverageSongAmplitude += amplitudeTmp;
		}
		lastAverageSongAmplitude /= lastAmplitudesTmp.size();
		Tools.log(this, "Getted amplitude : " + lastAverageSongAmplitude);
		return lastAverageSongAmplitude;
	}
}
