package com.example.keepthebeat.game.engine;

import com.example.keepthebeat.R;
import com.example.keepthebeat.R.raw;
import com.example.keepthebeat.game.GameNotifier;

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
	//private MediaPlayer mWitnessPlayer; 
	// Lit la musique
	private static MediaPlayer mRealPlayer; 
	// R�cup�re les informations sur la musique
//	private Visualizer mVisualizer;
//	private Equalizer mEqualizer;
	// Lecture en cour
	private boolean isPlaying;
	
	@SuppressLint("NewApi")
	public SoundEngine(Context context) {
		// Initialisation des lecteur r�els et t�moins
		int mediaToOpen = 0;
		mediaToOpen = R.raw.testsound1;


		mRealPlayer = MediaPlayer.create(context, mediaToOpen);
//		mWitnessPlayer = MediaPlayer.create(context, mediaToOpen);
//		// Coupe le volume du lecteur t�moin, sevira seulement � r�cup�rer les infos
//		mWitnessPlayer.setVolume(0, 0);
		
		// On initialise le n�cessaire pour r�cup�rer les informations 
		// sur le son jou�
//		mEqualizer = new Equalizer(0, mWitnessPlayer.getAudioSessionId());
//		mEqualizer.setEnabled(true); // need to enable equalizer
//		mVisualizer = new Visualizer(mWitnessPlayer.getAudioSessionId());
//		mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
//		
//		// Lors de la lecture on r�cup�re les informations
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
//				// Et on la transf�re au moteur du jeux
//				sendToTheListenersTheStringAndTheParam("amplitude", new Double(amplitude));
//			}
//
//		}, Visualizer.getMaxCaptureRate(), true, true);
//		mVisualizer.setEnabled(true);
	}
	
	/**
	 * D�marre ou stoppe la musique suivant au choix
	 * @param needToPlay T = Play, F = Pause
	 */
	public void playIfNeedToPlay(boolean needToPlay) {
		if(needToPlay) {
			mRealPlayer.start();
//			mWitnessPlayer.seekTo(mRealPlayer.getCurrentPosition() + (int)Game.time);
//			mWitnessPlayer.start();
		}
		else {
			mRealPlayer.pause();
//			mWitnessPlayer.pause();
		}
		isPlaying = needToPlay;
	}
	
	/**
	 * D�marre ou stoppe la musique
	 */
	public void PlayOrPause() {
		playIfNeedToPlay(!isPlaying);
	}
	
	/**
	 * Destructeur
	 */
	public void onDestroy() {
		mRealPlayer.stop();
//		mWitnessPlayer.stop();
	}

	public static int getCurrentMusicTime() {
		return mRealPlayer.getCurrentPosition()/10;
	}
}
