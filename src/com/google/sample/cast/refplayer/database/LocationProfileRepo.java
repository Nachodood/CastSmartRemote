package com.google.sample.cast.refplayer.database;

/*
Parts of code from:
Dr. Tom Owen: CSC306    TVYM
Swansea University
*/

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class LocationProfileRepo {
    private DBHelper dbHelper;

    public LocationProfileRepo(Context context) {
        dbHelper = new DBHelper(context);
    }

    public int insert(LocationProfileDBItem locationProfileDBItem) {

        //Open connection to write data
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LocationProfileDBItem.KEY_name, locationProfileDBItem.name);

        // Inserting Row
        long locationProfile_Id = db.insert(LocationProfileDBItem.TABLE, null, values);
        db.close(); // Closing database connection
        return (int) locationProfile_Id;
    }

    public void delete(int locationProfile_Id) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete(LocationProfileDBItem.TABLE, LocationProfileDBItem.KEY_ID + "= ?", new String[] { String.valueOf(locationProfile_Id) });
        db.close(); // Closing database connection
    }

    public void update(LocationProfileDBItem locationProfileDBItem) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(LocationProfileDBItem.KEY_name, locationProfileDBItem.name);

        // It's a good practice to use parameter ?, instead of concatenate string
        db.update(LocationProfileDBItem.TABLE, values, LocationProfileDBItem.KEY_ID + "= ?", new String[] { String.valueOf(locationProfileDBItem.locationProfile_ID) });
        db.close(); // Closing database connection
    }

    public ArrayList<HashMap<String, String>> getLocationProfileList() {
        //Open connection to read only
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                LocationProfileDBItem.KEY_ID + "," +
                LocationProfileDBItem.KEY_name +
                " FROM " + LocationProfileDBItem.TABLE;

        //Student student = new Student();
        ArrayList<HashMap<String, String>> locationProfileList = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> locationProfile = new HashMap<>();
                locationProfile.put("id", cursor.getString(cursor.getColumnIndex(LocationProfileDBItem.KEY_ID)));
                locationProfile.put("name", cursor.getString(cursor.getColumnIndex(LocationProfileDBItem.KEY_name)));
                locationProfileList.add(locationProfile);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return locationProfileList;

    }

    public LocationProfileDBItem getLocationProfileById(int Id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                LocationProfileDBItem.KEY_ID + "," +
                LocationProfileDBItem.KEY_name +
                " FROM " + LocationProfileDBItem.TABLE
                + " WHERE " +
                LocationProfileDBItem.KEY_ID + "=?";// It's a good practice to use parameter ?, instead of concatenate string

        int iCount =0;
        LocationProfileDBItem locationProfileDBItem = new LocationProfileDBItem();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );

        if (cursor.moveToFirst()) {
            do {
                locationProfileDBItem.locationProfile_ID =cursor.getInt(cursor.getColumnIndex(LocationProfileDBItem.KEY_ID));
                locationProfileDBItem.name =cursor.getString(cursor.getColumnIndex(LocationProfileDBItem.KEY_name));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return locationProfileDBItem;
    }

}
