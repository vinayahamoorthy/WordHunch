package com.daksh.wordhunch;

import android.app.Application;
import android.content.Context;

import com.daksh.wordhunch.Network.RetroFit;


public class WordHunch extends Application {

    /**
     * Context of the application
     */
    private static WordHunch context;

    /**
     * Determines if this app is in debug more or not
     */
    private static Boolean IS_DEBUG = true;

    @Override
    public void onCreate() {
        super.onCreate();
        //Reference application context
        context = this;
        //Initialize RetroFit
        RetroFit.initializeRetroFit();
    }

    /**
     * A method to return the context of the application
     * @return
     */
    public static Context getContext() {
        return context;
    }
}