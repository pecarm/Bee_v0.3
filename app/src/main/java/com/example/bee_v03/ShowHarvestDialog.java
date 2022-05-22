package com.example.bee_v03;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ShowHarvestDialog extends DialogFragment {
    TextView tvDate, tvAmount, tvWater, tvType, tvText;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_show_harvest, null);

        Bundle bundle = getArguments();
        HoneyHarvest harvest = (HoneyHarvest) bundle.getSerializable("HARVEST");

        builder.setView(view)
                .setTitle("Medobraní")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                                            }
                });

        tvDate = view.findViewById(R.id.dialog_harvest_date);
        tvAmount = view.findViewById(R.id.dialog_harvest_amount);
        tvWater = view.findViewById(R.id.dialog_harvest_water);
        tvType = view.findViewById(R.id.dialog_harvest_type);
        tvText = view.findViewById(R.id.dialog_harvest_text);

        tvDate.setText(harvest.getYear() + "/" + harvest.getMonth() + "/" + harvest.getDay());
        tvAmount.setText(harvest.getAmount() + " kg");
        tvWater.setText(harvest.getWater_content() + " %");
        tvType.setText(harvest.getType());
        tvText.setText(harvest.getText());

        return builder.create();
    }
}
