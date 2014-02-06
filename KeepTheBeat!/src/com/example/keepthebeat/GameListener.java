package com.example.keepthebeat;

public interface GameListener {
	
	abstract void doSomethingCorrespondingToTheString(String action);
	abstract void doSomethingCorrespondingToTheStringAndParam(String action, Object param);
}
