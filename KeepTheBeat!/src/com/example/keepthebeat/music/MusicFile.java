package com.example.keepthebeat.music;

public class MusicFile {
	private String title;
	private String path;
	
	public MusicFile(String title, String path) {
		this.title = title;
		this.path = path;
	}
	
	public String toString() {
		return title;
	}
	
	
}
