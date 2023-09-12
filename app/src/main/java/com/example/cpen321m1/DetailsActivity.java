package com.example.cpen321m1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class DetailsActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

//        getLocationInfo();

        String city = getIntent().getStringExtra("city");
        String manufacturer = getIntent().getStringExtra("manufacturer");
        String model = getIntent().getStringExtra("model");

        // Retrieve the TextViews from the layout
        TextView cityTextView = findViewById(R.id.city_textView);
        TextView manufacturerTextView = findViewById(R.id.manufacturer_textView);
        TextView modelTextView = findViewById(R.id.model_textView);

        // Update TextViews with the obtained information
        cityTextView.setText("Current City: " + city);
        manufacturerTextView.setText("Phone Manufacturer: " + manufacturer);
        modelTextView.setText("Phone Model: " + model);
    }

    // Get location information

}