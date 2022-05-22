package com.example.bee_v03;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class AddHoneyHarvestViewModel extends AndroidViewModel {
    BeeRepository repository;

    public AddHoneyHarvestViewModel(@NonNull Application application) {
        super(application);
        repository = new BeeRepository(application);
    }

    public void insert(HoneyHarvest harvest) {
        repository.insert(harvest);
    }
}
