package com.example.bee_v03;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.sql.Date;

@Entity(tableName = "record_table", foreignKeys = @ForeignKey(onDelete = ForeignKey.CASCADE, entity = Hive.class, parentColumns = "id_hive", childColumns = "id_hive"))
public class Record implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id_record;
    private int id_hive;
    //region private region date
    private int year;
    private int month;
    private int day;
    //endregion
    private int resources_state;
    private int brood_integrity;
    private String feeding;
    private int bases;
    private String text;

    //region Constructor
    public Record(int id_hive, int year, int month, int day, int resources_state, int brood_integrity, String feeding, int bases, String text) {
        this.id_hive = id_hive;
        this.year = year;
        this.month = month;
        this.day = day;
        this.resources_state = resources_state;
        this.brood_integrity = brood_integrity;
        this.feeding = feeding;
        this.bases = bases;
        this.text = text;
    }
    //endregion

    //region Getters & setters
    public int getId_record() {
        return id_record;
    }

    public void setId_record(int id_record) {
        this.id_record = id_record;
    }

    public int getId_hive() {
        return id_hive;
    }

    public void setId_hive(int id_hive) {
        this.id_hive = id_hive;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getResources_state() {
        return resources_state;
    }

    public void setResources_state(int resource_state) {
        this.resources_state = resource_state;
    }

    public int getBrood_integrity() {
        return brood_integrity;
    }

    public void setBrood_integrity(int brood_integrity) {
        this.brood_integrity = brood_integrity;
    }

    public String getFeeding() {
        return feeding;
    }

    public void setFeeding(String feeding) {
        this.feeding = feeding;
    }

    public int getBases() {
        return bases;
    }

    public void setBases(int bases) {
        this.bases = bases;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    //endregion
}
