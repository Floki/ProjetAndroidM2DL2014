package com.example.keepthebeat.title;

import com.example.keepthebeat.R;
import com.example.keepthebeat.game.Game;
import com.example.keepthebeat.utils.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class Title extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
				Intent myIntent = new Intent(Title.this, Game.class);
				startActivityForResult(myIntent, 0);
			}
		});

		create.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {	
				Constants.mode = Constants.Mode.CREATE;
				Intent myIntent = new Intent(Title.this, Game.class);
				startActivityForResult(myIntent, 0);
			}
		});
		
		scores.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {	
				// TODO : create score screen
			}
		});
		
		options.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {	
				// TODO : create params screen
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
