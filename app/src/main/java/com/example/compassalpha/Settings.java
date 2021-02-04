package com.example.compassalpha;

import android.content.Context;
import android.widget.Switch;

public class Settings {

    private final String animationOn_key = "animationOn";
    private static final Settings settings = null;

    private Context context;
    private Switch animationSwitch;

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
