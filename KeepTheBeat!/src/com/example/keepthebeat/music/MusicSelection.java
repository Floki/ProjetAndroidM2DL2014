package com.example.keepthebeat.music;

import java.util.ArrayList;
import com.example.keepthebeat.CustomActivity;
import com.example.keepthebeat.R;
import com.example.keepthebeat.game.Game;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;


public class MusicSelection extends CustomActivity {

	private MusicFile fileSelected = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listselection);

		ArrayList<MusicFile> songs = new ArrayList<MusicFile>();  

		String[] proj = { MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATA };  
		Cursor musicCursor = managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,proj, null, null, null);  
		if(musicCursor == null) {
			backToTitle("Musique introuvables", "KtB n'arrive pas ‡ rÈcupËrer les musiques de votre tÈlÈphone.");
			return;
		}

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
		final ListView list = (ListView)findViewById(R.id.list);
		list.setAdapter(new ArrayAdapter<MusicFile>(this, R.layout.custom_list_view_line, songs));
		list.setClickable(true);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				fileSelected = (MusicFile) list.getItemAtPosition(position);
				patternNamePopup();
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

	private final void launchGame(String patternName) {
		Intent intent = new Intent(MusicSelection.this, Game.class);
		String[] fileInformation = {fileSelected.getTitle(),fileSelected.getPath(), patternName};
		intent.putExtra("SELECTED_MUSIC", fileInformation);
		startActivityForResult(intent, 0);
	}
	
	public void patternNamePopup() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Piste personnalis√©e");
		alert.setMessage("Entrez le nom de votre piste.");

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				launchGame(input.getText().toString());
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				
			}
		});

		alert.show();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        backToTitle();
	    }

	    return super.onKeyDown(keyCode, event);
	}
}
