package com.nadt.keepthebeat.title;

import java.io.File;


import android.content.Intent;
import android.database.Cursor;

import com.nadt.keepthebeat.CustomActivity;
import com.nadt.keepthebeat.R;
import com.nadt.keepthebeat.ScoreActivity;
import com.nadt.keepthebeat.db.ScoreDbHelper;
import com.nadt.keepthebeat.db.ScoreContract.ScoreEntry;
import com.nadt.keepthebeat.game.PatternSelection;
import com.nadt.keepthebeat.music.MusicSelection;
import com.nadt.keepthebeat.parameters.Parameters;
import com.nadt.keepthebeat.utils.Constants;
import com.nadt.keepthebeat.utils.Tools;

import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class Title extends CustomActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Compute game path
		String packageName = Title.this.getPackageName();
		File externalPath = Environment.getExternalStorageDirectory();
		File storage = new File(externalPath.getAbsolutePath() + "/Android/data/" + packageName + "/files");
		if(storage == null) {
			storage = getApplication().getFilesDir();
		}
		Constants.keepTheBeatFolder = storage.getPath();
		Tools.log("", "File : " + Constants.keepTheBeatFolder );
		
		setContentView(R.layout.title);
		ImageView start = (ImageView) findViewById(R.id.playButton);
		ImageView create = (ImageView) findViewById(R.id.createButton);
		ImageView scores = (ImageView) findViewById(R.id.scoreButton);
		ImageView options = (ImageView) findViewById(R.id.settings);
		ImageView exit = (ImageView) findViewById(R.id.exitButton);

		start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {	
				Constants.mode = Constants.Mode.PLAY;
				Intent myIntent = new Intent(Title.this, PatternSelection.class);
				startActivityForResult(myIntent, 0);
			}
		});

		create.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {	
				Constants.mode = Constants.Mode.CREATE;
				Intent myIntent = new Intent(Title.this, MusicSelection.class);
				startActivityForResult(myIntent, 0);
			}
		});
		
		scores.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {	
				Intent myIntent = new Intent(Title.this, ScoreActivity.class);
				startActivityForResult(myIntent, 0);
			}
		});
		
		options.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {	
				Intent myIntent = new Intent(Title.this, Parameters.class);
				startActivityForResult(myIntent, 0);
			}
		});

		exit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		
		/*
		 * Fill up best score & track
		 */
		ScoreDbHelper scoreDbHelper = new ScoreDbHelper(this);
		Cursor c = scoreDbHelper.getBestScore();
		
		String bestScore = "";
		String bestSong = "not yet played";
		
		if( c.moveToFirst() ) {
			bestScore = c.getInt( c.getColumnIndex( ScoreEntry.COLUMN_NAME_SCORE) )+"";
			bestSong = c.getString( c.getColumnIndex( ScoreEntry.COLUMN_NAME_TRACK) );
			( (TextView) findViewById(R.id.bestSongLabel) ).setVisibility( TextView.VISIBLE );
		}
		else {
			( (TextView) findViewById(R.id.bestSongLabel) ).setVisibility( TextView.INVISIBLE );
		}
		
		c.close();
		scoreDbHelper.closeDb();
		
		( (TextView) findViewById(R.id.bestScoreValue) ).setText( bestScore );
		( (TextView) findViewById(R.id.bestSongValue) ).setText( bestSong );
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
