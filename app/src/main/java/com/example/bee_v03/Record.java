package com.example.bee_v03;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.sql.Date;

@Entity(tableName = "record_table", foreignKeys = @ForeignKey(entity = Hive.class, parentColumns = "id_hive", childColumns = "id_hive"))
public class Record implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id_record;
    private int id_hive;
    private String date;
    private String text;

    //region Constructor
    public Record(int id_hive, String date, String text) {
        this.id_hive = id_hive;
        this.date = date;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    //endregion
}
