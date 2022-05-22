package com.example.bee_v03;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class InfoFragment extends Fragment {

    private List<Hive> allHives;
    private List<HivesLocation> allLocations;
    private int idHive;

    public InfoFragment() {
        // Required empty public constructor
    }

    public static InfoFragment newInstance(List<Hive> allHives, List<HivesLocation> allLocations, int idHive) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putInt("ID_HIVE", idHive);
        args.putSerializable("ALL_HIVES", (Serializable) allHives);
        args.putSerializable("ALL_LOCATIONS", (Serializable) allLocations);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            idHive = getArguments().getInt("ID_HIVE");
            allHives = (List<Hive>) getArguments().getSerializable("ALL_HIVES");
            allLocations = (List<HivesLocation>) getArguments().getSerializable("ALL_LOCATIONS");
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //FILL IT WITH DATA

        Hive hive = allHives.stream().filter(h -> h.getId_hive() == idHive).collect(Collectors.toList()).get(0);

        TextView row = getView().findViewById(R.id.fragment_info_row_number);
        RatingBar rb_agr = getView().findViewById(R.id.fragment_info_rb_agr);
        RatingBar rb_siv = getView().findViewById(R.id.fragment_info_rb_siv);
        RatingBar rb_stp = getView().findViewById(R.id.fragment_info_rb_stp);
        RatingBar rb_bod = getView().findViewById(R.id.fragment_info_rb_bod);
        RatingBar rb_sli = getView().findViewById(R.id.fragment_info_rb_sli);
        RatingBar rb_cip = getView().findViewById(R.id.fragment_info_rb_cip);
        TextView year = getView().findViewById(R.id.fragment_info_mother_number);
        LinearLayout colourSquare = getView().findViewById(R.id.fragment_info_color);
        Button button = getView().findViewById(R.id.fragment_info_button);

        row.setText(((Integer) hive.getRow()).toString());
        rb_agr.setRating(hive.getAggressivity());
        rb_siv.setRating(hive.getHive_strength());
        rb_stp.setRating(hive.getBuild_instinct());
        rb_bod.setRating(hive.getStinging_instinct());
        rb_sli.setRating(hive.getExploration_instinct());
        rb_cip.setRating(hive.getCleaning_instinct());
        year.setText(((Integer) hive.getMother_year()).toString());

        switch (hive.getMother_year() % 5) {
            case 0:
                colourSquare.setBackgroundColor(Color.parseColor("#0000ff"));
                break;
            case 1:
                colourSquare.setBackgroundResource(R.drawable.border_white);
                break;
            case 2:
                colourSquare.setBackgroundColor(Color.parseColor("#ffff00"));
                break;
            case 3:
                colourSquare.setBackgroundColor(Color.parseColor("#ff0000"));
                break;
            case 4:
                colourSquare.setBackgroundColor(Color.parseColor("#006400"));
                break;
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HivesLocation location = allLocations.stream().filter(l -> l.getId_location() == hive.getId_location()).collect(Collectors.toList()).get(0);
                String uri = "geo:0,0?q=" + location.getLatitude() + "," + location.getLongitude() + "(" + location.getName() + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });

    }
}