package com.daksh.wordhunch.Util;

import android.content.Context;
import android.content.SharedPreferences;

import com.daksh.wordhunch.WordHunch;

public class SettingsUtil {

    //The shared preferences where the settings are saved for the app
    private static SharedPreferences preferences;
    private static final String SETTINGS_PREF = "WordHunch.SharedPrefs.Settings";
    private static final String SETTINGS_PREF_VIBRATION = "WordHunch.SharedPrefs.Settings.Vibration";
    private static final String SETTINGS_PREF_VOLUME = "WordHunch.SharedPrefs.Settings.Volume";

    /**
     * Access the settings shared preferences and returns if the vibration has been enabled by the
     * user or not.
     * @return True if vibration is enabled. Otherwise, false.
     */
    public static boolean isVibrationEnabled() {
        //re initialize sharedPref if not already done so
        if(preferences == null)
            preferences = WordHunch.getContext().getSharedPreferences(SETTINGS_PREF, Context.MODE_PRIVATE);
        //Access Vibration sharedPref and return value saved.
        //If no value is received, assume vibration is enabled
        return preferences.getBoolean(SETTINGS_PREF_VIBRATION, true);
    }

    /**
     * Saves the vibration state received to the shared preferences
     * @param isEnabled A boolean depicting whether user enabled / disabled the vibration
     */
    public static void setVibration(Boolean isEnabled) {
        //re initialize sharedPref if not already done so
        if(preferences == null)
            preferences = WordHunch.getContext().getSharedPreferences(SETTINGS_PREF, Context.MODE_PRIVATE);

        //Access SharedPref editor and save
        preferences
                .edit()
                .putBoolean(SETTINGS_PREF_VIBRATION, isEnabled)
                .apply();
    }

    /**
     * Saves the new volume level received to the SharedPreferences
     * @param intVolume An integer value depicting the current volume
     */
    public static void setVolume(Integer intVolume) {
        //re initialize sharedPref if not already done so
        if(preferences == null)
            preferences = WordHunch.getContext().getSharedPreferences(SETTINGS_PREF, Context.MODE_PRIVATE);

        //Access the SharedPref editor and save the new volume value
        preferences
                .edit()
                .putInt(SETTINGS_PREF_VOLUME, intVolume)
                .apply();
    }

    /**
     * Returns the volume saved in the sharedPrefs if modified by the user previously
     * @return An integer value depicting the volume level
     */
    public static Integer getVolume() {
        //re initialize sharedPref if not already done so
        if(preferences == null)
            preferences = WordHunch.getContext().getSharedPreferences(SETTINGS_PREF, Context.MODE_PRIVATE);

        //Return the volume integer | If not found, assume just 50
        return preferences.getInt(SETTINGS_PREF_VOLUME, 50);
    }
}