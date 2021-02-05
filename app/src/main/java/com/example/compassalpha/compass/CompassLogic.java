package com.example.compassalpha.compass;

import android.hardware.SensorManager;

public class CompassLogic {

    private float[] rMat = new float[9];
    private float[] orientation = new float[9];
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;

    // Compass Variable
    private int azimuth;


    // CONSTRUCTORS
    public CompassLogic() {
        this(0);
    }
    public CompassLogic(int defaultAzimuth){
        azimuth = defaultAzimuth;
    }


    // PUBLIC METHODS
    public void updateRotationVectorData(float[] values){
        SensorManager.getRotationMatrixFromVector(rMat, values);
        float rad = SensorManager.getOrientation(rMat, orientation)[0];
        this.azimuth = (int) ((Math.toDegrees(rad) + 360) % 360);
    }

    public void updateAccelerometerData(float[] values){
        System.arraycopy(values, 0, mLastAccelerometer, 0, values.length);
        mLastAccelerometerSet = true;
        updateAzimuth();
    }

    public void updateMagnetometerData(float[] values){
        System.arraycopy(values, 0, mLastMagnetometer, 0, values.length);
        mLastMagnetometerSet = true;
        updateAzimuth();
    }

    public int getAzimuth(){
        return this.azimuth;
    }


    // PRIVATE METHODS
    private void updateAzimuth() {
        if(mLastAccelerometerSet && mLastMagnetometerSet){
            SensorManager.getRotationMatrix(rMat, null, mLastAccelerometer, mLastMagnetometer);
            float rad = SensorManager.getOrientation(rMat, orientation)[0];
            this.azimuth = (int) ((Math.toDegrees(rad) + 360) % 360);
        }
    }

}
