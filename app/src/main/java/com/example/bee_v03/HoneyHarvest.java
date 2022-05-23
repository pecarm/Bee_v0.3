package com.example.bee_v03;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.time.LocalDate;

@Entity(tableName = "honey_harvest_table", foreignKeys = @ForeignKey(onDelete = ForeignKey.CASCADE, entity = Hive.class, parentColumns = "id_hive", childColumns = "id_hive"))
public class HoneyHarvest implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id_harvest;
    private int id_hive;
    //region private region date
    private int year;
    private int month;
    private int day;
    //endregion
    private double amount;
    private double water_content;
    private String type;
    private String text;

    //region Constructor
    public HoneyHarvest(int id_hive, int year, int month, int day, double amount, double water_content, String type, String text) {
        this.id_hive = id_hive;
        this.year = year;
        this.month = month;
        this.day = day;
        this.amount = amount;
        this.water_content = water_content;
        this.type = type;
        this.text = text;
    }
    //endregion

    //region Getters & setters

    public int getId_harvest() {
        return id_harvest;
    }

    public void setId_harvest(int id_harvest) {
        this.id_harvest = id_harvest;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getWater_content() {
        return water_content;
    }

    public void setWater_content(double water_content) {
        this.water_content = water_content;
    }

    public LocalDate getDate() {
        return LocalDate.of(this.getYear(), this.getMonth(), this.getDay());
    }

    //endregion
}
