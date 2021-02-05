package com.example.compassalpha;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.compassalpha.battery.BatteryInfo;
import com.example.compassalpha.nav.NavHandler;

public class BatteryActivity extends AppCompatActivity {
    private static final String tag = "BatteryActivity";

    private NavHandler navHandler;      // Sets up the navMenu
    private BatteryInfo batteryInfo;    // Retrieves battery information

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery);

        // Initialize the top navigation bar
        navHandler = new NavHandler(this);

        // Initialize batteryInfo passing it a TextView
        // This TextView will be populated with all battery information
        TextView textView = findViewById(R.id.batteryInformation);
        batteryInfo = new BatteryInfo(this, textView);
    }
}