package com.google.sample.cast.refplayer.database;

///////////////////////////////////////// OBJECT CLASS FOR SQL DATABASE /////////////////////////////////////////
public class PivotDeviceProfileDBItem {
    // Labels table name
    public static final String TABLE = "PivotDeviceProfile";

    // Labels Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_DEVICE_ID = "deviceId";
    public static final String KEY_PROFILE_ID = "profileId";
    public static final String KEY_DEVICE_NAME = "deviceName";
    public static final String KEY_PROFILE_NAME = "profileName";
    public static final String KEY_BEARING = "bearing";

    // property help us to keep data
    public int pivot_ID;
    public int device_ID;
    public int profile_ID;
    public String deviceName;
    public String profileName;
    public int bearing;

}
