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
	public void writeToFile(Context context, String data) {
	    String filename = "mysecondfile";
	    String outputString = "Hello world!";

	    try {
	        File outFile = new File("/test/filename.text");
	        if (outFile.getParentFile().mkdirs()) {
	            outFile.createNewFile();
	            FileOutputStream fos = new FileOutputStream(outFile);

	            fos.write(outputString.getBytes());
	            fos.flush();
	            fos.close();
	            Game.log(this, "Create file here : " + outFile.getAbsolutePath());
	        }
	        else {
	            Game.log(this, "Folder doesnt exist");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}


	private String readFromFile(Context context) {

	    String ret = "";

	    try {
	        InputStream inputStream = context.openFileInput("/config.txt");
	        Game.log("", context.getFileStreamPath("/config.txt"));
	        if ( inputStream != null ) {
	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	            String receiveString = "";
	            StringBuilder stringBuilder = new StringBuilder();

	            while ( (receiveString = bufferedReader.readLine()) != null ) {
	                stringBuilder.append(receiveString);
	            }

	            inputStream.close();
	            ret = stringBuilder.toString();
	        }
	    }
	    catch (FileNotFoundException e) {
	        Log.e("login activity", "File not found: " + e.toString());
	    } catch (IOException e) {
	        Log.e("login activity", "Can not read file: " + e.toString());
	    }

	    return ret;
	}
}
