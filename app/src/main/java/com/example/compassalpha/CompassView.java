package com.example.compassalpha;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicReference;

public class CompassView {

    // UI Variables
    TextView azDirectionView;
    TextView azDegreesView;
    ImageView compassView;

    public CompassView(TextView azDirectionView, TextView azDegreesView, ImageView compassView){
        this.azDirectionView = azDirectionView;
        this.azDegreesView = azDegreesView;
        this.compassView = compassView;
    }

    public void updateView(int azimuth){
        if(azDirectionView != null)
            azDirectionView.setText(getDirectionString(azimuth));

        if(azDegreesView != null)
            azDegreesView.setText(getDegreesString(azimuth));

        if(compassView != null)
            compassView.setRotation(getRotation(azimuth));

    }

    private String getDegreesString(int azimuth){
        final String degreesText = "Azimuth :";
        return String.format("%s%d", degreesText, azimuth);
    }

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

    private float getRotation(int azimuth) {
        return -azimuth;
    }
}
