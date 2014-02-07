package com.example.keepthebeat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.content.Context;
import android.graphics.AvoidXfermode.Mode;
import android.util.Log;
import android.widget.Toast;

public class FileAccess {
	public static String keepTheBeatFolder = "/sdcard/KeepTheBeat";
	public static String patternFolder = "/pattern/";
	
	public void writeToFile(Context context, String fileName, String data) {
	    try {
	    	String path = FileAccess.keepTheBeatFolder + FileAccess.patternFolder;
	    	File patternFolder = new File(path);
	    	if(!patternFolder.exists()) {
	    		patternFolder.mkdirs();
	    	}
	    	File outFile = new File(path + "pattern1.vlf");
	    	outFile.createNewFile();
	        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(outFile, true));
	        Game.log("", "File : " + outFile.getAbsolutePath());
	        outputStreamWriter.append(data);
	        outputStreamWriter.close();
	    }
	    catch (IOException e) {
	        Log.e("Exception", "File write failed: " + e.toString());
	    } 
	}


	private String readFromFile(Context context) {

	   return "";
	}
}
