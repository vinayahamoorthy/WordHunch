package com.daksh.wordhunch;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.daksh.wordhunch.Network.AutoComplete.DaoMaster;
import com.daksh.wordhunch.Network.AutoComplete.DaoSession;
import com.daksh.wordhunch.Network.RetroFit;

import org.greenrobot.greendao.database.Database;

public class WordHunch extends Application {

    /**
     * Context of the application
     */
    private static WordHunch context;

    /**
     * The Dao Session object used to access the database
     */
    private static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        //Reference application context
        context = this;
        //Initialize RetroFit
        RetroFit.initializeRetroFit();

        //Initialize GreenDAO
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "WordHunch-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    /**
     * A method to return the object of the DaoSession using which DB calls may be made by the user
     * @return
     */
    @NonNull
    public static DaoSession getDaoSession() {
        return daoSession;
    }

    /**
     * A method to return the context of the application
     * @return
     */
    @NonNull
    public static Context getContext() {
        return context;
    }
}