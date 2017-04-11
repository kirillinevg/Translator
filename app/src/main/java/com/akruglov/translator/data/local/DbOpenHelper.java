package com.akruglov.translator.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by akruglov on 09.04.17.
 */

public class DbOpenHelper extends SQLiteOpenHelper implements DbContract {

    private static final int DB_VERSION = 1;

    public DbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + LANGUAGES + "( " +
            Languages.ID + " INTEGER PRIMARY KEY, " +
            Languages.KEY + " TEXT UNIQUE NOT NULL, " +
            Languages.DESCRIPTION + " TEXT UNIQUE NOT NULL)");

        db.execSQL("CREATE TABLE " + HISTORY + "( " +
            History.ID + " INTEGER PRIMARY KEY, " +
            History.SOURCE_LANG_ID + " INTEGER NOT NULL, " +
            History.DEST_LANG_ID + " INTEGER NOT NULL, " +
            History.SOURCE_TEXT + " TEXT NOT NULL, " +
            History.TRANSLATED_TEXT + " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Update for dev versions!
        db.execSQL("DROP TABLE " + LANGUAGES);
        db.execSQL("DROP TABLE " + HISTORY);
    }
}
