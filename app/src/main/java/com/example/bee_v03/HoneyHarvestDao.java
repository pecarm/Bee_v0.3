package com.example.bee_v03;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface HoneyHarvestDao {
    @Insert
    void insert(HoneyHarvest harvest);

    @Update
    void update(HoneyHarvest harvest);

    @Delete
    void delete(HoneyHarvest harvest);

    @Query("SELECT * FROM honey_harvest_table")
    LiveData<List<HoneyHarvest>> getAllHoneyHarvests();

}
