package com.roki.learnsqlitemakers01;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

public class CreateActivity extends AppCompatActivity {

    private MapView mapView;
    private MapboxMap mapboxMap;
    private Marker marker;
    private EditText latLngEditText;
    private LatLng latLng;
    private EditText SchoolNameEditText;
    private EditText AddressEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_create);
        latLngEditText = findViewById(R.id.latLngET);
        SchoolNameEditText = findViewById(R.id.schoolET);
        AddressEditText = findViewById(R.id.addressET);
        mapView = findViewById(R.id.mapViewCreate);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                    }
                });
                mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                    @Override
                    public boolean onMapClick(@NonNull LatLng point) {
                        if (marker != null) {
                            marker.remove();
                        }
                        marker = mapboxMap.addMarker(new MarkerOptions().position(point));
                        latLng = point;
                        latLngEditText.setText(point.getLatitude() + "," + point.getLongitude());
                        return true;
                    }
                });
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    public void createData(View view) {
        String name = SchoolNameEditText.getText().toString().trim();
        String address = AddressEditText.getText().toString().trim();
        String latLng = latLngEditText.getText().toString().trim();

        if (name.isEmpty() || address.isEmpty() || latLng.isEmpty()) {
            Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] latLngSplit = latLng.split(",");
        if (latLngSplit.length != 2) {
            Toast.makeText(this, "Invalid latitude and longitude", Toast.LENGTH_SHORT).show();
            return;
        }

        double latitude, longitude;
        try {
            latitude = Double.parseDouble(latLngSplit[0].trim());
            longitude = Double.parseDouble(latLngSplit[1].trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid latitude and longitude values", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("CreateActivity", "Name: " + name + ", Address: " + address + ", Latitude: " + latitude + ", Longitude: " + longitude);

        DatabaseHelper dataBaseHelper = new DatabaseHelper(this);
        boolean isInserted = dataBaseHelper.createMarker(name, address, latitude, longitude);

        if (isInserted) {
            Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Failed to save data", Toast.LENGTH_SHORT).show();
        }
    }
}
