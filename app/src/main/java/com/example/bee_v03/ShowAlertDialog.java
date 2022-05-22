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
import androidx.lifecycle.ViewModelProvider;

public class ShowAlertDialog extends DialogFragment {
    TextView tvDate, tvSeverity, tvText;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_show_alert, null);

        ShowAlertDialogViewModel viewModel = new ViewModelProvider(this).get(ShowAlertDialogViewModel.class);

        Bundle bundle = getArguments();
        Alert alert = (Alert)bundle.getSerializable("ALERT");

        String severity;
        switch (alert.getSeverity()) {
            case 1:
                severity = "Vysoká";
                break;
            case 2:
                severity = "Střední";
                break;
            case 3:
                severity = "Nízká";
                break;
            default:
                severity = "Nedefinovaná";
                break;
        }

        builder.setView(view)
                .setTitle("Upozornění")
                .setNegativeButton("Archivovat", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alert.setArchived(true);
                        viewModel.update(alert);
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        tvDate = view.findViewById(R.id.dialog_alert_date);
        tvSeverity = view.findViewById(R.id.dialog_alert_severity);
        tvText = view.findViewById(R.id.dialog_alert_text);

        tvDate.setText(alert.getYear() + "/" + alert.getMonth() + "/" + alert.getDay());
        tvSeverity.setText(severity);
        tvText.setText(alert.getText());

        return builder.create();
    }
}
