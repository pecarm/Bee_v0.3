package com.example.bee_v03;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class StatsRecordFragment extends Fragment {
    private List<Hive> allHives;
    private List<Record> allRecords;
    private List<Record> records = new ArrayList<>();
    private int idLocation;
    private String from, to;
    private boolean includeArchived;

    public StatsRecordFragment() {
        // Required empty public constructor
    }

    public static StatsRecordFragment newInstance(List<Hive> allHives, List<Record> allRecords, int idLocation, String from, String to, boolean includeArchived) {
        StatsRecordFragment fragment = new StatsRecordFragment();
        Bundle args = new Bundle();
        args.putSerializable("ALL_RECORDS", (Serializable) allRecords);
        args.putSerializable("ALL_HIVES", (Serializable) allHives);
        args.putInt("ID_LOCATION", idLocation);
        args.putString("FROM", from);
        args.putString("TO", to);
        args.putBoolean("INCLUDE_ARCHIVED", includeArchived);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            idLocation = getArguments().getInt("ID_LOCATION");
            allRecords = (List<Record>) getArguments().getSerializable("ALL_RECORDS");
            allHives = (List<Hive>) getArguments().getSerializable("ALL_HIVES");
            from = getArguments().getString("FROM");
            to = getArguments().getString("TO");
            includeArchived = getArguments().getBoolean("INCLUDE_ARCHIVED");
        }
        return inflater.inflate(R.layout.fragment_stats_record, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] dateFrom = from.split("/");
        String[] dateTo = to.split("/");
        LocalDate calcFrom = LocalDate.of(Integer.parseInt(dateFrom[0]), Integer.parseInt(dateFrom[1]), Integer.parseInt(dateFrom[2]));
        LocalDate calcTo = LocalDate.of(Integer.parseInt(dateTo[0]), Integer.parseInt(dateTo[1]), Integer.parseInt(dateTo[2]));

        //FILL IT WITH DATA

        String[] from = new String[] {"date", "record"};
        int[] to = new int[] {R.id.adapter_view_object_history_date, R.id.adapter_view_object_history_text};

        try {
            //filtruje záznamy podle toho, jestli se nacházejí v lokalitě a/nebo jestli jsou archivované
            List<Integer> hiveIds;
            if (idLocation == -1) {
                if (includeArchived) {
                    hiveIds = allHives.stream().map(hive -> hive.getId_hive()).collect(Collectors.toList());
                } else {
                    hiveIds = allHives.stream().filter(hive -> !hive.isArchived()).map(hive -> hive.getId_hive()).collect(Collectors.toList());
                }
            } else {
                if (includeArchived) {
                    hiveIds = allHives.stream().filter(hive -> hive.getId_location() == idLocation).map(hive -> hive.getId_hive()).collect(Collectors.toList());
                } else {
                    hiveIds = allHives.stream().filter(hive -> (hive.getId_location() == idLocation) && (!hive.isArchived())).map(hive -> hive.getId_hive()).collect(Collectors.toList());
                }
            }

            List<Record> recordsFromHive = allRecords.stream().filter(record -> hiveIds.contains(record.getId_hive())).collect(Collectors.toList());
            for (Record r: recordsFromHive) {
                LocalDate recordDate = LocalDate.of(r.getYear(), r.getMonth(), r.getDay());
                if (!recordDate.isBefore(calcFrom) && !recordDate.isAfter(calcTo)) {
                    records.add(r);
                }
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "There are no records!", Toast.LENGTH_SHORT).show();
        }
        if (records.size() == 0) {
            Toast.makeText(getContext(), "There are no records!", Toast.LENGTH_SHORT).show();
        } else {
            //ListView of feeding
            ArrayList<HashMap<String, Object>> data = recordData(records);
            FeedingAdapter feedingAdapter = new FeedingAdapter(getContext(), data, R.layout.adapter_view_object_history, from, to);

            ListView lv = getView().findViewById(R.id.stats_record_list_view);
            lv.setAdapter(feedingAdapter);

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

            //Total
            TextView total = getView().findViewById(R.id.stats_record_total);
            total.setText(Integer.toString(records.size()));

            //Unique
            TextView unique = getView().findViewById(R.id.stats_record_unique);
            int uniqueDays = 0;
            List<LocalDate> recordDates = new ArrayList<>();
            for (Record r: allRecords) {
                LocalDate recordDate = LocalDate.of(r.getYear(), r.getMonth(), r.getDay());
                if (!recordDates.contains(recordDate)) {
                    uniqueDays++;
                    recordDates.add(recordDate);
                }
            }
            unique.setText(Integer.toString(uniqueDays));
        }
    }

    private ArrayList<HashMap<String, Object>> recordData(List<Record> records) {
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