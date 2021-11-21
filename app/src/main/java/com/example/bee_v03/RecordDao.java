package com.example.bee_v03;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RecordDao {

    @Insert
    void insert(Record record);

    @Update
    void update(Record record);

    @Delete
    void delete(Record record);

    @Query("SELECT * FROM record_table")
    LiveData<List<Record>> getAllRecords();

    //colon because we use parameter passed in the function
    @Query("SELECT * FROM record_table WHERE id_hive = :id")
    LiveData<List<Record>> getRecordsByHive(int id);

    @Query("SELECT * FROM record_table " +
            "INNER JOIN hive_table ON hive_table.id_hive = record_table.id_hive " +
            "WHERE hive_table.id_location = :id")
    LiveData<List<Record>> getRecordsByLocation(int id);
}
