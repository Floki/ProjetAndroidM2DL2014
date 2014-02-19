package com.nadt.keepthebeatsurvival.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import android.util.Log;

public class FileAccess {
	
	public static void writeToFile(String filePath, String fileName, String data) {
	    writeToFile(computeFullFilePathFromPathAndName(filePath, fileName), data);
	}
	
	public static void writeToFile(String filePath, String data) {
	    try {
	    	createPathIfNonExists(filePath);
	    	File outFile = new File(filePath);
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
	    return readFileAsString(computeFullFilePathFromPathAndName(filePath, fileName));
	}
	
	public static String readFileAsString(String filePath) {
	    String result = "";
	    File file = new File(filePath);
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
	    deleteFile(computeFullFilePathFromPathAndName(filePath, fileName));
	}
	
	public static void deleteFile(String filePath) {
	    File file = new File(filePath);
	    file.delete();
	}
	
	public static boolean fileExist(String filePath, String fileName) {
	    return fileExist(computeFullFilePathFromPathAndName(filePath, fileName));
	}
	
	public static boolean fileExist(String filePath) {
	    File file = new File(filePath);
	    if(!file.exists()) {
	    	Tools.log("", "File " + filePath + " doesn't exist!");
	    }
	    return file.exists();
	}
	
	public static void serialize(Object serializable, String filePath, String fileName) {
		serialize(serializable, computeFullFilePathFromPathAndName(filePath, fileName));
	}
	
	public static void serialize(Object serializable, String filePath) {
		try{
			createPathIfNonExists(filePath);
			FileOutputStream fileOut = new FileOutputStream(filePath);
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(serializable);
			objectOut.close();
		}catch(IOException ioe){
			System.err.println("ICI");
			System.err.print(ioe);
		}
	}

	public static Object deserialize(String filePath, String fileName) {
		return deserialize(computeFullFilePathFromPathAndName(filePath, fileName));
	}
	
	public static Object deserialize(String filePath) {
		try{
			FileInputStream fileIn = new FileInputStream(filePath);
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
	
	public static void createPathIfNonExists(String filePath) {
		File patternFolder = new File(filePath.substring(0, filePath.lastIndexOf("/")));
    	if(!patternFolder.exists()) {
    		patternFolder.mkdirs();
    	}
	}
}
