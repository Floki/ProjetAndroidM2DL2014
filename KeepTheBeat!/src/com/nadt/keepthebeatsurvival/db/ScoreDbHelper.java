package com.nadt.keepthebeatsurvival.db;

import com.nadt.keepthebeatsurvival.db.ScoreContract.ScoreEntry;
import com.nadt.keepthebeatsurvival.utils.Tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ScoreDbHelper extends SQLiteOpenHelper {

	// If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Scores.db";
    
    private SQLiteDatabase db = null;
    
    private static final String SQL_CREATE_ENTRIES =
    	    "CREATE TABLE " + ScoreEntry.TABLE_NAME + " (" +
    	    ScoreEntry._ID + " INTEGER PRIMARY KEY," +
    	    ScoreEntry.COLUMN_NAME_DIFFICULTY + " TEXT, " +
    	    ScoreEntry.COLUMN_NAME_SCORE + " INTEGER" +
    	    " )";

	private static final String SQL_DELETE_ENTRIES =
    	    "DROP TABLE IF EXISTS " + ScoreEntry.TABLE_NAME;

    public ScoreDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


	@Override
	public void onCreate(SQLiteDatabase db) {
		Tools.log(this, "Create database version " + DATABASE_VERSION);
		Tools.log(this, "SQL " + SQL_CREATE_ENTRIES);
		db.execSQL(SQL_CREATE_ENTRIES);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
		Tools.log(this, "Upgrading database from " + oldVersion +" to " + newVersion);
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

//    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        onUpgrade(db, oldVersion, newVersion);
//    }

	public Cursor getAllScoresSortByTrack() {
		SQLiteDatabase db = this.getReadableDatabase();

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = { 
				ScoreEntry.COLUMN_NAME_DIFFICULTY,
				ScoreEntry.COLUMN_NAME_SCORE };

		// How you want the results sorted in the resulting Cursor
		String sortOrder = ScoreEntry.COLUMN_NAME_DIFFICULTY + " ASC";

		Cursor c = db.query(ScoreEntry.TABLE_NAME, // The table to query
				projection, // The columns to return
				null, // The columns for the WHERE clause
				null, // The values for the WHERE clause
				null, // don't group the rows
				null, // don't filter by row groups
				sortOrder // The sort order
				);
		return c;
	}

	/**
	 * <p>Create and execute a request to the DB
	 * <p>Notice that the DB need to stay open untill all access to the cursor are done.
	 * <p>After cursor use don't forget to close database connection
	 * @return a cursor to the result query.
	 */
	public Cursor getBestScore() {
		db = this.getReadableDatabase();

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = { 
				ScoreEntry.COLUMN_NAME_DIFFICULTY,
				ScoreEntry.COLUMN_NAME_SCORE };

		// How you want the results sorted in the resulting Cursor
		String sortOrder = ScoreEntry.COLUMN_NAME_SCORE + " DESC";

		Cursor c = db.query(ScoreEntry.TABLE_NAME, // The table to query
				projection, // The columns to return
				null, // The columns for the WHERE clause
				null, // The values for the WHERE clause
				null, // don't group the rows
				null, // don't filter by row groups
				sortOrder // The sort order
				);
		return c;
	}

	public void insertScore( String level, int score) {
		db = this.getWritableDatabase();

		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(ScoreEntry.COLUMN_NAME_DIFFICULTY, level);
		values.put(ScoreEntry.COLUMN_NAME_SCORE, score);

		// Insert the new row, returning the primary key value of the new row
		long newRowId;
		newRowId = db.insert(ScoreEntry.TABLE_NAME, null, values);
		//normaly score is inserted in DB
		db.close();
	}

	/**
	 * Close the database connection
	 */
	public void closeDb() {
		if( db != null ) {
			if( db.isOpen() ) {
				db.close();
				Tools.log(this, "Database closed");
			}
			else {
				Tools.log(this, "Database already closed");
			}
		}
	}

}