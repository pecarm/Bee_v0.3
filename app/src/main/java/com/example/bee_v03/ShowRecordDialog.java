package com.example.bee_v03;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ShowRecordDialog extends DialogFragment {
    TextView tvDate, tvBases, tvFeeding, tvText;
    RatingBar rbResources, rbIntegrity;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_show_record, null);

        Bundle bundle = getArguments();
        Record record = (Record) bundle.getSerializable("RECORD");

        builder.setView(view)
                .setTitle("Záznam")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int  which) {
                    }
                });

        tvDate = view.findViewById(R.id.dialog_record_date);
        tvBases = view.findViewById(R.id.dialog_record_bases);
        tvFeeding = view.findViewById(R.id.dialog_record_feeding);
        tvText = view.findViewById(R.id.dialog_record_text);
        rbResources = view.findViewById(R.id.ratingBarDialogResourcesState);
        rbIntegrity = view.findViewById(R.id.ratingBarDialogBroodIntegrity);

        tvDate.setText(record.getYear() + "/" + record.getMonth() + "/" + record.getDay());
        tvBases.setText(Integer.toString(record.getBases()));
        tvFeeding.setText(record.getFeeding());
        tvText.setText(record.getText());
        rbResources.setRating(record.getResources_state());
        rbIntegrity.setRating(record.getBrood_integrity());

        return builder.create();
    }
}
