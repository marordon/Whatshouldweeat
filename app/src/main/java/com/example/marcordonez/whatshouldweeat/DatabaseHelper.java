package com.example.marcordonez.whatshouldweeat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.provider.Settings;
import android.util.Log;

import java.io.Console;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.example.marcordonez.whatshouldweeat.DatabaseHelper.FType.FISH;

/**
 * Created by Ben on 04/23/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "foodUp.db";

    private final String PREF_TABLE_NAME = "PREF_TABLE";
    private final String CHOICE_TABLE_NAME = "CHOICE_TABLE";

    public enum FType{
        MEXICAN(0, "mexican"), ITALIAN(20, "italian"), PIZZA(40, "pizza"),
        CHINESE(60, "chinese"), SUSHI(80, "sushi"), BREAKFAST(100, "breakfast"),
        THAI(120, "thai"), INDIAN(140, "indian"), HAMBURGER(160, "hamburger"),
        HOTDOG(180, "hotdog"), NOODLES(200, "noodles"), BBQ(220, "bbq"),
        SEAFOOD(240, "seafood"), STEAK(260, "steak"), WINGS(280, "wings"),
        VEGAN(300, "vegan"), SANDWICH(320, "sandwich"), CAJUN(340, "cajun"),
        FISH(360, "fish"), OTHER(380, "other");

        private int startIndex;
        private String displayName;

        FType(int startIndex, String displayName){
            this.startIndex = startIndex;
            this.displayName = displayName;
        }

        public int getStartIndex() {
            return startIndex;
        }

        public String getDisplayName() {
            return displayName;
        }

        static public FType findType(String s){
            for (FType e: FType.values()) {
                if(s.equals(e.getDisplayName())){
                    return e;
                }
            }

            return OTHER;
        }
    }

    // Choice Table cols
    private class CHOICE_COLS{
        /**
         * Index References
         *
         * 0-19 = Mexican
         * 20-39 = Italian
         * 40-59 = Pizza
         * 60-79 = Chinese
         * 80-99 = Sushi
         * 100-119 = Breakfast
         * 120-139 = Thai
         * 140-159 = Indian
         * 160-179 = Hamburger
         * 180-199 = Hotdog
         * 200-219 = Noodles
         * 220-239 = BBQ
         * 240-259 = Seafood
         * 260-279 = Steak
         * 280-299 = Wings
         * 300-319 = Vegan
         * 320-339 = Sandwich
         * 340-359 = Cajun
         * 360-379 = Fish
         */

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
        /**
         * Index Reference
         *
         * 0 = Total
         * 1 = Mexican
         * 2 = Italian
         * 3 = Pizza
         * 4 = Chinese
         * 5 = Sushi
         * 6 = Breakfast
         * 7 = Thai
         * 8 = Indian
         * 9 = Hamburger
         * 10 = Hotdog
         * 11 = Noodles
         * 12 = BBQ
         * 13 = Seafood
         * 14 = Steak
         * 15 = Wings
         * 16 = Vegan
         * 17 = Sandwich
         * 18 = Cajun
         * 19 = Fish
         */


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

    private final String DROP_CHOICE_TABLE = "DROP TABLE IF EXISTS " + CHOICE_TABLE_NAME;
    private final String DROP_PREF_TABLE = "DROP TABLE IF EXISTS " + PREF_TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CHOICE_TABLE);
        db.execSQL(CREATE_PREF_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_CHOICE_TABLE);
        db.execSQL(DROP_PREF_TABLE);
    }

    public void addChoice(Choice choice){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CHOICE_COLS.NAME, choice.getName());
        cv.put(CHOICE_COLS.LAT, Double.valueOf(choice.getLat()));
        cv.put(CHOICE_COLS.LONG, Double.valueOf(choice.getLng()));
        cv.put(CHOICE_COLS.ADDRESS, choice.getAddress());
        cv.put(CHOICE_COLS.IMGURL, choice.getImgurl());
        cv.put(CHOICE_COLS.RATING, Double.valueOf(choice.getRating()));
        cv.put(CHOICE_COLS.FTYPE, choice.getFtype().getDisplayName());

        long newId = db.insert(CHOICE_TABLE_NAME, null, cv);
        Log.d("DB TESTING", choice.toString() + " added at " + newId);
        db.close();
    }

    private int getLowestID(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Integer> allIds = new ArrayList<>();

        Cursor c = db.query(CHOICE_TABLE_NAME,
                new String[]{CHOICE_COLS.ID},
                null,
                null,
                null,
                null,
                null);

        if(c.moveToFirst()){
            while(!c.isAfterLast()){
                allIds.add(c.getInt(c.getColumnIndexOrThrow(CHOICE_COLS.ID)));
                c.moveToNext();
            }
        }
        c.close();

        return Collections.min(allIds);
    }

    private int getLargetID(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Integer> allIds = new ArrayList<>();

        Cursor c = db.query(CHOICE_TABLE_NAME,
                new String[]{CHOICE_COLS.ID},
                null,
                null,
                null,
                null,
                null);

        if(c.moveToFirst()){
            while(!c.isAfterLast()){
                allIds.add(c.getInt(c.getColumnIndexOrThrow(CHOICE_COLS.ID)));
                c.moveToNext();
            }
        }
        c.close();

        return Collections.max(allIds);
    }

    public int getNumElements(){
        SQLiteDatabase db = getReadableDatabase();

        String[] pro = {CHOICE_COLS.ID, CHOICE_COLS.NAME, CHOICE_COLS.LAT, CHOICE_COLS.LONG,
                CHOICE_COLS.ADDRESS, CHOICE_COLS.RATING, CHOICE_COLS.IMGURL,
                CHOICE_COLS.FTYPE};

        Cursor c = db.query(CHOICE_TABLE_NAME,
                pro,
                null,
                null,
                null,
                null,
                null);

        return c.getCount();

    }

    public Choice popChoice(){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] pro = {CHOICE_COLS.ID, CHOICE_COLS.NAME, CHOICE_COLS.LAT, CHOICE_COLS.LONG,
                CHOICE_COLS.ADDRESS, CHOICE_COLS.RATING, CHOICE_COLS.IMGURL,
                CHOICE_COLS.FTYPE};

        int lowestId = getLowestID();
        String selection = CHOICE_COLS.ID + " = ?";
        String[] selArgs = {String.valueOf(lowestId)};

        Cursor c = db.query(CHOICE_TABLE_NAME,
                pro,
                selection,
                selArgs,
                null,
                null,
                null);

        c.moveToFirst();
        Choice r = new Choice();
        r.setName(c.getString(c.getColumnIndexOrThrow(CHOICE_COLS.NAME)));
        r.setLat(String.valueOf(c.getDouble(c.getColumnIndexOrThrow(CHOICE_COLS.LAT))));
        r.setLng(String.valueOf(c.getDouble(c.getColumnIndexOrThrow(CHOICE_COLS.LONG))));
        r.setRating(String.valueOf(c.getDouble(c.getColumnIndexOrThrow(CHOICE_COLS.RATING))));
        r.setAddress(c.getString(c.getColumnIndexOrThrow(CHOICE_COLS.ADDRESS)));
        r.setImgurl(c.getString(c.getColumnIndexOrThrow(CHOICE_COLS.IMGURL)));
        r.setFtype(FType.findType(c.getString(c.getColumnIndexOrThrow(CHOICE_COLS.FTYPE))));

        Log.d("DB TESTING", r.toString());

        db.delete(CHOICE_TABLE_NAME, CHOICE_COLS.ID + " = ?", new String[]{String.valueOf(lowestId)});
        db.close();

        return r;
    }

    public void printAll(){
        SQLiteDatabase db = getReadableDatabase();

        String[] pro = {CHOICE_COLS.ID, CHOICE_COLS.NAME, CHOICE_COLS.LAT, CHOICE_COLS.LONG,
                CHOICE_COLS.ADDRESS, CHOICE_COLS.RATING, CHOICE_COLS.IMGURL,
                CHOICE_COLS.FTYPE};

        Cursor c = db.query(CHOICE_TABLE_NAME,
                pro,
                null,
                null,
                null,
                null,
                null);

        if(c.moveToFirst()){
            while(!c.isAfterLast()){
                Choice r = new Choice();
                r.setName(c.getString(c.getColumnIndexOrThrow(CHOICE_COLS.NAME)));
                r.setLat(String.valueOf(c.getDouble(c.getColumnIndexOrThrow(CHOICE_COLS.LAT))));
                r.setLng(String.valueOf(c.getDouble(c.getColumnIndexOrThrow(CHOICE_COLS.LONG))));
                r.setRating(String.valueOf(c.getDouble(c.getColumnIndexOrThrow(CHOICE_COLS.RATING))));
                r.setAddress(c.getString(c.getColumnIndexOrThrow(CHOICE_COLS.ADDRESS)));
                r.setImgurl(c.getString(c.getColumnIndexOrThrow(CHOICE_COLS.IMGURL)));
                r.setFtype(FType.findType(c.getString(c.getColumnIndexOrThrow(CHOICE_COLS.FTYPE))));
                Log.d("DB CONTENTS", r.toString());
                c.moveToNext();
            }
        }

    }

    public int getTotalVisits(){
        String[] pro = {PREF_COLS.ID, PREF_COLS.NAME, PREF_COLS.VALUE};
        String selection = CHOICE_COLS.NAME + " = ?";
        String[] selArgs = {String.valueOf("total")};

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(PREF_TABLE_NAME,
                pro,
                selection,
                selArgs,
                null,
                null,
                null);

        if (c.moveToFirst()) {
            return c.getInt(c.getColumnIndexOrThrow(PREF_COLS.VALUE));
        } else {
            return -1;
        }
    }

    public int getNumVisitedForCategory(FType t){
        String[] pro = {PREF_COLS.ID, PREF_COLS.NAME, PREF_COLS.VALUE};
        String selection = CHOICE_COLS.NAME + " = ?";
        String[] selArgs = {String.valueOf(t.getDisplayName())};

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(PREF_TABLE_NAME,
                pro,
                selection,
                selArgs,
                null,
                null,
                null);

        if (c.moveToFirst()) {
            return c.getInt(c.getColumnIndexOrThrow(PREF_COLS.VALUE));
        } else {
            return -1;
        }
    }

    public void incrementCategory(FType t){
        SQLiteDatabase db = this.getWritableDatabase();
        int currentValue = 0;
        String[] pro = {PREF_COLS.ID, PREF_COLS.NAME, PREF_COLS.VALUE};
        String selection = PREF_COLS.NAME + " = ?";
        String[] selArgs = {String.valueOf(t.getDisplayName())};

        Cursor c = db.query(PREF_TABLE_NAME,
                pro,
                selection,
                selArgs,
                null,
                null,
                null);

        if(c.moveToFirst()){
            currentValue = c.getInt(c.getColumnIndexOrThrow(PREF_COLS.VALUE));
        }

        ContentValues cv = new ContentValues();
        cv.put(PREF_COLS.NAME, t.getDisplayName());
        cv.put(PREF_COLS.VALUE, currentValue+1);

        db.update(PREF_TABLE_NAME, cv, PREF_COLS.NAME + " = ?", new String[]{t.getDisplayName()});
        db.close();
    }


}
