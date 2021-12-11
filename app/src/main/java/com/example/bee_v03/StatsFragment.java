package com.example.bee_v03;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class StatsFragment extends Fragment {

    private List<Hive> allHives;
    private int idHive;

    public StatsFragment() {
        // Required empty public constructor
    }

    public static StatsFragment newInstance(List<Hive> allHives, int idHive) {
        StatsFragment fragment = new StatsFragment();
        Bundle args = new Bundle();
        args.putInt("ID_HIVE", idHive);
        args.putSerializable("ALL_HIVES", (Serializable) allHives);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            idHive = getArguments().getInt("ID_HIVE");
            allHives = (List<Hive>) getArguments().getSerializable("ALL_HIVES");
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //FILL IT WITH DATA

        Hive hive = allHives.stream().filter(h -> h.getId_hive() == idHive).collect(Collectors.toList()).get(0);

        TextView row = getView().findViewById(R.id.fragment_stats_row_number);
        RatingBar rb_agr = getView().findViewById(R.id.fragment_stats_rb_agr);
        RatingBar rb_stz = getView().findViewById(R.id.fragment_stats_rb_stz);
        RatingBar rb_mep = getView().findViewById(R.id.fragment_stats_rb_mep);
        RatingBar rb_siv = getView().findViewById(R.id.fragment_stats_rb_siv);
        RatingBar rb_stp = getView().findViewById(R.id.fragment_stats_rb_stp);
        RatingBar rb_bod = getView().findViewById(R.id.fragment_stats_rb_bod);
        RatingBar rb_sli = getView().findViewById(R.id.fragment_stats_rb_sli);

        row.setText(((Integer) hive.getRow()).toString());
        rb_agr.setRating(hive.getAgresivnost());
        rb_stz.setRating(hive.getStav_zasob());
        rb_mep.setRating(hive.getMezerovitost_plodu());
        rb_siv.setRating(hive.getSila_vcelstva());
        rb_stp.setRating(hive.getStavebni_pud());
        rb_bod.setRating(hive.getBodavost());
        rb_sli.setRating(hive.getSlidivost());
    }
}