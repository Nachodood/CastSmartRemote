package com.google.sample.cast.refplayer.database;

/*
Parts of code from:
Dr. Tom Owen: CSC306    TVYM
Swansea University
*/

public class LocationProfile {

    // Labels table name
    public static final String TABLE = "LocationProfile";

    // Labels Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_name = "name";

    // property help us to keep data
    public int locationProfile_ID;
    public String name;

    public LocationProfile() {
    }

    public LocationProfile(String name) {
        this.name = name;
    }

    public int getLocationProfilee_ID() { return locationProfile_ID; }

    public void setLocationProfile_ID(int device_ID) { this.locationProfile_ID = device_ID; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }
}
