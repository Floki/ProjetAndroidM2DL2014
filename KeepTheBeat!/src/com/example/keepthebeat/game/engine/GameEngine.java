package com.example.keepthebeat.game.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.example.keepthebeat.game.Game;
import com.example.keepthebeat.game.GameListener;
import com.example.keepthebeat.game.GameNotifier;
import com.example.keepthebeat.game.shape.BeatShape;
import com.example.keepthebeat.game.shape.GameShape;
import com.example.keepthebeat.utils.Constants;
import com.example.keepthebeat.utils.FileAccess;
import com.example.keepthebeat.utils.Pair;
import com.example.keepthebeat.utils.Tools;

import android.R;
import android.os.Handler;
import android.util.Log;

public class GameEngine extends GameNotifier implements GameListener{
	
	// Conserve la puissance du dernier son jou�
	private double lastAmplitude;
	// Conserve la puissance maximale trouv� jusqu'� maintenant
	private double maxSongAmplitude;
	// Position du touch� de l'utilisateur
	private float userTouchX;
	private float userTouchY;
	private boolean userIsTouching;
	// Position courant de l'actionneur si on devait l'afficher
	private double actionnerX;
	private double actionnerY;
	private double oldActionnerX;
	private double oldActionnerY;
	// Variable n�cessaire au calcul de la prochaine position d'un futur actionneur
	private double actionnerMoveX;
	private double actionnerMoveY;
	private double actionnerMoveMinSpeed;
	// Taille de l'aire de jeu
	private int gameWidth;
	private int gameHeight;
	// Liste des actionneurs � afficher
	private List<GameShape> actionners;
	// Pattern line
	private SortedMap<Long, Pair<Integer, Integer>> pattern;
	private long lastComputedTime = 0;
	
	
	public GameEngine() {
		Constants.score = 0;
		userIsTouching = false;
		lastAmplitude = 0;
		maxSongAmplitude = 0;
		pattern = new TreeMap<Long,Pair<Integer, Integer>>();
		gameWidth = Game.screenWidth;
		gameHeight = Game.screenHeight;
		actionnerX = gameWidth / 2;
		actionnerY = gameHeight / 2;
		actionnerMoveMinSpeed = 5;
		actionnerMoveX = (Math.random() - 0.5) * actionnerMoveMinSpeed * 10;
		actionnerMoveY = (Math.random() - 0.5) * actionnerMoveMinSpeed * 10;
		actionners = new ArrayList<GameShape>();
	}
	
	public void addGameShape( float x, float y) {
		GameShape beatShape = new GameShape(Constants.showTimer,Constants.hideTimer);
		beatShape.setPosition((int)x, (int)y);
		actionners.add(beatShape);
	}
	
	/**
	 * Permet de savoir si le son jou� n�cessite une action du joueur
	 * @param amplitude Amplitude du son jou�
	 */
	public void computePlayerActionFromTheAmplitude(double amplitude) {
		// On r�cup�re l'amplitude maximale du son jou�
		maxSongAmplitude = Math.max(maxSongAmplitude, amplitude);
		// Si le son est un son fort 
		// OU si l'on est sur un pente ascendant
		// Une action du joueur est requise
		if(amplitude > 0.9 * maxSongAmplitude || (amplitude > 1300 && lastAmplitude > 1300 && amplitude > lastAmplitude * ((13000 - Math.min(amplitude/2, 3001))/10000))) {
			// On dessine une image
			BeatShape beatShape = new BeatShape();
			beatShape.setPosition((int)actionnerX, (int)actionnerY);
			actionners.add(beatShape);
		}
		else if(amplitude > 0.95 * maxSongAmplitude) {
			actionnerMoveMinSpeed = Math.min(actionnerMoveMinSpeed + 0.1,3);
		}
		// Si le son se calme un instant, on d�clenche un �v�nement
		else if(amplitude < maxSongAmplitude * 0.2) {
			actionnerX = Math.random() * gameWidth;
			actionnerY = Math.random() * gameHeight;
			actionnerMoveMinSpeed = Math.max(actionnerMoveMinSpeed - 1,2);
		}
		else {
			actionnerMoveMinSpeed = Math.max(actionnerMoveMinSpeed - 1,2);
		}
		// M�morise l'ancienne amplitude
		lastAmplitude = amplitude;
	}
	
