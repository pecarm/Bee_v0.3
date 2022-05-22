package com.example.bee_v03;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class StatsHiveAdapter extends FragmentStateAdapter {

    private List<Hive> allHives;
    private List<Record> allRecords;
    private List<Alert> allAlerts;
    private List<HivesLocation> allLocations;
    private List<HoneyHarvest> allHarvests;
    private int idHive;
    private String from, to;

    public StatsHiveAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<Hive> allHives, List<Record> allRecords, List<Alert> allAlerts,
                            List<HivesLocation> allLocations, List<HoneyHarvest> allHarvests, int idHive, String from, String to) {
        super(fragmentManager, lifecycle);
        this.allHives = allHives;
        this.allRecords = allRecords;
        this.allAlerts = allAlerts;
        this.allLocations = allLocations;
        this.allHarvests = allHarvests;
        this.idHive = idHive;
        this.from = from;
        this.to = to;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return StatsAlertHiveFragment.newInstance(allAlerts, idHive, from, to);
            case 1:
                return StatsRecordHiveFragment.newInstance(allRecords, idHive, from, to);
            case 2:
                return StatsHarvestHiveFragment.newInstance(allHarvests, idHive, from, to);
            case 3:
                return InfoFragment.newInstance(allHives, allLocations, idHive);
            default:
                return InfoFragment.newInstance(allHives, allLocations, idHive);
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
