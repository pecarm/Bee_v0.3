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
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class HoneyHarvestFragment extends Fragment {

    private List<HoneyHarvest> allHarvests;
    private int idHive;

    public HoneyHarvestFragment() {
        // Required empty public constructor
    }

    public static HoneyHarvestFragment newInstance(List<HoneyHarvest> allHarvests, int idHive) {
        HoneyHarvestFragment fragment = new HoneyHarvestFragment();
        Bundle args = new Bundle();
        args.putInt("ID_HIVE", idHive);
        args.putSerializable("ALL_HARVESTS", (Serializable) allHarvests);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            allHarvests = (List<HoneyHarvest>) getArguments().getSerializable("ALL_HARVESTS");
            idHive = getArguments().getInt("ID_HIVE");
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_honey_harvest, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<HoneyHarvest> harvests = new ArrayList<>();
        String[] from = new String[] {"date", "harvest"};
        int[] to = new int[] {R.id.adapter_view_object_honey_harvest_date, R.id.adapter_view_object_honey_harvest_amount, R.id.adapter_view_object_honey_harvest_water_content};

        try {
            harvests = allHarvests.stream().filter(harvest -> harvest.getId_hive() == idHive).collect(Collectors.toList());
        } catch (Exception e) {
            Toast.makeText(getContext(), "There are no harvests!", Toast.LENGTH_SHORT).show();
        }

        if (harvests == null || harvests.size() == 0) {
            Toast.makeText(getContext(), "There are no harvests!", Toast.LENGTH_SHORT).show();
        } else {
            ArrayList<HashMap<String, Object>> data = recordData(harvests);

            HoneyHarvestAdapter adapter = new HoneyHarvestAdapter(getContext(), data, R.layout.adapter_view_object_honey_harvest, from, to);

            ListView lv = getView().findViewById(R.id.list_view_object_honey_harvest);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    HoneyHarvest harvest = (HoneyHarvest) data.get(position).get("harvest");

                    ShowHarvestDialog dialog = new ShowHarvestDialog();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("HARVEST", harvest);

                    dialog.setArguments(bundle);
                    dialog.show(getParentFragmentManager(), "Harvest dialog");

                }
            });
        }
    }

    private ArrayList<HashMap<String, Object>> recordData(List<HoneyHarvest> harvests) {
        ArrayList<HashMap<String, Object>> data = new ArrayList<>();
        for (HoneyHarvest harvest : harvests) {
            //WE CAN PUT MULTIPLE ITEMS and then PASS THEM BY KEY, even a list of WARNINGS
            HashMap<String, Object> item = new HashMap<>();
            String date = harvest.getYear()+"/"+harvest.getMonth()+"/" + harvest.getDay();
            item.put("date", date);
            item.put("harvest", harvest);
            data.add(item);
        }
        return data;
    }
}