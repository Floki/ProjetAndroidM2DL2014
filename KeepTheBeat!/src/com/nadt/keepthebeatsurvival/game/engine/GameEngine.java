package com.nadt.keepthebeatsurvival.game.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.SortedMap;
import java.util.TreeMap;

import android.app.Activity;
import android.graphics.Color;

import com.nadt.keepthebeatsurvival.CustomActivity;
import com.nadt.keepthebeatsurvival.game.Game;
import com.nadt.keepthebeatsurvival.game.Score;
import com.nadt.keepthebeatsurvival.game.shape.DancingGuyShape;
import com.nadt.keepthebeatsurvival.game.shape.GameShape;
import com.nadt.keepthebeatsurvival.utils.Constants;
import com.nadt.keepthebeatsurvival.utils.FileAccess;
import com.nadt.keepthebeatsurvival.utils.Pair;
import com.nadt.keepthebeatsurvival.utils.Tools;

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
	// Amplitude information
	private double maxSongAmplitude;
	private double actionnerMoveMinSpeed;
	private double lastAmplitude;
	private double lastGettedAmplitude;
	// All needed to compute gameshape postion
	private float actionnerX;
	private float actionnerY;
	private float actionnerMoveX;
	private float actionnerMoveY;
	// Music duration before change
	public static final int durationOfASample = 500;
	public static int timeBeforeChangeMusic = durationOfASample;
	// Life
	public static final int maxLife = 100;
	public static int life = maxLife;
	// Debut partie
	public long beginTime;
	public ArrayList<DancingGuyShape> dancingGuysShapes;

	
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
		gameWidth = Game.screenWidth;
		gameHeight = Game.screenHeight;
		actionners = new ArrayList<GameShape>();
		Constants.pattern = actionners;
		endLoop = false;
		reallyEnd = false;
		actionnerX = Game.screenWidth / 2;
		actionnerY = Game.screenHeight / 2;
		actionnerMoveMinSpeed = 7;
		actionnerMoveX = (float) ((Math.random() - 0.5) * actionnerMoveMinSpeed * 10);
		actionnerMoveY = (float) ((Math.random() - 0.5) * actionnerMoveMinSpeed * 10);
		beginTime = System.currentTimeMillis();
		dancingGuysShapes = new ArrayList<DancingGuyShape>();
		// Background dancing guy
		dancingGuysShapes.add(new DancingGuyShape(Game.virtualXToScreenX(100), 
												  Game.virtualYToScreenY(630), 
												  Game.virtualXToScreenX(25), 
												  Game.virtualYToScreenY(30), 
												  Color.DKGRAY, 
												  Color.RED, 
												  Color.BLACK));
		dancingGuysShapes.add(new DancingGuyShape(Game.virtualXToScreenX(900), 
												  Game.virtualYToScreenY(630), 
												  Game.virtualXToScreenX(25), 
												  Game.virtualYToScreenY(30), 
												  Color.DKGRAY, 
												  Color.RED, 
												  Color.BLACK));
		// Middleground dancing guy
		dancingGuysShapes.add(new DancingGuyShape(Game.virtualXToScreenX(300), 
				  Game.virtualYToScreenY(650), 
				  Game.virtualXToScreenX(30), 
				  Game.virtualYToScreenY(40), 
				  Color.DKGRAY, 
				  Color.MAGENTA, 
				  Color.BLACK));
		dancingGuysShapes.add(new DancingGuyShape(Game.virtualXToScreenX(700), 
				  Game.virtualYToScreenY(650), 
				  Game.virtualXToScreenX(30), 
				  Game.virtualYToScreenY(40), 
				  Color.DKGRAY, 
				  Color.MAGENTA, 
				  Color.BLACK));
		// Center guy
		dancingGuysShapes.add(new DancingGuyShape(Game.virtualXToScreenX(500), 
				  Game.virtualYToScreenY(666), 
				  Game.virtualXToScreenX(60), 
				  Game.virtualYToScreenY(50), 
				  Color.DKGRAY, 
				  Color.YELLOW, 
				  Color.BLACK));
	}
	
	/**
	 * Add a game shape in the scene to draw
	 * @param x
	 * @param y
	 */
	public void addGameShape( float x, float y) {
		if(System.currentTimeMillis() > lastComputedTime + 100 ||
				Game.virtualXToScreenX(75) < Tools.distanceBetweenPosition(new Float(oldActionnerX).intValue(), 
												 new Float(oldActionnerY).intValue(), 
												 new Float(x).intValue(), 
												 new Float(y).intValue())) {
			long showTimer = Game.level.getShowTimer();
			GameShape beatShape = new GameShape(showTimer,Game.level.getHideTimer());
			beatShape.setPosition((int)x, (int)y);
			actionners.add(beatShape);
			oldActionnerX = x;
			oldActionnerY = y;
		}
		lastComputedTime = System.currentTimeMillis();
	}
	
	/**
	 * Game engine Loop
	 */
	public void engineLoop() {
		timeBeforeChangeMusic--;
		if(timeBeforeChangeMusic > durationOfASample - 30) {
			Tools.log(this, "Augmente volume");
			float transitionVolume = (float)Math.min(durationOfASample - timeBeforeChangeMusic, 30);
			transitionVolume = transitionVolume / 30;
			Tools.log(this, "Volume : " + transitionVolume);
			soundEngine.setVolume((float)transitionVolume);
		}
		if(timeBeforeChangeMusic < 30) {
			soundEngine.setVolume((float)timeBeforeChangeMusic / 30);
		}
		if(timeBeforeChangeMusic <= 0) {
			if(Game.level.getDifficulty() == Constants.EASY) {
				Game.level.setShowTimer((long) (Game.level.getShowTimer() * 0.95));
				Game.level.setShowTimer(Game.level.getShowTimer() - (System.currentTimeMillis() - beginTime)/50000);
			}
			else if(Game.level.getDifficulty() == Constants.NORMAL) {
				Game.level.setShowTimer((long) (Game.level.getShowTimer() * 0.93));
				Game.level.setShowTimer(Game.level.getShowTimer() - (System.currentTimeMillis() - beginTime)/10000);
			}
			if(Game.level.getDifficulty() == Constants.HARD) {
				Game.level.setShowTimer((long) (Game.level.getShowTimer() * 0.90));
				Game.level.setShowTimer(Game.level.getShowTimer() - (System.currentTimeMillis() - beginTime)/7500);

			}
			
			timeBeforeChangeMusic = durationOfASample;
			soundEngine.playRandomMedia(gameActivity);
			soundEngine.seekToRandomPosition();
		}
		soundEngine.setWitnessPlayerInFuture((int) Game.level.getShowTimer());
		computeNextActionnerPosition();
		if(endLoop) {
			Tools.log(this, "End of the game");
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
			updateDancingGuyAnimation();
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
			actionnerMoveX = (float) (actionnerMoveMinSpeed + (Math.random() + 0.1) * actionnerMoveMinSpeed * 6) + 1;
		}
		else if(this.actionnerX > gameWidth) {
			actionnerMoveX = (float) (-actionnerMoveMinSpeed + (Math.random() - 1.01) * actionnerMoveMinSpeed * 6) - 1;
		}
		if(this.actionnerY <= Game.topMargin + 5) {
			actionnerMoveY = (float) (actionnerMoveMinSpeed + (Math.random() + 0.1) * actionnerMoveMinSpeed * 6) + 1;
		}
		else if(this.actionnerY > gameHeight - Game.topMargin - 5) {
			actionnerMoveY = (float) (-actionnerMoveMinSpeed + (Math.random() - 1.01) * actionnerMoveMinSpeed * 6) - 1;
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

	public List<DancingGuyShape> getDancingGuy() {
		return dancingGuysShapes;
	}
	
	public void updateDancingGuyAnimation() {
		int brasG = (int) (Math.random() * 3);
		int brasD = (int) (Math.random() * 3);
		int jambeG = (int) (Math.random() * 2);
		int jambeD = (int) (Math.random() * 2);
		for(DancingGuyShape d: dancingGuysShapes) {
			d.setAnimation(brasG, brasD, jambeG, jambeD);
		}
	}
}
