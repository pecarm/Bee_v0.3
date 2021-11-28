package com.example.bee_v03;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {
    BeeRepository repository;
    LiveData<List<HivesLocation>> allLocations;
    LiveData<List<Record>> allRecords;
    LiveData<List<Hive>> allHives;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        repository = new BeeRepository(application);
        allLocations = repository.getAllLocations();
        allRecords = repository.getAllRecords();
        allHives = repository.getAllHives();
    }

    public LiveData<List<Hive>> getAllHives() {
        return allHives;
    }

    public LiveData<List<HivesLocation>> getAllLocations() {
        return allLocations;
    }

    public LiveData<List<Record>> getAllRecords() {
        return allRecords;
    }

    public String getHiveName(int id) {
        return repository.getHiveName(id);
    }
}
