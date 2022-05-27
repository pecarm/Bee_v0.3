package com.example.bee_v03;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.stream.Collectors;

public class StatsHiveActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private DrawerLayout drawer;
    private List<HivesLocation> allLocations;
    private List<Hive> allHives;
    private List<Record> allRecords;
    private List<Alert> allAlerts;
    private List<HoneyHarvest> allHarvests;
    private StatsHiveAdapter adapter;
    private int idHive;
    private String from;
    private String to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_hive);
        Toolbar toolbar = (Toolbar) findViewById(R.id.stats_hive_toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_stats_hive);
        NavigationView navigationView = findViewById(R.id.nav_view_stats_hive);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idHive = extras.getInt("HIVE_ID");
            from = extras.getString("FROM");
            to = extras.getString("TO");
        }

        ShowHiveViewModel viewModel = new ViewModelProvider(this).get(ShowHiveViewModel.class);

        tabLayout = (TabLayout) findViewById(R.id.tabLayoutStatsHive);
        viewPager = (ViewPager2) findViewById(R.id.viewPagerStatsHive);

        FragmentManager fm = getSupportFragmentManager();

        tabLayout.addTab(tabLayout.newTab().setText("Upozornění"));
        tabLayout.addTab(tabLayout.newTab().setText("Záznamy"));
        tabLayout.addTab(tabLayout.newTab().setText("Medobraní"));
        tabLayout.addTab(tabLayout.newTab().setText("Info"));
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        viewPager.setOffscreenPageLimit(100);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(@NonNull TabLayout.Tab tab) {
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
                    adapter = new StatsHiveAdapter(fm, getLifecycle(), allHives, allRecords, allAlerts, allLocations, allHarvests, idHive, from, to);
                    try {
                        viewPager.setAdapter(adapter);
                        toolbar.setTitle("Statistiky včelstva - " + allHives.stream().filter(hive -> hive.getId_hive() == idHive).collect(Collectors.toList()).get(0).getName());
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
                    adapter = new StatsHiveAdapter(fm, getLifecycle(), allHives, allRecords, allAlerts, allLocations, allHarvests, idHive, from, to);
                    try {
                        viewPager.setAdapter(adapter);
                        toolbar.setTitle("Statistiky včelstva - " + allHives.stream().filter(hive -> hive.getId_hive() == idHive).collect(Collectors.toList()).get(0).getName());
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
                    adapter = new StatsHiveAdapter(fm, getLifecycle(), allHives, allRecords, allAlerts, allLocations, allHarvests, idHive, from, to);
                    try {
                        viewPager.setAdapter(adapter);
                        toolbar.setTitle("Statistiky včelstva - " + allHives.stream().filter(hive -> hive.getId_hive() == idHive).collect(Collectors.toList()).get(0).getName());
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
                    adapter = new StatsHiveAdapter(fm, getLifecycle(), allHives, allRecords, allAlerts, allLocations, allHarvests, idHive, from, to);
                    try {
                        viewPager.setAdapter(adapter);
                        toolbar.setTitle("Statistiky včelstva - " + allHives.stream().filter(hive -> hive.getId_hive() == idHive).collect(Collectors.toList()).get(0).getName());
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
                    adapter = new StatsHiveAdapter(fm, getLifecycle(), allHives, allRecords, allAlerts, allLocations, allHarvests, idHive, from, to);
                    try {
                        viewPager.setAdapter(adapter);
                        toolbar.setTitle("Statistiky včelstva - " + allHives.stream().filter(hive -> hive.getId_hive() == idHive).collect(Collectors.toList()).get(0).getName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //endregion
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.nav_dashboard:
                intent = new Intent(this, com.example.bee_v03.MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.nav_select:
                intent = new Intent(this, com.example.bee_v03.SelectActivity.class);
                intent.putExtra("TARGET", "view");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.nav_archive:
                intent = new Intent(this, com.example.bee_v03.SelectActivity.class);
                intent.putExtra("TARGET", "archive");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.nav_stats:
                intent = new Intent(this, com.example.bee_v03.ScopeSelectorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}