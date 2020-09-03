package com.google.sample.cast.refplayer.database;

public class PivotTable {

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
    public int device_ID;
    public int locationProfile_ID;
    public String deviceName;
    public String profileName;
    public int bearing;

        public PivotTable() {
        }

    public PivotTable(String deviceName, String profileName, int bearing) {
        this.deviceName = deviceName;
        this.profileName = profileName;
        this.bearing = bearing;
    }

    public int getDevice_ID() {
        return device_ID;
    }

    public void setDevice_ID(int device_ID) {
        this.device_ID = device_ID;
    }

    public int getLocationProfile_ID() {
        return locationProfile_ID;
    }

    public void setLocationProfile_ID(int locationProfile_ID) {
        this.locationProfile_ID = locationProfile_ID;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public int getBearing() {
        return bearing;
    }

    public void setBearing(int bearing) {
        this.bearing = bearing;
    }
}
