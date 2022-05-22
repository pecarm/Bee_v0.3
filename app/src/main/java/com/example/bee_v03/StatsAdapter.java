package com.example.bee_v03;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class StatsAdapter extends FragmentStateAdapter {

    private List<Hive> allHives;
    private List<Record> allRecords;
    private List<Alert> allAlerts;
    private List<HivesLocation> allLocations;
    private List<HoneyHarvest> allHarvests;
    private int idLocation;
    private String from, to;
    private boolean includeArchived;

    public StatsAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<Hive> allHives, List<Record> allRecords, List<Alert> allAlerts,
                        List<HivesLocation> allLocations, List<HoneyHarvest> allHarvests, int idLocation, String from, String to, boolean includeArchived) {
        super(fragmentManager, lifecycle);
        this.allHives = allHives;
        this.allRecords = allRecords;
        this.allAlerts = allAlerts;
        this.allLocations = allLocations;
        this.allHarvests = allHarvests;
        this.idLocation = idLocation;
        this.from = from;
        this.to = to;
        this.includeArchived = includeArchived;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return StatsAlertFragment.newInstance(allHives, allAlerts, idLocation, from, to, includeArchived);
            case 1:
                return StatsRecordFragment.newInstance(allHives, allRecords, idLocation, from, to, includeArchived);
            case 2:
                return StatsHarvestFragment.newInstance(allHives, allHarvests, idLocation,from, to, includeArchived);
            default:
                return StatsAlertFragment.newInstance(allHives, allAlerts, idLocation, from, to, includeArchived);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
