package com.example.bee_v03;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ObjectAdapter extends FragmentPagerAdapter {

    private Context context;
    int numberOfTabs;

    public ObjectAdapter(Context context, FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.context = context;
        this.numberOfTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case(0):
                AlertsFragment alertsFragment = new AlertsFragment();
                return alertsFragment;
            case(1):
                HistoryFragment historyFragment = new HistoryFragment();
                return historyFragment;
            case(2):
                StatsFragment statsFragment = new StatsFragment();
                return statsFragment;
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}