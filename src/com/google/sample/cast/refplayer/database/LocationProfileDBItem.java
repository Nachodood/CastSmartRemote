package com.google.sample.cast.refplayer.database;

/*
Parts of code from:
Dr. Tom Owen: CSC306    TVYM
Swansea University
*/

///////////////////////////////////////// OBJECT CLASS FOR SQL DATABASE /////////////////////////////////////////
public class LocationProfileDBItem {
    // Labels table name
    public static final String TABLE = "LocationProfile";

    // Labels Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_name = "name";

    // property help us to keep data
    public int locationProfile_ID;
    public String name;
}
