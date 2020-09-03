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

public class DeviceRepo {
    private DBHelper dbHelper;

    public DeviceRepo(Context context) {
        dbHelper = new DBHelper(context);
    }

    public int insert(DeviceDBItem deviceDBItem) {

        //Open connection to write data
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DeviceDBItem.KEY_name, deviceDBItem.name);

        // Inserting Row
        long device_Id = db.insert(DeviceDBItem.TABLE, null, values);
        db.close(); // Closing database connection
        return (int) device_Id;
    }

    public void delete(int device_Id) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete(DeviceDBItem.TABLE, DeviceDBItem.KEY_ID + "= ?", new String[] { String.valueOf(device_Id) });
        db.close(); // Closing database connection
    }

    public void update(DeviceDBItem deviceDBItem) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DeviceDBItem.KEY_name, deviceDBItem.name);

        // It's a good practice to use parameter ?, instead of concatenate string
        db.update(DeviceDBItem.TABLE, values, DeviceDBItem.KEY_ID + "= ?", new String[] { String.valueOf(deviceDBItem.device_ID) });
        db.close(); // Closing database connection
    }

    public ArrayList<HashMap<String, String>> getDeviceList() {
        //Open connection to read only
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                DeviceDBItem.KEY_ID + "," +
                DeviceDBItem.KEY_name +
                " FROM " + DeviceDBItem.TABLE;

        //Student student = new Student();
        ArrayList<HashMap<String, String>> deviceList = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> device = new HashMap<>();
                device.put("id", cursor.getString(cursor.getColumnIndex(DeviceDBItem.KEY_ID)));
                device.put("name", cursor.getString(cursor.getColumnIndex(DeviceDBItem.KEY_name)));
                deviceList.add(device);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return deviceList;

    }

    public DeviceDBItem getDeviceById(int Id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                DeviceDBItem.KEY_ID + "," +
                DeviceDBItem.KEY_name +
                " FROM " + DeviceDBItem.TABLE
                + " WHERE " +
                DeviceDBItem.KEY_ID + "=?";// It's a good practice to use parameter ?, instead of concatenate string

        int iCount =0;
        DeviceDBItem deviceDBItem = new DeviceDBItem();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );

        if (cursor.moveToFirst()) {
            do {
                deviceDBItem.device_ID =cursor.getInt(cursor.getColumnIndex(DeviceDBItem.KEY_ID));
                deviceDBItem.name =cursor.getString(cursor.getColumnIndex(DeviceDBItem.KEY_name));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return deviceDBItem;
    }

}