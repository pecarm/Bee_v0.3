package com.example.bee_v03;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class ShowAlertDialogViewModel extends AndroidViewModel {
    BeeRepository repository;

    public ShowAlertDialogViewModel(@NonNull Application application) {
        super(application);
        this.repository = new BeeRepository(application);
    }

    public void update(Alert alert) {
        repository.update(alert);
    }
}
