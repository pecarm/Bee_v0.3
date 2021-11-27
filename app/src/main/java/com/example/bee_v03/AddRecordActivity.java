package com.example.bee_v03;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class AddRecordActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    Button buttonSelectDate, buttonAddRecord;
    TextView textViewSelectedDate;
    EditText editText;
    AddRecordViewModel addRecordViewModel;
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

        //to simplify running this activity
        if (addRecordViewModel.getAllHives().getValue().size() == 0) {
            Toast.makeText(this, "There are no hives, add hive first!", Toast.LENGTH_SHORT).show();
            this.finish();
        }

        buttonAddRecord = (Button) findViewById(R.id.buttonAddRecord);
        buttonSelectDate = (Button) findViewById(R.id.buttonSelectDate);
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
                } else {
                    Toast.makeText(AddRecordActivity.this, "Date or text not inserted!", Toast.LENGTH_SHORT).show();
                }
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
}