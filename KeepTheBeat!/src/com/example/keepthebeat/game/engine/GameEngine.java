package com.example.keepthebeat.game.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
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
	
	/**
	 * Constructeur
	 */
	public GameEngine( SoundEngine soundEngine ) {
		this.soundEngine = soundEngine;
		score = new Score();
		Constants.score = 0;
		userIsTouching = false;
		patternMap = new TreeMap<Long,Pair<Integer, Integer>>();
		gameWidth = Game.screenWidth;
		gameHeight = Game.screenHeight;
		actionners = new ArrayList<GameShape>();
	}
	
	/**
	 * Add a game shape in the scene to draw
	 * @param x
	 * @param y
	 */
	public void addGameShape( float x, float y) {
		long showTimer = (Constants.mode == Constants.Mode.CREATE ? 1 : Constants.SHOW_TIMER);
		GameShape beatShape = new GameShape(showTimer,Constants.HIDE_TIMER);
		beatShape.setPosition((int)x, (int)y);
		actionners.add(beatShape);
	}
	
	public void engineLoop() {
		if(Constants.mode == Constants.Mode.PLAY) {
			playLoop();
		}
		else if(Constants.mode == Constants.Mode.CREATE) {
			createLoop();
		}
	}
	
	/**
	 * Ce que le jeux fait régulierement en mode jeux
	 */
	private void playLoop() {
		List<GameShape> actionnersTmp = new ArrayList<GameShape>(actionners);
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
		long currentMusicTime = soundEngine.getCurrentMusicTime() + (int) Constants.SHOW_TIMER / 10;
		SortedMap<Long,Pair<Integer,Integer>> subMap = patternMap.subMap(Math.min(lastComputedTime,currentMusicTime), currentMusicTime);
		if(subMap.size() > 0) {
			Tools.log(this, "SE Time : " + soundEngine.getCurrentMusicTime() + " A Time : " + subMap.keySet().toArray()[0].toString());
			addGameShapes(subMap.values());
		}
		lastComputedTime = currentMusicTime;
		patternMap = new TreeMap<Long, Pair<Integer, Integer>>(patternMap.tailMap(currentMusicTime));
	}
	
	/**
	 * Ce que le jeux fait régulierement en mode création
	 */
	private void createLoop() {
		List<GameShape> actionnersTmp = new ArrayList<GameShape>(actionners);
		for(GameShape actionner : actionnersTmp) {
			if(!actionner.stillUse()) {
				actionners.remove(actionner);
				actionner = null;
			}
			else {
				actionner.hideMore();
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
		Game.virtualXToScreenX(75) < Tools.distanceBetweenPosition(new Float(oldActionnerX).intValue(), 
										 new Float(oldActionnerY).intValue(), 
										 new Float(x).intValue(), 
										 new Float(y).intValue())) {
			int savedX = Game.screenXToVirtualX(new Float(x).intValue());
			int savedY = Game.screenYToVirtualY(new Float(y).intValue());
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
	public void savePattern(String filePath) {
		Tools.log(this, "Save file in path" + filePath);
		if(pattern == null) {
			pattern = new Pattern();
		}
		// Save pattern map
		pattern.setPattern(patternMap);
		// Save the msic file information
		pattern.setMusicFile(new MusicFile(soundEngine.getMediaFileName(), soundEngine.getMediaPath()));
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
		pattern = (Pattern)FileAccess.deserialize(fileName);
		Tools.log(pattern,pattern);
		// Retrieve pattern map
		patternMap = pattern.getPattern();
		// Reload music
		soundEngine.changeMediaPlayed(pattern.getMusicFile().getPath());
		soundEngine.playIfNeedToPlay(true);
	}

	public SoundEngine getSoundEngine() {
		return this.soundEngine;
	}
}
