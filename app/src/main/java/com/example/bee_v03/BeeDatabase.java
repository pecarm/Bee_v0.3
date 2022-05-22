package com.example.bee_v03;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {HivesLocation.class, Hive.class, Record.class, Alert.class, HoneyHarvest.class}, version = 2)
public abstract class BeeDatabase extends RoomDatabase {

    private static BeeDatabase instance;

    public abstract HivesLocationDao locationDao();
    public abstract HiveDao hiveDao();
    public abstract RecordDao recordDao();
    public abstract AlertDao alertDao();
    public abstract HoneyHarvestDao honeyHarvestDao();

    public static synchronized BeeDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    BeeDatabase.class,"bee_database")
                    .fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
