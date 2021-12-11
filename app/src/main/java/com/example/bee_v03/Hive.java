package com.example.bee_v03;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "hive_table", foreignKeys = @ForeignKey(entity = HivesLocation.class, parentColumns = "id_location", childColumns = "id_location"))
public class Hive implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id_hive;
    private int id_location;
    private String name;
    private int row;
    private int agresivnost;
    private int stav_zasob;
    private int mezerovitost_plodu;
    private int sila_vcelstva;
    private int stavebni_pud;
    private int bodavost;
    private int slidivost;

    //region Constructor
    public Hive(int id_location, String name, int row, int agresivnost, int stav_zasob, int mezerovitost_plodu, int sila_vcelstva, int stavebni_pud, int bodavost, int slidivost) {
        this.id_location = id_location;
        this.name = name;
        this.row = row;
        this.agresivnost = agresivnost;
        this.stav_zasob = stav_zasob;
        this.mezerovitost_plodu = mezerovitost_plodu;
        this.sila_vcelstva = sila_vcelstva;
        this.stavebni_pud = stavebni_pud;
        this.bodavost = bodavost;
        this.slidivost = slidivost;
    }
    //endregion

    //region Getters & setters
    public int getId_hive() {
        return id_hive;
    }

    public void setId_hive(int id_hive) {
        this.id_hive = id_hive;
    }

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

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getAgresivnost() {
        return agresivnost;
    }

    public void setAgresivnost(int agresivnost) {
        this.agresivnost = agresivnost;
    }

    public int getStav_zasob() {
        return stav_zasob;
    }

    public void setStav_zasob(int stav_zasob) {
        this.stav_zasob = stav_zasob;
    }

    public int getMezerovitost_plodu() {
        return mezerovitost_plodu;
    }

    public void setMezerovitost_plodu(int mezerovitost_plodu) {
        this.mezerovitost_plodu = mezerovitost_plodu;
    }

    public int getSila_vcelstva() {
        return sila_vcelstva;
    }

    public void setSila_vcelstva(int sila_vcelstva) {
        this.sila_vcelstva = sila_vcelstva;
    }

    public int getStavebni_pud() {
        return stavebni_pud;
    }

    public void setStavebni_pud(int stavebni_pud) {
        this.stavebni_pud = stavebni_pud;
    }

    public int getBodavost() {
        return bodavost;
    }

    public void setBodavost(int bodavost) {
        this.bodavost = bodavost;
    }

    public int getSlidivost() {
        return slidivost;
    }

    public void setSlidivost(int slidivost) {
        this.slidivost = slidivost;
    }
    //endregion

    @Override
    public String toString() {
        return name;
    }
}
