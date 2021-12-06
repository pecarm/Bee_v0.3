package com.example.bee_v03;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddLocationActivity extends AppCompatActivity {
    TextView textViewLatitude, textViewLongitude;
    Button buttonAddLocation;
    EditText editTextLocationName;
    AddLocationViewModel addLocationViewModel;

    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        addLocationViewModel = new ViewModelProvider(this).get(AddLocationViewModel.class);

        editTextLocationName = (EditText) findViewById(R.id.add_location_location_name);
        textViewLatitude = (TextView) findViewById(R.id.add_location_latitude);
        textViewLongitude = (TextView) findViewById(R.id.add_location_longitude);
        buttonAddLocation = (Button) findViewById(R.id.add_location_button_add);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //works, just need to get location first (e.g. open gmaps)
        if (ActivityCompat.checkSelfPermission(AddLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        try {
                            textViewLatitude.setText(Double.toString(location.getLatitude()));
                            textViewLongitude.setText(Double.toString(location.getLongitude()));
                        } catch (Exception e) {
                            Toast.makeText(AddLocationActivity.this, "No location acquired", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(AddLocationActivity.this, "No location acquired", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            ActivityCompat.requestPermissions(AddLocationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        buttonAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!textViewLongitude.getText().toString().equals("Longitude") && !editTextLocationName.getText().toString().equals("")) {
                    addLocation(editTextLocationName.getText().toString(), textViewLatitude.getText().toString(), textViewLongitude.getText().toString());
                    Toast.makeText(AddLocationActivity.this, "Location added!", Toast.LENGTH_SHORT).show();
                    AddLocationActivity.this.finish();
                }
                else {
                    Toast.makeText(AddLocationActivity.this, "Name or location not provided!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addLocation(String name, String latitude, String longitude) {
        addLocationViewModel.insert(new HivesLocation(name, latitude, longitude));
    }
}