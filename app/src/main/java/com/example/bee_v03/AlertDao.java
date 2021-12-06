package com.example.bee_v03;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AlertDao {

    @Insert
    void insert(Alert alert);

    @Update
    void update(Alert alert);

    @Delete
    void delete(Alert alert);

    @Query("SELECT * FROM alert_table")
    LiveData<List<Alert>> getAllAlerts();
}
