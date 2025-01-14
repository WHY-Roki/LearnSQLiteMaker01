package com.roki.learnsqlitemakers01;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

public class MainActivity extends AppCompatActivity {

    private MapView mapView;
    private MapboxMap mapboxMap;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_main);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(this);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                MainActivity.this.mapboxMap = mapboxMap;
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        addMarkersFromDatabase();
                    }
                });
            }
        });
    }

    private void addMarkersFromDatabase() {
        Cursor cursor = databaseHelper.readAllMarkers();
        if (cursor.moveToFirst()) {
            do {
                int nameIndex = cursor.getColumnIndex("SCHOOL_NAME");
                int addressIndex = cursor.getColumnIndex("ADDRESS");
                int latitudeIndex = cursor.getColumnIndex("LATITUDE");
                int longitudeIndex = cursor.getColumnIndex("LONGITUDE");

                if (nameIndex != -1 && addressIndex != -1 && latitudeIndex != -1 && longitudeIndex != -1) {
                    String name = cursor.getString(nameIndex);
                    String address = cursor.getString(addressIndex);
                    double latitude = cursor.getDouble(latitudeIndex);
                    double longitude = cursor.getDouble(longitudeIndex);

                    Log.d("MainActivity", "Adding marker: " + name + ", " + latitude + ", " + longitude);

                    mapboxMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .title(name)
                            .snippet(address));
                } else {
                    Log.e("MainActivity", "One or more column indices not found");
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.createMenu) {
            startActivity(new Intent(this, CreateActivity.class));
            return true;
        } else if (id == R.id.markersMenu) {
            startActivity(new Intent(this, MarkerActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
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
}
/////   INI TAHAP BAGUS