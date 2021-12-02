package com.example.bee_v03;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.security.cert.CollectionCertStoreParameters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TabLayout tabLayout;
    ViewPager viewPager;
    DrawerLayout drawer;
    ExtendedFloatingActionButton fab;
    ListView listViewTimeline;
    FloatingActionButton fabAddRecord, fabAddHive, fabAddLocation, fabView;
    TextView addRecordText, addHiveText, addLocationText, viewText;
    MainActivityViewModel viewModel;
    boolean isFabOpen;

    List<HivesLocation> allLocations;
    List<Hive> allHives;
    List<Record> allRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        //region Toolbar and navigation drawer
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //endregion

        //region Tablayout and viewpager
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        //initializes tabLayout
        tabLayout.addTab(tabLayout.newTab().setText("Dashboard"));
        tabLayout.addTab(tabLayout.newTab().setText("Timeline"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //fills viewpager with fragments
        final com.example.bee_v03.CustomAdapter customAdapter = new CustomAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(customAdapter);

        //tells viewpager how to behave
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
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
        //endregion

        //region Observers
        viewModel.getAllLocations().observe(this, new Observer<List<HivesLocation>>() {
            @Override
            public void onChanged(List<HivesLocation> hivesLocations) {
                allLocations = hivesLocations;
            }
        });
        viewModel.getAllHives().observe(this, new Observer<List<Hive>>() {
            @Override
            public void onChanged(List<Hive> hives) {
                allHives = hives;
                if (hives == null) {
                    return;
                }
                onDataChanged();
            }
        });
        viewModel.getAllRecords().observe(this, new Observer<List<Record>>() {
            @Override
            public void onChanged(List<Record> records) {
                allRecords = records;
                if (allHives == null) {
                    return;
                }
                onDataChanged();
            }
        });
        //endregion

        //This is a lot of code, moved to separate methods
        initializeFabs();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.nav_dashboard:
                break;
            case R.id.nav_global:
                intent = new Intent(this, com.example.bee_v03.GlobalActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_select:
                intent = new Intent(this, com.example.bee_v03.SelectActivity.class);
                intent.putExtra("TARGET", "view");
                startActivity(intent);
                break;
            case R.id.nav_settings:
                intent = new Intent(this, SettingsActivity.class);
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
        }
        else if (isFabOpen) {
            fabAddLocation.hide();
            fabAddHive.hide();
            fabAddRecord.hide();
            fabView.hide();
            addLocationText.setVisibility(View.GONE);
            addHiveText.setVisibility(View.GONE);
            addRecordText.setVisibility(View.GONE);
            viewText.setVisibility(View.GONE);
            fab.shrink();
            isFabOpen = false;
        }
        else {
            super.onBackPressed();
        }
    }

    //region Private methods
    private void initializeFabs() {
        fab = (ExtendedFloatingActionButton) findViewById(R.id.fab_main);
        fabAddLocation = (FloatingActionButton) findViewById(R.id.fab_main_add_location);
        fabAddHive = (FloatingActionButton) findViewById(R.id.fab_main_add_hive);
        fabAddRecord = (FloatingActionButton) findViewById(R.id.fab_main_record);
        fabView = (FloatingActionButton) findViewById(R.id.fab_main_view);
        addLocationText = (TextView) findViewById(R.id.fab_main_add_location_text);
        addHiveText = (TextView) findViewById(R.id.fab_main_add_hive_text);
        addRecordText = (TextView) findViewById(R.id.fab_main_record_text);
        viewText = (TextView) findViewById(R.id.fab_main_view_text);
        isFabOpen = false;

        fabAddLocation.setVisibility(View.GONE);
        fabAddRecord.setVisibility(View.GONE);
        fabAddHive.setVisibility(View.GONE);
        fabView.setVisibility(View.GONE);
        addLocationText.setVisibility(View.GONE);
        addHiveText.setVisibility(View.GONE);
        addRecordText.setVisibility(View.GONE);
        viewText.setVisibility(View.GONE);

        fab.shrink();

        fabsSetOnClickListeners();
    }

    private void fabsSetOnClickListeners() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFabOpen) {
                    fabAddHive.show();
                    fabAddLocation.show();
                    fabAddRecord.show();
                    fabView.show();
                    addLocationText.setVisibility(View.VISIBLE);
                    addHiveText.setVisibility(View.VISIBLE);
                    addRecordText.setVisibility(View.VISIBLE);
                    viewText.setVisibility(View.VISIBLE);
                    fab.extend();
                    isFabOpen = true;
                } else {
                    fabAddLocation.hide();
                    fabAddHive.hide();
                    fabAddRecord.hide();
                    fabView.hide();
                    addLocationText.setVisibility(View.GONE);
                    addHiveText.setVisibility(View.GONE);
                    addRecordText.setVisibility(View.GONE);
                    viewText.setVisibility(View.GONE);
                    fab.shrink();
                    isFabOpen = false;
                }
            }
        });

        fabAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), com.example.bee_v03.AddLocationActivity.class);
                startActivity(intent);
            }
        });

        fabAddRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allHives == null || allHives.size() == 0) {
                    Toast.makeText(MainActivity.this, "There are no hives, add hive first!", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(v.getContext(), com.example.bee_v03.SelectActivity.class);
                intent.putExtra("TARGET", "record");
                startActivity(intent);
            }
        });

        fabAddHive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allLocations == null || allLocations.size() == 0) {
                    Toast.makeText(MainActivity.this, "There are no locations, add a location first", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(v.getContext(), com.example.bee_v03.AddHiveActivity.class);
                intent.putExtra("PARENT_ACTIVITY", "dashboard");
                startActivity(intent);
            }
        });

        fabView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), com.example.bee_v03.SelectActivity.class);
                intent.putExtra("TARGET", "view");
                startActivity(intent);
            }
        });
    }

    private void onDataChanged() {
        //TODO Fill Dashboard

        //region Fills timeline
        listViewTimeline = (ListView) findViewById(R.id.list_view_dashboard_timeline);

        String[] from = new String[] {"name", "record"};
        int[] to = new int[] {R.id.adapter_view_dashboard_timeline_hive, R.id.adapter_view_dashboard_timeline_date};

        if (allRecords == null || allRecords.size() == 0) {
            Toast.makeText(MainActivity.this, "There are no hives!", Toast.LENGTH_SHORT).show();
            return;
        }

        TimelineAdapter timelineAdapter = new TimelineAdapter(this, recordData(allRecords), R.layout.adapter_view_dashboard_timeline, from, to);
        listViewTimeline.setAdapter(timelineAdapter);
        //endregion
    }

    private ArrayList<HashMap<String, Object>> recordData(List<Record> records) {
        ArrayList<HashMap<String, Object>> data = new ArrayList<>();
        for (Record record : records) {
            //WE CAN PUT MULTIPLE ITEMS and then PASS THEM BY KEY, even a list of WARNINGS
            HashMap<String, Object> item = new HashMap<>();
            //I honestly doubt this is gonna work
            item.put("name", allHives.stream().filter(hive -> hive.getId_hive() == record.getId_hive()).collect(Collectors.toList()).get(0).getName());
            item.put("record", record);
            data.add(item);
        }
        return data;
    }
    //endregion
}