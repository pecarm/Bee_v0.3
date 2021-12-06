package com.example.bee_v03;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class CustomAdapter extends FragmentStateAdapter {

    private List<Record> allRecords;
    private List<Hive> allHives;
    private List<Alert> allAlerts;

    public CustomAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<Record> allRecords, List<Hive> allHives, List<Alert> allAlerts) {
        super(fragmentManager, lifecycle);
        this.allRecords = allRecords;
        this.allHives = allHives;
        this.allAlerts = allAlerts;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case(0):
                return DashboardFragment.newInstance(allHives, allAlerts);
            case(1):
                return TimelineFragment.newInstance(allRecords, allHives);
            default: return new DashboardFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
