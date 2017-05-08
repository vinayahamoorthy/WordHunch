package com.daksh.wordhunch.Menu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daksh.wordhunch.Menu.Events.IsFirstRunEvent;
import com.daksh.wordhunch.R;
import com.daksh.wordhunch.Rink.Events.RequestChallengeEvent;
import com.daksh.wordhunch.Rink.RingActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityMainMenu extends AppCompatActivity {

    //Bind views with butterknife
    @BindView(R.id.menu_start_container) TextView flStart;
    @BindView(R.id.menu_start) TextView txStart;
    @BindView(R.id.menu_about) ImageView vAbout;

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Register this class to accept Events from the event bus
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Send Event to Request New challenge | Will be received in RinkSuggestions class
        if(EventBus.getDefault().hasSubscriberForEvent(RequestChallengeEvent.class))
            EventBus.getDefault().post(new RequestChallengeEvent());
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Unregister
        EventBus.getDefault().unregister(this);
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
        Intent intent = new ActivityAbout
                .Builder(ActivityMainMenu.this)
                .build();
        startActivity(intent);
    }

    @OnClick(R.id.menu_settings)
    public void onMenuSettings(View view) {
        Intent intent = new ActivitySettings
                .Builder(ActivityMainMenu.this)
                .build();
        startActivity(intent);
    }

    //MainMenu is used in associated with a builder pattern. This is done so as to ensure all
    //prerequisite parameters are sent to the activity that are required to ensure its proper
    //functioning. This ensures even when someone else than the primary developer works on this activity.
    //s/he will not have to go through layers of code to find which params are required
    //to instantiate this module.
    public static class Builder {

        //A context to the instantiating activity
        private Context context;
        //A boolean value that determines if the game is being played for the first time or not
        private Boolean isFirstTime;

        private Builder() {
            //Empty private constructor to disable instantiation
        }

        /**
         * The constructor to be called to instantiate the class.
         * @param context The context of the calling activity is required to manufacture the intent
         */
        public Builder(Context context) {
            this.context = context;
        }

        /**
         * The build method executes and generates an intent. Returns the intent that may be used with
         * startActivity or startActivityForResult.
         *
         * @return Intent that may be used to start the RingActivity.
         */
        public Intent build() {
            return new Intent(context, ActivityMainMenu.class);
        }
    }
}