package com.example.bee_v03;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class MainActivityViewModel extends AndroidViewModel {
    BeeRepository repository;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        repository = new BeeRepository(application);
    }
}
