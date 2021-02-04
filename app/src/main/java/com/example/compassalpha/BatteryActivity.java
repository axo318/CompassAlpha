package com.example.compassalpha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

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