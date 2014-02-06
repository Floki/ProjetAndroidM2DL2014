package com.example.keepthebeat;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import android.os.Handler;
import android.util.Log;

public class GameEngine extends GameNotifier implements GameListener{
	// Conserve la puissance du dernier son joué
	private double lastAmplitude;
	// Conserve la puissance maximale trouvé jusqu'à maintenant
	private double maxSongAmplitude;
	// Position courant de l'actionneur si on devait l'afficher
	private double actionnerX;
	private double actionnerY;
	// Variable nécessaire au calcul de la prochaine position d'un futur actionneur
	private double actionnerMoveX;
	private double actionnerMoveY;
	private double actionnerMoveMinSpeed;
	// Taille de l'aire de jeu
	private int gameWidth;
	private int gameHeight;
	// Boucle du jeux
	private Handler gameLoop;
	private Runnable whatGameLoopDo;
	// Liste des actionneurs à afficher
	private List<BeatShape> actionners;
	
	
	public GameEngine(int width, int height) {
		lastAmplitude = 0;
		maxSongAmplitude = 0;
		gameWidth = width;
		gameHeight = height;
		actionnerX = width / 2;
		actionnerY = height / 2;
		actionnerMoveMinSpeed = 5;
		actionnerMoveX = (Math.random() - 0.5) * actionnerMoveMinSpeed * 10;
		actionnerMoveY = (Math.random() - 0.5) * actionnerMoveMinSpeed * 10;
		gameLoop = new Handler();
		gameLoop.postDelayed(whatGameLoopDo, 10);
		actionners = new ArrayList<BeatShape>();
		whatGameLoopDo = new Runnable() {
			@Override 
			public void run() {
				whatGameLoopDo();
			}
		};
		whatGameLoopDo();
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
		if(amplitude > 0.9 * maxSongAmplitude || (amplitude > 1300 && lastAmplitude > 1300 && amplitude > lastAmplitude * ((13000 - Math.min(amplitude/2, 3001))/10000))) {
			// On dessine une image
			BeatShape beatShape = new BeatShape();
			beatShape.setPosition((int)actionnerX, (int)actionnerY, gameWidth, gameHeight);
			actionners.add(beatShape);
		}
		else if(amplitude > 0.95 * maxSongAmplitude) {
			actionnerMoveMinSpeed = Math.min(actionnerMoveMinSpeed + 0.1,3);
		}
		// Si le son se calme un instant, on déclenche un évènement
		else if(amplitude < maxSongAmplitude * 0.2) {
			actionnerX = Math.random() * gameWidth;
			actionnerY = Math.random() * gameHeight;
			actionnerMoveMinSpeed = Math.max(actionnerMoveMinSpeed - 1,2);
		}
		else {
			actionnerMoveMinSpeed = Math.max(actionnerMoveMinSpeed - 1,2);
		}
		// Mémorise l'ancienne amplitude
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
		List<BeatShape> actionnersToRemove = new ArrayList<BeatShape>();
		for(BeatShape actionner : actionners) {
			if(!actionner.stillUse()) {
				actionnersToRemove.add(actionner);
			}
			actionner.hideMore();
		}
		for(BeatShape actionnerToRemove : actionnersToRemove) {
			actionners.remove(actionnerToRemove);
			actionnerToRemove = null;
		}
	
		sendToTheListenersTheStringAndTheParam("redraw", actionners);
		gameLoop.postDelayed(whatGameLoopDo, 1);
	}
}
