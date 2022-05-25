package com.example.bee_v03;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddRecordActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AddAlertDialog.AddAlertDialogListener {
    Button buttonSelectDate, buttonAddRecord, buttonAddAlert;
    TextView textViewSelectedDate;
    EditText editTextNote, editTextFeeding;
    AddRecordViewModel addRecordViewModel;
    ListView listView;
    RatingBar rbResourcesState, rbBroodIntegrity;
    NumberPicker numberPicker;
    List<Alert> allAlerts;
    ArrayList<String> alerts = new ArrayList<>();
    ArrayList<Alert> addedAlerts = new ArrayList<>();
    ArrayAdapter<String> adapter;
    int idHive;
    int bases;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idHive = extras.getInt("HIVE_ID");
        }

        addRecordViewModel = new ViewModelProvider(this).get(AddRecordViewModel.class);

        buttonAddRecord = (Button) findViewById(R.id.buttonAddRecord);
        buttonSelectDate = (Button) findViewById(R.id.buttonSelectDateRecord);
        buttonAddAlert = (Button) findViewById(R.id.buttonAddAlert);
        textViewSelectedDate = (TextView) findViewById(R.id.add_record_text_view_date);
        editTextNote = (EditText) findViewById(R.id.add_record_edit_text_record);
        rbResourcesState = (RatingBar) findViewById(R.id.ratingBarResourcesState);
        rbBroodIntegrity = (RatingBar) findViewById(R.id.ratingBarBroodIntegrity);
        editTextFeeding = (EditText) findViewById(R.id.add_record_edit_text_feeding);

        numberPicker = (NumberPicker) findViewById(R.id.numberPickerBases);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(40);
        numberPicker.setValue(20);

        numberPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return Integer.toString(value - 20);
            }
        });

        setRatingBarToRegisterProperly(rbBroodIntegrity);
        setRatingBarToRegisterProperly(rbResourcesState);

        bases = 0;

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                bases = newVal - 20;
            }
        });

        buttonSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "Date picker");
            }
        });

        buttonAddRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!textViewSelectedDate.getText().equals("Datum") && (int)rbResourcesState.getRating()!=0 && (int)rbBroodIntegrity.getRating()!=0) {
                    String[] date = textViewSelectedDate.getText().toString().split("/");

                    Record r = new Record(idHive, Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]), (int)rbResourcesState.getRating(),
                            (int)rbBroodIntegrity.getRating(), editTextFeeding.getText().toString(), bases, editTextNote.getText().toString());
                    addRecordViewModel.insert(r);
                    Toast.makeText(AddRecordActivity.this, "Record added!", Toast.LENGTH_SHORT).show();
                    for (Alert alert:addedAlerts
                    ) {
                        addRecordViewModel.insert(alert);
                    }
                    AddRecordActivity.this.finish();
                } else {
                    Toast.makeText(AddRecordActivity.this, "Date or ratings not selected!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonAddAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, R.id.list_item_item, alerts);
        listView = findViewById(R.id.add_record_list_view_alerts);
        listView.setAdapter(adapter);

        addRecordViewModel.getAllAlerts().observe(this, new Observer<List<Alert>>() {
            @Override
            public void onChanged(List<Alert> alerts) {
                allAlerts = alerts;

                if (alerts == null || alerts.size() ==0) {
                    return;
                }

                listView.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        LocalDate today = LocalDate.now();
        if (LocalDate.of(year, month + 1, dayOfMonth).isAfter(today)) {
            Toast.makeText(this, "Zvolené datum je pozdější, než dnešní.", Toast.LENGTH_SHORT).show();
            return;
        }
        textViewSelectedDate.setText(year + "/" + (month+1) + "/" + month);
    }

    private void openDialog() {
        AddAlertDialog dialog = new AddAlertDialog();
        dialog.show(getSupportFragmentManager(), "Alert dialog");
    }

    @Override
    public void applyTexts(String severity, String text) {
        //Toast.makeText(this, severity, Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, text, Toast.LENGTH_SHORT).show();

        String shortened = text;
        if (text.length() > 30) {
            shortened = text.substring(0, 27) + "...";
        }

        int svrt;

        switch (severity) {
            case "Vysoká":
                svrt = 1;
                break;
            case "Střední":
                svrt = 2;
                break;
            case "Nízká":
                svrt = 3;
                break;
            default:
                svrt = 0;
        }

        if (textViewSelectedDate.getText().toString().equals("Datum")) {
            Toast.makeText(this, "Select date first", Toast.LENGTH_SHORT).show();
            return;
        }
        String[] date = textViewSelectedDate.getText().toString().split("/");
        Alert alert = new Alert(idHive, svrt, Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]), text, false);
        addedAlerts.add(alert);
        alerts.add(severity + ", " + shortened);
        adapter.notifyDataSetChanged();
    }

    private void setRatingBarToRegisterProperly(RatingBar rb) {
        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) ratingBar.setRating((float) Math.ceil(rating));
            }
        });
    }
}