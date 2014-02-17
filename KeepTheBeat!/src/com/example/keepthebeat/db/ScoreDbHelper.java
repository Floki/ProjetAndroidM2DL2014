package com.example.keepthebeat.db;

import com.example.keepthebeat.db.ScoreContract.ScoreEntry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ScoreDbHelper extends SQLiteOpenHelper {

	// If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Score.db";
    
    private static final String SQL_CREATE_ENTRIES =
    	    "CREATE TABLE " + ScoreEntry.TABLE_NAME + " (" +
    	    ScoreEntry._ID + " INTEGER PRIMARY KEY," +
    	    ScoreEntry.COLUMN_NAME_TRACK + " TEXT, " +
    	    ScoreEntry.COLUMN_NAME_PATTERN + " TEXT, " +
    	    ScoreEntry.COLUMN_NAME_SCORE + " INTEGER" +
    	    " )";

	private static final String SQL_DELETE_ENTRIES =
    	    "DROP TABLE IF EXISTS " + ScoreEntry.TABLE_NAME;

    public ScoreDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_ENTRIES);

	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
	
//    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        onUpgrade(db, oldVersion, newVersion);
//    }


}
