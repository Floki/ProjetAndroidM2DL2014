package com.example.keepthebeat.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.example.keepthebeat.game.Game;

import android.app.Application;
import android.content.Context;
import android.graphics.AvoidXfermode.Mode;
import android.util.Log;
import android.widget.Toast;

public class FileAccess {
	public static String keepTheBeatFolder = "/sdcard/KeepTheBeat";
	public static String patternFolder = "/pattern/";
	
	public static void writeToFile(String fileName, String data) {
	    try {
	    	String path = FileAccess.keepTheBeatFolder + FileAccess.patternFolder;
	    	File patternFolder = new File(path);
	    	if(!patternFolder.exists()) {
	    		patternFolder.mkdirs();
	    	}
	    	File outFile = new File(path + "pattern1.vlf");
	    	outFile.createNewFile();
	        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(outFile, true));
	        outputStreamWriter.append(data);
	        outputStreamWriter.close();
	    }
	    catch (IOException e) {
	    	Tools.log("Exception", "File write failed: " + e.toString());
	    } 
	}

	public static String readFileAsString(String filePath) {

	    String result = "";
	    String path = FileAccess.keepTheBeatFolder + FileAccess.patternFolder;
	    File file = new File(path + "pattern1.vlf");
	    if ( file.exists() ) {
	        FileInputStream fis = null;
	        Tools.log("", "File read : " + file.getAbsolutePath());
	        try {
	            fis = new FileInputStream(file);
	            char current;
	            while (fis.available() > 0) {
	                current = (char) fis.read();
	                result = result + String.valueOf(current);
	            }

	        } catch (Exception e) {
	            Log.d("TourGuide", e.toString());
	        } finally {
	            if (fis != null)
	                try {
	                    fis.close();
	                } catch (IOException ignored) {
	            }
	        }
	    }
	    return result;
	}
	
	public static void deleteFile(String filePath) {
		String path = FileAccess.keepTheBeatFolder + FileAccess.patternFolder;
	    File file = new File(path + "pattern1.vlf");
	    file.delete();
	}
	
	public static boolean fileExist(String filePath) {
		String path = FileAccess.keepTheBeatFolder + FileAccess.patternFolder;
	    File file = new File(path + "pattern1.vlf");
	    return file.exists();
	}
}
