package com.example.bee_v03;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SelectActivity extends AppCompatActivity {
    private ExpandableListView expandableListView;
    private SelectCustomExpandableListAdapter adapter;
    private List<String> expandableListTitle;
    private HashMap<String, List<String>> expandableListDetail;
    private SelectViewModel selectViewModel;
    private ExtendedFloatingActionButton fab;
    private FloatingActionButton fabAddLocation, fabAddHive;
    private TextView addLocationText, addHiveText;
    boolean isFabOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListViewSelect);
        selectViewModel = new ViewModelProvider(this).get(SelectViewModel.class);

        selectViewModel.getAllHives().observe(this, new Observer<List<Hive>>() {
            @Override
            public void onChanged(List<Hive> hives) {
                onDataChanged();
            }
        });

        selectViewModel.getAllLocations().observe(this, new Observer<List<HivesLocation>>() {
            @Override
            public void onChanged(List<HivesLocation> hivesLocations) {
                onDataChanged();
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return false;
            }
        });

        /*  NEEDS TWEAKING, LOOK AT HivesLocationDao
        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                long packedPosition = expandableListView.getExpandableListPosition(position);
                int itemType = ExpandableListView.getPackedPositionType(position);
                int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);

                if (itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                    LiveData<List<HivesLocation>> allHivesLocations = selectViewModel.getAllLocations();
                    String s = expandableListTitle.get(groupPosition);
                    List<HivesLocation> a = selectViewModel.getLocationIdByName("\"" + s + "\"").getValue();

                    HivesLocation hivesLocation = a.get(0);
                    selectViewModel.delete(hivesLocation);
                }

                return false;
            }
        });*/

        initializeFabs();
    }

    private void initializeFabs() {
        fab = (ExtendedFloatingActionButton) findViewById(R.id.fab_select);
        fabAddLocation = (FloatingActionButton) findViewById(R.id.fab_select_add_location);
        fabAddHive = (FloatingActionButton) findViewById(R.id.fab_select_add_hive);
        addLocationText = (TextView) findViewById(R.id.fab_select_add_location_text);
        addHiveText = (TextView) findViewById(R.id.fab_select_add_hive_text);

        fabAddLocation.setVisibility(View.GONE);
        fabAddHive.setVisibility(View.GONE);
        addLocationText.setVisibility(View.GONE);
        addHiveText.setVisibility(View.GONE);
        isFabOpen = false;

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
                    addLocationText.setVisibility(View.VISIBLE);
                    addHiveText.setVisibility(View.VISIBLE);
                    fab.extend();
                    isFabOpen = true;
                } else {
                    fabAddLocation.hide();
                    fabAddHive.hide();
                    addLocationText.setVisibility(View.GONE);
                    addHiveText.setVisibility(View.GONE);
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

        fabAddHive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void onDataChanged() {
        expandableListDetail = SelectExpandableListDataPump.getData(selectViewModel);
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        adapter = new SelectCustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(adapter);
    }
}