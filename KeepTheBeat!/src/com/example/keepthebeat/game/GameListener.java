package com.example.keepthebeat.game;

public interface GameListener {
	
	abstract void doSomethingCorrespondingToTheString(String action);
	abstract void doSomethingCorrespondingToTheStringAndParam(String action, Object param);
}
