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

public class DashboardFragment extends Fragment {

    private List<Hive> allHives;
    private List<Alert> allAlerts;
    private DashboardCustomExpandableListAdapter adapter;

    public DashboardFragment() {
        // Required empty public constructor
    }

    public static DashboardFragment newInstance(List<Hive> allHives, List<Alert> allAlerts) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putSerializable("ALL_HIVES", (Serializable) allHives);
        args.putSerializable("ALL_ALERTS", (Serializable) allAlerts);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            allHives = (List<Hive>) getArguments().getSerializable("ALL_HIVES");
            allAlerts = (List<Alert>) getArguments().getSerializable("ALL_ALERTS");
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //FILL IT WITH DATA
        if (allAlerts== null || allAlerts.size() == 0) {
            Toast.makeText(getContext(), "There are no alerts!", Toast.LENGTH_SHORT).show();
        } else {
            List<String> severities = new ArrayList<>();
            severities.add("High");
            severities.add("Medium");
            severities.add("Low");

            adapter = new DashboardCustomExpandableListAdapter(getContext(), severities, allAlerts, allHives);
            ExpandableListView elv = view.findViewById(R.id.expandable_list_view_dashboard);
            elv.setAdapter(adapter);
        }
    }
}