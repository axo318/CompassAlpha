package com.example.compassalpha.compass;

import android.hardware.SensorManager;

public class CompassLogic {

    private float[] rMat = new float[9];                    // Rotation Matrix
    private float[] orientation = new float[9];             // Device Orientation Matrix
    private float[] mLastAccelerometer = new float[3];      // Last Accelerometer data
    private float[] mLastMagnetometer = new float[3];       // Last Magnetometer data
    private boolean mLastAccelerometerSet = false;          // Becomes True when first data arrives
    private boolean mLastMagnetometerSet = false;           // Becomes True when first data arrives

    // Compass Variable holding current degrees
    private int azimuth;


    // CONSTRUCTORS
    /**
     * Default constructor
     * Sets the initial compass azimuth to 0
     */
    public CompassLogic() {
        this(0);
    }

    /**
     * Constructor accepting a default azimuth value
     *
     * @param defaultAzimuth    int default azimuth heading
     */
    public CompassLogic(int defaultAzimuth){
        azimuth = defaultAzimuth;
    }


    // PUBLIC METHODS
    /**
     * Updates azimuth with new RotationVector sensor data
     *
     * @param values    values array from Event.values
     */
    public void updateRotationVectorData(float[] values){
        SensorManager.getRotationMatrixFromVector(rMat, values);
        float rad = SensorManager.getOrientation(rMat, orientation)[0];
        this.azimuth = (int) ((Math.toDegrees(rad) + 360) % 360);
    }

    /**
     * Updates last Accelerometer sensor data and attempts to update azimuth heading
     * if Magnetometer data are available
     *
     * @param values    values array from Event.values
     */
    public void updateAccelerometerData(float[] values){
        System.arraycopy(values, 0, mLastAccelerometer, 0, values.length);
        mLastAccelerometerSet = true;
        updateAzimuth();
    }

    /**
     * Updates last Magnetometer sensor data and attempts to update azimuth heading
     * if Accelerometer data are available
     *
     * @param values    values array from Event.values
     */
    public void updateMagnetometerData(float[] values){
        System.arraycopy(values, 0, mLastMagnetometer, 0, values.length);
        mLastMagnetometerSet = true;
        updateAzimuth();
    }

    /**
     * Returns the current compass azimuth heading
     *
     * @return      int azimuth
     */
    public int getAzimuth(){
        return this.azimuth;
    }


    // PRIVATE METHODS
    /**
     * Updates the current azimuth heading using Accelerometer and Magnetometer data
     */
    private void updateAzimuth() {
        if(mLastAccelerometerSet && mLastMagnetometerSet){
            SensorManager.getRotationMatrix(rMat, null, mLastAccelerometer, mLastMagnetometer);
            float rad = SensorManager.getOrientation(rMat, orientation)[0];
            this.azimuth = (int) ((Math.toDegrees(rad) + 360) % 360);
        }
    }

}
