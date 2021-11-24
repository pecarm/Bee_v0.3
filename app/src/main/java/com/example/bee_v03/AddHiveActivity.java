package com.example.bee_v03;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class AddHiveActivity extends AppCompatActivity {
    Spinner spinnerLocations;
    EditText editTextName, editTextRow;
    RatingBar rbAgresivnost, rbStavZasob, rbMezolitostPlodu, rbSilaVcelstva, rbStavebniPud, rbBodavost, rbSlidivost;
    Button buttonAdd;
    AddHiveViewModel addHiveViewModel;
    HivesLocation selectedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hive);

        addHiveViewModel = new ViewModelProvider(this).get(AddHiveViewModel.class);

        spinnerLocations = (Spinner) findViewById(R.id.spinnerLocations);
        editTextName = (EditText) findViewById(R.id.add_hive_name);
        editTextRow = (EditText) findViewById(R.id.add_hive_row);
        rbAgresivnost = (RatingBar) findViewById(R.id.ratingBarAgresivnost);
        rbStavZasob = (RatingBar) findViewById(R.id.ratingBarStavZasob);
        rbMezolitostPlodu = (RatingBar) findViewById(R.id.ratingBarMezolitostPlodu);
        rbSilaVcelstva = (RatingBar) findViewById(R.id.ratingBarSilaVcelstva);
        rbStavebniPud = (RatingBar) findViewById(R.id.ratingBarStavebniPud);
        rbBodavost = (RatingBar) findViewById(R.id.ratingBodavost);
        rbSlidivost = (RatingBar) findViewById(R.id.ratingSlidivost);
        buttonAdd = (Button) findViewById(R.id.buttonAddHive);

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

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextName.equals("") || !editTextRow.equals("") || (rbAgresivnost.getRating() != 0) ||
                        (rbStavZasob.getRating() != 0) || (rbMezolitostPlodu.getRating() != 0) || (rbSilaVcelstva.getRating() != 0) || (rbStavebniPud.getRating() != 0) ||
                        (rbBodavost.getRating() != 0) || (rbSlidivost.getRating() != 0)) {
                    Hive hive = new Hive(selectedLocation.getId_location(), editTextName.getText().toString(), Integer.parseInt(editTextRow.getText().toString()),
                            (int)rbAgresivnost.getRating(), (int)rbStavZasob.getRating(), (int)rbMezolitostPlodu.getRating(),
                            (int)rbSilaVcelstva.getRating(), (int)rbStavebniPud.getRating(), (int)rbBodavost.getRating(), (int)rbSlidivost.getRating());
                    addHiveViewModel.insert(hive);
                } else Toast.makeText(AddHiveActivity.this, "Some of items above has not been declared. Please declare before proceeding.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}