package com.example.bee_v03;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class StatsRecordHiveFragment extends Fragment {
    private List<Record> allRecords;
    private List<Record> records = new ArrayList<>();
    private int idHive;
    private String from, to;

    public StatsRecordHiveFragment() {
        // Required empty public constructor
    }

    public static StatsRecordHiveFragment newInstance(List<Record> allRecords, int idHive, String from, String to) {
        StatsRecordHiveFragment fragment = new StatsRecordHiveFragment();
        Bundle args = new Bundle();
        args.putInt("ID_HIVE", idHive);
        args.putSerializable("ALL_RECORDS", (Serializable) allRecords);
        args.putString("FROM", from);
        args.putString("TO", to);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            idHive = getArguments().getInt("ID_HIVE");
            allRecords = (List<Record>) getArguments().getSerializable("ALL_RECORDS");
            from = getArguments().getString("FROM");
            to = getArguments().getString("TO");
        }
        return inflater.inflate(R.layout.fragment_stats_record_hive, container, false);
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
            List<Record> recordsFromHive = allRecords.stream().filter(record -> record.getId_hive() == idHive).collect(Collectors.toList());
            for (Record r: recordsFromHive) {
                LocalDate recordDate = LocalDate.of(r.getYear(), r.getMonth(), r.getDay());
                if (!recordDate.isBefore(calcFrom) && !recordDate.isAfter(calcTo)) {
                    records.add(r);
                }
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "There are no records!", Toast.LENGTH_SHORT).show();
        }
        if (records == null || records.size() == 0) {
            Toast.makeText(getContext(), "There are no records!", Toast.LENGTH_SHORT).show();
        } else {
            //ListView of feeding
            ArrayList<HashMap<String, Object>> data = recordData(records);
            FeedingHiveAdapter feedingHiveAdapter = new FeedingHiveAdapter(getContext(), data, R.layout.adapter_view_object_history, from, to);

            ListView lv = getView().findViewById(R.id.stats_record_list_view);
            lv.setAdapter(feedingHiveAdapter);

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
            TextView total = getView().findViewById(R.id.stats_record_hive_total);
            total.setText(Integer.toString(records.size()));

            //Graphs
            GraphView graphResources = (GraphView) getView().findViewById(R.id.stats_record_hive_graph_resources);
            LineGraphSeries<DataPoint> dataResources = new LineGraphSeries<>(new DataPoint[0]);
            dataResources.resetData(recordDataResources());
            graphResources.addSeries(dataResources);
            graphResources.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
            graphResources.getGridLabelRenderer().setNumHorizontalLabels(4);
            graphResources.getGridLabelRenderer().setNumVerticalLabels(6);
            graphResources.getGridLabelRenderer().setHumanRounding(false);
            graphResources.getViewport().setMinY(0);
            graphResources.getViewport().setMaxY(5);
            graphResources.getViewport().setYAxisBoundsManual(true);

            GraphView graphIntegrity = (GraphView) getView().findViewById(R.id.stats_record_hive_graph_integrity);
            LineGraphSeries<DataPoint> dataIntegrity = new LineGraphSeries<>(new DataPoint[0]);
            dataIntegrity.resetData(recordDataIntegrity());
            graphIntegrity.addSeries(dataIntegrity);
            graphIntegrity.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
            graphIntegrity.getGridLabelRenderer().setNumHorizontalLabels(4);
            graphIntegrity.getGridLabelRenderer().setNumVerticalLabels(6);
            graphIntegrity.getGridLabelRenderer().setHumanRounding(false);
            graphIntegrity.getViewport().setMinY(0);
            graphIntegrity.getViewport().setMaxY(5);
            graphIntegrity.getViewport().setYAxisBoundsManual(true);
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
            //WE CAN PUT MULTIPLE ITEMS and then PASS THEM BY KEY
            if (!record.getFeeding().equals("")) {
                HashMap<String, Object> item = new HashMap<>();
                String date = record.getYear()+"/"+record.getMonth()+"/"+record.getDay();
                item.put("date", date);
                item.put("record", record);
                data.add(item);
            }
        }
        return data;
    }

    private DataPoint[] recordDataResources() {
        DataPoint[] data = new DataPoint[records.size()];
        HashMap<LocalDate, Double> dataHelp = new HashMap<>();
        for (Record r: records) {
            LocalDate date = LocalDate.of(r.getYear(), r.getMonth(), r.getDay());

            dataHelp.computeIfPresent(date, (k, v) -> (v + r.getResources_state())/2);
            dataHelp.putIfAbsent(date, (double) r.getResources_state());
        }

        List<LocalDate> keyList = new ArrayList<>(dataHelp.keySet());
        Collections.sort(keyList);

        int i = 0;
        for (LocalDate key: keyList) {
            Calendar c = new GregorianCalendar(key.getYear(), key.getMonthValue()-1, key.getDayOfMonth());
            Date date = c.getTime();
            data[i] = new DataPoint(date, dataHelp.get(key));
            i++;
        }
        return data;
    }

    private DataPoint[] recordDataIntegrity() {
        DataPoint[] data = new DataPoint[records.size()];
        HashMap<LocalDate, Double> dataHelp = new HashMap<>();
        for (Record r: records) {
            LocalDate date = LocalDate.of(r.getYear(), r.getMonth(), r.getDay());

            dataHelp.computeIfPresent(date, (k, v) -> (v + r.getBrood_integrity())/2);
            dataHelp.putIfAbsent(date, (double) r.getBrood_integrity());
        }

        List<LocalDate> keyList = new ArrayList<>(dataHelp.keySet());
        Collections.sort(keyList);

        int i = 0;
        for (LocalDate key: keyList) {
            Calendar c = new GregorianCalendar(key.getYear(), key.getMonthValue()-1, key.getDayOfMonth());
            Date date = c.getTime();
            data[i] = new DataPoint(date, dataHelp.get(key));
            i++;
        }
        return data;
    }
}