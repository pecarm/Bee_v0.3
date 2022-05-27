package com.example.bee_v03;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class AddHoneyHarvestActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    AddHoneyHarvestViewModel addHoneyHarvestViewModel;
    EditText etAmount, etWaterContent, etType, etText;
    Button buttonDate, buttonAdd;
    TextView textViewDate;

    int idHive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_honey_harvest);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Nové medobraní");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4F1308")));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idHive = extras.getInt("HIVE_ID");
        }

        buttonAdd = (Button) findViewById(R.id.buttonAddHarvest);
        buttonDate = (Button) findViewById(R.id.buttonSelectDateHarvest);
        etAmount = (EditText) findViewById(R.id.add_harvest_amount);
        etWaterContent = (EditText) findViewById(R.id.add_harvest_water_content);
        etType = (EditText) findViewById(R.id.add_harvest_type);
        etText = (EditText) findViewById(R.id.add_harvest_text);
        textViewDate = (TextView) findViewById(R.id.add_harvest_text_view_date);

        addHoneyHarvestViewModel = new ViewModelProvider(this).get(AddHoneyHarvestViewModel.class);

        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "Date picker");
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!textViewDate.getText().equals("Datum") && !etAmount.getText().toString().equals("") && !etWaterContent.getText().toString().equals("")) {
                    String[] date = textViewDate.getText().toString().split("/");

                    HoneyHarvest harvest = new HoneyHarvest(idHive, Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]),
                            Double.parseDouble(etAmount.getText().toString()), Double.parseDouble(etWaterContent.getText().toString()), etType.getText().toString(),
                            etText.getText().toString());

                    addHoneyHarvestViewModel.insert(harvest);
                    AddHoneyHarvestActivity.this.finish();
                } else {
                    Toast.makeText(AddHoneyHarvestActivity.this, "Vyplňte datum, množství a obsah vody", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, dayOfMonth);
        textViewDate.setText(c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH)+1) + "/" + c.get(Calendar.DAY_OF_MONTH));
    }
}