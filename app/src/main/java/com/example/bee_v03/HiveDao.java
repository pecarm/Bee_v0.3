package com.example.bee_v03;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface HiveDao {

    @Insert
    void insert(Hive hive);

    @Update
    void update(Hive hive);

    @Delete
    void delete(Hive hive);

    @Query("SELECT * FROM hive_table")
    LiveData<List<Hive>> getAllHives();

    //colon because we use parameter passed in the function
    @Query("SELECT * FROM hive_table WHERE id_location = :id")
    LiveData<List<Hive>> getHivesByLocation(int id);

    //I just fucking hope this works...
    @Query("SELECT name FROM hive_table WHERE id_hive = :id")
    String getHiveName(int id);
}
