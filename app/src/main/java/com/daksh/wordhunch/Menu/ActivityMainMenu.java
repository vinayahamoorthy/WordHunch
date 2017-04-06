package com.daksh.wordhunch.Menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.daksh.wordhunch.Menu.Events.IsFirstRunEvent;
import com.daksh.wordhunch.R;
import com.daksh.wordhunch.Rink.Events.RequestChallengeEvent;
import com.daksh.wordhunch.Rink.RingActivity;
import com.daksh.wordhunch.Rink.RinkSuggestions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.leaderboard.Leaderboards;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityMainMenu extends AppCompatActivity {

    //An instance of RinkSuggestions class to retrieve suggestions
    private RinkSuggestions rinkSuggestions;
    //A boolean to track if the user is arriving on this app for the first time or not.
    //Depending on the response, the FirtBlood (R.string.play_achievement_FirstBlood)
    //achievement will be unlocked or not by sending a param to the RinkActivity activity.
    private boolean isFirstTime = true;

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

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onFirstRunResult(IsFirstRunEvent isFirstRunEvent) {
        isFirstTime = isFirstRunEvent.isFirstRun();
    }

    /**
     * The menu button starts the game | Navigates to RinkActivity
     */
    @OnClick(R.id.menu_start)
    public void onMenuStart() {

        Intent intent = new RingActivity.Builder(this)
                .setFirstTime(isFirstTime)
                .build();

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
}