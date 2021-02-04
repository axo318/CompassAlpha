package com.example.compassalpha;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {
    private static final String tag = "SettingsActivity";

    private NavHandler navHandler;      // Sets up the navMenu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        navHandler = new NavHandler(this);
    }
}