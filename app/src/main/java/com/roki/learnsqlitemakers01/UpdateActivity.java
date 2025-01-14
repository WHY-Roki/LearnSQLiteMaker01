package com.roki.learnsqlitemakers01;

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

public class UpdateActivity extends AppCompatActivity {

    private MapView mapView;
    private MapboxMap mapboxMap;
    private Marker marker;
    private EditText latLngEditText;
    private LatLng latLng;
    private EditText schoolNameEditText;
    private EditText addressEditText;
    private String originalName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("UpdateActivity", "onCreate started");
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_update);

        latLngEditText = findViewById(R.id.coordinateETUpdate);
        schoolNameEditText = findViewById(R.id.schoolNameETUpdate);
        addressEditText = findViewById(R.id.addressETUpdate);
        mapView = findViewById(R.id.mapViewUpdate);
        mapView.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("NAME")) {
            originalName = intent.getStringExtra("NAME");
            String address = intent.getStringExtra("ADDRESS");
            double latitude = intent.getDoubleExtra("LATITUDE", 0);
            double longitude = intent.getDoubleExtra("LONGITUDE", 0);

            schoolNameEditText.setText(originalName);
            addressEditText.setText(address);
            latLngEditText.setText(latitude + "," + longitude);
            Log.d("UpdateActivity", "Intent data loaded");
        }

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                UpdateActivity.this.mapboxMap = mapboxMap;
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        Log.d("UpdateActivity", "Mapbox style loaded");
                        latLng = new LatLng(
                                Double.parseDouble(latLngEditText.getText().toString().split(",")[0]),
                                Double.parseDouble(latLngEditText.getText().toString().split(",")[1])
                        );
                        marker = mapboxMap.addMarker(new MarkerOptions().position(latLng));
                        Log.d("UpdateActivity", "Marker loaded");
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
                        Log.d("UpdateActivity", "Marker updated to: " + point);
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

    public void updateData(View view) {
        String name = schoolNameEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String latLngStr = latLngEditText.getText().toString().trim();

        if (name.isEmpty() || address.isEmpty() || latLngStr.isEmpty()) {
            Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] latLngSplit = latLngStr.split(",");
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

        Log.d("UpdateActivity", "Updating marker with data: " +
                "Name: " + name +
                "Address: " + address +
                "Latitude: " + latitude +
                "Longitude: " + longitude);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        boolean isUpdated = databaseHelper.updateMarker(originalName, name, address, latitude, longitude);

        if (isUpdated) {
            Toast.makeText(this, "Data updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to update data", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void deleteData(View view) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        boolean isDeleted = databaseHelper.deleteMarkerByName(originalName);

        if (isDeleted) {
            Toast.makeText(this, "Data deleted successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to delete data", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
