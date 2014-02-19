package com.nadt.keepthebeat.game;

import java.io.File;
import java.util.ArrayList;
import com.nadt.keepthebeat.CustomActivity;
import com.nadt.keepthebeat.R;
import com.nadt.keepthebeat.game.Game;
import com.nadt.keepthebeat.utils.FileAccess;
import com.nadt.keepthebeat.utils.Tools;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class PatternSelection extends CustomActivity {

	private enum SELECTION {FOLDER, PATTERN};
	private SELECTION state;
	private String pathToExplore;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listselection);
		state = SELECTION.FOLDER;
		refreshList();
	}

	private void refreshList() {
		if(state == SELECTION.FOLDER) {
			pathToExplore = Pattern.patternPath();
		}
		
		Tools.log(this, state + " " + pathToExplore);
		ArrayList<String> listOfFileAndPath = new ArrayList<String>();
		FileAccess.createPathIfNonExists(pathToExplore);
		File dir = new File(pathToExplore);
		File file[] = dir.listFiles();  
		if (state == SELECTION.FOLDER) {
			listOfFileAndPath.add("I Wonder If God Was Sleeping");
		}
		for (File f : file)
		{
			if (f.isDirectory() && state == SELECTION.FOLDER) {
				listOfFileAndPath.add(f.getName());
			}
			else if(f.getName().contains("vlf") && state == SELECTION.PATTERN) {
				listOfFileAndPath.add(f.getName().substring(0, f.getName().lastIndexOf(".")));
			}
		}

		final ListView list = (ListView)findViewById(R.id.list);
		list.setAdapter(new ArrayAdapter<String>(this, R.layout.custom_list_view_line,listOfFileAndPath));
		list.invalidateViews();
		list.setClickable(true);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				if(state == SELECTION.FOLDER) {
					// Play the default pattern of the application
					if(position == 0) {
						Intent intent = new Intent(PatternSelection.this, Game.class);
						Tools.log(this, "Play default pattern");
						intent.putExtra("SELECTED_PATTERN", "default");
						startActivityForResult(intent, 0);
						return;
					}
					String folderName = (String) list.getItemAtPosition(position);
					pathToExplore = FileAccess.computeFullFilePathFromPathAndName(pathToExplore, folderName + "/");
					state = SELECTION.PATTERN;
					refreshList();
				}
				else {
					String patternName = (String) list.getItemAtPosition(position);
					Intent intent = new Intent(PatternSelection.this, Game.class);
					String fileInformation = FileAccess.computeFullFilePathFromPathAndName(pathToExplore, patternName+".vlf");
					Tools.log(this, fileInformation);
					intent.putExtra("SELECTED_PATTERN", fileInformation);
					startActivityForResult(intent, 0);
				}
				
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	if(state == SELECTION.PATTERN) {
	    		state = SELECTION.FOLDER;
	    		refreshList();
	    		return true;
	        }
	    	else {
	    		backToTitle();
	    	}
	    }
	    return super.onKeyDown(keyCode, event);
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
