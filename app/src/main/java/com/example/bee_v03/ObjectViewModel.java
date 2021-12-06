package com.example.bee_v03;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ObjectViewModel extends AndroidViewModel {
    private BeeRepository repository;
    LiveData<List<Hive>> allHives;
    LiveData<List<Record>> allRecords;
    LiveData<List<Alert>> allAlerts;

    public ObjectViewModel(@NonNull Application application) {
        super(application);
        this.repository = new BeeRepository(application);
        allHives = repository.getAllHives();
        allRecords = repository.getAllRecords();
        allAlerts = repository.getAllAlerts();
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
}