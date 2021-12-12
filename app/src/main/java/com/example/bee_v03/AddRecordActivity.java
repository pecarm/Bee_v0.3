package com.example.bee_v03;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddRecordActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AddAlertDialog.AddAlertDialogListener {
    Button buttonSelectDate, buttonAddRecord, buttonAddAlert;
    TextView textViewSelectedDate;
    EditText editText;
    AddRecordViewModel addRecordViewModel;
    ListView listView;
    List<Alert> allAlerts;
    ArrayList<String> alerts = new ArrayList<>();
    ArrayList<Alert> addedAlerts = new ArrayList<>();
    ArrayAdapter<String> adapter;
    int idHive;

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
        buttonSelectDate = (Button) findViewById(R.id.buttonSelectDate);
        buttonAddAlert = (Button) findViewById(R.id.buttonAddAlert);
        textViewSelectedDate = (TextView) findViewById(R.id.add_record_text_view_date);
        editText = (EditText) findViewById(R.id.add_record_edit_text_record);

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
                if (!textViewSelectedDate.getText().equals("Select date") && !editText.getText().toString().equals("")) {
                    Record r = new Record(idHive, textViewSelectedDate.getText().toString(), editText.getText().toString());
                    addRecordViewModel.insert(r);
                    Toast.makeText(AddRecordActivity.this, "Record added!", Toast.LENGTH_SHORT).show();
                    AddRecordActivity.this.finish();
                } else {
                    Toast.makeText(AddRecordActivity.this, "Date or text not inserted!", Toast.LENGTH_SHORT).show();
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
        Calendar c = Calendar.getInstance();
        c.set(year, month, dayOfMonth);
        String selectedDate = DateFormat.getDateInstance().format(c.getTime());
        textViewSelectedDate.setText(selectedDate);
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
            shortened = text.substring(27) + "...";
        }

        int svrt;

        switch (severity) {
            case "High":
                svrt = 1;
                break;
            case "Medium":
                svrt = 2;
                break;
            case "Low":
                svrt = 3;
                break;
            default:
                svrt = 0;
        }

        if (textViewSelectedDate.getText().toString().equals("Select date")) {
            Toast.makeText(this, "Select date first", Toast.LENGTH_SHORT).show();
            return;
        }
        Alert alert = new Alert(idHive, svrt, textViewSelectedDate.getText().toString(), text);
        addedAlerts.add(alert);
        addRecordViewModel.insert(alert);
        alerts.add(severity + ", " + shortened);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        for (Alert alert:addedAlerts
             ) {
            addRecordViewModel.delete(alert);
        }
    }
}