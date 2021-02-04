package com.example.compassalpha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private static final String tag = "MainActivity";

    private NavHandler navHandler;          // Sets up the navMenu
    private CameraIntent cameraIntent;      // Handles the takePicture Intent
    private Compass compass;                // Handles compass logic and view

    // UI variables
    CompassView compassView;
    TextView azDirectionView;
    TextView azDegreesView;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Navigation
        DrawerLayout mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.nav_view);
        navHandler = new NavHandler(this, mDrawerLayout, navView);

        // Initialize Camera button
        cameraIntent = new CameraIntent();
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            String compassTag =
                    azDegreesView.getText().toString().replaceAll("\\D+","") +
                    azDirectionView.getText().toString();
            cameraIntent.goToCamera(this, compassTag);
        });

        // Initialize CompassView and compass objects
        azDirectionView = findViewById(R.id.azimuth_direction);
        azDegreesView = findViewById(R.id.azimuth_degrees);
        compassView = ((CompassView) findViewById(R.id.compass_view))
                            .setDirectionView(azDirectionView)
                            .setDegreesView(azDegreesView);
        compass = new Compass(this, compassView, new CompassLogic());

        // Check that compass is supported by device
        if(compass.isSupported())
            compass.start();
        else
            noSupportAlert();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        cameraIntent.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraIntent.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(navHandler.onOptionsItemSelected(item)) {return true;}
        return super.onOptionsItemSelected(item);
    }

    private void noSupportAlert() {
        Toast.makeText(this, "Warning! No sensor data available", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Warning! No sensor data available. This device does not support the compass")
                .setCancelable(false)
                .setNegativeButton("Close", (dialog, which) -> finish());
    }

}