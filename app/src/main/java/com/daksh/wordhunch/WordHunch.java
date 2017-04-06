package com.daksh.wordhunch;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.daksh.wordhunch.Menu.Events.IsFirstRunEvent;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.leaderboard.LeaderboardScore;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.android.gms.games.leaderboard.Leaderboards;

import net.sqlcipher.database.SQLiteDatabase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.greendao.database.Database;

import java.util.concurrent.TimeUnit;

import io.fabric.sdk.android.Fabric;

public class WordHunch extends MultiDexApplication implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<Leaderboards.LoadPlayerScoreResult> {

    //Context of the application
    private static WordHunch context;

    //The Dao Session object used to access the database
    private static DaoSession daoSession;

    //The google API client that connects to google play services for Games
    private static GoogleApiClient mGoogleApiClient;

    //A boolean to identify if the build is debug or not
    private boolean isDebug = true;

    @Override
    public void onCreate() {
        super.onCreate();
        //Reference application context
        context = this;
        //Initialize RetroFit
        RetroFit.initializeRetroFit();

        if(!isDebug) {
            Fabric fabric = new Fabric.Builder(this)
                    .kits(
                            new Crashlytics(),
                            new Answers()
                    )
                    .debuggable(true)
                    .build();
            Fabric.with(fabric);
        }

        //Load SQL Cipher only if application is not of debug type
        if (!isDebug)
            SQLiteDatabase.loadLibs(this);

        //Initialize GreenDAO
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "WordHunch-db");

        Database db = null;
        //Get normal writable DB if debug is enabled | If not, use Cipher
        if (isDebug)
            db = helper.getWritableDb();
        else
            db = helper.getEncryptedWritableDb(WordHunch.class.getSimpleName());

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

        //Generate a game options object to set connecting popup as always displayed
        Games.GamesOptions gamesOptions = Games.GamesOptions.builder()
                .setShowConnectingPopup(true)
                .build();

        //Build the google api client to connect to Google Play Games
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Games.API, gamesOptions)
                .addScope(Games.SCOPE_GAMES)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        //Initiate the connection
        mGoogleApiClient.connect();
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

    /**
     * Returns the GoogleApiClient used app wide to communicate with Google APIs
     * @return
     */
    @NonNull
    public static GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        //Get high score if exists. This would check if the person is playing the game for the first time
        //or not. Depending on the scenario, a flag is sent to the RinkActivity to inform it to
        //unlock the FirstBlood(R.string.play_achievement_FirstBlood) achievement.
        Games.Leaderboards.loadCurrentPlayerLeaderboardScore(
                mGoogleApiClient,
                getString(R.string.play_leaderboard_SingleRoundHighScore),
                LeaderboardVariant.TIME_SPAN_ALL_TIME,
                LeaderboardVariant.COLLECTION_SOCIAL
        ).setResultCallback(WordHunch.this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(WordHunch.this);

        // 2. Chain together various setter methods to set the dialog characteristics
        if(!TextUtils.isEmpty(connectionResult.getErrorMessage()) && connectionResult.getErrorCode() != 0)
            builder
                    .setMessage(connectionResult.getErrorMessage())
                    .setTitle(connectionResult.getErrorCode());

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onResult(@NonNull Leaderboards.LoadPlayerScoreResult loadPlayerScoreResult) {
        //Extract the highest score for the scoreboard for which this request was received
        //Single Round HighScore (R.string.play_leaderboard_SingleaRoundHighScore)
        LeaderboardScore scoreboard = loadPlayerScoreResult.getScore();
        if(scoreboard != null) {
            if (EventBus.getDefault().hasSubscriberForEvent(IsFirstRunEvent.class))
                EventBus.getDefault().post(new IsFirstRunEvent(false));
        } else if (EventBus.getDefault().hasSubscriberForEvent(IsFirstRunEvent.class))
            EventBus.getDefault().post(new IsFirstRunEvent(true));
    }
}