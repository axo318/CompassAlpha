package com.example.compassalpha;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class BatteryActivity extends AppCompatActivity {
    private static final String tag = "BatteryActivity";

    private NavHandler navHandler;      // Sets up the navMenu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery);
        navHandler = new NavHandler(this);
    }
}