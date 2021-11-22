package com.example.bee_v03;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class SelectViewModel extends AndroidViewModel {
    private BeeRepository repository;
    private LiveData<List<HivesLocation>> allLocations;
    private LiveData<List<Hive>> allHives;

    //region Constructor
    public SelectViewModel(@NonNull Application application) {
        super(application);
        repository = new BeeRepository(application);
        allLocations = repository.getAllLocations();
        allHives = repository.getAllHives();
    }
    //endregion

    //region HivesLocation methods
    public void insert(HivesLocation hivesLocation) {
        repository.insert(hivesLocation);
    }

    public void update(HivesLocation hivesLocation) {
        repository.update(hivesLocation);
    }

    public void delete(HivesLocation hivesLocation) {
        repository.delete(hivesLocation);
    }

    public LiveData<List<HivesLocation>> getAllLocations() {
        return allLocations;
    }

    public LiveData<List<HivesLocation>> getLocationIdByName(String name) {
        return repository.getLocationIdByName(name);
    }
    //endregion

    //region Hive methods
    public void insert(Hive hive) {
        repository.insert(hive);
    }

    public void update(Hive hive) {
        repository.update(hive);
    }

    public void delete(Hive hive) {
        repository.delete(hive);
    }

    public LiveData<List<Hive>> getAllHives() {
        return allHives;
    }

    public LiveData<List<Hive>> getHivesByLocationId(int id) {
        return repository.getHivesByLocationId(id);
    }
    //endregion
}
