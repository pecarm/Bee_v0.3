package com.example.bee_v03;

import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "alert_table", foreignKeys = @ForeignKey(onDelete = ForeignKey.CASCADE, entity = Hive.class, parentColumns = "id_hive", childColumns = "id_hive"))
public class Alert implements Serializable{

    @PrimaryKey(autoGenerate = true)
    private int id_alert;
    private int id_hive;
    //1 high, 2 med, 3 low
    private int severity;
    //region private region date
    private int year;
    private int month;
    private int day;
    //endregion
    private String text;
    private boolean archived;

    //region Constructor
    public Alert(int id_hive, int severity, int year, int month, int day, String text, boolean archived) {
        this.id_hive = id_hive;
        this.severity = severity;
        this.year = year;
        this.month = month;
        this.day = day;
        this.text = text;
        this.archived = archived;
    }
    //endregion

    //region Getters and setters
    public int getId_alert() {
        return id_alert;
    }

    public void setId_alert(int id_alert) {
        this.id_alert = id_alert;
    }


    public int getId_hive() {
        return id_hive;
    }

    public void setId_hive(int id_hive) {
        this.id_hive = id_hive;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getSeverity() {
        return severity;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
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

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    //endregion
}
