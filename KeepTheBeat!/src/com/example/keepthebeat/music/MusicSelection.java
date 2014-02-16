package com.example.keepthebeat.music;

import java.util.ArrayList;
import java.util.Arrays;

import com.example.keepthebeat.R;
import com.example.keepthebeat.game.Game;
import com.example.keepthebeat.utils.Constants;
import com.example.keepthebeat.utils.Tools;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


public class MusicSelection extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.musicselection);
		
		ArrayList<MusicFile> songs = new ArrayList<MusicFile>();  
		  
	    String[] proj = { MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATA };  
	    Cursor musicCursor = managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,proj, null, null, null);  
	  
	    if(musicCursor.moveToFirst()) {  
	        do {  
	        	if(musicCursor.getString(1) != null) {
		        	if(Integer.parseInt(musicCursor.getString(1)) > 60 * 1000) {
			        	MusicFile song = new MusicFile(musicCursor.getString(0), musicCursor.getString(2)); 
			        	songs.add(song);
		        	}
	        	}
	        }  
	        while(musicCursor.moveToNext());  
	    } 
	    final ListView list = (ListView)findViewById(R.id.musicList);
	    list.setAdapter(new ArrayAdapter<MusicFile>(this, android.R.layout.simple_list_item_1,songs));
	    list.setClickable(true);
	    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

	    	  @Override
	    	  public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
	    		  MusicFile file = (MusicFile) list.getItemAtPosition(position);
	    		  Tools.log(file, file);
	    	  }
	    	});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		return false;
	}
}
