package com.example.bee_v03;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;

public class ScopeSelectorActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private RadioGroup radioGroupScope, radioGroupTimeframe;
    private CheckBox archivedCheck;
    private Button buttonFrom, buttonTo, buttonProceed;
    private TextView textFrom, textTo;
    private LocalDate today;
    private String target;
    private boolean isFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scope_selector);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Vyberte rozsah");

        radioGroupScope = (RadioGroup) findViewById(R.id.scope_radio_subset);
        radioGroupTimeframe = (RadioGroup) findViewById(R.id.scope_radio_timeframe);
        buttonFrom = (Button) findViewById(R.id.scope_button_from);
        buttonTo = (Button) findViewById(R.id.scope_button_to);
        buttonProceed = (Button) findViewById(R.id.scope_button_proceed);
        textFrom = (TextView) findViewById(R.id.scope_from);
        textTo = (TextView) findViewById(R.id.scope_to);

        today = LocalDate.now();

        buttonFrom.setEnabled(false);
        buttonTo.setEnabled(false);

        radioGroupScope.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.scope_radio_hive:
                        target = "hive";
                        break;
                    case R.id.scope_radio_location:
                        target = "location";
                        break;
                    case R.id.scope_radio_all:
                        target = "all";
                        break;
                }
            }
        });

        radioGroupTimeframe.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                buttonFrom.setEnabled(false);
                buttonTo.setEnabled(false);
                switch (checkedId) {
                    case R.id.scope_radio_month:
                        LocalDate monthAgo = today.minusMonths(1);
                        textFrom.setText(monthAgo.getYear() + "/" + monthAgo.getMonthValue() + "/" + monthAgo.getDayOfMonth());
                        textTo.setText(today.getYear() + "/" + today.getMonthValue() + "/" + today.getDayOfMonth());
                        break;
                    case R.id.scope_radio_year:
                        LocalDate yearAgo = today.minusYears(1);
                        textFrom.setText(yearAgo.getYear() + "/" + yearAgo.getMonthValue() + "/" + yearAgo.getDayOfMonth());
                        textTo.setText(today.getYear() + "/" + today.getMonthValue() + "/" + today.getDayOfMonth());
                        break;
                    case R.id.scope_radio_this_month:
                        LocalDate startOfMonth = LocalDate.of(today.getYear(), today.getMonthValue(), 1);
                        textFrom.setText(startOfMonth.getYear() + "/" + startOfMonth.getMonthValue() + "/" + startOfMonth.getDayOfMonth());
                        textTo.setText(today.getYear() + "/" + today.getMonthValue() + "/" + today.getDayOfMonth());
                        break;
                    case R.id.scope_radio_this_year:
                        LocalDate startOfYear = LocalDate.of(today.getYear(), 1, 1);
                        textFrom.setText(startOfYear.getYear() + "/" + startOfYear.getMonthValue() + "/" + startOfYear.getDayOfMonth());
                        textTo.setText(today.getYear() + "/" + today.getMonthValue() + "/" + today.getDayOfMonth());
                        break;
                    case R.id.scope_radio_from_to:
                        buttonFrom.setEnabled(true);
                        buttonTo.setEnabled(true);
                        break;
                }
            }
        });

        buttonFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFrom = true;
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "Date picker");
            }
        });

        buttonTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFrom = false;
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "Date picker");
            }
        });

        buttonProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                archivedCheck = (CheckBox) findViewById(R.id.scope_checkbox);
                Intent intent;
                if (textFrom.getText().equals("Od") || textTo.getText().equals("Do")) {
                    Toast.makeText(ScopeSelectorActivity.this, "Vyberte časový rozsah!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (target == null) {
                    Toast.makeText(ScopeSelectorActivity.this, "Vyberte rozsah!", Toast.LENGTH_SHORT).show();
                    return;
                }

                switch (target) {
                    case "hive":
                        intent = new Intent(v.getContext(), com.example.bee_v03.SelectActivity.class);
                        Bundle bundle = new Bundle();
                        if (!archivedCheck.isChecked()) {
                            bundle.putString("TARGET", "stats_hive");
                        } else {
                            bundle.putString("TARGET", "stats_hive_all");
                        }
                        bundle.putString("FROM", textFrom.getText().toString());
                        bundle.putString("TO", textTo.getText().toString());
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case "location":
                        intent = new Intent(v.getContext(), com.example.bee_v03.SelectActivity.class);
                        Bundle bundle1 = new Bundle();
                        if (!archivedCheck.isChecked()) {
                            bundle1.putString("TARGET", "stats_location");
                            bundle1.putBoolean("INCLUDE_ARCHIVE", archivedCheck.isChecked());
                        } else {
                            bundle1.putString("TARGET", "stats_location_all");
                            bundle1.putBoolean("INCLUDE_ARCHIVE", archivedCheck.isChecked());
                        }
                        bundle1.putString("FROM", textFrom.getText().toString());
                        bundle1.putString("TO", textTo.getText().toString());
                        intent.putExtras(bundle1);
                        startActivity(intent);
                        break;
                    case "all":
                        intent = new Intent(v.getContext(), com.example.bee_v03.StatsActivity.class);
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("FROM", textFrom.getText().toString());
                        bundle2.putString("TO", textTo.getText().toString());
                        bundle2.putInt("ID_LOCATION", -1);
                        bundle2.putBoolean("INCLUDE_ARCHIVE", archivedCheck.isChecked());
                        intent.putExtras(bundle2);
                        startActivity(intent);
                        break;
                }
            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (isFrom) {
            if (LocalDate.of(year, month + 1, dayOfMonth).isAfter(today)) {
                Toast.makeText(this, "Zvolené datum je pozdější, než dnešní.", Toast.LENGTH_SHORT).show();
                return;
            } else if (!textTo.getText().equals("Do")) {
                String[] date = textTo.getText().toString().split("/");
                if (LocalDate.of(year, month + 1, dayOfMonth).isAfter(LocalDate.of(Integer.valueOf(date[0]), Integer.valueOf(date[1]), Integer.valueOf(date[2])))) {
                    Toast.makeText(this, "Zvolené datum Od je po datu Do.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            textFrom.setText(year + "/" + (month+1) + "/" + dayOfMonth);
        } else {
            if (LocalDate.of(year, month + 1, dayOfMonth).isAfter(today)) {
                Toast.makeText(this, "Zvolené datum je pozdější, než dnešní.", Toast.LENGTH_SHORT).show();
                return;
            } else if (!textFrom.getText().equals("Od")) {
                String[] date = textFrom.getText().toString().split("/");
                if (LocalDate.of(year, month + 1, dayOfMonth).isBefore(LocalDate.of(Integer.valueOf(date[0]), Integer.valueOf(date[1]), Integer.valueOf(date[2])))) {
                    Toast.makeText(this, "Zvolené datum Do je před datem Od.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            textTo.setText(year + "/" + (month+1) + "/" + dayOfMonth);
        }
    }
}