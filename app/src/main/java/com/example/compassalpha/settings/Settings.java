package com.example.compassalpha.settings;

import android.content.Context;
import android.widget.Switch;


public class Settings {
    private Context context;
    private Switch animationSwitch;


    // CONSTRUCTOR
    /**
     * Initializes Settings options
     *
     * @param context           Current app context
     * @param animationSwitch   Initialized switch for compass animation
     */
    public Settings(Context context, Switch animationSwitch){
        this.context = context;
        this.animationSwitch = animationSwitch;

        // Set switch to previous position
        boolean animationOn = SettingsData.getSettingsData().isAnimationOn();
        this.animationSwitch.setChecked(animationOn);

        // Set a listener for switch and save state to shared preferences
        animationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SettingsData.getSettingsData().setAnimationON(isChecked);
            animationSwitch.setChecked(isChecked);
        });
    }


}
