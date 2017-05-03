package com.example.marcordonez.whatshouldweeat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.util.Log;

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

        public FType valueof(String s){
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
         * 20 = Mexican Index
         * 21 = Italian Index
         * 22 = Pizza Index
         * 23 = Chinese Index
         * 24 = Sushi Index
         * 25 = Breakfast Index
         * 26 = Thai Index
         * 27 = Indian Index
         * 28 = Hamburger Index
         * 29 = Hotdog Index
         * 30 = Noodles Index
         * 31 = BBQ Index
         * 32 = Seafood Index
         * 33 = Steak Index
         * 34 = Wings Index
         * 35 = Vegan Index
         * 36 = Sandwich Index
         * 37 = Cajun Index
         * 38 = Fish Index
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
        db.execSQL(DROP_CHOICE_TABLE);
        db.execSQL(DROP_PREF_TABLE);
    }

    public void testAdd(){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CHOICE_COLS.NAME, "test1");
        cv.put(CHOICE_COLS.LAT, 100.0);
        cv.put(CHOICE_COLS.LONG, 100.0);
        cv.put(CHOICE_COLS.ADDRESS, "TEST ADDRESS");
        cv.put(CHOICE_COLS.IMGURL, "http://test.url");
        cv.put(CHOICE_COLS.RATING, 5.0);
        cv.put(CHOICE_COLS.FTYPE, "FISHY");

        db.insert(CHOICE_TABLE_NAME, null, cv);

        ContentValues cv2 = new ContentValues();
        cv2.put(CHOICE_COLS.NAME, "test2");
        cv2.put(CHOICE_COLS.LAT, 100.0);
        cv2.put(CHOICE_COLS.LONG, 100.0);
        cv2.put(CHOICE_COLS.ADDRESS, "TEST ADDRESS");
        cv2.put(CHOICE_COLS.IMGURL, "http://test.url");
        cv2.put(CHOICE_COLS.RATING, 5.0);
        cv2.put(CHOICE_COLS.FTYPE, "FISHY");

        db.insert(CHOICE_TABLE_NAME, null, cv2);
    }

    public void testRemove(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(CHOICE_TABLE_NAME, CHOICE_COLS.ID + " = ?", new String[]{String.valueOf(0)});
    }

    public void testGet(){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] pro = {CHOICE_COLS.ID, CHOICE_COLS.NAME, CHOICE_COLS.LAT, CHOICE_COLS.LONG,
                CHOICE_COLS.ADDRESS, CHOICE_COLS.RATING, CHOICE_COLS.IMGURL,
                CHOICE_COLS.FTYPE};

        String selection = CHOICE_COLS.ID + " = ?";
        String[] selArgs = {String.valueOf(0)};

        Cursor c = db.query(CHOICE_TABLE_NAME,
                pro,
                selection,
                selArgs,
                null,
                null,
                null);

        c.moveToFirst();

        Log.d("DB TESTING, obj at in 0", c.getString(c.getColumnIndexOrThrow(CHOICE_COLS.NAME)));
    }

    public void addChoice(Choice choice){
        // Calculate appropriate index to add
        if(choice != null){
            FType type = choice.getFtype();
            int idOfOffset = (type.getStartIndex()/20) + 20;
            int currentOffset;
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor c = db.query(PREF_TABLE_NAME,
                    new String[]{PREF_COLS.ID, PREF_COLS.NAME, PREF_COLS.VALUE},
                    PREF_COLS.ID + " = ?",
                    new String[]{String.valueOf(idOfOffset)},
                    null,
                    null,
                    null);

            if(c.moveToFirst()){
                currentOffset = c.getInt(c.getColumnIndexOrThrow(PREF_COLS.VALUE));
            }
        }
    }

    public Choice getChoice(int withID){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] pro = {CHOICE_COLS.ID, CHOICE_COLS.NAME, CHOICE_COLS.LAT, CHOICE_COLS.LONG,
                CHOICE_COLS.ADDRESS, CHOICE_COLS.RATING, CHOICE_COLS.IMGURL,
                CHOICE_COLS.FTYPE};

        String selection = CHOICE_COLS.ID + " = ?";
        String[] selArgs = {String.valueOf(withID)};

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
        r.setFtype(FType.valueOf(c.getString(c.getColumnIndexOrThrow(CHOICE_COLS.FTYPE))));

        return r;
    }

    public int getNumVisitedForCategory(FType t){
        int id = 0;
        switch(t){
            case MEXICAN:
                id = 1;
                break;
            case ITALIAN:
                id = 2;
                break;
            case PIZZA:
                id = 3;
                break;
            case CHINESE:
                id = 4;
                break;
            case SUSHI:
                id = 5;
                break;
            case BREAKFAST:
                id = 6;
                break;
            case THAI:
                id = 7;
                break;
            case INDIAN:
                id = 8;
                break;
            case HAMBURGER:
                id = 9;
                break;
            case HOTDOG:
                id = 10;
                break;
            case NOODLES:
                id = 11;
                break;
            case BBQ:
                id = 12;
                break;
            case SEAFOOD:
                id = 13;
                break;
            case STEAK:
                id = 14;
                break;
            case WINGS:
                id = 15;
                break;
            case VEGAN:
                id = 16;
                break;
            case SANDWICH:
                id = 17;
                break;
            case CAJUN:
                id = 18;
                break;
            case FISH:
                id = 19;
                break;
        }

        String[] pro = {PREF_COLS.ID, PREF_COLS.NAME, PREF_COLS.VALUE};
        String selection = CHOICE_COLS.ID + " = ?";
        String[] selArgs = {String.valueOf(id)};

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


}
