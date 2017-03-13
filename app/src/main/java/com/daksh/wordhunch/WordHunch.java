package com.daksh.wordhunch;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.daksh.wordhunch.Network.AutoComplete.DaoMaster;
import com.daksh.wordhunch.Network.AutoComplete.DaoSession;
import com.daksh.wordhunch.Network.AutoComplete.PurgeScheduler;
import com.daksh.wordhunch.Network.AutoComplete.SuggestionsScheduler;
import com.daksh.wordhunch.Network.RetroFit;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.greendao.database.Database;

import java.util.concurrent.TimeUnit;

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

        //Execute a job scheduler to download suggestions once everyday when the device is on charging
        //and internet is present
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));

        //Create individual job scheduler to cater to certain tasks
        createSuggestionJobScheduler(dispatcher);
        createPurgeJobScheduler(dispatcher);

        //Configure the EventBus
        EventBus.builder()
                .addIndex(new WordHunchBusIndex())
//                .throwSubscriberException(true)
//                .sendNoSubscriberEvent(true)
//                .sendSubscriberExceptionEvent(true)
                .installDefaultEventBus();
    }

    /**
     * Creates a purge job scheduler to be executed in the background which accesses the local database
     * for suggestions table. Refer {@link PurgeScheduler} for documentation.
     * @param dispatcher The FireJobDispatcher which creates the job & schedules it
     */
    private void createPurgeJobScheduler(FirebaseJobDispatcher dispatcher) {
        //Build a new job dispatcher to specify when suggestions should be purged in the background
        //to save space and/or purge parts of the suggestions which are not usable in WordHunch app
        Job purgeJob = dispatcher.newJobBuilder()
                //Set a tag for the job
                .setTag(PurgeScheduler.class.getSimpleName())
                //Lifetime of forever | Persistence across device reboots
                .setLifetime(Lifetime.FOREVER)
                //Set the service to be executed
                .setService(PurgeScheduler.class)
                //Execute over and over again based on trigger
                .setRecurring(true)
                //Constraint | Device is charging and connected to internet
                .setConstraints(
                        Constraint.DEVICE_CHARGING
                )
                //Set a trigger to execute this task every 24 hours with a latency tolerance of 1 hour
                .setTrigger(Trigger.executionWindow(
                        (int) TimeUnit.HOURS.toSeconds(1),
                        (int) TimeUnit.HOURS.toSeconds(2)
                ))
                //Do not replace if a job exists with the same tag
                .setReplaceCurrent(false)
                //Build it
                .build();

        //Schedule job
        dispatcher.schedule(purgeJob);
    }

    /**
     * Creates a Suggestions job scheduler to be executed in the background which accesses collins'
     * APIs to download word of the day and uses it's description to create suggestions.
     * Also, all downloaded entities are stored in the local database to be used when internet connectivity
     * is missing
     * Refer {@link PurgeScheduler} for documentation.
     * @param dispatcher The FireJobDispatcher which creates the job & schedules it
     */
    private void createSuggestionJobScheduler(FirebaseJobDispatcher dispatcher) {
        //Build a new job dispatcher to specify when suggestions should be downloaded
        //in the background
        Job backgroundJob = dispatcher.newJobBuilder()
                //Set a tag for the job
                .setTag(SuggestionsScheduler.class.getSimpleName())
                //Lifetime of forever | Persistence across device reboots
                .setLifetime(Lifetime.FOREVER)
                //Set the service to be executed
                .setService(SuggestionsScheduler.class)
                //Execute over and over again based on trigger
                .setRecurring(true)
                //Constraint | Device is charging and connected to internet
                .setConstraints(
                        Constraint.DEVICE_CHARGING,
                        Constraint.ON_ANY_NETWORK
                )
                //Set a trigger to execute this task every 24 hours with a latency tolerance of 1 hour
                .setTrigger(Trigger.executionWindow(
                        (int) TimeUnit.HOURS.toSeconds(1),
                        (int) TimeUnit.HOURS.toSeconds(2)
                ))
                //Do not replace if a job exists with the same tag
                .setReplaceCurrent(false)
                //Build it
                .build();

        //Schedule job
        dispatcher.schedule(backgroundJob);
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