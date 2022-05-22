package com.example.bee_v03;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "hive_table", foreignKeys = @ForeignKey(onDelete = ForeignKey.CASCADE, entity = HivesLocation.class, parentColumns = "id_location", childColumns = "id_location"))
public class Hive implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id_hive;
    private int id_location;
    private String name;
    private int row;
    private int aggressivity;
    private int hive_strength;
    private int build_instinct;
    private int stinging_instinct;
    private int exploration_instinct;
    private int cleaning_instinct;
    private int mother_year;
    private boolean archived;
    //instead of complete deletion it just gets moved to an archive where the stats can still be seen, but CANNOT be added or removed anything, except for the whole thing

    //region Constructor
    public Hive(int id_location,
                String name,
                int row,
                int aggressivity,
                int hive_strength,
                int build_instinct,
                int stinging_instinct,
                int exploration_instinct,
                int cleaning_instinct,
                int mother_year,
                boolean archived) {
        this.id_location = id_location;
        this.name = name;
        this.row = row;
        this.aggressivity = aggressivity;
        this.hive_strength = hive_strength;
        this.build_instinct = build_instinct;
        this.stinging_instinct = stinging_instinct;
        this.exploration_instinct = exploration_instinct;
        this.cleaning_instinct = cleaning_instinct;
        this.mother_year = mother_year;
        this.archived = archived;
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

    public int getAggressivity() {
        return aggressivity;
    }

    public void setAggressivity(int aggressivity) {
        this.aggressivity = aggressivity;
    }

    public int getHive_strength() {
        return hive_strength;
    }

    public void setHive_strength(int hive_strength) {
        this.hive_strength = hive_strength;
    }

    public int getBuild_instinct() {
        return build_instinct;
    }

    public void setBuild_instinct(int build_instinct) {
        this.build_instinct = build_instinct;
    }

    public int getStinging_instinct() {
        return stinging_instinct;
    }

    public void setStinging_instinct(int stinging_instinct) {
        this.stinging_instinct = stinging_instinct;
    }

    public int getExploration_instinct() {
        return exploration_instinct;
    }

    public void setExploration_instinct(int exploration_instinct) {
        this.exploration_instinct = exploration_instinct;
    }

    public int getCleaning_instinct() {
        return cleaning_instinct;
    }

    public void setCleaning_instinct(int cleaning_instinct) {
        this.cleaning_instinct = cleaning_instinct;
    }

    public int getMother_year() {
        return mother_year;
    }

    public void setMother_year(int mother_year) {
        this.mother_year = mother_year;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }
//endregion

    @Override
    public String toString() {
        return name;
    }
}
