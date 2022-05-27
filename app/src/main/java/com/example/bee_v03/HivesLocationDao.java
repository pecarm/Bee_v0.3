package com.example.bee_v03;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface HivesLocationDao {

    @Insert
    void insert(HivesLocation location);

    @Update
    void update(HivesLocation location);

    @Delete
    void delete(HivesLocation location);

    @Query("SELECT * FROM hives_location_table")
    LiveData<List<HivesLocation>> getAllLocations();
}