package com.example.bee_v03;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

public class StatsHarvestFragment extends Fragment {
    private List<Hive> allHives;
    private List<HoneyHarvest> allHarvests;
    private List<HoneyHarvest> harvests = new ArrayList<>();
    private int idLocation;
    private String from, to;
    private boolean includeArchived;

    public StatsHarvestFragment() {
        // Required empty public constructor
    }

    public static StatsHarvestFragment newInstance(List<Hive> allHives, List<HoneyHarvest> allHarvests, int idLocation, String from, String to, boolean includeArchived) {
        StatsHarvestFragment fragment = new StatsHarvestFragment();
        Bundle args = new Bundle();
        args.putSerializable("ALL_HARVESTS", (Serializable) allHarvests);
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
            allHarvests = (List<HoneyHarvest>) getArguments().getSerializable("ALL_HARVESTS");
            allHives = (List<Hive>) getArguments().getSerializable("ALL_HIVES");
            from = getArguments().getString("FROM");
            to = getArguments().getString("TO");
            includeArchived = getArguments().getBoolean("INCLUDE_ARCHIVED");
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
                    hiveIds = allHives.stream().filter(hive -> (hive.getId_location() == idLocation) && (!hive.isArchived())).map(hive -> hive.getId_hive())
                            .collect(Collectors.toList());
                }
            }

            List<HoneyHarvest> harvestsFromHive = allHarvests.stream().filter(h -> hiveIds.contains(h.getId_hive())).collect(Collectors.toList());
            for (HoneyHarvest h: harvestsFromHive) {
                LocalDate recordDate = LocalDate.of(h.getYear(), h.getMonth(), h.getDay());
                if (!recordDate.isBefore(calcFrom) && !recordDate.isAfter(calcTo)) {
                    harvests.add(h);
                }
            }
        } catch (Exception e) {
            //Toast.makeText(getContext(), "There are no records!", Toast.LENGTH_SHORT).show();
        }
        if (harvests.size() == 0) {
            Toast.makeText(getContext(), "Žádné záznamy!", Toast.LENGTH_SHORT).show();
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
            BigDecimal bd = new BigDecimal(Double.toString(averageWaterContent));
            bd = bd.setScale(3, RoundingMode.HALF_UP);
            average.setText(bd.doubleValue() + " %");

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