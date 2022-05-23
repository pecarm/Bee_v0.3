package com.example.bee_v03;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class HistoryFragment extends Fragment {

    private List<Record> allRecords;
    private int idHive;

    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance(List<Record> allRecords, int idHive) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putInt("ID_HIVE", idHive);
        args.putSerializable("ALL_RECORDS", (Serializable) allRecords);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            idHive = getArguments().getInt("ID_HIVE");
            allRecords = (List<Record>) getArguments().getSerializable("ALL_RECORDS");
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //FILL IT WITH DATA

        List<Record> records = new ArrayList<>();
        String[] from = new String[] {"date", "record"};
        int[] to = new int[] {R.id.adapter_view_object_history_date, R.id.adapter_view_object_history_text};

        try {
            records = allRecords.stream().filter(record -> record.getId_hive() == idHive).collect(Collectors.toList());
        } catch (Exception e) {
            Toast.makeText(getContext(), "There are no records!", Toast.LENGTH_SHORT).show();
        }
        if (records == null || records.size() == 0) {
            Toast.makeText(getContext(), "There are no records!", Toast.LENGTH_SHORT).show();
        } else {
            ArrayList<HashMap<String, Object>> data = recordData(records);

            HistoryAdapter historyAdapter = new HistoryAdapter(getContext(), data, R.layout.adapter_view_object_history, from, to);

            ListView lv = getView().findViewById(R.id.list_view_object_history);
            lv.setAdapter(historyAdapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Record record = (Record) data.get(position).get("record");

                    ShowRecordDialog dialog = new ShowRecordDialog();
                    Bundle bundle = new Bundle();

                    bundle.putSerializable("RECORD", record);

                    dialog.setArguments(bundle);
                    dialog.show(getParentFragmentManager(), "Record dialog");
                }
            });
        }
    }

    private ArrayList<HashMap<String, Object>> recordData(List<Record> records) {
        try {
            Collections.sort(records, new Comparator<Record>() {
                @Override
                public int compare(Record o1, Record o2) {
                    return o2.getDate().compareTo(o1.getDate());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<HashMap<String, Object>> data = new ArrayList<>();
        for (Record record : records) {
            //WE CAN PUT MULTIPLE ITEMS and then PASS THEM BY KEY, even a list of WARNINGS
            HashMap<String, Object> item = new HashMap<>();
            String date = record.getYear()+"/"+record.getMonth()+"/"+record.getDay();
            item.put("date", date);
            item.put("record", record);
            data.add(item);
        }
        return data;
    }
}