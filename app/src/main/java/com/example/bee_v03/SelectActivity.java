package com.example.bee_v03;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Locale;

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
    private String from;
    private String to;
    private boolean includeArchive;

    List<Hive> allHives;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Vyberte cíl");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            target = extras.getString("TARGET");
            try {
                from = extras.getString("FROM");
                to = extras.getString("TO");
                includeArchive = extras.getBoolean("INCLUDE_ARCHIVE");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        initializeFabs();

        if (!target.equals("view") && !target.equals("record")) {
            fab.setEnabled(false);
        }

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListViewSelect);
        selectViewModel = new ViewModelProvider(this).get(SelectViewModel.class);

        //region Observers

        //fuck it, it works...
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

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (target.equals("stats_location") || target.equals("stats_location_all")) {
                    Intent intent = new Intent(v.getContext(), com.example.bee_v03.StatsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("ID_LOCATION", ((HivesLocation)adapter.getGroup(groupPosition)).getId_location());
                    bundle.putString("FROM", from);
                    bundle.putString("TO", to);
                    bundle.putBoolean("INCLUDE_ARCHIVE", includeArchive);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent;
                switch (target) {
                    case "archive":
                        intent = new Intent(v.getContext(), com.example.bee_v03.ObjectActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("HIVE_ID", ((Hive)adapter.getChild(groupPosition,childPosition)).getId_hive());
                        bundle.putBoolean("IS_ARCHIVE", true);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case "view":
                        intent = new Intent(v.getContext(), com.example.bee_v03.ObjectActivity.class);
                        Bundle bndl = new Bundle();
                        bndl.putInt("HIVE_ID", ((Hive)adapter.getChild(groupPosition,childPosition)).getId_hive());
                        bndl.putBoolean("IS_ARCHIVE", false);
                        intent.putExtras(bndl);
                        startActivity(intent);
                        break;
                    case "record":
                        intent = new Intent(v.getContext(), com.example.bee_v03.AddRecordActivity.class);
                        intent.putExtra("HIVE_ID", ((Hive)adapter.getChild(groupPosition,childPosition)).getId_hive());
                        startActivity(intent);
                        break;
                    case "stats_hive":
                    case "stats_hive_all":
                        intent = new Intent(v.getContext(), com.example.bee_v03.StatsHiveActivity.class);
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("FROM", from);
                        bundle1.putString("TO", to);
                        bundle1.putInt("HIVE_ID", ((Hive)adapter.getChild(groupPosition,childPosition)).getId_hive());
                        intent.putExtras(bundle1);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });

        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (!target.equals("view") && !target.equals("record")) {
                    return false;
                }
                long packedPosition = expandableListView.getExpandableListPosition(position);
                int itemType = ExpandableListView.getPackedPositionType(packedPosition);
                int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
                int childPosition = ExpandableListView.getPackedPositionChild(packedPosition);

                if (itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                    Toast.makeText(SelectActivity.this, "Easter egg!", Toast.LENGTH_SHORT).show();
                } else if (itemType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    Hive hive = (Hive) adapter.getChild(groupPosition, childPosition);

                    PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.select_popup_archive:
                                    hive.setArchived(true);
                                    selectViewModel.update(hive);
                                    return true;
                                case R.id.select_popup_delete:
                                    selectViewModel.delete(hive);
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popupMenu.inflate(R.menu.select_activity_popup);
                    popupMenu.show();
                }

                return true;
            }
        });
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

        if (target.equals("archive")) {
            adapter = new SelectCustomExpandableListAdapter(this, expandableListTitle, expandableListDetail, "archive");
        } else if (target.equals("view") || target.equals("record") || target.equals("stats_hive")) {
            adapter = new SelectCustomExpandableListAdapter(this, expandableListTitle, expandableListDetail, "active");
        } else {
            adapter = new SelectCustomExpandableListAdapter(this, expandableListTitle, expandableListDetail, "all");
        }

        try {
            expandableListView.setAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(this, "There are no hives!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        //super.onBackPressed();
    }
}