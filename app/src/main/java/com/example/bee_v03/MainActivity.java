package com.example.bee_v03;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TabLayout tabLayout;
    ViewPager viewPager;
    DrawerLayout drawer;
    ExtendedFloatingActionButton fab;
    FloatingActionButton fabAddRecord, fabAddHive, fabAddLocation, fabView;
    TextView addRecordText, addHiveText, addLocationText, viewText;
    boolean isFabOpen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        //initializes tablayout
        tabLayout.addTab(tabLayout.newTab().setText("Dashboard"));
        tabLayout.addTab(tabLayout.newTab().setText("Timeline"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //fills viewpager with fragments
        final com.example.bee_v03.CustomAdapter customAdapter = new com.example.bee_v03.CustomAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
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

        initializeFabs();
    }

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
                    isFabOpen = true;;
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

            }
        });

        fabAddRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        fabAddHive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        fabView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSelect();
            }
        });
    }

    private void startSelect() {
        Intent intent = new Intent(this, com.example.bee_v03.SelectActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.nav_dashboard:
                break;
            case R.id.nav_global:
                intent = new Intent(this, com.example.bee_v03.GlobalActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_select:
                intent = new Intent(this, com.example.bee_v03.SelectActivity.class);
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
        } else {
            super.onBackPressed();
        }
    }
}