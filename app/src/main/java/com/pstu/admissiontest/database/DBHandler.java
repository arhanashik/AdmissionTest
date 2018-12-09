package com.pstu.admissiontest.database;

/**
 * Created by blackSpider on 11/08/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pstu.admissiontest.model.Item;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "admission_test.db";
    private static String TABLE_NAME = "";
    private static String PACKAGE_NAME = "";
    private static String DB_FILEPATH = "";

   //************* table parameters *******************//
    public final String TABLE_A_UNIT = "a_unit";
    public final String TABLE_B_UNIT = "b_unit";
    public final String TABLE_C_UNIT = "c_unit";
    public final String TABLE_SAVED_DATA = "saved_data";
    //Table Columns names
    private static final String KEY_SL = "_SL";
    private static final String KEY_ROLL = "roll";
    private static final String KEY_UNIT = "unit";
    private static final String KEY_START_ROLL = "start_roll";
    private static final String KEY_END_ROLL = "end_roll";
    private static final String KEY_CENTER = "center";
    private static final String KEY_FLOOR = "floor";
    private static final String KEY_HALL_NUMBER = "hall_number";
    private static final String KEY_ROOM_NUMBER = "room_number";
    private static final String KEY_ROOM_NAME = "room_name";
    private static final String KEY_TOTAL = "total";

    // Table create query
    String CREATE_A_UNIT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_A_UNIT
            + "("
            + KEY_SL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_HALL_NUMBER + " TEXT,"
            + KEY_ROOM_NUMBER + " TEXT,"
            + KEY_CENTER + " TEXT,"
            + KEY_ROOM_NAME + " TEXT,"
            + KEY_START_ROLL + " TEXT,"
            + KEY_END_ROLL + " TEXT,"
            + KEY_TOTAL + " TEXT" + ");";

    String CREATE_B_UNIT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_B_UNIT
            + "("
            + KEY_SL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_HALL_NUMBER + " TEXT,"
            + KEY_ROOM_NUMBER + " TEXT,"
            + KEY_CENTER + " TEXT,"
            + KEY_ROOM_NAME + " TEXT,"
            + KEY_START_ROLL + " TEXT,"
            + KEY_END_ROLL + " TEXT,"
            + KEY_TOTAL + " TEXT" + ");";

    String CREATE_C_UNIT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_C_UNIT
            + "("
            + KEY_SL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_HALL_NUMBER + " TEXT,"
            + KEY_ROOM_NUMBER + " TEXT,"
            + KEY_CENTER + " TEXT,"
            + KEY_ROOM_NAME + " TEXT,"
            + KEY_START_ROLL + " TEXT,"
            + KEY_END_ROLL + " TEXT,"
            + KEY_TOTAL + " TEXT" + ");";

    String CREATE_SAVED_DATA_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_SAVED_DATA
            + "("
            + KEY_SL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_HALL_NUMBER + " TEXT,"
            + KEY_ROOM_NUMBER + " TEXT,"
            + KEY_CENTER + " TEXT,"
            + KEY_ROOM_NAME + " TEXT,"
            + KEY_ROLL + " TEXT,"
            + KEY_UNIT + " TEXT" + ");";

    public static String getTableName()
    {
        return TABLE_NAME;
    }

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        PACKAGE_NAME = context.getPackageName();
        DB_FILEPATH = "/data/data/" + PACKAGE_NAME + "/databases/" + DATABASE_NAME;
    }

    public DBHandler(Context context, String TABLE_NAME) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        PACKAGE_NAME = context.getPackageName();
        DB_FILEPATH = "/data/data/" + PACKAGE_NAME + "/databases/" + DATABASE_NAME;
        this.TABLE_NAME = TABLE_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_A_UNIT_TABLE);
        db.execSQL(CREATE_B_UNIT_TABLE);
        db.execSQL(CREATE_C_UNIT_TABLE);
        db.execSQL(CREATE_SAVED_DATA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_A_UNIT + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_B_UNIT + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_C_UNIT + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVED_DATA + ";");
        // Creating tables again
        onCreate(db);
    }

    // Checking table has data or not
    public int getRowCount(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        int count = -1;
        Cursor cursor  = db.rawQuery("SELECT "+KEY_SL+" FROM " + tableName, null);

        count = cursor.getCount();
        cursor.close();
        db.close();

        return count;
    }

    public long insertItem(Item item, String tableName){
        TABLE_NAME = tableName;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_HALL_NUMBER, item.getHALL_NUMBER());
        values.put(KEY_ROOM_NUMBER, item.getROOM_NUMBER());
        values.put(KEY_CENTER, item.getCENTER());
        values.put(KEY_ROOM_NAME, item.getROOM_NAME());
        values.put(KEY_START_ROLL, item.getSTART_ROLL());
        values.put(KEY_END_ROLL, item.getEND_ROLL());
        values.put(KEY_TOTAL, item.getTOTAL());
        // Inserting Row
        long inserted = db.insert(TABLE_NAME, null, values);
        // Closing database connection
        db.close();

        return inserted;
    }

    // Getting all items
    public List<Item> getAllItem(String tableName) {
        TABLE_NAME = tableName;
        SQLiteDatabase db = this.getWritableDatabase();
        List<Item> allItem = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NAME + ";";

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.setHALL_NUMBER(cursor.getString(1));
                item.setROOM_NUMBER(cursor.getString(2));
                item.setCENTER(cursor.getString(3));
                item.setROOM_NAME(cursor.getString(4));
                item.setSTART_ROLL(cursor.getString(5));
                item.setEND_ROLL(cursor.getString(6));
                item.setTOTAL(cursor.getString(7));
                // Adding contact to list
                allItem.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return allItem;
    }

    public long saveData(Item item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_HALL_NUMBER, item.getHALL_NUMBER());
        values.put(KEY_ROOM_NUMBER, item.getROOM_NUMBER());
        values.put(KEY_CENTER, item.getCENTER());
        values.put(KEY_ROOM_NAME, item.getROOM_NAME());
        values.put(KEY_ROLL, item.getROLL());
        values.put(KEY_UNIT, item.getUNIT());
        // Inserting Row
        long inserted = db.insert(TABLE_SAVED_DATA, null, values);
        // Closing database connection
        db.close();

        return inserted;
    }


    public String isDataSaved(String key) {
        SQLiteDatabase db = this.getReadableDatabase();
        String res = "";
        Cursor cursor = db.query(TABLE_SAVED_DATA, null,
                KEY_ROLL + "=?", new String[] {key}, null, null, null, null);

        if (cursor != null){
            if(cursor.moveToFirst()){
                res = cursor.getString(cursor.getColumnIndex(KEY_ROLL));

            }
        }
        cursor.close();
        db.close();

        return res;
    }

    // Getting all saved data
    public List<Item> getAllSavedData() {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Item> allItem = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SAVED_DATA + ";";

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.set_SL(cursor.getInt(0));
                item.setHALL_NUMBER(cursor.getString(1));
                item.setROOM_NUMBER(cursor.getString(2));
                item.setCENTER(cursor.getString(3));
                item.setROOM_NAME(cursor.getString(4));
                item.setROLL(cursor.getString(5));
                item.setUNIT(cursor.getString(6));
                // Adding contact to list
                allItem.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return allItem;
    }

    public void deleteAllSavedData(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVED_DATA + ";");
        db.execSQL(CREATE_SAVED_DATA_TABLE);

        db.close();
    }

}
