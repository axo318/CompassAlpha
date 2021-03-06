package com.example.compassalpha.nav;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.compassalpha.BatteryActivity;
import com.example.compassalpha.R;
import com.example.compassalpha.SettingsActivity;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class NavHandler {
    private final String tag = "NavHandler";

    // Activity this object belongs to
    private final Activity activity;

    // UI Variables
    private DrawerLayout mDrawerLayout;
    private NavigationView navView;
    private ActionBarDrawerToggle mToggle;


    // CONSTRUCTORS
    /**
     * Constructor used for Non-parent activities. This only enables the "back" arrow
     *
     * @param activity      Current activity instance
     */
    public NavHandler(AppCompatActivity activity){
        this.activity = activity;
        Objects.requireNonNull(activity.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Constructor used for parent activities. Sets up the navigation drawer.
     *
     * @param activity          Current activity instance
     * @param mDrawerLayout     Initialized drawerLayout
     * @param navView           Initialized NavigationView
     */
    public NavHandler(AppCompatActivity activity, DrawerLayout mDrawerLayout, NavigationView navView){
        this.activity = activity;
        this.mDrawerLayout = mDrawerLayout;
        this.navView = navView;

        mToggle = new ActionBarDrawerToggle(activity, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        Objects.requireNonNull(activity.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setUpMenu();
    }

    /**
     * Connects the button of the nav menu to the menu. Should be called in
     * Activity.onOptionsItemSelected callback.
     *
     * @param item      MenuItem pressed
     * @return          Boolean
     */
    public boolean onOptionsItemSelected(MenuItem item){
        return mToggle.onOptionsItemSelected(item);
    }


    // PRIVATE METHODS
    /**
     * Sets up navigation menu
     */
    private void setUpMenu(){
        navView.setNavigationItemSelectedListener(
                menuItem -> {
                    // set item as selected to persist highlight
                    menuItem.setChecked(true);
                    // close drawer when item is tapped
                    mDrawerLayout.closeDrawers();

                    // Depending what button is pressed
                    switch(menuItem.getItemId()) {

                        case R.id.nav_battery:
                            Log.d(tag, "[setUpNavigationMenu]: Home was pressed");
                            goToActivity(BatteryActivity.class);
                            break;

                        case R.id.nav_settings:
                            Log.d(tag, "[setUpNavigationMenu]: Sensors was pressed and going to activity");
                            goToActivity(SettingsActivity.class);
                            break;
                    }
                    return true;
                });
        Log.d(tag, "[setUpNavigationMenu]: Nav menu buttons were successfully set");
    }

    /**
     * Changes the current activity
     *
     * @param act   Class of target activity
     */
    private void goToActivity(final Class<? extends Activity> act){
        Intent intent = new Intent((Context) activity, act);
        activity.startActivity(intent);
    }

}
