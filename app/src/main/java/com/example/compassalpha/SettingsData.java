package com.example.compassalpha;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsData {
    private final String animationOn_key = "animationOn";

    private static SettingsData settingsData;

    private SharedPreferences sharedPreferences;
    private Context context;


    // SINGLETON PATTERN
    private SettingsData(Context context){
        this.sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
    }

    public static void initSettingsData(Context context){
        if(settingsData == null){
            settingsData = new SettingsData(context);
        }
    }

    public static SettingsData getSettingsData(){
        return settingsData;
    }


    public void setAnimationON(boolean on){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(animationOn_key, on);
        editor.apply();
    }

    public boolean isAnimationOn(){
        return sharedPreferences.getBoolean(animationOn_key, true);
    }

}
