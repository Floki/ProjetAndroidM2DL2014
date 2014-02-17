package com.example.keepthebeat.game.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.SortedMap;
import java.util.TreeMap;

import android.app.Activity;

import com.example.keepthebeat.CustomActivity;
import com.example.keepthebeat.game.Game;
import com.example.keepthebeat.game.Pattern;
import com.example.keepthebeat.game.Score;
import com.example.keepthebeat.game.shape.GameShape;
import com.example.keepthebeat.music.MusicFile;
import com.example.keepthebeat.utils.Constants;
import com.example.keepthebeat.utils.FileAccess;
import com.example.keepthebeat.utils.Pair;
import com.example.keepthebeat.utils.Tools;

public class GameEngine {
	
	// Position du touché de l'utilisateur
	private float userTouchX;
	private float userTouchY;
	private boolean userIsTouching;
	// Taille de l'aire de jeu
	private int gameWidth;
	private int gameHeight;
	// Liste des actionneurs à afficher
	private List<GameShape> actionners;
	// Position des dernières shape
	private float oldActionnerX;
	private float oldActionnerY;
	// Informations sur le pattern
	private SortedMap<Long, Pair<Integer, Integer>> patternMap;
	private Pattern pattern;
	private long lastComputedTime = 0;
	// Score
	public static Score score;
	// Moteur de lecture de musique
	private SoundEngine soundEngine;
	// If the game is going to end
	private boolean endLoop;
	private boolean reallyEnd;
	// Game activity
	private Activity gameActivity;
	private double maxSongAmplitude;
	private double actionnerMoveMinSpeed;
	private double lastAmplitude;
	private float actionnerX;
	private float actionnerY;
	private float actionnerMoveX;
	private float actionnerMoveY;
	public static final int durationOfASample = 500;
	public static int timeBeforeChangeMusic = durationOfASample;
	private double lastGettedAmplitude;
	public static final int maxLife = 100;
	public static int life = maxLife;

	
	/**
	 * Constructeur
	 */
	public GameEngine( Activity activity, SoundEngine soundEngine ) {
		this.soundEngine = soundEngine;
		this.gameActivity = activity;
		lastGettedAmplitude = 0;
		life = maxLife;
		timeBeforeChangeMusic = durationOfASample;
		score = new Score();
		Constants.score = 0;
		userIsTouching = false;
		patternMap = new TreeMap<Long,Pair<Integer, Integer>>();
		gameWidth = Game.screenWidth;
		gameHeight = Game.screenHeight;
		actionners = new ArrayList<GameShape>();
		endLoop = false;
		actionnerX = Game.screenWidth / 2;
		actionnerY = Game.screenHeight / 2;
		actionnerMoveMinSpeed = 7;
		actionnerMoveX = (float) ((Math.random() - 0.5) * actionnerMoveMinSpeed * 10);
		actionnerMoveY = (float) ((Math.random() - 0.5) * actionnerMoveMinSpeed * 10);
	}
	
	/**
	 * Add a game shape in the scene to draw
	 * @param x
	 * @param y
	 */
	public void addGameShape( float x, float y) {
		if(soundEngine.getCurrentMusicTime() > lastComputedTime + 100 ||
				Game.virtualXToScreenX(75) < Tools.distanceBetweenPosition(new Float(oldActionnerX).intValue(), 
												 new Float(oldActionnerY).intValue(), 
												 new Float(x).intValue(), 
												 new Float(y).intValue())) {
			long showTimer = (Constants.mode == Constants.Mode.CREATE ? 1 : Game.level.getShowTimer() );
			GameShape beatShape = new GameShape(showTimer,Game.level.getHideTimer());
			beatShape.setPosition((int)x, (int)y);
			actionners.add(beatShape);
			oldActionnerX = x;
			oldActionnerY = y;
		}
		lastComputedTime = soundEngine.getCurrentMusicTime();
	}
	
