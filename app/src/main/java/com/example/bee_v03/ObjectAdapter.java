package com.example.bee_v03;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class ObjectAdapter extends FragmentStateAdapter {

    private List<Hive> allHives;
    private List<Record> allRecords;
    private List<Alert> allAlerts;
    private int idHive;

    public ObjectAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<Hive> allHives, List<Record> allRecords, List<Alert> allAlerts, int idHive) {
        super(fragmentManager, lifecycle);
        this.allHives = allHives;
        this.allRecords = allRecords;
        this.allAlerts = allAlerts;
        this.idHive = idHive;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return AlertsFragment.newInstance(allAlerts, idHive);
            case 1:
                return HistoryFragment.newInstance(allRecords, idHive);
            case 2:
                return StatsFragment.newInstance(allHives, idHive);
            default:
                return new AlertsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}