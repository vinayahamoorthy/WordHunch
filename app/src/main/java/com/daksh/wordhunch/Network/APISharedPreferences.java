package com.daksh.wordhunch.Network;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.daksh.wordhunch.R;
import com.daksh.wordhunch.WordHunch;

public class APISharedPreferences {

    /**
     * A method to remove everything from the APISharedPreferences. This is used when the user calls
     * logout.
     */
//    public static void clear() {
//        //Get SharedPreferences Editor
//        SharedPreferences.Editor editor = WordHunch.getContext().getSharedPreferences(
//                WordHunch.getContext().getString(R.string.Retrofit_Preferences),
//                Context.MODE_PRIVATE
//        ).edit();
//
//        editor.clear();
//        editor.apply();
//    }

    /**
     * A method to store Auth key received from the server at the time of login to shared preferences
     * for auto insertion in every service
     *
     * @param strAuthKey
     * @return
     */
//    public static void setAuthKey(String strAuthKey) {
//        //Get SharedPreferences Editor
//        SharedPreferences.Editor editor = WordHunch.getContext().getSharedPreferences(
//                WordHunch.getContext().getString(R.string.Retrofit_Preferences),
//                Context.MODE_PRIVATE
//        ).edit();
//        //Store string
//        editor.putString(
//                WordHunch.getContext().getString(R.string.Retrofit_Preferences_AuthKey),
//                strAuthKey
//        ).apply();
//
//        Log.i("APIsharedPref", strAuthKey);
//    }

    /**
     * A method to retrieve Auth key received from the server at the time of login to shared preferences
     * for auto insertion in every service
     *
     * @return Returns the value saved in the sharedPrefernces against the key
     */
//    public static String getAuthKey() {
//        //Get SharedPreferences Editor
//        SharedPreferences sharedPreferences = WordHunch.getContext().getSharedPreferences(
//                WordHunch.getContext().getString(R.string.Retrofit_Preferences),
//                Context.MODE_PRIVATE
//        );
//        Log.i("APIsharedPref", "taken");
//        //Store string
//        return sharedPreferences.getString(
//                WordHunch.getContext().getString(R.string.Retrofit_Preferences_AuthKey),
//                ""
//        );
//
//    }
}