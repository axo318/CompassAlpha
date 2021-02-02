package com.example.compassalpha;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final String tag = "MainActivity";

    // Sensor Variables
    private SensorManager mSensorManager;
    private Sensor mRotationV, mAccelerometer, mMagnetometer;

    float[] rMat = new float[9];
    float[] orientation = new float[9];
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean haveSensorRotation = false;
    private boolean haveSensorAccelerometer = false;
    private boolean haveSensorMagnetometer = false;
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;

    // Compass Variable
    private int azimuth;

    // UI Variables
    TextView azDirectionView;
    TextView azDegreesView;
    ImageView compassView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

        azDirectionView = findViewById(R.id.azimuth_direction);
        azDegreesView = findViewById(R.id.azimuth_degrees);
        compassView = findViewById(R.id.compass_view);

        init();
    }

    private void init() {
        // Collect sensor objects
        mRotationV = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // Check sensor access
        if(mRotationV == null){
            if(mAccelerometer == null || mMagnetometer == null){
                noSensorAlert();
            }
            else {
                haveSensorAccelerometer = mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
                haveSensorMagnetometer = mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_UI);
            }
        }
        else{
            haveSensorRotation = mSensorManager.registerListener(this, mRotationV, SensorManager.SENSOR_DELAY_UI);
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        // If we have rotation vector sensor available, we use its event to calculate azimuth
        if(event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR){
            SensorManager.getRotationMatrixFromVector(rMat, event.values);
            float rad = SensorManager.getOrientation(rMat, orientation)[0];
            azimuth = (int) ((Math.toDegrees(rad) + 360) % 360);
        }

        // If we use accelerometer/magnetometer, we save their data asynchronously to private arrays
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }

        // If both arrays are set, we can calculate azimuth
        if(mLastAccelerometerSet && mLastMagnetometerSet){
            SensorManager.getRotationMatrix(rMat, null, mLastAccelerometer, mLastMagnetometer);
            float rad = SensorManager.getOrientation(rMat, orientation)[0];
            azimuth = (int) ((Math.toDegrees(rad) + 360) % 360);
        }

        compassView.setRotation(-azimuth);
        azDegreesView.setText("Azimuth: "+azimuth);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopSensors();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    public void stopSensors(){
        mSensorManager.unregisterListener(this);
    }

    private void noSensorAlert() {
        Toast.makeText(this, "Warning! No sensor data available", Toast.LENGTH_SHORT).show();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Warning! No sensor data available. This device does not support the compass")
                .setCancelable(false)
                .setNegativeButton("Close", (dialog, which) -> finish());
    }

}