package com.example.keepthebeat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
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
	
	// Lit la musique silencieusement en avance
	private MediaPlayer mWitnessPlayer; 
	// Lit la musique
	private MediaPlayer mRealPlayer; 
	// Récupère les informations sur la musique
//	private Visualizer mVisualizer;
//	private Equalizer mEqualizer;
	// Lecture en cour
	private boolean isPlaying;
	
	@SuppressLint("NewApi")
	public SoundEngine(Context context) {
		// Initialisation des lecteur réels et témoins
		int mediaToOpen = 0;
		if(Math.random() > 0.5) {
			mediaToOpen = R.raw.testsound1;
		}
		else {
			mediaToOpen = R.raw.testsound2;
		}
		mRealPlayer = MediaPlayer.create(context, mediaToOpen);
		mWitnessPlayer = MediaPlayer.create(context, mediaToOpen);
		// Coupe le volume du lecteur témoin, sevira seulement à récupérer les infos
		mWitnessPlayer.setVolume(0, 0);
		
		// On initialise le nécessaire pour récupérer les informations 
		// sur le son joué
//		mEqualizer = new Equalizer(0, mWitnessPlayer.getAudioSessionId());
//		mEqualizer.setEnabled(true); // need to enable equalizer
//		mVisualizer = new Visualizer(mWitnessPlayer.getAudioSessionId());
//		mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
//		
//		// Lors de la lecture on récupère les informations
//		// sur le fichier lu
//		mVisualizer.setDataCaptureListener(new OnDataCaptureListener() {
//			@Override
//			public void onWaveFormDataCapture(Visualizer visualizer,
//					byte[] bytes, int samplingRate) {}
//
//			@Override
//			public void onFftDataCapture(Visualizer visualizer, byte[] bytes,
//					int samplingRate) {
//				// On calcule l'amplitude (Merci Google)
//				int mDivisions = 1;
//				double amplitude = 0;
//				for (int i = 0; i < (bytes.length - 1) / mDivisions; i++) {
//					byte rfk = bytes[mDivisions * i];
//					byte ifk = bytes[mDivisions * i + 1];
//					float magnitude = (rfk * rfk + ifk * ifk);
//					double dbValue = (int) (10 * Math.log10(magnitude));
//					if (dbValue > 0) {
//						amplitude += dbValue;
//					}
//				}
//				// Et on la transfère au moteur du jeux
//				sendToTheListenersTheStringAndTheParam("amplitude", new Double(amplitude));
//			}
//
//		}, Visualizer.getMaxCaptureRate(), true, true);
//		mVisualizer.setEnabled(true);
	}
	
	/**
	 * Démarre ou stoppe la musique suivant au choix
	 * @param needToPlay T = Play, F = Pause
	 */
	public void playIfNeedToPlay(boolean needToPlay) {
		if(needToPlay) {
			mRealPlayer.start();
			mWitnessPlayer.seekTo(mRealPlayer.getCurrentPosition() + (int)Game.time);
			mWitnessPlayer.start();
		}
		else {
			mRealPlayer.pause();
			mWitnessPlayer.pause();
		}
		isPlaying = needToPlay;
	}
	
	/**
	 * Démarre ou stoppe la musique
	 */
	public void PlayOrPause() {
		playIfNeedToPlay(!isPlaying);
	}
	
	/**
	 * Destructeur
	 */
	public void onDestroy() {
		mRealPlayer.stop();
		mWitnessPlayer.stop();
	}

	public float getCurrentMusicTime() {
		return mRealPlayer.getCurrentPosition();
	}
	
}
