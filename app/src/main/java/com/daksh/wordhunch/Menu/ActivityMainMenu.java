package com.daksh.wordhunch.Menu;

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
import com.google.android.gms.games.Games;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityMainMenu extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    //An instance of RinkSuggestions class to retrieve suggestions
    private RinkSuggestions rinkSuggestions;
    //The google API client that connects to googl play services for Games
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

        //Build the google api client to connect to Google Play Games
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Games.API)
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}