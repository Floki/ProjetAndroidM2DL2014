package com.example.keepthebeat;

import java.io.BufferedReader;
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
	public void writeToFile(Context context, String fileName, String data) {
	    try {
	        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_WORLD_READABLE));
	        Game.log("", context.getFileStreamPath(fileName));
	        outputStreamWriter.write(data);
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
