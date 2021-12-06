package com.example.bee_v03;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "alert_table", foreignKeys = @ForeignKey(entity = Hive.class, parentColumns = "id_hive", childColumns = "id_hive"))
public class Alert implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id_alert;
    private int id_hive;
    private int severity;
    private String date;
    private String text;

    //region Constructor
    public Alert(int id_hive, int severity, String date, String text) {
        this.id_hive = id_hive;
        this.severity = severity;
        this.date = date;
        this.text = text;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    //endregion
}
