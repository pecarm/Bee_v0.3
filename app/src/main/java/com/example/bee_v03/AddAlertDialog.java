package com.example.bee_v03;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddAlertDialog extends DialogFragment {
    private Spinner spinner;
    private EditText text;
    private AddAlertDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_alert, null);

        builder.setView(view)
                .setTitle("Upozornění")
                .setNegativeButton("Zrušit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String alert = text.getText().toString();
                        String severity = spinner.getSelectedItem().toString();

                        listener.applyTexts(severity, alert);
                    }
                });
        text = view.findViewById(R.id.alert_edit_text);
        spinner = view.findViewById(R.id.alert_spinner_severity);

        String[] severities = new String[] {"Vysoká", "Střední", "Nízká"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, severities);
        spinner.setAdapter(adapter);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (AddAlertDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement ExampleDialogListener");
        }
    }

    public interface AddAlertDialogListener {
        void applyTexts(String severity, String text);
    }
}
