package com.example.keepthebeat.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;

import javax.net.ssl.ManagerFactoryParameters;

import com.example.keepthebeat.game.Game;

import android.app.Application;
import android.content.Context;
import android.graphics.AvoidXfermode.Mode;
import android.os.Parcelable.Creator;
import android.util.Log;
import android.widget.Toast;

public class FileAccess {
	
	public static void writeToFile(String filePath, String fileName, String data) {
	    try {
	    	createPath(filePath);
	    	File outFile = new File(computeFullFilePathFromPathAndName(filePath, fileName));
	    	outFile.createNewFile();
	        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(outFile, true));
	        outputStreamWriter.append(data);
	        outputStreamWriter.close();
	    }
	    catch (IOException e) {
	    	Tools.log("Exception", "File write failed: " + e.toString());
	    } 
	}

	public static String readFileAsString(String filePath ,String fileName) {
	    String result = "";
	    File file = new File(computeFullFilePathFromPathAndName(filePath, fileName));
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
	
	public static void deleteFile(String filePath, String fileName) {
	    File file = new File(computeFullFilePathFromPathAndName(filePath, fileName));
	    file.delete();
	}
	
	public static boolean fileExist(String filePath, String fileName) {
	    File file = new File(computeFullFilePathFromPathAndName(filePath, fileName));
	    return file.exists();
	}
	
	public static void serialize(Object serializable, String filePath, String fileName) {
		try{
			createPath(filePath);
			FileOutputStream fileOut = new FileOutputStream(computeFullFilePathFromPathAndName(filePath, fileName));
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(serializable);
			objectOut.close();
		}catch(IOException ioe){
			System.err.println("ICI");
			System.err.print(ioe);
		}
	}

	public static Object deserialize(String filePath, String fileName) {
		try{
			FileInputStream fileIn = new FileInputStream(computeFullFilePathFromPathAndName(filePath, fileName));
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);
			Object object = objectIn.readObject();
			objectIn.close();
			return object;
		}
		catch(ClassNotFoundException e) {
			System.err.print(e);
			return null;
		}
		catch(IOException ioe){
			System.err.print(ioe);
			return null;
		}
	}
	
	public static String computeFullFilePathFromPathAndName(String filePath,String fileName) {
		String fullPath;
		if(filePath.substring(filePath.length() - 1).equals("/")) {
			fullPath = filePath + fileName;
		}
		else {
			fullPath = filePath + "/" + fileName;
		}
		return fullPath;
	}
	
	public static void createPath(String filePath) {
		File patternFolder = new File(filePath);
    	if(!patternFolder.exists()) {
    		patternFolder.mkdirs();
    	}
	}
}
