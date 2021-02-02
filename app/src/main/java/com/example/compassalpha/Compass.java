package com.example.compassalpha;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Compass implements SensorEventListener {
    private final String tag = "Compass";
    private final Boolean compassSupported;

    private CompassView compassView;
    private CompassLogic compassLogic;

    // Sensor Variables
    private SensorManager mSensorManager;
    private Sensor mRotationV, mAccelerometer, mMagnetometer;

    private final int SENSOR_DELAY = SensorManager.SENSOR_DELAY_FASTEST;


    // CONSTRUCTOR
    public Compass(Context context, CompassView compassView, CompassLogic compassLogic){
        this.compassView = compassView;
        this.compassLogic = compassLogic;

        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mRotationV = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        compassSupported = checkRequirements();
    }


    // PUBLIC METHODS
    @Override
    public void onSensorChanged(SensorEvent event) {
        // Update sensor data in compassLogic
        switch (event.sensor.getType()){
            case Sensor.TYPE_ROTATION_VECTOR:
                compassLogic.updateRotationVectorData(event.values);
                break;
            case Sensor.TYPE_ACCELEROMETER:
                compassLogic.updateAccelerometerData(event.values);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                compassLogic.updateMagnetometerData(event.values);
                break;
        }

        // Update the view of the compass
        int azimuth = compassLogic.getAzimuth();
        compassView.updateView(azimuth);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void start(){
        if(mRotationV == null){
            if(mAccelerometer != null && mMagnetometer != null){
                mSensorManager.registerListener(this, mAccelerometer, SENSOR_DELAY);
                mSensorManager.registerListener(this, mMagnetometer, SENSOR_DELAY);
            }
        }
        else{
            mSensorManager.registerListener(this, mRotationV, SENSOR_DELAY);
        }
    }

    public void stop(){
        mSensorManager.unregisterListener(this);
    }

    public Boolean isSupported(){
        return compassSupported;
    }


    // PRIVATE METHODS
    private Boolean checkRequirements() {
        if(mRotationV == null){
            return mAccelerometer != null && mMagnetometer != null;
        }
        return true;

    }

}
