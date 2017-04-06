package com.daksh.wordhunch.Menu;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.daksh.wordhunch.R;
import com.daksh.wordhunch.Rink.Events.RequestChallengeEvent;
import com.daksh.wordhunch.Rink.RingActivity;
import com.daksh.wordhunch.Rink.RinkSuggestions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.leaderboard.LeaderboardScore;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.android.gms.games.leaderboard.Leaderboards;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityMainMenu extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<Leaderboards.LoadPlayerScoreResult> {

    //An instance of RinkSuggestions class to retrieve suggestions
    private RinkSuggestions rinkSuggestions;
    //The google API client that connects to google play services for Games
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        //Bind Views
        ButterKnife.bind(this);

        //instantiate the rink suggestions class to register Event listeners on the class
        rinkSuggestions = new RinkSuggestions();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Register this class to accept Events from the event bus
        EventBus.getDefault().register(this);
        //Register receiver in the suggestions class
        rinkSuggestions.registerSubscriber();
    }

    @Override
    protected void onResume() {
        super.onResume();

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

        //Send Event to Request New challenge | Will be received in RinkSuggestions class
        if(EventBus.getDefault().hasSubscriberForEvent(RequestChallengeEvent.class))
            EventBus.getDefault().post(new RequestChallengeEvent(RequestChallengeEvent.RequestMode.CHALLENGE_NETWORK));
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Unregister
        EventBus.getDefault().unregister(this);
        rinkSuggestions.unregisterSubscribe();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onChallengeReceived(String strChallenge) {
        //Challenge received | Do nothing.
    }

    /**
     * The menu button starts the game | Navigates to RinkActivity
     */
    @OnClick(R.id.menu_start)
    public void onMenuStart() {
        Intent intent = new Intent(this, RingActivity.class);
        startActivity(intent);
    }

    /**
     * The menu button takes the user to the about page | Navigates to RinkActivity
     */
    @OnClick(R.id.menu_about)
    public void onMenuAbout() {
        Intent intent = new Intent(this, ActivityAbout.class);
        startActivity(intent);
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
        ).setResultCallback(ActivityMainMenu.this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityMainMenu.this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder
                .setMessage(connectionResult.getErrorMessage())
                .setTitle(connectionResult.getErrorCode());

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onResult(@NonNull Leaderboards.LoadPlayerScoreResult loadPlayerScoreResult) {
//        if(loadPlayerScoreResult.getStatus() == )
        //Extract the highest score for the scoreboard for which this request was received
        //Single Round HighScore (R.string.play_leaderboard_SingleaRoundHighScore)
        LeaderboardScore scoreboard = loadPlayerScoreResult.getScore();
    }
}