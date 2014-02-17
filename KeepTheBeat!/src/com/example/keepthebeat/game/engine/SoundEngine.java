package com.example.keepthebeat.game.engine;

import java.io.IOException;
import com.example.keepthebeat.R;
import com.example.keepthebeat.utils.Constants;
import com.example.keepthebeat.utils.Tools;
import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.media.audiofx.Visualizer.OnDataCaptureListener;
import android.net.Uri;

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
	
	@SuppressLint("NewApi")
	public SoundEngine(Context context) {
		// Initialisation des lecteur 
		int mediaToOpen = 0;
		witnessAdvance = 0;
		mediaToOpen = R.raw.defaultsound;
		mRealPlayer = MediaPlayer.create(context, mediaToOpen);
		mWitnessPlayer = MediaPlayer.create(context, mediaToOpen);
		// Coupe le volume du lecteur témoin, sevira seulement à récupérer les infos
		mWitnessPlayer.setVolume(0, 0);
		volume = 1;
		mRealPlayer.setVolume(volume, volume);
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
				// Et on la transfère au moteur du jeux
				Tools.log(this, amplitudeTmp);
				amplitude = amplitudeTmp;
			}

		}, Visualizer.getMaxCaptureRate(), true, true);
		mVisualizer.setEnabled(true);
	}
	
	@SuppressLint("NewApi")
	public SoundEngine(Context context, String filePath) {
		// Initialisation des lecteur 
		Uri mediaToOpen = Uri.parse(filePath);
		mRealPlayer = MediaPlayer.create(context, mediaToOpen);
		mediaPath = filePath;
		volume = 1;
		mRealPlayer.setVolume(volume, volume);
	}
	
	/**
	 * Demarre ou stoppe la musique suivant au choix
	 * @param needToPlay T = Play, F = Pause
	 */
	public void playIfNeedToPlay(boolean needToPlay) {
		if(needToPlay) {
			mRealPlayer.start();
			setWitnessPlayerInFuture(witnessAdvance);
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
		mWitnessPlayer.seekTo(mRealPlayer.getCurrentPosition() + timeInFuture);
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
			volume = 1;
			mRealPlayer.setVolume(volume, volume);
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

	public void setVolume(float volume) {
		this.volume = Math.max(Math.min(1, volume),0);
		mRealPlayer.setVolume(volume, volume);
	}
	
	public float getVolume() {
		return this.volume;
	}
	
	public double getAmplitude() {
		return amplitude;
	}
}
