package com.example.bee_v03;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TabLayout tabLayout;
    ViewPager2 viewPager;
    DrawerLayout drawer;
    ExtendedFloatingActionButton fab;
    FloatingActionButton fabAddRecord, fabAddHive, fabAddLocation, fabView;
    TextView addRecordText, addHiveText, addLocationText, viewText;
    MainActivityViewModel viewModel;
    CustomAdapter adapter;
    boolean isFabOpen;

    List<HivesLocation> allLocations;
    List<Hive> allHives;
    List<Record> allRecords;
    List<Alert> allAlerts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
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
        viewPager = (ViewPager2) findViewById(R.id.viewPager);

        //initializes tabLayout
        tabLayout.addTab(tabLayout.newTab().setText("Upozornění"));
        tabLayout.addTab(tabLayout.newTab().setText("Návštěvy"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //fills viewpager with fragments
        FragmentManager fm = getSupportFragmentManager();

        //tells viewpager how to behave
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
            public void onChanged(List<HivesLocation> hivesLocations) {
                allLocations = hivesLocations;
                adapter = new CustomAdapter(fm, getLifecycle(), allRecords, allHives, allAlerts);
                viewPager.setAdapter(adapter);
            }
        });
        viewModel.getAllHives().observe(this, new Observer<List<Hive>>() {
            @Override
            public void onChanged(List<Hive> hives) {
                allHives = hives;
                adapter = new CustomAdapter(fm, getLifecycle(), allRecords, allHives, allAlerts);
                viewPager.setAdapter(adapter);
            }
        });
        viewModel.getAllRecords().observe(this, new Observer<List<Record>>() {
            @Override
            public void onChanged(List<Record> records) {
                allRecords = records;
                adapter = new CustomAdapter(fm, getLifecycle(), allRecords, allHives, allAlerts);
                viewPager.setAdapter(adapter);
            }
        });
        viewModel.getAllAlerts().observe(this, new Observer<List<Alert>>() {
            @Override
            public void onChanged(List<Alert> alerts) {
                allAlerts = alerts;
                adapter = new CustomAdapter(fm, getLifecycle(), allRecords, allHives, allAlerts);
                viewPager.setAdapter(adapter);
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
            case R.id.nav_select:
                intent = new Intent(this, com.example.bee_v03.SelectActivity.class);
                intent.putExtra("TARGET", "view");
                startActivity(intent);
                break;
            case R.id.nav_archive:
                intent = new Intent(this, com.example.bee_v03.SelectActivity.class);
                intent.putExtra("TARGET", "archive");
                startActivity(intent);
                break;
            case R.id.nav_stats:
                intent = new Intent(this, com.example.bee_v03.ScopeSelectorActivity.class);
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
    //endregion
}