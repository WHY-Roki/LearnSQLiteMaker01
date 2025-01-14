package com.roki.learnsqlitemakers01;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Markers.db";
    public static final String TABLE_NAME = "markers";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, SCHOOL_NAME TEXT, ADDRESS TEXT, LATITUDE REAL, LONGITUDE REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean createMarker(String name, String address, double latitude, double longitude) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("SCHOOL_NAME", name);
        contentValues.put("ADDRESS", address);
        contentValues.put("LATITUDE", latitude);
        contentValues.put("LONGITUDE", longitude);
        long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Cursor readAllMarkers() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
    // update
    public boolean updateMarker(String originalName, String newName, String address, double latitude, double longitude) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("SCHOOL_NAME", newName);
        contentValues.put("ADDRESS", address);
        contentValues.put("LATITUDE", latitude);
        contentValues.put("LONGITUDE", longitude);
        int result = sqLiteDatabase.update(TABLE_NAME, contentValues, "SCHOOL_NAME=?", new String[]{originalName});
        return result > 0;
    }

    public boolean deleteMarkerByName(String name) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int result = sqLiteDatabase.delete(TABLE_NAME, "SCHOOL_NAME=?", new String[]{name});
        return result > 0;
    }


}
