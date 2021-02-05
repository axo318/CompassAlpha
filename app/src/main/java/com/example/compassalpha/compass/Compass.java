package com.example.compassalpha.compass;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Compass implements SensorEventListener {
    private final String tag = "Compass";

    private final int SENSOR_DELAY = SensorManager.SENSOR_DELAY_UI;     // Delay used for sensors
    private final Boolean compassSupported;
    private Boolean withAnimation = true;

    // Compass variables
    private CompassView compassView;
    private CompassLogic compassLogic;

    // Sensor Variables
    private SensorManager mSensorManager;
    private Sensor mRotationV, mAccelerometer, mMagnetometer;


    // CONSTRUCTOR
    /**
     * Public constructor
     * Initializes sensor services using the app context and checks sensor requirements
     * User is responsible for checking whether the phone supports required sensors
     * after initialization
     *
     * @param context           App Context object
     * @param compassView       Initialized CompassView object
     * @param compassLogic      Initialized CompassLogic object
     */
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
    /**
     * Callback method which handles sensor events. Everytime sensor readings
     * are updated, this method is called. It updates compass position using
     * CompassLogic object, and sends the updated information to CompassView
     *
     * @param event     SensorEvent containing values
     */
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
        compassView.updateView(azimuth, withAnimation);
    }

    /**
     * SensorEventListener interface method. Does nothing.
     *
     * @param sensor
     * @param accuracy
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    /**
     * Registers sensor listeners depending on what sensors are available.
     * Should be added as a callback in Activity.onStart()
     */
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

    /**
     * Unregisters sensor listeners.
     * Should be added as a callback in Activity.onStop()
     */
    public void stop(){ mSensorManager.unregisterListener(this); }

    /**
     * If device does not support the sensors required for this compass to function
     * this returns false
     *
     * @return      Boolean isSupported
     */
    public Boolean isSupported(){ return compassSupported; }

    /**
     * Turns on/off the compass animation
     *
     * @param withAnimation     Boolean that sets animation on/off
     */
    public void setAnimation(boolean withAnimation){ this.withAnimation = withAnimation; }


    // PRIVATE METHODS
    /**
     * Checks sensor requirements
     *
     * @return      Boolean device support
     */
    private Boolean checkRequirements() {
        if(mRotationV == null)
            return mAccelerometer != null && mMagnetometer != null;
        else
            return true;
    }

}
