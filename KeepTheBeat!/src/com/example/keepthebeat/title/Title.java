package com.example.keepthebeat.title;

import java.io.File;

import com.example.keepthebeat.CustomActivity;
import com.example.keepthebeat.R;
import com.example.keepthebeat.ScoreActivity;
import com.example.keepthebeat.game.PatternSelection;
import com.example.keepthebeat.music.MusicSelection;
import com.example.keepthebeat.utils.Constants;
import com.example.keepthebeat.utils.Tools;

import android.content.Intent;
import com.example.keepthebeat.parameters.Parameters;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class Title extends CustomActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Compute game path
		File storage = getApplication().getExternalFilesDir(null);
		Constants.keepTheBeatFolder = storage.getPath();
		Tools.log("", "File : " + Constants.keepTheBeatFolder );
		
		setContentView(R.layout.title);
		Button start = (Button) findViewById(R.id.Start);
		Button create = (Button) findViewById(R.id.Create);
		Button scores = (Button) findViewById(R.id.Scores);
		Button options = (Button) findViewById(R.id.Options);
		Button exit = (Button) findViewById(R.id.exit);

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
