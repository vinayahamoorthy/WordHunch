package com.daksh.wordhunch.Network.AutoComplete;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.NotificationCompat;

import com.daksh.wordhunch.R;
import com.daksh.wordhunch.Rink.RinkSuggestions;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class SuggestionsScheduler extends JobService implements OnSuggestionCompleteListener {

    @Override
    public boolean onStartJob(JobParameters job) {

        //Create a new instance of suggestions class which downloads and saves the suggestions
        //in the DB
        RinkSuggestions rinkSuggestions = new RinkSuggestions(SuggestionsScheduler.this);
        rinkSuggestions.getSuggestions();

        //Return true if there is work remaining in the scheduler | Keep thread alive
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {

        //Return false if the Job needs to be retried.
        return false;
    }

    @Override
    public void onChallengeReceived(String strChallenge) {

        Notification notification = new NotificationCompat.Builder(SuggestionsScheduler.this)
                .setTicker("New challengeg")
                .setContentTitle("Word Hunch")
                .setContentText("A new challenge was received")
                .setSmallIcon(R.drawable.ic_replay)
                .build();

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(1, notification);

        //Challenge already saved in local DB | End job - nothing doing now
        onStopJob(null);
    }

    @Override
    public Context getContext() {
        return null;
    }
}
