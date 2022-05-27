package com.example.bee_v03;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ShowHiveViewModel extends AndroidViewModel {
    private BeeRepository repository;
    LiveData<List<Hive>> allHives;
    LiveData<List<Record>> allRecords;
    LiveData<List<Alert>> allAlerts;
    LiveData<List<HivesLocation>> allLocations;
    LiveData<List<HoneyHarvest>> allHarvests;

    public ShowHiveViewModel(@NonNull Application application) {
        super(application);
        this.repository = new BeeRepository(application);
        allHives = repository.getAllHives();
        allRecords = repository.getAllRecords();
        allAlerts = repository.getAllAlerts();
        allLocations = repository.getAllLocations();
        allHarvests = repository.getAllHoneyHarvests();
    }

    public LiveData<List<Hive>> getAllHives() {
        return allHives;
    }

    public LiveData<List<Record>> getAllRecords() {
        return allRecords;
    }

    public LiveData<List<Alert>> getAllAlerts() {
        return allAlerts;
    }

    public LiveData<List<HivesLocation>> getAllLocations() {
        return allLocations;
    }

    public LiveData<List<HoneyHarvest>> getAllHarvests() {
        return allHarvests;
    }
}