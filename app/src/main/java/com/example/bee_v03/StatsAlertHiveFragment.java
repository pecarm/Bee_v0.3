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

public class StatsAlertHiveFragment extends Fragment {
    private List<Alert> allAlerts;
    private int idHive;
    private String from, to;
    private AlertsCustomExpandableListAdapter adapter;

    public StatsAlertHiveFragment(){
        // Required empty public constructor
    }

    public static StatsAlertHiveFragment newInstance(List<Alert> allAlerts, int idHive, String from, String to) {
        StatsAlertHiveFragment fragment = new StatsAlertHiveFragment();
        Bundle args = new Bundle();
        args.putInt("ID_HIVE", idHive);
        args.putSerializable("ALL_ALERTS", (Serializable) allAlerts);
        args.putString("FROM", from);
        args.putString("TO", to);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            idHive = getArguments().getInt("ID_HIVE");
            allAlerts = (List<Alert>) getArguments().getSerializable("ALL_ALERTS");
            from = getArguments().getString("FROM");
            to = getArguments().getString("TO");
        }
        return inflater.inflate(R.layout.fragment_stats_alert_hive, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] dateFrom = from.split("/");
        String[] dateTo = to.split("/");
        LocalDate calcFrom = LocalDate.of(Integer.parseInt(dateFrom[0]), Integer.parseInt(dateFrom[1]), Integer.parseInt(dateFrom[2]));
        LocalDate calcTo = LocalDate.of(Integer.parseInt(dateTo[0]), Integer.parseInt(dateTo[1]), Integer.parseInt(dateTo[2]));

        //FILL IT WITH DATA

        TextView total = (TextView) getView().findViewById(R.id.stats_alert_hive_total);
        TextView solved = (TextView) getView().findViewById(R.id.stats_alert_hive_solved);
        TextView high = (TextView) getView().findViewById(R.id.stats_alert_hive_high);
        TextView medium = (TextView) getView().findViewById(R.id.stats_alert_hive_medium);
        TextView low = (TextView) getView().findViewById(R.id.stats_alert_hive_low);
        ExpandableListView expandableListView = (ExpandableListView) getView().findViewById(R.id.stats_alert_hive_expandable_list_view);


        List<Alert> alerts = new ArrayList<>();
        try {
            List<Alert> alertsFromHive = allAlerts.stream().filter(alert -> (alert.getId_hive() == idHive)).collect(Collectors.toList());
            for (Alert al: alertsFromHive) {
                LocalDate alertDate = LocalDate.of(al.getYear(), al.getMonth(), al.getDay());
                if (!alertDate.isBefore(calcFrom) && !alertDate.isAfter(calcTo)) {
                    alerts.add(al);
                }
            }
        } catch (Exception e) {
            //Toast.makeText(getContext(), "There are no alerts!", Toast.LENGTH_SHORT).show();
        }
        if (alerts == null || alerts.size() == 0) {
            Toast.makeText(getContext(), "Žádná upozornění!", Toast.LENGTH_SHORT).show();
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
