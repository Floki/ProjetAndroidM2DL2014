package com.example.keepthebeat.music;

public class MusicFile {
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

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
