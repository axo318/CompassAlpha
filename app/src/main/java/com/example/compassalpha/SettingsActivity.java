package com.example.compassalpha;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Switch;

import com.example.compassalpha.nav.NavHandler;
import com.example.compassalpha.settings.Settings;

public class SettingsActivity extends AppCompatActivity {
    private static final String tag = "SettingsActivity";

    private NavHandler navHandler;      // Sets up the navMenu
    private Settings settings;          // Handles app settings

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize the top navigation bar
        navHandler = new NavHandler(this);

        // Initialize settings object passing it the CompassAnimation
        // switch
        Switch animationSwitch = findViewById(R.id.animation_on);
        settings = new Settings(this, animationSwitch);
    }
}