package com.example.keepthebeat.game.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import android.graphics.Color;

import com.example.keepthebeat.CustomActivity;
import com.example.keepthebeat.game.Game;
import com.example.keepthebeat.game.Pattern;
import com.example.keepthebeat.game.Score;
import com.example.keepthebeat.game.shape.DancingGuyShape;
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
	private CustomActivity gameActivity;
	// Current percentage
	private int initialNumberOfShapes;
	private static float currentPercent;
	private ArrayList<DancingGuyShape> dancingGuysShapes;
	// Animation time
	private long lastAnimationTime;
	
	/**
	 * Constructeur
	 */
	public GameEngine( SoundEngine soundEngine ) {
		this.soundEngine = soundEngine;
		lastAnimationTime = 0;
		score = new Score();
		Constants.score = 0;
		userIsTouching = false;
		patternMap = new TreeMap<Long,Pair<Integer, Integer>>();
		gameWidth = Game.screenWidth;
		gameHeight = Game.screenHeight;
		actionners = new ArrayList<GameShape>();
		endLoop = false;
		initialNumberOfShapes = 0;
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
		long showTimer = (Constants.mode == Constants.Mode.CREATE ? 1 : Game.level.getShowTimer() );
		GameShape beatShape = new GameShape(showTimer,Game.level.getHideTimer());
		beatShape.setPosition((int)x, (int)y);
		actionners.add(beatShape);
	}
	
	public void engineLoop() {
		currentPercent = ((float)patternMap.size() / (float)initialNumberOfShapes) * 100;
		Tools.log(this, "Percent : " + currentPercent);
		if(endLoop) {
			soundEngine.setVolume((float) (soundEngine.getVolume() - 0.01));
			if(soundEngine.getVolume() <= 0.01) {
				reallyEnd = true;
			}
			for(DancingGuyShape d: dancingGuysShapes) {
				d.setAnimation(0, 0, 0, 0);
			}
		}
		else {
			if(Constants.mode == Constants.Mode.PLAY) {
				playLoop();
			}
			else if(Constants.mode == Constants.Mode.CREATE) {
				createLoop();
			}
		}
	}
	
	/**
	 * Ce que le jeux fait régulierement en mode jeux
	 */
	private void playLoop() {

		List<GameShape> actionnersTmp = new ArrayList<GameShape>(actionners);
		if(soundEngine.getCurrentMusicTime() - lastAnimationTime > 50) {
			updateDancingGuyAnimation();
			lastAnimationTime = soundEngine.getCurrentMusicTime();
		}
		// Calcule l'animation des gameshapes
		for(GameShape actionner : actionnersTmp) {
			actionner.hideMore();
			if(userIsTouching && !actionner.isExploding()) {
				int distance = (int) Math.sqrt((actionner.getX() - userTouchX) * (actionner.getX() - userTouchX) 
											 + (actionner.getY() - userTouchY) * (actionner.getY() - userTouchY));
				if(distance < actionner.getHeight() / 2 && distance < actionner.getWidth()) {
					score.computeScoreFromShape( actionner );
					actionner.hideAndExplode();
				}
			}
			if(!actionner.stillUse()) {
				actionners.remove(actionner);
				score.computeScoreFromShape( actionner );
				actionner = null;
			}
		}
		// Partage le pattern pour qu'il soit déssiné
		Constants.pattern = actionners;
		// Recupère les shape à afficher la prochaine fois
		long currentMusicTime = soundEngine.getCurrentMusicTime() + (int) Game.level.getShowTimer() / 10;
		SortedMap<Long,Pair<Integer,Integer>> subMap = patternMap.subMap(Math.min(lastComputedTime,currentMusicTime), currentMusicTime);
		if(subMap.size() > 0) {
			Tools.log(this, "SE Time : " + soundEngine.getCurrentMusicTime() + " A Time : " + subMap.keySet().toArray()[0].toString());
			addGameShapes(subMap.values());
		}
		lastComputedTime = currentMusicTime;
		patternMap = new TreeMap<Long, Pair<Integer, Integer>>(patternMap.tailMap(currentMusicTime));
		if(patternMap.size() == 0 && actionners.size() == 0) {
			endLoop = true;
		}
	}
	
	/**
	 * Ce que le jeux fait régulierement en mode création
	 */
	private void createLoop() {
		List<GameShape> actionnersTmp = new ArrayList<GameShape>(actionners);
		for(GameShape actionner : actionnersTmp) {
			if(actionner != null) {
				if(!actionner.stillUse()) {
					actionners.remove(actionner);
					actionner = null;
				}
				else {
					actionner.hideMore();
				}
			}
		}
		Constants.pattern = actionners;

		lastComputedTime = soundEngine.getCurrentMusicTime();
	}

	/**
	 * Permet d'ajouter plusieurs shape
	 * @param values
	 */
	private void addGameShapes(Collection<Pair<Integer, Integer>> values) {
		for(Pair<Integer, Integer> position: values) {
			addGameShape(Game.virtualXToScreenX(position.getFirst()), Game.virtualYToScreenY(position.getSecond()));
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
	 * Save game shpae int he pattern in CREATION mode
	 * @param time
	 * @param x
	 * @param y
	 */
	public void saveShape(float time, float x, float y) {
		if(soundEngine.getCurrentMusicTime() > lastComputedTime + 100 ||
		Game.virtualXToScreenX(75) < Tools.distanceBetweenPosition(Float.valueOf(oldActionnerX).intValue(), 
										 Float.valueOf(oldActionnerY).intValue(), 
										 Float.valueOf(x).intValue(), 
										 Float.valueOf(y).intValue())) {
			int savedX = Game.screenXToVirtualX(Float.valueOf(x).intValue());
			int savedY = Game.screenYToVirtualY(Float.valueOf(y).intValue());
			patternMap.put((long)time, new Pair<Integer, Integer>(savedX,savedY));	
			addGameShape(x, y);
			oldActionnerX = x;
			oldActionnerY = y;
		}
	}
	
	/**
	 * Save the pattern in file
	 * @param filePath
	 */
	public void savePattern(String filePath, String musicName) {
		Tools.log(this, "Save file in path" + filePath);
		if(pattern == null) {
			pattern = new Pattern();
		}
		// Save pattern map
		pattern.setPattern(patternMap);
		// Save the msic file information
		pattern.setMusicFile(new MusicFile(musicName, soundEngine.getMediaPath()));
		System.out.println( musicName );
		System.out.println( soundEngine.getMediaPath() );
		// Save the pattern name and pattern file path
		pattern.setPatternName(filePath.substring(filePath.lastIndexOf("/") + 1));
		pattern.setPatternPath(filePath.substring(0, filePath.lastIndexOf("/") - 1));
		FileAccess.serialize(pattern, filePath);
	}
	
	/**
	 * Retrieve pattern information
	 * @param fileName
	 */
	public void loadPattern(String fileName) {
		// Retrieve information
		if(!fileName.equals("default")) {
			pattern = (Pattern)FileAccess.deserialize(fileName);
		}
		else {
			Tools.log(this,  "Load default pattern");
			pattern = Constants.defaultPattern;
		}
		Tools.log(pattern,pattern);
		// Retrieve pattern map
		patternMap = pattern.getPattern();
		// Reload music
		if(!fileName.equals("default")) {
			soundEngine.changeMediaPlayed(pattern.getMusicFile().getPath());
			soundEngine.playIfNeedToPlay(true);
		}
		initialNumberOfShapes = patternMap.size();
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
	
	public static float getPatternPercent() {
		return currentPercent;
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
