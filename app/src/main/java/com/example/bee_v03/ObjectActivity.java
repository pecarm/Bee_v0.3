package com.example.bee_v03;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ObjectActivity extends AppCompatActivity  {
    ListView listViewHistory;
    FloatingActionButton fab;
    TabLayout tabLayout;
    ViewPager viewPager;
    List<Hive> allHives;
    List<Record> allRecords;
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
        fab = (FloatingActionButton) findViewById(R.id.fab_object);

        //region Tablayout and viewpager
        tabLayout = (TabLayout) findViewById(R.id.tabLayoutObject);
        viewPager = (ViewPager) findViewById(R.id.viewPagerObject);

        tabLayout.addTab(tabLayout.newTab().setText("Alerts"));
        tabLayout.addTab(tabLayout.newTab().setText("History"));
        tabLayout.addTab(tabLayout.newTab().setText("Stats"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final com.example.bee_v03.ObjectAdapter objectAdapter = new com.example.bee_v03.ObjectAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(objectAdapter);

        listViewHistory = (ListView) findViewById(R.id.list_view_object_history);

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
        viewModel.getAllHives().observe(this, new Observer<List<Hive>>() {
            @Override
            public void onChanged(List<Hive> hives) {
                allHives = hives;
                //onDataChanged();
            }
        });

        viewModel.getAllRecords().observe(this, new Observer<List<Record>>() {
            @Override
            public void onChanged(List<Record> records) {
                allRecords = records;
                //onDataChanged();
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

    private void onDataChanged() {

        //TODO Fill alerts

        //TODO Fill history


        //ALWAYS RETURNS NULL, WHYYYYYYYYYYY
        listViewHistory = (ListView) findViewById(R.id.list_view_object_history);
        List<Record> records = new ArrayList<>();

        try {
            records = allRecords.stream().filter(record -> record.getId_hive() == idHive).collect(Collectors.toList());
        } catch (Exception e) {
            Toast.makeText(ObjectActivity.this, "There are no records!", Toast.LENGTH_SHORT).show();
        }
        if (records.size() == 0) {
            Toast.makeText(ObjectActivity.this, "There are no records!", Toast.LENGTH_SHORT).show();
        } else {
            String[] from = new String[] {"date", "preview"};
            int[] to = new int[] {R.id.adapter_view_object_history_date, R.id.adapter_view_object_history_text};


            HistoryAdapter historyAdapter = new HistoryAdapter(this, recordData(records), R.layout.adapter_view_object_history, from, to);
            listViewHistory.setAdapter(historyAdapter);
        }

        //TODO Fill stats

    }

    private ArrayList<HashMap<String, Object>> recordData(List<Record> records) {
        ArrayList<HashMap<String, Object>> data = new ArrayList<>();
        for (Record record : records) {
            //WE CAN PUT MULTIPLE ITEMS and then PASS THEM BY KEY, even a list of WARNINGS
            HashMap<String, Object> item = new HashMap<>();
            //I honestly doubt this is gonna work
            String preview;
            if (record.getText().length() > 30) {
                preview = record.getText().substring(0, 27) + "...";
            } else preview = record.getText();
            item.put("date", record.getDate());
            item.put("preview", preview);
            data.add(item);
        }
        return data;
    }
}