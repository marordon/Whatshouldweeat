package com.example.marcordonez.whatshouldweeat;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ben on 04/23/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "foodUp.db";

    private final String PREF_TABLE_NAME = "PREF_TABLE";
    private final String CHOICE_TABLE_NAME = "CHOICE_TABLE";

    // Choice Table cols
    private class CHOICE_COLS{
        private static final String ID = "id";
        private static final String NAME = "name";
        private static final String LAT = "lat";
        private static final String LONG = "long";
        private static final String RATING = "rating";
        private static final String ADDRESS = "address";
        private static final String IMGURL = "imgurl";
        private static final String FTYPE = "ftype";
    }

    // Pref Table cols
    private class PREF_COLS{
        private static final String ID = "id";
        private static final String NAME = "name";
        private static final String VALUE = "value";
    }


    private final String CREATE_CHOICE_TABLE = "CREATE TABLE IF NOT EXISTS " + CHOICE_TABLE_NAME
            + "(" + CHOICE_COLS.ID + " INTEGER PRIMARY KEY,"
            + CHOICE_COLS.NAME + " TEXT,"
            + CHOICE_COLS.LAT + " REAL,"
            + CHOICE_COLS.LONG + " REAL,"
            + CHOICE_COLS.ADDRESS + " TEXT,"
            + CHOICE_COLS.RATING + " REAL,"
            + CHOICE_COLS.IMGURL + " TEXT,"
            + CHOICE_COLS.FTYPE + " TEXT)";

    private final String CREATE_PREF_TABLE = "CREATE TABLE IF NOT EXISTS " + PREF_TABLE_NAME
            + "(" + PREF_COLS.ID + " INTEGER PRIMARY KEY,"
            + PREF_COLS.NAME + " TEXT,"
            + PREF_COLS.VALUE + " INTEGER)";


    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CHOICE_TABLE);
        db.execSQL(CREATE_PREF_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Choice getChoice(int withID){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] pro = {CHOICE_COLS.NAME, CHOICE_COLS.LAT, CHOICE_COLS.LONG,
                CHOICE_COLS.ADDRESS, CHOICE_COLS.RATING, CHOICE_COLS.IMGURL,
                CHOICE_COLS.FTYPE};


        return null;
    }


}
