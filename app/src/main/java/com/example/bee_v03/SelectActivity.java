package com.example.bee_v03;

import android.bluetooth.le.ScanRecord;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class SelectActivity extends AppCompatActivity {
    private ExpandableListView expandableListView;
    private SelectCustomExpandableListAdapter adapter;
    private List<HivesLocation> expandableListTitle;
    private List<Hive> expandableListDetail;
    private SelectViewModel selectViewModel;
    private ExtendedFloatingActionButton fab;
    private FloatingActionButton fabAddLocation, fabAddHive;
    private TextView addLocationText, addHiveText;
    boolean isFabOpen;
    private String target;

    List<Hive> allHives;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            target = extras.getString("TARGET");
        }

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListViewSelect);
        selectViewModel = new ViewModelProvider(this).get(SelectViewModel.class);

        //region Observers

        //TODO: Předělat na stejný způsob jako všude jinde
        //PŘEPSAT, OPTIMALIZOVAT, použít lokální proměnné a alokaci v onChanged
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

        selectViewModel.getAllHives().observe(this, new Observer<List<Hive>>() {
            @Override
            public void onChanged(List<Hive> hives) {
                allHives = selectViewModel.getAllHives().getValue();
            }
        });
        //endregion

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent;
                switch (target) {
                    case "view":
                        intent = new Intent(v.getContext(), com.example.bee_v03.ObjectActivity.class);
                        intent.putExtra("HIVE_ID", ((Hive)adapter.getChild(groupPosition,childPosition)).getId_hive());
                        startActivity(intent);
                        break;
                    case "record":
                        intent = new Intent(v.getContext(), com.example.bee_v03.AddRecordActivity.class);
                        intent.putExtra("HIVE_ID", ((Hive)adapter.getChild(groupPosition,childPosition)).getId_hive());
                        startActivity(intent);
                        break;
                }

                return false;
            }
        });

        //PŘEPSAT DO NĚČEHO JINÉHO, long click je příliš nebezpečný
        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                long packedPosition = expandableListView.getExpandableListPosition(position);
                int itemType = ExpandableListView.getPackedPositionType(packedPosition);
                int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
                int childPosition = ExpandableListView.getPackedPositionChild(packedPosition);

                if (itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                    if (expandableListDetail.size() != 0) {
                        return false;
                    }
                    HivesLocation hivesLocation = (HivesLocation) adapter.getGroup(groupPosition);
                    selectViewModel.delete(hivesLocation);
                } else if (itemType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    Hive hive = (Hive) adapter.getChild(groupPosition, childPosition);
                    try {
                        selectViewModel.delete(hive);
                    } catch (Exception e) {}
                }

                return false;
            }
        });

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
                if (selectViewModel.getAllLocations().getValue() == null || selectViewModel.getAllLocations().getValue().size() == 0) {
                    Toast.makeText(SelectActivity.this, "There are no locations, add a location first", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(v.getContext(), com.example.bee_v03.AddHiveActivity.class);
                intent.putExtra("PARENT_ACTIVITY", "select");
                startActivity(intent);
            }
        });
    }

    private void onDataChanged() {
        expandableListTitle = selectViewModel.getAllLocations().getValue();
        expandableListDetail = selectViewModel.getAllHives().getValue();

        adapter = new SelectCustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        try {
            expandableListView.setAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(this, "There are no hives!", Toast.LENGTH_SHORT).show();
        }
    }
}