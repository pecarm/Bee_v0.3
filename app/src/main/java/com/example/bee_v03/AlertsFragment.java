package com.example.bee_v03;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AlertsFragment extends Fragment {

    private List<Alert> allAlerts;
    private int idHive;
    private AlertsCustomExpandableListAdapter adapter;

    public AlertsFragment() {
        // Required empty public constructor
    }

    public static AlertsFragment newInstance(List<Alert> allAlerts, int idHive) {
        AlertsFragment fragment = new AlertsFragment();
        Bundle args = new Bundle();
        args.putInt("ID_HIVE", idHive);
        args.putSerializable("ALL_ALERTS", (Serializable) allAlerts);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            idHive = getArguments().getInt("ID_HIVE");
            allAlerts = (List<Alert>) getArguments().getSerializable("ALL_ALERTS");
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alerts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //FILL IT WITH DATA

        List<Alert> alerts = new ArrayList<>();
        try {
            alerts = allAlerts.stream().filter(alert -> alert.getId_hive() == idHive).collect(Collectors.toList());
        } catch (Exception e) {
            Toast.makeText(getContext(), "There are no alerts!", Toast.LENGTH_SHORT).show();
        }
        if (alerts.size() == 0) {
            Toast.makeText(getContext(), "There are no alerts!", Toast.LENGTH_SHORT).show();
        } else {
            List<String> severities = new ArrayList<>();
            severities.add("High");
            severities.add("Medium");
            severities.add("Low");

            adapter = new AlertsCustomExpandableListAdapter(getContext(), severities, allAlerts);
            ExpandableListView elv = view.findViewById(R.id.expandable_list_view_alerts);
            elv.setAdapter(adapter);
        }
    }
}