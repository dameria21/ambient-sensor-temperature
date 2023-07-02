package com.unisbank.sensorsshuhu2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TemperatureDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "temperature.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_TEMPERATURES = "temperatures";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TEMPERATURE = "temperature";

    public TemperatureDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_TEMPERATURES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TEMPERATURE + " TEXT" +
                ");";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEMPERATURES);
        onCreate(db);
    }

    public void saveTemperature(SQLiteDatabase db, String temperature) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEMPERATURE, temperature);
        db.insert(TABLE_TEMPERATURES, null, values);
    }

    public Cursor getAllTemperatures(SQLiteDatabase db) {
        return db.query(TABLE_TEMPERATURES, null, null, null, null, null, COLUMN_ID + " DESC");
    }

    public void deleteAllTemperatures(SQLiteDatabase db) {
        db.delete(TABLE_TEMPERATURES, null, null);
    }
}
