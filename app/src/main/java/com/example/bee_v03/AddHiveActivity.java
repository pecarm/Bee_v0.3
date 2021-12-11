package com.example.bee_v03;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
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
    RatingBar rbAgresivnost, rbStavZasob, rbMezerovitostPlodu, rbSilaVcelstva, rbStavebniPud, rbBodavost, rbSlidivost;
    Button buttonAdd;
    AddHiveViewModel addHiveViewModel;
    HivesLocation selectedLocation;
    String parentActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hive);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            parentActivity = extras.getString("PARENT_ACTIVITY");
        }

        addHiveViewModel = new ViewModelProvider(this).get(AddHiveViewModel.class);

        LiveData<List<HivesLocation>> a = addHiveViewModel.getAllLocations();
        List<HivesLocation> b = a.getValue();

        //CANNOT BE HERE, query doesnt process before it gets opened
        //to simplify running this activity


        spinnerLocations = (Spinner) findViewById(R.id.spinnerLocations);
        editTextName = (EditText) findViewById(R.id.add_hive_name);
        editTextRow = (EditText) findViewById(R.id.add_hive_row);
        rbAgresivnost = (RatingBar) findViewById(R.id.ratingBarAgresivnost);
        rbStavZasob = (RatingBar) findViewById(R.id.ratingBarStavZasob);
        rbMezerovitostPlodu = (RatingBar) findViewById(R.id.ratingBarMezerovitostPlodu);
        rbSilaVcelstva = (RatingBar) findViewById(R.id.ratingBarSilaVcelstva);
        rbStavebniPud = (RatingBar) findViewById(R.id.ratingBarStavebniPud);
        rbBodavost = (RatingBar) findViewById(R.id.ratingBodavost);
        rbSlidivost = (RatingBar) findViewById(R.id.ratingSlidivost);
        buttonAdd = (Button) findViewById(R.id.buttonAddHive);

        makeRatingBarsBehave();

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

        //toast "select"
        Toast.makeText(this, parentActivity, Toast.LENGTH_SHORT).show();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextName.equals("") && !editTextRow.equals("") && (rbAgresivnost.getRating() != 0) &&
                        (rbStavZasob.getRating() != 0) && (rbMezerovitostPlodu.getRating() != 0) && (rbSilaVcelstva.getRating() != 0) && (rbStavebniPud.getRating() != 0) &&
                        (rbBodavost.getRating() != 0) && (rbSlidivost.getRating() != 0)) {
                    Hive hive = new Hive(selectedLocation.getId_location(), editTextName.getText().toString(), Integer.parseInt(editTextRow.getText().toString()),
                            (int)rbAgresivnost.getRating(), (int)rbStavZasob.getRating(), (int)rbMezerovitostPlodu.getRating(),
                            (int)rbSilaVcelstva.getRating(), (int)rbStavebniPud.getRating(), (int)rbBodavost.getRating(), (int)rbSlidivost.getRating());
                    addHiveViewModel.insert(hive);
                    Toast.makeText(AddHiveActivity.this, "Hive added!", Toast.LENGTH_SHORT).show();

                    //WHY did I make this?
                    switch (parentActivity) {
                        case "select":
                            AddHiveActivity.this.finish();
                            break;
                        case "dashboard" :
                            AddHiveActivity.this.finish();
                            break;
                    }
                } else Toast.makeText(AddHiveActivity.this, "Some of items above has not been declared. Please declare before proceeding.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void makeRatingBarsBehave() {
        setRatingBarToRegisterProperly(rbAgresivnost);
        setRatingBarToRegisterProperly(rbBodavost);
        setRatingBarToRegisterProperly(rbMezerovitostPlodu);
        setRatingBarToRegisterProperly(rbSlidivost);
        setRatingBarToRegisterProperly(rbSilaVcelstva);
        setRatingBarToRegisterProperly(rbStavebniPud);
        setRatingBarToRegisterProperly(rbStavZasob);
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