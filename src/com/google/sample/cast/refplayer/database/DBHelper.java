package com.google.sample.cast.refplayer.database;

/*
Parts of code from:
Dr. Tom Owen: CSC306    TVYM
Swansea University
*/

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper  extends SQLiteOpenHelper {
    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private static final int DATABASE_VERSION = 12;

    // Database Name
    private static final String DATABASE_NAME = "application.db";

    public DBHelper(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //All necessary tables you like to create will create here

        String CREATE_TABLE_DEVICE = "CREATE TABLE " + DeviceDBItem.TABLE  + "("
                + DeviceDBItem.KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + DeviceDBItem.KEY_name + " TEXT )";

        db.execSQL(CREATE_TABLE_DEVICE);

        String CREATE_TABLE_LOCATION_PROFILES = "CREATE TABLE " + LocationProfileDBItem.TABLE  + "("
                + LocationProfileDBItem.KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + LocationProfileDBItem.KEY_name + " TEXT )";

        db.execSQL(CREATE_TABLE_LOCATION_PROFILES);

        String CREATE_TABLE_Pivot_DEVICE_PROFILES = "CREATE TABLE "
                + PivotDeviceProfileDBItem.TABLE            + "("
                + PivotDeviceProfileDBItem.KEY_ID           + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + PivotDeviceProfileDBItem.KEY_DEVICE_ID    + " INTEGER ,"
                + PivotDeviceProfileDBItem.KEY_PROFILE_ID   + " INTEGER REFERENCES " + LocationProfileDBItem.KEY_ID + " ON DELETE CASCADE ,"
                + PivotDeviceProfileDBItem.KEY_DEVICE_NAME  + " TEXT ,"
                + PivotDeviceProfileDBItem.KEY_PROFILE_NAME + " TEXT ,"
                + PivotDeviceProfileDBItem.KEY_BEARING      + " STRING )";

        db.execSQL(CREATE_TABLE_Pivot_DEVICE_PROFILES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone!!!
        db.execSQL("DROP TABLE IF EXISTS " + DeviceDBItem.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + LocationProfileDBItem.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PivotDeviceProfileDBItem.TABLE);
        // Create tables again
        onCreate(db);

    }

}