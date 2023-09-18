package com.example.cpen321m1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;


public class DetailsActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        String city = getIntent().getStringExtra("city");
        String manufacturer = getIntent().getStringExtra("manufacturer");
        String model = getIntent().getStringExtra("model");

        // get the TextViews
        TextView cityTextView = findViewById(R.id.city_textView);
        TextView manufacturerTextView = findViewById(R.id.manufacturer_textView);
        TextView modelTextView = findViewById(R.id.model_textView);

        // modify the TextViews
        cityTextView.setText("Current City: " + city);
        manufacturerTextView.setText("Phone Manufacturer: " + manufacturer);
        modelTextView.setText("Phone Model: " + model);
    }
}