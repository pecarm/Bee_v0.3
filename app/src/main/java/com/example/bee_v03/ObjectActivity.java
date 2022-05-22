package com.example.bee_v03;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.compose.runtime.Stable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.stream.Collectors;

public class ObjectActivity extends AppCompatActivity  {
    ExtendedFloatingActionButton fab;
    FloatingActionButton fabAddRecord, fabAddHarvest;
    TextView fabAddRecordText, fabAddHarvestText;
    TabLayout tabLayout;
    ViewPager2 viewPager;
    List<HivesLocation> allLocations;
    List<Hive> allHives;
    List<Record> allRecords;
    List<Alert> allAlerts;
    List<HoneyHarvest> allHarvests;
    ObjectAdapter adapter;

    boolean isFabOpen;
    int idHive;
    boolean isArchive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Včelstvo");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idHive = extras.getInt("HIVE_ID");
            isArchive = extras.getBoolean("IS_ARCHIVE");
        }

        ObjectViewModel viewModel = new ViewModelProvider(this).get(ObjectViewModel.class);

        fab = (ExtendedFloatingActionButton) findViewById(R.id.fab_object);
        fabAddRecord = (FloatingActionButton) findViewById(R.id.fab_object_add_record);
        fabAddHarvest = (FloatingActionButton) findViewById(R.id.fab_object_add_harvest);
        fabAddRecordText = (TextView) findViewById(R.id.fab_object_add_record_text);
        fabAddHarvestText = (TextView) findViewById(R.id.fab_object_add_harvest_text);
        isFabOpen = false;

        fabAddHarvest.setVisibility(View.GONE);
        fabAddRecord.setVisibility(View.GONE);
        fabAddHarvestText.setVisibility(View.GONE);
        fabAddRecordText.setVisibility(View.GONE);

        fab.shrink();

        //region Tablayout and viewpager
        tabLayout = (TabLayout) findViewById(R.id.tabLayoutObject);
        viewPager = (ViewPager2) findViewById(R.id.viewPagerObject);

        FragmentManager fm = getSupportFragmentManager();

        tabLayout.addTab(tabLayout.newTab().setText("Upozornění"));
        tabLayout.addTab(tabLayout.newTab().setText("Historie"));
        tabLayout.addTab(tabLayout.newTab().setText("Medobraní"));
        tabLayout.addTab(tabLayout.newTab().setText("Info"));
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
        //endregion

        //region Observers
        viewModel.getAllLocations().observe(this, new Observer<List<HivesLocation>>() {
            @Override
            public void onChanged(List<HivesLocation> locations) {
                allLocations = locations;
                if (locations != null) {
                    adapter = new ObjectAdapter(fm, getLifecycle(), allHives, allRecords, allAlerts, allLocations, allHarvests, idHive, isArchive);
                    try {
                        viewPager.setAdapter(adapter);
                        getSupportActionBar().setTitle("Včelstvo - " + allHives.stream().filter(hive -> hive.getId_hive() == idHive).collect(Collectors.toList()).get(0).getName());
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
                    adapter = new ObjectAdapter(fm, getLifecycle(), allHives, allRecords, allAlerts, allLocations, allHarvests, idHive, isArchive);
                    try {
                        viewPager.setAdapter(adapter);
                        getSupportActionBar().setTitle("Včelstvo - " + allHives.stream().filter(hive -> hive.getId_hive() == idHive).collect(Collectors.toList()).get(0).getName());
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
                    adapter = new ObjectAdapter(fm, getLifecycle(), allHives, allRecords, allAlerts, allLocations, allHarvests, idHive, isArchive);
                    try {
                        viewPager.setAdapter(adapter);
                        getSupportActionBar().setTitle("Včelstvo - " + allHives.stream().filter(hive -> hive.getId_hive() == idHive).collect(Collectors.toList()).get(0).getName());
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
                    adapter = new ObjectAdapter(fm, getLifecycle(), allHives, allRecords, allAlerts, allLocations, allHarvests, idHive, isArchive);
                    try {
                        viewPager.setAdapter(adapter);
                        getSupportActionBar().setTitle("Včelstvo - " + allHives.stream().filter(hive -> hive.getId_hive() == idHive).collect(Collectors.toList()).get(0).getName());
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
                    adapter = new ObjectAdapter(fm, getLifecycle(), allHives, allRecords, allAlerts, allLocations, allHarvests, idHive, isArchive);
                    try {
                        viewPager.setAdapter(adapter);
                        getSupportActionBar().setTitle("Včelstvo - " + allHives.stream().filter(hive -> hive.getId_hive() == idHive).collect(Collectors.toList()).get(0).getName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //endregion

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFabOpen) {
                    fabAddHarvest.show();
                    fabAddRecord.show();
                    fabAddRecordText.setVisibility(View.VISIBLE);
                    fabAddHarvestText.setVisibility(View.VISIBLE);
                    fab.extend();
                    isFabOpen = true;
                } else {
                    fabAddHarvest.hide();
                    fabAddRecord.hide();
                    fabAddRecordText.setVisibility(View.GONE);
                    fabAddHarvestText.setVisibility(View.GONE);
                    fab.shrink();
                    isFabOpen = false;
                }
            }
        });

        fabAddRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), com.example.bee_v03.AddRecordActivity.class);
                intent.putExtra("HIVE_ID", idHive);
                startActivity(intent);
            }
        });

        fabAddHarvest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), com.example.bee_v03.AddHoneyHarvestActivity.class);
                intent.putExtra("HIVE_ID", idHive);
                startActivity(intent);
            }
        });

        if (isArchive) {
            fab.setEnabled(false);
        }
    }
}