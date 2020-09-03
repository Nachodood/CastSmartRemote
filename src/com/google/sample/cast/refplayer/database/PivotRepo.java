package com.google.sample.cast.refplayer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/*
Parts of code from:
Dr. Tom Owen: CSC306    TVYM
Swansea University
*/

public class PivotRepo {

        private DBHelper dbHelper;

        public PivotRepo(Context context) {
            dbHelper = new DBHelper(context);
        }

        public int insert(PivotDeviceProfileDBItem pivotDeviceProfileDBItem) {

            //Open connection to write data
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(PivotDeviceProfileDBItem.KEY_DEVICE_ID, pivotDeviceProfileDBItem.device_ID);
            values.put(PivotDeviceProfileDBItem.KEY_DEVICE_NAME, pivotDeviceProfileDBItem.deviceName);

            values.put(PivotDeviceProfileDBItem.KEY_PROFILE_ID, pivotDeviceProfileDBItem.profile_ID);
            values.put(PivotDeviceProfileDBItem.KEY_PROFILE_NAME, pivotDeviceProfileDBItem.profileName);

            values.put(PivotDeviceProfileDBItem.KEY_BEARING, pivotDeviceProfileDBItem.bearing);

            // Inserting Row
            long pivot_Id = db.insert(PivotDeviceProfileDBItem.TABLE, null, values);
            db.close(); // Closing database connection
            return (int) pivot_Id;

        }

        public void delete(int pivot_Id) {

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            // It's a good practice to use parameter ?, instead of concatenate string
            db.delete(PivotDeviceProfileDBItem.TABLE, PivotDeviceProfileDBItem.KEY_ID + "= ?", new String[] { String.valueOf(pivot_Id) });
            db.close(); // Closing database connection
        }

        public void update(PivotDeviceProfileDBItem pivotDeviceProfileDBItem) {

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(PivotDeviceProfileDBItem.KEY_DEVICE_NAME, pivotDeviceProfileDBItem.deviceName);
            values.put(PivotDeviceProfileDBItem.KEY_PROFILE_NAME, pivotDeviceProfileDBItem.profileName);
            values.put(PivotDeviceProfileDBItem.KEY_BEARING, pivotDeviceProfileDBItem.bearing);
            // It's a good practice to use parameter ?, instead of concatenate string
            db.update(PivotDeviceProfileDBItem.TABLE, values, PivotDeviceProfileDBItem.KEY_ID + "= ?", new String[] { (pivotDeviceProfileDBItem.KEY_ID) });
            db.close(); // Closing database connection
        }

        public ArrayList<HashMap<String, String>> getPivotList() {
            //Open connection to read only
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String selectQuery =  "SELECT  " +
                    PivotDeviceProfileDBItem.KEY_ID + "," +
                    PivotDeviceProfileDBItem.KEY_PROFILE_ID + "," +
                    PivotDeviceProfileDBItem.KEY_DEVICE_ID + "," +
                    PivotDeviceProfileDBItem.KEY_DEVICE_NAME + "," +
                    PivotDeviceProfileDBItem.KEY_PROFILE_NAME + "," +
                    PivotDeviceProfileDBItem.KEY_BEARING +
                    " FROM " + PivotDeviceProfileDBItem.TABLE;

            //Student student = new Student();
            ArrayList<HashMap<String, String>> pivotList = new ArrayList<>();

            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list

            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> pivot = new HashMap<>();
                    pivot.put("id", cursor.getString(cursor.getColumnIndex(PivotDeviceProfileDBItem.KEY_ID)));
                    pivot.put("deviceId", cursor.getString(cursor.getColumnIndex(PivotDeviceProfileDBItem.KEY_DEVICE_ID)));
                    pivot.put("profileId", cursor.getString(cursor.getColumnIndex(PivotDeviceProfileDBItem.KEY_PROFILE_ID)));
                    pivot.put("deviceName", cursor.getString(cursor.getColumnIndex(PivotDeviceProfileDBItem.KEY_DEVICE_NAME)));
                    pivot.put("profileName", cursor.getString(cursor.getColumnIndex(PivotDeviceProfileDBItem.KEY_PROFILE_NAME)));
                    pivot.put("bearing", cursor.getString(cursor.getColumnIndex(PivotDeviceProfileDBItem.KEY_BEARING)));
                    pivotList.add(pivot);

                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
            return pivotList;

        }

        public PivotDeviceProfileDBItem getPivotById(int Id){
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String selectQuery =  "SELECT  " +
                    PivotDeviceProfileDBItem.KEY_ID + "," +
                    PivotDeviceProfileDBItem.KEY_DEVICE_ID +
                    " FROM " + PivotDeviceProfileDBItem.TABLE
                    + " WHERE " +
                    PivotDeviceProfileDBItem.KEY_ID + "=?";// It's a good practice to use parameter ?, instead of concatenate string

            int iCount =0;
            PivotDeviceProfileDBItem pivotDBItem = new PivotDeviceProfileDBItem();

            Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );

            if (cursor.moveToFirst()) {
                do {
                    pivotDBItem.pivot_ID =cursor.getInt(cursor.getColumnIndex(PivotDeviceProfileDBItem.KEY_ID));
                    //pivotDBItem.name =cursor.getString(cursor.getColumnIndex(PivotDeviceProfileDBItem.KEY_name));

                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
            return pivotDBItem;
        }


}
