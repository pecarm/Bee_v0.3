package com.example.bee_v03;

import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SelectExpandableListDataPump {
    public static HashMap<String, List<String>> getData(SelectViewModel selectViewModel) {
        HashMap<String, List<String>> expandableList = new HashMap<String, List<String>>();

        try {
            for (HivesLocation location : selectViewModel.getAllLocations().getValue()) {
                List<String> hiveNames = new ArrayList<String>();
                for (Hive hive : selectViewModel.getAllHives().getValue()) {
                    if (hive.getId_location() == location.getId_location()) {
                        hiveNames.add(hive.getName());
                    }
                }
                expandableList.put(location.getName(), hiveNames);
            }
        }
        catch (Exception e) {
            Toast.makeText(selectViewModel.getApplication(), "There are no Locations or Hives", Toast.LENGTH_SHORT).show();
        }

        return expandableList;
    }
}
