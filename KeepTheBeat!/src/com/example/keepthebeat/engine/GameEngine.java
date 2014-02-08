package com.example.keepthebeat.engine;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.keepthebeat.Game;
import com.example.keepthebeat.GameListener;
import com.example.keepthebeat.GameNotifier;
import com.example.keepthebeat.shape.BeatShape;
import com.example.keepthebeat.shape.GameShape;
import com.example.keepthebeat.utils.Constants;

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
	// Variable n�cessaire au calcul de la prochaine position d'un futur actionneur
	private double actionnerMoveX;
	private double actionnerMoveY;
	private double actionnerMoveMinSpeed;
	// Taille de l'aire de jeu
	private int gameWidth;
	private int gameHeight;
	// Boucle du jeux
	private Handler gameLoop;
	private Runnable whatGameLoopDo;
	// Liste des actionneurs � afficher
	private List<GameShape> actionners;
	// Pattern line
	private Map<String,String> pattern;
	// Score
	private int score;
	
	
	public GameEngine() {
		score = 0;
		userIsTouching = false;
		lastAmplitude = 0;
		maxSongAmplitude = 0;
		pattern = new HashMap<String, String>();
		gameWidth = Game.screenWidth;
		gameHeight = Game.screenHeight;
		actionnerX = gameWidth / 2;
		actionnerY = gameHeight / 2;
		actionnerMoveMinSpeed = 5;
		actionnerMoveX = (Math.random() - 0.5) * actionnerMoveMinSpeed * 10;
		actionnerMoveY = (Math.random() - 0.5) * actionnerMoveMinSpeed * 10;
		gameLoop = new Handler();
		gameLoop.postDelayed(whatGameLoopDo, 10);
		actionners = new ArrayList<GameShape>();
		whatGameLoopDo = new Runnable() {
			@Override 
			public void run() {
				whatGameLoopDo();
			}
		};
		whatGameLoopDo();
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
		Game.log(this, "Actionner Pos : " + actionnerX + " " + actionnerY);
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
	
	/**
	 * Explain what the game engine regulary do
	 */
	private void whatGameLoopDo() {
		computeNextActionnerPosition();
		List<GameShape> actionnersToRemove = new ArrayList<GameShape>();
		int currentMusicTime = SoundEngine.getCurrentMusicTime() - (int)Constants.showTimer/10;
		if(pattern.containsKey(""+currentMusicTime)) {
			int x = new Integer(pattern.get(""+currentMusicTime).split(" ")[0]).intValue();
			int y = new Integer(pattern.get(""+currentMusicTime).split(" ")[1]).intValue();
			addGameShape(x,y);
		}
		for(GameShape actionner : actionners) {
			if(!actionner.stillUse()) {
				actionnersToRemove.add(actionner);
			}
			actionner.hideMore();
			if(userIsTouching && !actionner.isExploding()) {
				int distance = (int) Math.sqrt((actionner.getX() - userTouchX) * (actionner.getX() - userTouchX) 
											 + (actionner.getY() - userTouchY) * (actionner.getY() - userTouchY));
				if(distance < actionner.getHeight() / 2 && distance < actionner.getWidth()) {
					if( actionner.isGoodMoment() || actionner.isBonus() ) { //a bonus is always a bonus because we are nice developers :)
						score += actionner.getScore();
					}
					else {
						score = score - actionner.getScore() * Constants.tooLatePercent/100;
					}
					actionner.hideAndExplode();
				}
			}
		}
		for(GameShape actionnerToRemove : actionnersToRemove) {
			actionners.remove(actionnerToRemove);
			//loose points if miss
			if( !actionnerToRemove.isExploding() ) {
				if( !actionnerToRemove.isBonus()) //a bonus is definitively not a malus because we are nice developers !
					score = score - actionnerToRemove.getScore() * Constants.missPercent/100;
			}
			actionnerToRemove = null;
		}
		sendToTheListenersTheStringAndTheParam("redraw", actionners);
		sendToTheListenersTheStringAndTheParam("score", new Integer(score));
		gameLoop.postDelayed(whatGameLoopDo, 1);
	}

	public void setUserTouchPosition(float x, float y) {
		userTouchX = x;
		userTouchY = y;
		Game.log(this, "Touch : [" + userTouchX + "," + userTouchY + "]");
	}

	public void setPatternFromString(String stringPattern) {
		Game.log(this, "New pattern : " + stringPattern);
		String[] patternLines = stringPattern.split("\n");
		for(String line: patternLines) {
			String[] information = line.split(" ");
			//Game.log(this, "Put pattern " + ""+new Integer(information[2]).intValue() + " " + new Integer(information[1]).intValue()+" "+new Integer(information[2]).intValue());
			pattern.put(""+new Float(information[2]).intValue(), Game.virtualXToScreenX(new Float(information[0]).intValue())+" "+Game.virtualYToScreenY(new Float(information[1]).intValue()));
		}
	}
	
	public void isTouching(boolean touch) {
		userIsTouching = touch;
	}
}
