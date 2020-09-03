package com.google.sample.cast.refplayer.database;

public class Device {

    // Labels table name
    public static final String TABLE = "Device";

    // Labels Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_name = "name";

    // property help us to keep data
    public int device_ID;
    public String name;

    public Device() {
    }

    public Device(String name) {
        this.name = name;
    }

    public int getDevice_ID() { return device_ID; }

    public void setDevice_ID(int device_ID) { this.device_ID = device_ID; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

}