	/**
	 * Game engine Loop
	 */
	public void engineLoop() {
		timeBeforeChangeMusic--;
		if(timeBeforeChangeMusic <= 0) {
			if(Game.level.getDifficulty() == Constants.EASY) {
				Game.level.setShowTimer((long) (Game.level.getShowTimer() * 0.95));
			}
			else if(Game.level.getDifficulty() == Constants.NORMAL) {
				Game.level.setShowTimer((long) (Game.level.getShowTimer() * 0.93));
			}
			if(Game.level.getDifficulty() == Constants.HARD) {
				Game.level.setShowTimer((long) (Game.level.getShowTimer() * 0.90));
			}
			
			timeBeforeChangeMusic = durationOfASample;
			soundEngine.playRandomMedia(gameActivity);
			soundEngine.seekToRandomPosition();
		}
		soundEngine.setWitnessPlayerInFuture((int) Game.level.getShowTimer());
		computeNextActionnerPosition();
		if(endLoop) {
			List<GameShape> actionnersTmp = new ArrayList<GameShape>(actionners);
			for(GameShape actionner : actionnersTmp) {
				actionner.hideMore();
			}
			soundEngine.setVolume((float) (soundEngine.getVolume() - 0.01));
			if(soundEngine.getVolume() <= 0.01) {
				reallyEnd = true;
			}
		}
		else {
			computePlayerActionFromTheAmplitude(soundEngine.getAmplitude());
			List<GameShape> actionnersTmp = new ArrayList<GameShape>(actionners);
			// Calcule l'animation des gameshapes
			for(GameShape actionner : actionnersTmp) {
				actionner.hideMore();
				if(userIsTouching && !actionner.isExploding()) {
					int distance = (int) Math.sqrt((actionner.getX() - userTouchX) * (actionner.getX() - userTouchX) 
												 + (actionner.getY() - userTouchY) * (actionner.getY() - userTouchY));
					if(distance < actionner.getHeight() / 2 && distance < actionner.getWidth()) {
						if(actionner.notGoodMomentAfterDisplay()) {
							life--;
						}
						score.computeScoreFromShape( actionner );
						actionner.hideAndExplode();
					}
				}
				if(!actionner.stillUse()) {
					actionners.remove(actionner);
					score.computeScoreFromShape( actionner );
					if(!actionner.isExploding()) { 
						life--;
					}
					actionner = null;
				}
			}

			// Partage le pattern pour qu'il soit déssiné
			Constants.pattern = actionners;
			if(life <= 0) {
				endLoop = true;
				life = 0;
			}
		}
	}

	/**
	 * Indique où l'utilisateur appuie
	 */
	public void setUserTouchPosition(float x, float y) {
		userTouchX = x;
		userTouchY = y;
	}
	
	/**
	 * Indique si l'utilisateur appuie
	 * @param touch
	 */
	public void isTouching(boolean touch) {
		userIsTouching = touch;
	}
	
	/**
	 * Permet de savoir si le son joué nécessite une action du joueur
	 * @param amplitude Amplitude du son joué
	 */
	public void computePlayerActionFromTheAmplitude(double amplitude) {
		// On récupère l'amplitude maximale du son joué
		maxSongAmplitude = Math.max(maxSongAmplitude, amplitude);
		// Si le son est un son fort 
		// OU si l'on est sur un pente ascendant
		// Une action du joueur est requise
		if(amplitude > lastGettedAmplitude * 1.01) {
			// On dessine une image
			addGameShape(actionnerX, actionnerY);
		}
		// Si le son se calme un instant, on déclenche un évènement
		else if(amplitude < lastGettedAmplitude * 0.2) {
			actionnerX = (float) (Math.random() * gameWidth);
			actionnerY = (float) (Math.random() * gameHeight);
		}
		actionnerMoveMinSpeed = Math.max(lastGettedAmplitude / amplitude, 1) * 3;
		// Mémorise l'ancienne amplitude
		lastGettedAmplitude = amplitude;
	}
	
	/**
	 * Compute the next position of an actionner
	 */
	public void computeNextActionnerPosition() {
		actionnerX += this.actionnerMoveX;
		actionnerY += this.actionnerMoveY;
		if(this.actionnerX < 0) {
			actionnerMoveX = (float) (actionnerMoveMinSpeed + (Math.random() + 0.1) * actionnerMoveMinSpeed * 6);
		}
		else if(this.actionnerX > gameWidth) {
			actionnerMoveX = (float) (-actionnerMoveMinSpeed + (Math.random() - 1.01) * actionnerMoveMinSpeed * 6);
		}
		if(this.actionnerY < 0) {
			actionnerMoveY = (float) (actionnerMoveMinSpeed + (Math.random() + 0.1) * actionnerMoveMinSpeed * 6);
		}
		else if(this.actionnerY > gameHeight) {
			actionnerMoveY = (float) (-actionnerMoveMinSpeed + (Math.random() - 1.01) * actionnerMoveMinSpeed * 6);
		}
		if(actionnerMoveY < 1 && actionnerMoveY >= 0) {
			actionnerMoveY = 1;
		}
		if(actionnerMoveY > -1 && actionnerMoveY <= 0) {
			actionnerMoveY = -1;
		}
		if(actionnerMoveX < 1 && actionnerMoveX >= 0) {
			actionnerMoveX = 1;
		}
		if(actionnerMoveX > -1 && actionnerMoveX <= 0) {
			actionnerMoveX = -1;
		}
	}
	
	public SoundEngine getSoundEngine() {
		return this.soundEngine;
	}
	
	public boolean isEnded() {
		return reallyEnd;
	}

	public int getScore() {
		return score.getScore();
	}
	
	public Pattern getPattern() {
		return pattern;
	}
}
