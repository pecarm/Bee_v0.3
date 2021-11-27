package com.example.bee_v03;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class AddRecordViewModel extends AndroidViewModel {
    BeeRepository repository;

    public AddRecordViewModel(@NonNull Application application) {
        super(application);
        repository = new BeeRepository(application);
    }

    public void insert(Record record) {
        repository.insert(record);
    }

    public LiveData<List<Hive>> getAllHives() {
        return repository.getAllHives();
    }
}
