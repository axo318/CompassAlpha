package com.example.compassalpha.settings;

import android.content.Context;
import android.content.SharedPreferences;


public class SettingsData {
    private final String animationOn_key = "animationOn";

    // Static Singleton object
    private static SettingsData settingsData;

    private SharedPreferences sharedPreferences;
    private Context context;


    // CONSTRUCTOR
    /**
     * Private constructor implement Singleton pattern
     *
     * @param context   App context
     */
    private SettingsData(Context context){
        this.sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
    }


    // PUBLIC METHODS
    /**
     * Initializes setting data from sharedpreferences
     *
     * @param context   App context
     */
    public static void initSettingsData(Context context){
        if(settingsData == null){
            settingsData = new SettingsData(context);
        }
    }

    /**
     * Returns the SettingsData Singleton object
     *
     * @return  SettingsData object
     */
    public static SettingsData getSettingsData(){
        return settingsData;
    }

    /**
     * Changes animation setting and updates shared preferences
     *
     * @param on
     */
    public void setAnimationON(boolean on){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(animationOn_key, on);
        editor.apply();
    }

    /**
     * Returns state of animation switch
     *
     * @return      Boolean isAnimationOn
     */
    public boolean isAnimationOn(){
        return sharedPreferences.getBoolean(animationOn_key, true);
    }

}
