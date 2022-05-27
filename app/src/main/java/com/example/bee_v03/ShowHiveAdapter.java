package com.example.bee_v03;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class ShowHiveAdapter extends FragmentStateAdapter {

    private List<Hive> allHives;
    private List<Record> allRecords;
    private List<Alert> allAlerts;
    private List<HivesLocation> allLocations;
    private List<HoneyHarvest> allHarvests;
    private int idHive;
    private boolean isArchive;

    public ShowHiveAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<Hive> allHives, List<Record> allRecords, List<Alert> allAlerts, List<HivesLocation> allLocations, List<HoneyHarvest> allHarvests, int idHive, boolean isArchive) {
        super(fragmentManager, lifecycle);
        this.allHives = allHives;
        this.allRecords = allRecords;
        this.allAlerts = allAlerts;
        this.allLocations = allLocations;
        this.allHarvests = allHarvests;
        this.idHive = idHive;
        this.isArchive = isArchive;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return AlertsFragment.newInstance(allAlerts, idHive, isArchive);
            case 1:
                return HistoryFragment.newInstance(allRecords, idHive);
            case 2:
                return HoneyHarvestFragment.newInstance(allHarvests, idHive);
            case 3:
                return InfoFragment.newInstance(allHives, allLocations, idHive);
            default:
                return new AlertsFragment();
        }

    }

    @Override
    public int getItemCount() {
        return 4;
    }
}