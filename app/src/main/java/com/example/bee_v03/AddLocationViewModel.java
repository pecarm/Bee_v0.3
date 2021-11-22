package com.example.bee_v03;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class AddLocationViewModel extends AndroidViewModel {
    BeeRepository repository;

    public AddLocationViewModel(@NonNull Application application) {
        super(application);
        repository = new BeeRepository(application);
    }

    public void insert(HivesLocation hivesLocation) {
        repository.insert(hivesLocation);
    }
}
