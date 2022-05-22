package com.example.bee_v03;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.stream.Collectors;

public class StatsActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private List<HivesLocation> allLocations;
    private List<Hive> allHives;
    private List<Record> allRecords;
    private List<Alert> allAlerts;
    private List<HoneyHarvest> allHarvests;
    private StatsAdapter adapter;
    private int idLocation;
    private String from, to;
    private boolean includeArchive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idLocation = extras.getInt("ID_LOCATION");
            from = extras.getString("FROM");
            to = extras.getString("TO");
            includeArchive = extras.getBoolean("INCLUDE_ARCHIVE");
        }

        ObjectViewModel viewModel = new ViewModelProvider(this).get(ObjectViewModel.class);

        tabLayout = (TabLayout) findViewById(R.id.tabLayoutStats);
        viewPager = (ViewPager2) findViewById(R.id.viewPagerStats);

        FragmentManager fm = getSupportFragmentManager();

        tabLayout.addTab(tabLayout.newTab().setText("Upozornění"));
        tabLayout.addTab(tabLayout.newTab().setText("Záznamy"));
        tabLayout.addTab(tabLayout.newTab().setText("Medobraní"));
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        viewPager.setOffscreenPageLimit(100);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        //region Observers
        viewModel.getAllLocations().observe(this, new Observer<List<HivesLocation>>() {
            @Override
            public void onChanged(List<HivesLocation> locations) {
                allLocations = locations;
                if (locations != null) {
                    adapter = new StatsAdapter(fm, getLifecycle(), allHives,allRecords,allAlerts,allLocations, allHarvests, idLocation, from, to, includeArchive);
                    try {
                        viewPager.setAdapter(adapter);
                        if (idLocation == -1) {
                            getSupportActionBar().setTitle("Statistiky všeho");
                        } else {
                            getSupportActionBar().setTitle("Statistiky lokality - " + allLocations.stream().filter(location -> location.getId_location()==idLocation).collect(Collectors.toList()).get(0).getName());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        viewModel.getAllHives().observe(this, new Observer<List<Hive>>() {
            @Override
            public void onChanged(List<Hive> hives) {
                allHives = hives;
                if (allHives != null) {
                    adapter = new StatsAdapter(fm, getLifecycle(), allHives,allRecords,allAlerts,allLocations, allHarvests, idLocation, from, to, includeArchive);
                    try {
                        viewPager.setAdapter(adapter);
                        if (idLocation == -1) {
                            getSupportActionBar().setTitle("Statistiky všeho");
                        } else {
                            getSupportActionBar().setTitle("Statistiky lokality - " + allLocations.stream().filter(location -> location.getId_location()==idLocation).collect(Collectors.toList()).get(0).getName());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        viewModel.getAllRecords().observe(this, new Observer<List<Record>>() {
            @Override
            public void onChanged(List<Record> records) {
                allRecords = records;
                if (allRecords != null) {
                    adapter = new StatsAdapter(fm, getLifecycle(), allHives,allRecords,allAlerts,allLocations, allHarvests, idLocation, from, to, includeArchive);
                    try {
                        viewPager.setAdapter(adapter);
                        if (idLocation == -1) {
                            getSupportActionBar().setTitle("Statistiky všeho");
                        } else {
                            getSupportActionBar().setTitle("Statistiky lokality - " + allLocations.stream().filter(location -> location.getId_location()==idLocation).collect(Collectors.toList()).get(0).getName());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        viewModel.getAllAlerts().observe(this, new Observer<List<Alert>>() {
            @Override
            public void onChanged(List<Alert> alerts) {
                allAlerts = alerts;
                if (allAlerts != null) {
                    adapter = new StatsAdapter(fm, getLifecycle(), allHives,allRecords,allAlerts,allLocations, allHarvests, idLocation, from, to, includeArchive);
                    try {
                        viewPager.setAdapter(adapter);
                        if (idLocation == -1) {
                            getSupportActionBar().setTitle("Statistiky všeho");
                        } else {
                            getSupportActionBar().setTitle("Statistiky lokality - " + allLocations.stream().filter(location -> location.getId_location()==idLocation).collect(Collectors.toList()).get(0).getName());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        viewModel.getAllHarvests().observe(this, new Observer<List<HoneyHarvest>>() {
            @Override
            public void onChanged(List<HoneyHarvest> honeyHarvests) {
                allHarvests = honeyHarvests;
                if (allHarvests != null) {
                    adapter = new StatsAdapter(fm, getLifecycle(), allHives,allRecords,allAlerts,allLocations, allHarvests, idLocation, from, to, includeArchive);
                    try {
                        viewPager.setAdapter(adapter);
                        if (idLocation == -1) {
                            getSupportActionBar().setTitle("Statistiky všeho");
                        } else {
                            getSupportActionBar().setTitle("Statistiky lokality - " + allLocations.stream().filter(location -> location.getId_location()==idLocation).collect(Collectors.toList()).get(0).getName());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //endregion
    }
}