package com.example.bee_v03;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.EntityDeletionOrUpdateAdapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class AddLocationActivity extends AppCompatActivity {
    EditText editTextLatitude, editTextLongitude;
    Button buttonAddLocation,buttonSelectLocation, buttonShowMap;
    EditText editTextLocationName;
    AddLocationViewModel addLocationViewModel;

    LocationRequest locationRequest;

    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Nová lokalita");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4F1308")));

        addLocationViewModel = new ViewModelProvider(this).get(AddLocationViewModel.class);

        editTextLocationName = (EditText) findViewById(R.id.add_location_location_name);
        editTextLatitude = (EditText) findViewById(R.id.add_location_latitude);
        editTextLongitude = (EditText) findViewById(R.id.add_location_longitude);
        buttonAddLocation = (Button) findViewById(R.id.add_location_button_add);
        buttonSelectLocation = (Button) findViewById(R.id.add_location_button_select);
        buttonShowMap = (Button) findViewById(R.id.add_location_button_map);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        //Lol, labely jsou v současné době bugnuté, našel jsem neopravený bug od googlu :D
        buttonShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextLongitude.getText().toString().equals("") && !editTextLatitude.getText().toString().equals("")) {
                    String uri = "geo:0,0?q=" + editTextLatitude.getText().toString() + "," + editTextLongitude.getText().toString()+ "(" + editTextLocationName.getText().toString() + ")";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);
                } else if (editTextLongitude.getText().toString().equals("") && editTextLatitude.getText().toString().equals("")) {
                    String uri = "geo:0,0?q=49.227109523641396,16.57445768637967(FEKT)";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);
                    // :)
                }
            }
        });

        buttonSelectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddLocationActivity.this, "Počkejte prosím...", Toast.LENGTH_SHORT).show();
                getLocation();
            }
        });

        buttonAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextLongitude.getText().toString().equals("") && !editTextLatitude.getText().toString().equals("") && !editTextLocationName.getText().toString().equals("")) {
                    addLocation(editTextLocationName.getText().toString(), editTextLatitude.getText().toString(), editTextLongitude.getText().toString());
                    Toast.makeText(AddLocationActivity.this, "Lokalita včelnice přidána!", Toast.LENGTH_SHORT).show();
                    AddLocationActivity.this.finish();
                }
                else {
                    Toast.makeText(AddLocationActivity.this, "Zadejte jméno a souřadnice!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(AddLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (!isGpsOn()) {
                    Toast.makeText(this, "Zapněte určování polohy pomocí GPS", Toast.LENGTH_SHORT).show();
                    return;
                }

                LocationServices.getFusedLocationProviderClient(AddLocationActivity.this)
                        .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        super.onLocationResult(locationResult);

                        LocationServices.getFusedLocationProviderClient(AddLocationActivity.this).removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            latitude = locationResult.getLastLocation().getLatitude();
                            longitude = locationResult.getLastLocation().getLongitude();

                            editTextLatitude.setText(Double.toString(latitude));
                            editTextLongitude.setText(Double.toString(longitude));
                            Toast.makeText(AddLocationActivity.this, "Poloha úspěšně určena!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, Looper.getMainLooper());

            } else {
                ActivityCompat.requestPermissions(AddLocationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            }
        }
    }

    private boolean isGpsOn() {
        LocationManager lm = null;
        boolean isOn = false;

        if (lm == null) {
            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

        isOn = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isOn;
    }

    private void addLocation(String name, String latitude, String longitude) {
        addLocationViewModel.insert(new HivesLocation(name, latitude, longitude));
    }
}