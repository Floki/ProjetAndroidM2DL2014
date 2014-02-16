package com.example.keepthebeat.game;

import java.io.Serializable;
import java.util.SortedMap;

import com.example.keepthebeat.music.MusicFile;
import com.example.keepthebeat.utils.Constants;
import com.example.keepthebeat.utils.Pair;

public class Pattern implements Serializable {

	private static final long serialVersionUID = 4563337076568725111L;
	public static String patternFolder = "/pattern/";
	public static String patternPath() {return Constants.keepTheBeatFolder + patternFolder;}
	public static String filePatternPath(String file) {return patternPath() + file;}
	
	private SortedMap<Long, Pair<Integer, Integer>> pattern;

	public SortedMap<Long, Pair<Integer, Integer>> getPattern() {
		return pattern;
	}
	public void setPattern(SortedMap<Long, Pair<Integer, Integer>> pattern) {
		this.pattern = pattern;
	}

	private String patternName = null;
	private String patternPath = null;
	private MusicFile musicFile = null;
	
	public String getPatternName() {
		return patternName;
	}

	public void setPatternName(String patternName) {
		this.patternName = patternName;
	}

	public String getPatternPath() {
		return patternPath;
	}

	public void setPatternPath(String patternPath) {
		this.patternPath = patternPath;
	}

	public MusicFile getMusicFile() {
		return musicFile;
	}

	public void setMusicFile(MusicFile musicFile) {
		this.musicFile = musicFile;
	}

	public Pattern(String name, String path, MusicFile music) {
		patternName = name;
		patternPath = path;
		musicFile = music;
	}
	
	public Pattern(String name, String path) {
		patternName = name;
		patternPath = path;
	}
	
	public Pattern() {
	}
	
	public String toString() {
		return "File name : " + patternName + " Music File : " + musicFile.getPath() + "/" + musicFile.getTitle();
	}
}
