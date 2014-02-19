package com.example.keepthebeat.db;

import android.provider.BaseColumns;

/*
 * from http://developer.android.com/training/basics/data-storage/databases.html
 */

public final class ScoreContract {
	// To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public ScoreContract() {}
    
    /* Inner class that defines the table contents */
    public static abstract class ScoreEntry implements BaseColumns {
        public static final String TABLE_NAME = "score";
        public static final String COLUMN_NAME_DIFFICULTY = "difficulty";
        public static final String COLUMN_NAME_SCORE = "score";
    }


}
