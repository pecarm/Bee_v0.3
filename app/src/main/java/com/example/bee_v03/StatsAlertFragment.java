package com.example.bee_v03;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StatsAlertFragment extends Fragment {
    private List<Hive> allHives;
    private List<Alert> allAlerts;
    private int idLocation;
    private String from, to;
    private AlertsCustomExpandableListAdapter adapter;
    private boolean includeArchived;

    public StatsAlertFragment(){
        // Required empty public constructor
    }

    public static StatsAlertFragment newInstance(List<Hive> allHives, List<Alert> allAlerts, int idLocation, String from, String to, boolean includeArchived) {
        StatsAlertFragment fragment = new StatsAlertFragment();
        Bundle args = new Bundle();
        args.putSerializable("ALL_ALERTS", (Serializable) allAlerts);
        args.putSerializable("ALL_HIVES", (Serializable) allHives);
        args.putInt("ID_LOCATION", idLocation);
        args.putString("FROM", from);
        args.putString("TO", to);
        args.putBoolean("INCLUDE_ARCHIVED", includeArchived);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            idLocation = getArguments().getInt("ID_LOCATION");
            allAlerts = (List<Alert>) getArguments().getSerializable("ALL_ALERTS");
            allHives = (List<Hive>) getArguments().getSerializable("ALL_HIVES");
            from = getArguments().getString("FROM");
            to = getArguments().getString("TO");
            includeArchived = getArguments().getBoolean("INCLUDE_ARCHIVED");
        }
        return inflater.inflate(R.layout.fragment_stats_alert, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] dateFrom = from.split("/");
        String[] dateTo = to.split("/");
        LocalDate calcFrom = LocalDate.of(Integer.parseInt(dateFrom[0]), Integer.parseInt(dateFrom[1]), Integer.parseInt(dateFrom[2]));
        LocalDate calcTo = LocalDate.of(Integer.parseInt(dateTo[0]), Integer.parseInt(dateTo[1]), Integer.parseInt(dateTo[2]));

        //FILL IT WITH DATA

        TextView total = (TextView) getView().findViewById(R.id.stats_alert_total);
        TextView solved = (TextView) getView().findViewById(R.id.stats_alert_solved);
        TextView high = (TextView) getView().findViewById(R.id.stats_alert_high);
        TextView medium = (TextView) getView().findViewById(R.id.stats_alert_medium);
        TextView low = (TextView) getView().findViewById(R.id.stats_alert_low);
        ExpandableListView expandableListView = (ExpandableListView) getView().findViewById(R.id.stats_alert_expandable_list_view);


        List<Alert> alerts = new ArrayList<>();
        try {
            //filtruje alerty podle toho, jestli se nacházejí v lokalitě a/nebo jestli jsou archivované
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

            List<Alert> alertsFromHive = allAlerts.stream().filter(alert -> hiveIds.contains(alert.getId_hive())).collect(Collectors.toList());
            for (Alert al: alertsFromHive) {
                LocalDate alertDate = LocalDate.of(al.getYear(), al.getMonth(), al.getDay());
                if (!alertDate.isBefore(calcFrom) && !alertDate.isAfter(calcTo)) {
                    alerts.add(al);
                }
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "There are no alerts!", Toast.LENGTH_SHORT).show();
        }
        if (alerts == null || alerts.size() == 0) {
            Toast.makeText(getContext(), "There are no alerts!", Toast.LENGTH_SHORT).show();
        } else {
            total.setText(Integer.toString(alerts.size()));
            solved.setText(Integer.toString(alerts.stream().filter(alert -> alert.isArchived()).collect(Collectors.toList()).size()));
            high.setText(alerts.stream().filter(alert -> alert.getSeverity() == 1).collect(Collectors.toList()).size() + "("
                    + alerts.stream().filter(alert -> (alert.getSeverity() == 1) && alert.isArchived()).collect(Collectors.toList()).size() + ")");
            medium.setText(alerts.stream().filter(alert -> alert.getSeverity() == 2).collect(Collectors.toList()).size() + "("
                    + alerts.stream().filter(alert -> (alert.getSeverity() == 2) && alert.isArchived()).collect(Collectors.toList()).size() + ")");
            low.setText(alerts.stream().filter(alert -> alert.getSeverity() == 3).collect(Collectors.toList()).size() + "("
                    + alerts.stream().filter(alert -> (alert.getSeverity() == 3) && alert.isArchived()).collect(Collectors.toList()).size() + ")");
            List<String> severities = new ArrayList<>();
            severities.add("Vysoká");
            severities.add("Střední");
            severities.add("Nízká");

            adapter = new AlertsCustomExpandableListAdapter(getContext(), severities, alerts);
            expandableListView.setAdapter(adapter);

            expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    Alert alert = (Alert) adapter.getChild(groupPosition, childPosition);

                    ShowAlertDialog dialog = new ShowAlertDialog();
                    Bundle bundle = new Bundle();

                    bundle.putSerializable("ALERT", alert);

                    dialog.setArguments(bundle);
                    dialog.show(getParentFragmentManager(), "Alert dialog");
                    return false;
                }
            });
        }
    }
}
