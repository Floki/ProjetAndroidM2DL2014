package com.example.keepthebeat;

import com.example.keepthebeat.db.ScoreContract.ScoreEntry;
import com.example.keepthebeat.db.ScoreDbHelper;

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

		// fill the tableLayout with scores
		ScoreDbHelper scoreDbHelper = new ScoreDbHelper(this);
		SQLiteDatabase db = scoreDbHelper.getReadableDatabase();

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = { 
				ScoreEntry.COLUMN_NAME_TRACK,
				ScoreEntry.COLUMN_NAME_PATTERN,
				ScoreEntry.COLUMN_NAME_LEVEL,
				ScoreEntry.COLUMN_NAME_SCORE };

		// How you want the results sorted in the resulting Cursor
		String sortOrder = ScoreEntry.COLUMN_NAME_TRACK + " ASC";

		Cursor c = db.query(ScoreEntry.TABLE_NAME, // The table to query
				projection, // The columns to return
				null, // The columns for the WHERE clause
				null, // The values for the WHERE clause
				null, // don't group the rows
				null, // don't filter by row groups
				sortOrder // The sort order
				);
		
		c.moveToFirst();
		boolean next = c.isFirst();
		while( next ) {
			
			String trackValue = c.getString( c.getColumnIndexOrThrow( ScoreEntry.COLUMN_NAME_TRACK ) );
			String patternValue = c.getString( c.getColumnIndexOrThrow( ScoreEntry.COLUMN_NAME_PATTERN ) );
			String levelValue = c.getString( c.getColumnIndexOrThrow( ScoreEntry.COLUMN_NAME_LEVEL ) );
			int scoreValue = c.getInt( c.getColumnIndexOrThrow( ScoreEntry.COLUMN_NAME_SCORE ) );

			TextView trackV = new TextView(this);
			trackV.setText( trackValue );
			TextView patternV = new TextView(this);
			patternV.setText( patternValue );
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

		db.close();
		
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
		}

		return super.onKeyDown(keyCode, event);
	}
}
