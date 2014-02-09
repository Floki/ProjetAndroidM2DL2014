package com.example.keepthebeat.game;

import java.util.ArrayList;
import java.util.List;

import com.example.keepthebeat.utils.Tools;

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
		for(GameListener gameListener : listeners) {
			gameListener.doSomethingCorrespondingToTheString(action);
		}
	}
	
	/**
	 * On envoie l'informations à tous les observateurs
	 * @param info
	 */
	protected void sendToTheListenersTheStringAndTheParam(String action, Object param) {
		for(GameListener gameListener : listeners) {
			gameListener.doSomethingCorrespondingToTheStringAndParam(action, param);
		}
	}
}
