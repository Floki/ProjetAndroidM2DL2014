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
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;


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
