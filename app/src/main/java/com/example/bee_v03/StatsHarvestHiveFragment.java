package com.example.bee_v03;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatsHarvestHiveFragment extends Fragment {
    private List<HoneyHarvest> allHarvests;
    private List<HoneyHarvest> harvests = new ArrayList<>();
    private int idHive;
    private String from, to;

    public StatsHarvestHiveFragment() {
        // Required empty public constructor
    }

    public static StatsHarvestHiveFragment newInstance(List<HoneyHarvest> allHarvests, int idHive, String from, String to) {
        StatsHarvestHiveFragment fragment = new StatsHarvestHiveFragment();
        Bundle args = new Bundle();
        args.putInt("ID_HIVE", idHive);
        args.putSerializable("ALL_HARVESTS", (Serializable) allHarvests);
        args.putString("FROM", from);
        args.putString("TO", to);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            idHive = getArguments().getInt("ID_HIVE");
            allHarvests = (List<HoneyHarvest>) getArguments().getSerializable("ALL_HARVESTS");
            from = getArguments().getString("FROM");
            to = getArguments().getString("TO");
        }
        return inflater.inflate(R.layout.fragment_stats_harvest_hive, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] dateFrom = from.split("/");
        String[] dateTo = to.split("/");
        LocalDate calcFrom = LocalDate.of(Integer.parseInt(dateFrom[0]), Integer.parseInt(dateFrom[1]), Integer.parseInt(dateFrom[2]));
        LocalDate calcTo = LocalDate.of(Integer.parseInt(dateTo[0]), Integer.parseInt(dateTo[1]), Integer.parseInt(dateTo[2]));

        //FILL IT WITH DATA

        try {
            List<HoneyHarvest> harvestsFromHive = allHarvests.stream().filter(harvest -> harvest.getId_hive() == idHive).collect(Collectors.toList());
            for (HoneyHarvest h: harvestsFromHive) {
                LocalDate recordDate = LocalDate.of(h.getYear(), h.getMonth(), h.getDay());
                if (!recordDate.isBefore(calcFrom) && !recordDate.isAfter(calcTo)) {
                    harvests.add(h);
                }
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "There are no harvests!", Toast.LENGTH_SHORT).show();
        }
        if (harvests == null || harvests.size() == 0) {
            Toast.makeText(getContext(), "There are no harvests!", Toast.LENGTH_SHORT).show();
        } else {

            double totalAmount = 0;
            double sumWaterContent = 0;
            int effectiveHarvests = 0;
            List<LocalDate> harvestDates = new ArrayList<>();

            //count everything all at once
            for (HoneyHarvest h: harvests) {
                totalAmount += h.getAmount();
                sumWaterContent += h.getWater_content();
                LocalDate harvestDate = LocalDate.of(h.getYear(), h.getMonth(), h.getDay());

                //nemusí být chronologicky, data můžou skákat, ověřuje pomocí List<>.contains
                if (!harvestDates.contains(harvestDate)) {
                    effectiveHarvests++;
                    harvestDates.add(harvestDate);
                }
            }

            double averageWaterContent = sumWaterContent / harvests.size();

            TextView total = (TextView) getView().findViewById(R.id.stats_harvest_hive_total);
            TextView average = (TextView) getView().findViewById(R.id.stats_harvest_hive_water);
            total.setText(totalAmount + " kg");
            average.setText(averageWaterContent + " %");

            //Graphs
            GraphView graphAmount = (GraphView) getView().findViewById(R.id.stats_harvest_hive_graph_amounts);
            BarGraphSeries<DataPoint> dataAmount = new BarGraphSeries<>(new DataPoint[0]);
            dataAmount.resetData(recordDataAmount(effectiveHarvests));
            dataAmount.setColor(Color.parseColor("#a86807"));
            graphAmount.addSeries(dataAmount);
            graphAmount.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
            graphAmount.getGridLabelRenderer().setNumHorizontalLabels(4);
            graphAmount.getGridLabelRenderer().setHumanRounding(false);
            graphAmount.getViewport().setMinY(0);
            graphAmount.getViewport().setYAxisBoundsManual(true);

            GraphView graphWater = (GraphView) getView().findViewById(R.id.stats_harvest_hive_graph_water);
            LineGraphSeries<DataPoint> dataWater = new LineGraphSeries<>(new DataPoint[0]);
            dataWater.resetData(recordDataWater(effectiveHarvests));
            dataWater.setColor(Color.parseColor("#a86807"));
            graphWater.addSeries(dataWater);
            graphWater.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
            graphWater.getGridLabelRenderer().setNumHorizontalLabels(4);
            graphWater.getGridLabelRenderer().setHumanRounding(false);
        }
    }

    private DataPoint[] recordDataAmount(int effectiveHarvests) {
        DataPoint[] data = new DataPoint[effectiveHarvests];
        HashMap<LocalDate, Double> dataHelp = new HashMap<LocalDate, Double>();
        for (HoneyHarvest h: harvests) {
            LocalDate date = LocalDate.of(h.getYear(), h.getMonth(), h.getDay());

            //java8 <3
            dataHelp.computeIfPresent(date, (k, v) -> v + h.getAmount());
            dataHelp.putIfAbsent(date, h.getAmount());
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

    private DataPoint[] recordDataWater(int effectiveHarvests) {
        DataPoint[] data = new DataPoint[effectiveHarvests];
        HashMap<LocalDate, Double> dataHelp = new HashMap<LocalDate, Double>();
        for (HoneyHarvest h: harvests) {
            LocalDate date = LocalDate.of(h.getYear(), h.getMonth(), h.getDay());

            //java8 <3
            dataHelp.computeIfPresent(date, (k, v) -> (v + h.getWater_content())/2);
            dataHelp.putIfAbsent(date, h.getWater_content());
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