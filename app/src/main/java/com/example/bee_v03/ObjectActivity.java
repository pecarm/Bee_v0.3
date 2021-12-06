package com.example.bee_v03;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class ObjectActivity extends AppCompatActivity  {
    FloatingActionButton fab;
    TabLayout tabLayout;
    ViewPager2 viewPager;
    List<Hive> allHives;
    List<Record> allRecords;
    List<Alert> allAlerts;
    ObjectAdapter adapter;
    int idHive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_object);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idHive = extras.getInt("HIVE_ID");
        }

        ObjectViewModel viewModel = new ViewModelProvider(this).get(ObjectViewModel.class);
        fab = (FloatingActionButton) findViewById(R.id.fab_object_add_record);

        //region Tablayout and viewpager
        tabLayout = (TabLayout) findViewById(R.id.tabLayoutObject);
        viewPager = (ViewPager2) findViewById(R.id.viewPagerObject);

        FragmentManager fm = getSupportFragmentManager();

        tabLayout.addTab(tabLayout.newTab().setText("Alerts"));
        tabLayout.addTab(tabLayout.newTab().setText("History"));
        tabLayout.addTab(tabLayout.newTab().setText("Stats"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

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
        viewModel.getAllHives().observe(this, new Observer<List<Hive>>() {
            @Override
            public void onChanged(List<Hive> hives) {
                allHives = hives;
                if (allRecords != null) {
                    adapter = new ObjectAdapter(fm, getLifecycle(), allHives, allRecords, allAlerts, idHive);
                    viewPager.setAdapter(adapter);
                }
            }
        });

        viewModel.getAllRecords().observe(this, new Observer<List<Record>>() {
            @Override
            public void onChanged(List<Record> records) {
                allRecords = records;
                if (allRecords != null) {
                    adapter = new ObjectAdapter(fm, getLifecycle(), allHives, allRecords, allAlerts, idHive);
                    viewPager.setAdapter(adapter);
                }
            }
        });

        viewModel.getAllAlerts().observe(this, new Observer<List<Alert>>() {
            @Override
            public void onChanged(List<Alert> alerts) {
                allAlerts = alerts;
                if (allRecords != null) {
                    adapter = new ObjectAdapter(fm, getLifecycle(), allHives, allRecords, allAlerts, idHive);
                    viewPager.setAdapter(adapter);
                }
            }
        });
        //endregion

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), com.example.bee_v03.AddRecordActivity.class);
                intent.putExtra("HIVE_ID", idHive);
                startActivity(intent);
            }
        });
    }

    //OBSOLETE NOW
    private void onDataChanged() {

        //TODO Fill alerts

    }
}