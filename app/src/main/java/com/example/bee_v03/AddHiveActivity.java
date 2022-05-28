package com.example.bee_v03;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class AddHiveActivity extends AppCompatActivity {
    Spinner spinnerLocations;
    EditText editTextName, editTextRow;
    RatingBar rbAgresivnost, rbSilaVcelstva, rbStavebniPud, rbBodavost, rbSlidivost, rbCleaningInstinct;
    LinearLayout colourSquare;
    NumberPicker numberPicker;
    Button buttonAdd;
    AddHiveViewModel addHiveViewModel;
    HivesLocation selectedLocation;
    String parentActivity;
    int queenYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hive);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Nové včelstvo");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4F1308")));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            parentActivity = extras.getString("PARENT_ACTIVITY");
        }

        addHiveViewModel = new ViewModelProvider(this).get(AddHiveViewModel.class);

        spinnerLocations = (Spinner) findViewById(R.id.spinnerLocations);
        editTextName = (EditText) findViewById(R.id.add_hive_name);
        editTextRow = (EditText) findViewById(R.id.add_hive_row);
        rbAgresivnost = (RatingBar) findViewById(R.id.ratingBarAgresivnost);
        rbSilaVcelstva = (RatingBar) findViewById(R.id.ratingBarSilaVcelstva);
        rbStavebniPud = (RatingBar) findViewById(R.id.ratingBarStavebniPud);
        rbBodavost = (RatingBar) findViewById(R.id.ratingBodavost);
        rbSlidivost = (RatingBar) findViewById(R.id.ratingSlidivost);
        rbCleaningInstinct = (RatingBar) findViewById(R.id.ratingCisticiPud);
        numberPicker = (NumberPicker) findViewById(R.id.numberPickerMatka);
        colourSquare = (LinearLayout) findViewById(R.id.colourSquare);
        buttonAdd = (Button) findViewById(R.id.buttonAddHive);
        queenYear = Calendar.getInstance().get(Calendar.YEAR);

        makeRatingBarsBehave();

        numberPicker.setMinValue(2010);
        numberPicker.setMaxValue(2050);
        numberPicker.setValue(queenYear);

        setColourSquare(queenYear);

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                setColourSquare(newVal);
            }
        });

        addHiveViewModel.getAllLocations().observe(this, new Observer<List<HivesLocation>>() {
            @Override
            public void onChanged(List<HivesLocation> hivesLocations) {
                ArrayAdapter<HivesLocation> adapter = new ArrayAdapter<HivesLocation>(AddHiveActivity.this, android.R.layout.simple_spinner_item, hivesLocations);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinnerLocations.setAdapter(adapter);
            }
        });

        spinnerLocations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLocation = (HivesLocation) spinnerLocations.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Toast.makeText(this, parentActivity, Toast.LENGTH_SHORT).show();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextName.equals("") && !editTextRow.equals("") && (rbAgresivnost.getRating() != 0) &&
                        (rbCleaningInstinct.getRating() != 0) && (rbSilaVcelstva.getRating() != 0) && (rbStavebniPud.getRating() != 0) &&
                        (rbBodavost.getRating() != 0) && (rbSlidivost.getRating() != 0)) {
                    Hive hive = new Hive(selectedLocation.getId_location(), editTextName.getText().toString(), Integer.parseInt(editTextRow.getText().toString()),
                            (int)rbAgresivnost.getRating(), (int)rbSilaVcelstva.getRating(), (int)rbStavebniPud.getRating(), (int)rbBodavost.getRating(),
                            (int)rbSlidivost.getRating(), (int)rbCleaningInstinct.getRating(), queenYear, false);
                    addHiveViewModel.insert(hive);
                    Toast.makeText(AddHiveActivity.this, "Včelstvo přidáno!", Toast.LENGTH_SHORT).show();

                    //WHY did I make this?
                    switch (parentActivity) {
                        case "select":
                            AddHiveActivity.this.finish();
                            break;
                        case "dashboard" :
                            AddHiveActivity.this.finish();
                            break;
                    }
                } else Toast.makeText(AddHiveActivity.this, "Některá pole výše nevbyla vyplněna. Prosím vyplňte, než budete pokračovat.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void makeRatingBarsBehave() {
        setRatingBarToRegisterProperly(rbAgresivnost);
        setRatingBarToRegisterProperly(rbBodavost);
        setRatingBarToRegisterProperly(rbSlidivost);
        setRatingBarToRegisterProperly(rbSilaVcelstva);
        setRatingBarToRegisterProperly(rbStavebniPud);
        setRatingBarToRegisterProperly(rbCleaningInstinct);
    }

    private void setRatingBarToRegisterProperly(RatingBar rb) {
        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) ratingBar.setRating((float) Math.ceil(rating));
            }
        });
    }

    private void setColourSquare(int year) {
        switch (year % 5) {
            case 0:
                colourSquare.setBackgroundColor(Color.parseColor("#0000ff"));
                break;
            case 1:
                colourSquare.setBackgroundResource(R.drawable.border_white);
                break;
            case 2:
                colourSquare.setBackgroundColor(Color.parseColor("#ffff00"));
                break;
            case 3:
                colourSquare.setBackgroundColor(Color.parseColor("#ff0000"));
                break;
            case 4:
                colourSquare.setBackgroundColor(Color.parseColor("#006400"));
                break;
        }
    }
}