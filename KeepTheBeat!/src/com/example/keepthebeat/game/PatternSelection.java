package com.example.keepthebeat.game;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import com.example.keepthebeat.R;
import com.example.keepthebeat.game.Game;
import com.example.keepthebeat.title.Title;
import com.example.keepthebeat.utils.Constants;
import com.example.keepthebeat.utils.FileAccess;
import com.example.keepthebeat.utils.Pair;
import com.example.keepthebeat.utils.Tools;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


public class PatternSelection extends Activity {

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
		File dir = new File(pathToExplore);
		File file[] = dir.listFiles();  
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
		list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listOfFileAndPath));
		list.invalidateViews();
		list.setClickable(true);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				if(state == SELECTION.FOLDER) {
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
	        }
	    	else {
	    		Intent myIntent = new Intent(PatternSelection.this, Title.class);
	    		startActivityForResult(myIntent, 0);
	    		onDestroy();
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
