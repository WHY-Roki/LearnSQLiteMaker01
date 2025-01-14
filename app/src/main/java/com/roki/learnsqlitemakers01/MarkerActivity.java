package com.roki.learnsqlitemakers01;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MarkerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MarkerAdapter markerAdapter;
    private List<MarkerModel> markerList;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker);

        recyclerView = findViewById(R.id.recyclerViewMarkers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseHelper = new DatabaseHelper(this);
        markerList = new ArrayList<>();

        loadMarkersFromDatabase();

        markerAdapter = new MarkerAdapter(this, markerList, new MarkerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MarkerModel marker) {
                Intent intent = new Intent(MarkerActivity.this, UpdateActivity.class);
                intent.putExtra("NAME", marker.getName());
                intent.putExtra("ADDRESS", marker.getAddress());
                intent.putExtra("LATITUDE", marker.getLatitude());
                intent.putExtra("LONGITUDE", marker.getLongitude());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(MarkerModel marker) {
                boolean isDeleted = databaseHelper.deleteMarkerByName(marker.getName());
                if (isDeleted) {
                    markerList.remove(marker);
                    markerAdapter.notifyDataSetChanged();
                    Toast.makeText(MarkerActivity.this, "Marker deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MarkerActivity.this, "Failed to delete marker", Toast.LENGTH_SHORT).show();
                }
            }
        });

        recyclerView.setAdapter(markerAdapter);
    }

    private void loadMarkersFromDatabase() {
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

                    markerList.add(new MarkerModel(name, address, latitude, longitude));
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