	/**
	 * Compute the next position of an actionner
	 */
	public void computeNextActionnerPosition() {
		actionnerX += this.actionnerMoveX;
		actionnerY += this.actionnerMoveY;
		if(this.actionnerX < 0) {
			actionnerMoveX = actionnerMoveMinSpeed + (Math.random() + 0.1) * actionnerMoveMinSpeed * 10;
		}
		else if(this.actionnerX > gameWidth) {
			actionnerMoveX = -actionnerMoveMinSpeed + (Math.random() - 1.01) * actionnerMoveMinSpeed * 10;
		}
		if(this.actionnerY < 0) {
			actionnerMoveY = actionnerMoveMinSpeed + (Math.random() + 0.1) * actionnerMoveMinSpeed * 10;
		}
		else if(this.actionnerY > gameHeight) {
			actionnerMoveY = -actionnerMoveMinSpeed + (Math.random() - 1.01) * actionnerMoveMinSpeed * 10;
		}
	}

	@Override
	public void doSomethingCorrespondingToTheString(String action) {
		// Rien
	}

	@Override
	public void doSomethingCorrespondingToTheStringAndParam(String action,
			Object param) {
		if(action.contains("amplitude") && param instanceof Double) {
			computePlayerActionFromTheAmplitude(((Double)param).doubleValue());
		}	
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
	 * Explain what the game engine regulary do
	 */
	private void playLoop() {
		//computeNextActionnerPosition();
		List<GameShape> actionnersTmp = new ArrayList<GameShape>(actionners);
		for(GameShape actionner : actionnersTmp) {
			actionner.hideMore();
			if(userIsTouching && !actionner.isExploding()) {
				int distance = (int) Math.sqrt((actionner.getX() - userTouchX) * (actionner.getX() - userTouchX) 
											 + (actionner.getY() - userTouchY) * (actionner.getY() - userTouchY));
				if(distance < actionner.getHeight() / 2 && distance < actionner.getWidth()) {
					if( actionner.isGoodMoment() || actionner.isBonus() ) { //a bonus is always a bonus because we are nice developers :)
						Constants.score += actionner.getScore();
						if( actionner.isBonus() ) {
							actionner.setExplodingText( "BONUS + " + actionner.getScore() );
						}
						else {
							actionner.setExplodingText( "+ " + actionner.getScore() );
						}
					}
					else {
						Constants.score -= actionner.getScore() * Constants.tooLatePercent/100;
						actionner.setExplodingText( "- " + actionner.getScore() * Constants.tooLatePercent/100 );
					}
					actionner.hideAndExplode();
				}
			}
			if(!actionner.stillUse()) {
				actionners.remove(actionner);
				if( !actionner.isExploding() ) {
					if( !actionner.isBonus()) //a bonus is definitively not a malus because we are nice developers !
						Constants.score -= actionner.getScore() * Constants.missPercent/100;
				}
				actionner = null;
			}
		}

		Constants.pattern = actionners;
		
		long currentMusicTime = SoundEngine.getCurrentMusicTime() + (int)Constants.showTimer/10;
		SortedMap<Long,Pair<Integer,Integer>> subMap = pattern.subMap(Math.min(lastComputedTime,currentMusicTime), currentMusicTime);
		if(subMap.size() > 0) {
			Tools.log(this, "SE Time : " + SoundEngine.getCurrentMusicTime() + " A Time : " + subMap.keySet().toArray()[0].toString());
			addGameShapes(subMap.values());
		}
		lastComputedTime = currentMusicTime;
		pattern = new TreeMap<Long, Pair<Integer, Integer>>(pattern.tailMap(currentMusicTime));
	}
	
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

		lastComputedTime = SoundEngine.getCurrentMusicTime();
	}

	private void addGameShapes(Collection<Pair<Integer, Integer>> values) {
		for(Pair<Integer, Integer> position: values) {
			addGameShape(Game.virtualXToScreenX(position.getFirst()), Game.virtualYToScreenY(position.getSecond()));
		}
	}

	public void setUserTouchPosition(float x, float y) {
		userTouchX = x;
		userTouchY = y;
	}
	
	public void isTouching(boolean touch) {
		userIsTouching = touch;
	}

	public void saveShape(float time, float x, float y) {
		if(SoundEngine.getCurrentMusicTime() > lastComputedTime + 100 ||
		Game.virtualXToScreenX(75) < Tools.distanceBetweenPosition(new Float(oldActionnerX).intValue(), 
										 new Float(oldActionnerY).intValue(), 
										 new Float(x).intValue(), 
										 new Float(y).intValue())) {
			int savedX = Game.screenXToVirtualX(new Float(x).intValue());
			int savedY = Game.screenYToVirtualY(new Float(y).intValue());
			pattern.put((long)time, new Pair<Integer, Integer>(savedX,savedY));	
			addGameShape(x, y);
			oldActionnerX = x;
			oldActionnerY = y;
		}
	}
	
	public void savePattern(String fileName) {
		FileAccess.serialize(pattern, fileName);
	}
	
	public void loadPattern(String fileName) {
		pattern = (SortedMap<Long,Pair<Integer, Integer>>)FileAccess.deserialize(fileName);
	}
}
