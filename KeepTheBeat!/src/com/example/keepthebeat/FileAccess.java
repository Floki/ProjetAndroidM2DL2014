package com.example.keepthebeat;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.content.Context;
import android.graphics.AvoidXfermode.Mode;
import android.widget.Toast;

public class FileAccess {
	public static void WriteSettings(String file, Context context, String data){ 
		FileOutputStream fOut = null; 
		OutputStreamWriter osw = null; 

		try{ 
			fOut = context.openFileOutput(file, Context.MODE_APPEND);       
			osw = new OutputStreamWriter(fOut); 
			osw.write(data); 
			osw.flush(); 
			Game.log(context, "Settings saved");
		} 
		catch (Exception e) {       
			Game.log(context, "Settings not saved"); 
		} 
		finally { 
			try { 
				osw.close(); 
				fOut.close(); 
			} catch (IOException e) { 
				Game.log(context, "Settings not saved"); 
			} 
		} 
	}

	public static String ReadSettings(Context context){ 
		FileInputStream fIn = null; 
		InputStreamReader isr = null; 

		char[] inputBuffer = new char[255]; 
		String data = null; 

		try{ 
			fIn = context.openFileInput("settings.dat");       
			isr = new InputStreamReader(fIn); 
			isr.read(inputBuffer); 
			data = new String(inputBuffer); 
			//affiche le contenu de mon fichier dans un popup surgissant
			Game.log(context, " "+data); 
		} 
		catch (Exception e) {       
			Game.log(context, "Settings not read"); 
		} 
		finally { 
           try { 
                  isr.close(); 
                  fIn.close(); 
                  } catch (IOException e) { 
                	  Game.log(context, "Settings not read"); 
                  } 
        } 
		return data; 
	}
}
