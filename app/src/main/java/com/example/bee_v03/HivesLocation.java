package com.example.bee_v03;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import android.location.Location;

@Entity(tableName = "hives_location_table")
public class HivesLocation {

    @PrimaryKey(autoGenerate = true)
    private int id_location;
    private String name;
    private String latitude;
    private String longitude;

    //region Constructor
    public HivesLocation(String name, String latitude, String longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    //endregion

    //region Getters & setters
    public int getId_location() {
        return id_location;
    }

    public void setId_location(int id_location) {
        this.id_location = id_location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    //endregion

    //this is for AddHiveActivity and filling up the spinner with ArrayAdapter
    @Override
    public String toString() {
        return name;
    }
}
