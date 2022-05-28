package com.example.bee_v03;

import android.app.Dialog;
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
    private boolean isArchive;
    private AlertsCustomExpandableListAdapter adapter;

    public AlertsFragment() {
        // Required empty public constructor
    }

    public static AlertsFragment newInstance(List<Alert> allAlerts, int idHive, boolean isArchive) {
        AlertsFragment fragment = new AlertsFragment();
        Bundle args = new Bundle();
        args.putInt("ID_HIVE", idHive);
        args.putSerializable("ALL_ALERTS", (Serializable) allAlerts);
        args.putBoolean("IS_ARCHIVE", isArchive);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            idHive = getArguments().getInt("ID_HIVE");
            allAlerts = (List<Alert>) getArguments().getSerializable("ALL_ALERTS");
            isArchive = getArguments().getBoolean("IS_ARCHIVE");

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
            if (isArchive) {
                alerts = allAlerts.stream().filter(alert -> (alert.getId_hive() == idHive)).collect(Collectors.toList());
            } else {
                alerts = allAlerts.stream().filter(alert -> (alert.getId_hive() == idHive) && !alert.isArchived()).collect(Collectors.toList());
            }
        } catch (Exception e) {
            //Toast.makeText(getContext(), "There are no alerts!", Toast.LENGTH_SHORT).show();
        }
        if (alerts == null || alerts.size() == 0) {
            Toast.makeText(getContext(), "Žádná upozornění!", Toast.LENGTH_SHORT).show();
        } else {
            List<String> severities = new ArrayList<>();
            severities.add("Vysoká");
            severities.add("Střední");
            severities.add("Nízká");

            adapter = new AlertsCustomExpandableListAdapter(getContext(), severities, alerts);
            ExpandableListView elv = view.findViewById(R.id.expandable_list_view_alerts);
            elv.setAdapter(adapter);

            elv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    Alert alert = (Alert)adapter.getChild(groupPosition, childPosition);

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