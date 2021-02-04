package com.example.compassalpha;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BatteryInfo {

    private Context context;
    private IntentFilter iFilter;

    TextView batteryInfo;

    // Battery variables
    private int batteryL;           // Battery Level
    private int batteryV;           // Battery Voltage
    private double batteryT;        // Battery Temperature
    private String batteryTech;     // Battery Technology
    private String batteryStatus;   // Battery Status
    private String batteryHealth;   // Battery Health
    private String batteryPlugged;  // Battery Plugged in

    // Create custom Receiver for saving battery information
    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if(Intent.ACTION_BATTERY_CHANGED.equals(action)){
                batteryL = intent.getIntExtra("level", 0);
                batteryV = intent.getIntExtra("voltage", 0);
                batteryT = intent.getIntExtra("temperature", 0);
                batteryTech = intent.getStringExtra("technology");

                // Set Battery Status
                switch(intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN)){
                    case BatteryManager.BATTERY_STATUS_CHARGING:
                        batteryStatus = "Charging";
                        break;
                    case BatteryManager.BATTERY_STATUS_DISCHARGING:
                        batteryStatus = "Discharging";
                        break;
                    case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                        batteryStatus = "Not Charging";
                        break;
                    case BatteryManager.BATTERY_STATUS_FULL:
                        batteryStatus = "Battery Full";
                        break;
                    case BatteryManager.BATTERY_STATUS_UNKNOWN:
                        batteryStatus = "Unknown Status";
                        break;
                }

                // Set Battery Health
                switch(intent.getIntExtra("health", BatteryManager.BATTERY_HEALTH_UNKNOWN)){
                    case BatteryManager.BATTERY_HEALTH_GOOD:
                        batteryHealth = "Good Status";
                        break;
                    case BatteryManager.BATTERY_HEALTH_DEAD:
                        batteryHealth = "Dead Status";
                        break;
                    case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                        batteryHealth = "Over Voltage";
                        break;
                    case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                        batteryHealth = "Unknown Status";
                        break;
                    case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                        batteryHealth = "Overheat";
                        break;
                }

                // Set Battery Health
                switch(intent.getIntExtra("plugged", 0)){
                    case BatteryManager.BATTERY_PLUGGED_AC:
                        batteryPlugged = "Plugged to AC";
                        break;
                    case BatteryManager.BATTERY_PLUGGED_USB:
                        batteryPlugged = "Plugged to USB";
                        break;
                    case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                        batteryPlugged = "Plugged to Wireless";
                        break;
                    default:
                        batteryPlugged = "------";
                }

                // Set Battery information on screen
                String batInfoString =  "Battery Level: " + batteryL + "%" + "\n\n"  +
                        "Battery Status: " + batteryStatus + "\n\n"  +
                        "Battery Plugged: " + batteryPlugged + "\n\n"  +
                        "Battery Health: " + batteryHealth + "\n\n"  +
                        "Battery Voltage: " + (batteryV/1000) + "V\n\n"  +
                        "Battery Temperature: " + (batteryT*0.1) + "C\n\n"  +
                        "Battery Technology: " + batteryTech + "\n\n";
                batteryInfo.setText(batInfoString);

            }
        }
    };

    public BatteryInfo(Context context, TextView batteryInfo){
        this.context = context;
        this.batteryInfo = batteryInfo;

        iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        context.registerReceiver(mBatInfoReceiver, iFilter);
    }

}
