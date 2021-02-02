package com.example.compassalpha;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

public class CompassView extends androidx.appcompat.widget.AppCompatImageView {

    // Default Constants (These never change)
    static final public float TIME_DELTA_THRESHOLD = 0.25f;     // maximum time difference between iterations, s
    static final public float ANGLE_DELTA_THRESHOLD = 0.1f;     // minimum rotation change to be redrawn, deg
    static final public float INERTIA_MOMENT_DEFAULT = 0.1f;    // moment of inertia
    static final public float ALPHA_DEFAULT = 10;               // damping coefficient
    static final public float MB_DEFAULT = 1000;                // magnetic field coefficient

    // Value holders
    long time1, time2;              // timestamps of previous iterations--used in numerical integration
    float angle1, angle2, angle0;   // angles of previous iterations
    float angleLastDrawn;           // last drawn angular position
    boolean animationOn = false;    // if animation should be performed

    // Parameters
    float inertiaMoment = INERTIA_MOMENT_DEFAULT;
    float alpha = ALPHA_DEFAULT;
    float mB = MB_DEFAULT;

    // UI Text Variables
    TextView azDirectionView;
    TextView azDegreesView;


    // CONSTRUCTORS
    /**
     * Constructor inherited from ImageView
     *
     * @param context
     */
    public CompassView(Context context) {
        super(context);
    }

    /**
     * Constructor inherited from ImageView
     *
     * @param context
     * @param attrs
     */
    public CompassView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Constructor inherited from ImageView
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public CompassView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    // PUBLIC METHODS
    /**
     * Tracks TextView instance for updating live direction
     * (N, NW, W, SW, S, SE, E, NE)
     *
     * @param azDirectionView
     * @return
     */
    public CompassView setDirectionView(TextView azDirectionView){
        this.azDirectionView = azDirectionView;
        return this;
    }

    /**
     * Tracks TextView instance for updating live degrees
     *
     * @param azDegreesView
     * @return
     */
    public CompassView setDegreesView(TextView azDegreesView){
        this.azDegreesView = azDegreesView;
        return this;
    }

    /**
     *  Inherited from ImageView
     *  Intercepts Draw() call and checks to see if angle of image should be updated
     *
     * @param canvas
     */
    @Override
    public void onDraw(Canvas canvas){
        if (animationOn){
            if (angleRecalculate(new Date().getTime())){
                this.setRotation(angle1);
            }
        }
        else {
            this.setRotation(angle1);
        }
        super.onDraw(canvas);
        if (animationOn){
            this.invalidate();
        }
    }

    /**
     * Sets physical properties by 3rd party
     * Replaces Negative values with defaults
     *
     * @param inertiaMoment     Moment of inertia (default 0.1)
     * @param alpha             Damping coefficient (default 10)
     * @param mB                Magnetic field coefficient (default 1000)
     */
    public void setPhysical(float inertiaMoment, float alpha, float mB){
        this.inertiaMoment = inertiaMoment >= 0 ? inertiaMoment : INERTIA_MOMENT_DEFAULT;
        this.alpha = alpha >= 0 ? alpha : ALPHA_DEFAULT;
        this.mB = mB >= 0 ? mB : MB_DEFAULT;
    }

    /**
     * Updates the real compass angle (and TextViews if present)
     * Called using a Sensor data callback
     *
     * @param azimuth
     */
    public void updateView(int azimuth, boolean animate){
        if(azDirectionView != null)
            azDirectionView.setText(getDirectionString(azimuth));

        if(azDegreesView != null)
            azDegreesView.setText(getDegreesString(azimuth));

        this.rotationUpdate(-azimuth, animate);

    }


    // PRIVATE METHODS
    /**
     * Sets new angle at which image should rotate
     *
     * @param   angleNew    new angle
     * @param   animate     true, if image should rotate using animation, false to set new rotation instantly
     */
    private void rotationUpdate(final float angleNew, boolean animate){
        if (animate){
            if (Math.abs(angle0 - angleNew) > ANGLE_DELTA_THRESHOLD){
                angle0 = angleNew;
                this.invalidate();
            }
            animationOn = true;
        } else {
            angle1 = angleNew;
            angle2 = angleNew;
            angle0 = angleNew;
            angleLastDrawn = angleNew;
            this.invalidate();
            animationOn = false;
        }
    }

    /**
     * Recalculate angles
     *
     * @param   timeNew     timestamp of method invoke
     * @return              if there is a need to redraw rotation
     */
    protected boolean angleRecalculate(final long timeNew){

        // recalculate angle using motion equation
        float deltaT1 = (timeNew - time1)/1000f;
        if (deltaT1 > TIME_DELTA_THRESHOLD){
            deltaT1 = TIME_DELTA_THRESHOLD;
            time1 = timeNew + Math.round(TIME_DELTA_THRESHOLD * 1000);
        }
        float deltaT2 = (time1 - time2)/1000f;
        if (deltaT2 > TIME_DELTA_THRESHOLD){
            deltaT2 = TIME_DELTA_THRESHOLD;
        }

        // circular acceleration coefficient
        float koefI = inertiaMoment / deltaT1 / deltaT2;

        // circular velocity coefficient
        float koefAlpha = alpha / deltaT1;

        // angular momentum coefficient
        float koefk = mB * (float)(Math.sin(Math.toRadians(angle0))*Math.cos(Math.toRadians(angle1)) -
                (Math.sin(Math.toRadians(angle1))*Math.cos(Math.toRadians(angle0))));

        float angleNew = ( koefI*(angle1 * 2f - angle2) + koefAlpha*angle1 + koefk) / (koefI + koefAlpha);

        // reassign previous iteration variables
        angle2 = angle1;
        angle1 = angleNew;
        time2 = time1;
        time1 = timeNew;

        // if angles changed less then threshold, return false - no need to redraw the view
        if (Math.abs(angleLastDrawn - angle1) < ANGLE_DELTA_THRESHOLD){
            return false;
        } else {
            angleLastDrawn = angle1;
            return true;
        }
    }

    /**
     * Returns the text for the degrees TextView
     *
     * @param azimuth
     * @return
     */
    private String getDegreesString(int azimuth){
        final String degreesText = "Azimuth :";
        return String.format("%s%d", degreesText, azimuth);
    }

    /**
     * Returns the text for the direction TextView
     *
     * @param azimuth
     * @return
     */
    private String getDirectionString(int azimuth) {
        AtomicReference<String> direction = new AtomicReference<>("Unknown");
        if(azimuth >= 350 || azimuth <= 10) {direction.set("N");}
        if(azimuth < 350 && azimuth > 280)  {direction.set("NW");}
        if(azimuth <= 280 && azimuth > 260) {direction.set("W");}
        if(azimuth <= 260 && azimuth > 190) {direction.set("SW");}
        if(azimuth <= 190 && azimuth > 170) {direction.set("S");}
        if(azimuth <= 170 && azimuth > 100) {direction.set("SE");}
        if(azimuth <= 100 && azimuth > 80)  {direction.set("E");}
        if(azimuth <= 80 && azimuth > 10)  {direction.set("NE");}
        return direction.get();
    }

}
