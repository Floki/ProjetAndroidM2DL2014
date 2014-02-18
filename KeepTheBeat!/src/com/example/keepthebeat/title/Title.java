package com.example.keepthebeat.title;

import java.io.File;

import com.example.keepthebeat.CustomActivity;
import com.example.keepthebeat.R;
import com.example.keepthebeat.ScoreActivity;
import com.example.keepthebeat.game.Game;
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
import android.widget.ImageView;


public class Title extends CustomActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Compute game path
		File storage = getApplication().getExternalFilesDir(null);
		if(storage == null) {
			storage = getApplication().getFilesDir();
		}
		Constants.keepTheBeatFolder = storage.getPath();
		Tools.log("", "File : " + Constants.keepTheBeatFolder );
		
		setContentView(R.layout.title);
		ImageView start = (ImageView) findViewById(R.id.playButton);
		ImageView scores = (ImageView) findViewById(R.id.scoreButton);
		//ImageView options = (ImageView) findViewById(R.id.Options);
		ImageView exit = (ImageView) findViewById(R.id.exitButton);

		start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {	
				Intent myIntent = new Intent(Title.this, Game.class);
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
		
//		options.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {	
//				Intent myIntent = new Intent(Title.this, Parameters.class);
//				startActivityForResult(myIntent, 0);
//			}
//		});

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
