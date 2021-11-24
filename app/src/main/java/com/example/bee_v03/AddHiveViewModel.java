package com.example.bee_v03;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class AddHiveViewModel extends AndroidViewModel {
    BeeRepository repository;

    public AddHiveViewModel(@NonNull Application application) {
        super(application);
        repository = new BeeRepository(application);
    }

    public LiveData<List<HivesLocation>> getAllLocations() {
        return repository.getAllLocations();
    }

    public void insert(Hive hive) {
        repository.insert(hive);
    }
}
