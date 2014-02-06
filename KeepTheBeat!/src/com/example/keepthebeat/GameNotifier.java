package com.example.keepthebeat;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public abstract class GameNotifier {
	private List<GameListener> listeners;

	public GameNotifier() {
		listeners = new ArrayList<GameListener>();
	}
	/**
	 * Ajoute une classe qui écoutera les évènements
	 */
	public void addToTheListnersTheListener(GameListener gameListener) {
		listeners.add(gameListener);
	}
	
	/**
	 * On envoie l'informations à tous les observateurs
	 * @param info
	 */
	protected void sendToTheListenersTheString(String action) {
		Game.log(this , "Action : " + action);
		for(GameListener gameListener : listeners) {
			gameListener.doSomethingCorrespondingToTheString(action);
		}
	}
	
	/**
	 * On envoie l'informations à tous les observateurs
	 * @param info
	 */
	protected void sendToTheListenersTheStringAndTheParam(String action, Object param) {
		Game.log(this , "Action : " + action + " Param  : " + param);
		for(GameListener gameListener : listeners) {
			gameListener.doSomethingCorrespondingToTheStringAndParam(action, param);
		}
	}
}
