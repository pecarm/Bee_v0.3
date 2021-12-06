package com.example.bee_v03;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class TimelineFragment extends Fragment {

    List<Record> allRecords;
    List<Hive> allHives;

    public TimelineFragment() {
        // Required empty public constructor
    }

    public static TimelineFragment newInstance(List<Record> allRecords, List<Hive> allHives) {
        TimelineFragment fragment = new TimelineFragment();
        Bundle args = new Bundle();
        args.putSerializable("ALL_HIVES", (Serializable) allHives);
        args.putSerializable("ALL_RECORDS", (Serializable) allRecords);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            allHives = (List<Hive>) getArguments().getSerializable("ALL_HIVES");
            allRecords = (List<Record>) getArguments().getSerializable("ALL_RECORDS");
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] from = new String[] {"name", "record"};
        int[] to = new int[] {R.id.adapter_view_dashboard_timeline_hive, R.id.adapter_view_dashboard_timeline_date};

        if (allRecords == null || allRecords.size() == 0) {
            Toast.makeText(getContext(), "There are no hives!", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<HashMap<String, Object>> data = recordData(allRecords);

        TimelineAdapter timelineAdapter = new TimelineAdapter(getContext(), data, R.layout.adapter_view_dashboard_timeline, from, to);

        ListView listViewTimeline = getView().findViewById(R.id.list_view_dashboard_timeline);
        listViewTimeline.setAdapter(timelineAdapter);

        listViewTimeline.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //just get the RAW DATA, extract id_hive from there
                Record record = (Record) data.get(position).get("record");

                Intent intent = new Intent(view.getContext(), com.example.bee_v03.ObjectActivity.class);
                intent.putExtra("HIVE_ID", record.getId_hive());
                startActivity(intent);
            }
        });
    }

    private ArrayList<HashMap<String, Object>> recordData(List<Record> records) {
        ArrayList<HashMap<String, Object>> data = new ArrayList<>();
        for (Record record : records) {
            //WE CAN PUT MULTIPLE ITEMS and then PASS THEM BY KEY, even a list of WARNINGS
            HashMap<String, Object> item = new HashMap<>();
            item.put("name", allHives.stream().filter(hive -> hive.getId_hive() == record.getId_hive()).collect(Collectors.toList()).get(0).getName());
            item.put("record", record);
            data.add(item);
        }
        return data;
    }
}