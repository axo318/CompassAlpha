package com.example.compassalpha;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String tag = "MainActivity";

    private Compass compass;

    // UI Variables
    TextView azDirectionView;
    TextView azDegreesView;
    CompassView compassView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        azDirectionView = findViewById(R.id.azimuth_direction);
        azDegreesView = findViewById(R.id.azimuth_degrees);

        compassView = ((CompassView) findViewById(R.id.compass_view))
                            .setDirectionView(azDirectionView)
                            .setDegreesView(azDegreesView);

        compass = new Compass(this,
                compassView,
                new CompassLogic()
        );

        // Check that compass is supported by device
        if(! compass.isSupported()) {
            noSupportAlert();
            return;
        }

        compass.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        compass.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        compass.start();
    }

    private void noSupportAlert() {
        Toast.makeText(this, "Warning! No sensor data available", Toast.LENGTH_SHORT).show();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Warning! No sensor data available. This device does not support the compass")
                .setCancelable(false)
                .setNegativeButton("Close", (dialog, which) -> finish());
    }

}