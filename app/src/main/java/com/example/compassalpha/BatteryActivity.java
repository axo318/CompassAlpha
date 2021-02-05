package com.example.compassalpha;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.compassalpha.battery.BatteryInfo;
import com.example.compassalpha.nav.NavHandler;

public class BatteryActivity extends AppCompatActivity {
    private static final String tag = "BatteryActivity";

    private NavHandler navHandler;      // Sets up the navMenu
    private BatteryInfo batteryInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery);
        navHandler = new NavHandler(this);

        TextView textView = findViewById(R.id.batteryInformation);
        batteryInfo = new BatteryInfo(this, textView);
    }
}