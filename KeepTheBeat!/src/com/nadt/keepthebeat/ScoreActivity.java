package com.nadt.keepthebeat;

import com.nadt.keepthebeat.db.ScoreDbHelper;
import com.nadt.keepthebeat.db.ScoreContract.ScoreEntry;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ScoreActivity extends CustomActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score);

		TableLayout tableL = (TableLayout) findViewById(R.id.tableScore);

		tableL.setShrinkAllColumns(true);
		tableL.setStretchAllColumns(true);
		tableL.setPadding(5, 5, 5, 5);
		
		// fill the tableLayout with scores
		ScoreDbHelper scoreDbHelper = new ScoreDbHelper(this);
		
		Cursor c = scoreDbHelper.getAllScoresSortByTrack();
		
		c.moveToFirst();
		boolean next = c.isFirst();
		while( next ) {
			
			String trackValue = c.getString( c.getColumnIndexOrThrow( ScoreEntry.COLUMN_NAME_TRACK ) );
			String patternValue = c.getString( c.getColumnIndexOrThrow( ScoreEntry.COLUMN_NAME_PATTERN ) );
			String levelValue = c.getString( c.getColumnIndexOrThrow( ScoreEntry.COLUMN_NAME_LEVEL ) );
			int scoreValue = c.getInt( c.getColumnIndexOrThrow( ScoreEntry.COLUMN_NAME_SCORE ) );

			if( trackValue.length() > 25 ) {
				trackValue = trackValue.substring(0,25 ) + "...";
			}
			
			TextView trackV = new TextView(this);
			trackV.setText( trackValue );
			TextView patternV = new TextView(this);
			patternV.setText( patternValue.substring(0, patternValue.lastIndexOf(".")) );
			TextView levelV = new TextView(this);
			levelV.setText( levelValue );
			TextView scoreV = new TextView(this);
			scoreV.setText( scoreValue + "" );

			TableRow row = new TableRow(this);

			row.addView(trackV); // Line 39
			row.addView(patternV);
			row.addView(levelV);
			row.addView(scoreV);

			tableL.addView(row);
			
			next = c.moveToNext();
		}
		
		c.close();
		scoreDbHelper.closeDb();
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			backToTitle();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
}
