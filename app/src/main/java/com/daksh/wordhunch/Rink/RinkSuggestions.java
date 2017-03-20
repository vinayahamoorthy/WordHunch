package com.daksh.wordhunch.Rink;

import android.text.TextUtils;

import com.daksh.wordhunch.Network.AutoComplete.DMSuggestions;
import com.daksh.wordhunch.Network.AutoComplete.DMSuggestionsDao;
import com.daksh.wordhunch.Network.AutoComplete.RFAutocomplete;
import com.daksh.wordhunch.Rink.Events.RequestChallengeEvent;
import com.daksh.wordhunch.Util.Util;
import com.daksh.wordhunch.WordHunch;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RinkSuggestions implements Callback<DMSuggestions> {

    /**
     * Suggestions DAO to interact with the suggestions table.
     */
    private DMSuggestionsDao suggestionsDao;

    public RinkSuggestions() {
        //Instantiate the suggestions table DAO
        suggestionsDao = WordHunch.getDaoSession().getDMSuggestionsDao();
    }

    /**
     * Registers this class as a subscriber for Events from the EventBus
     */
    public void registerSubscriber() {
        EventBus.getDefault().register(this);
    }

    /**
     * Unregisters the subscription by this class
     */
    public void unregisterSubscribe() {
        EventBus.getDefault().unregister(this);
    }

    /**
     * A method that fetches word of the day from collins
     */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void getSuggestions(RequestChallengeEvent event) {

        switch (event.getChallenge()) {
            case CHALLENGE_ALL:
                if (hasTodaysChallenge())
                    getSuggestions(new RequestChallengeEvent(RequestChallengeEvent.RequestMode.CHALLENGE_STORAGE));
                else
                    getSuggestions(new RequestChallengeEvent(RequestChallengeEvent.RequestMode.CHALLENGE_NETWORK));
                break;

            case CHALLENGE_NETWORK:
                RFAutocomplete.SuggestionsAPIInterface apiInterface = RFAutocomplete.getSuggestionsAPIInterface();
                Call<DMSuggestions> listCall = apiInterface.getWordOfTheDay(
                        Util.getTodaysDate(Util.YYYYMMDD)
                );
                listCall.enqueue(RinkSuggestions.this);
                break;

            case CHALLENGE_STORAGE:
                onFailure(null, null);
                break;
        }
    }

    @Override
    public void onResponse(Call<DMSuggestions> call, Response<DMSuggestions> response) {
        if(response != null && response.isSuccessful())
            if(response.body() != null && !TextUtils.isEmpty(response.body().getDefinition())) {
                DMSuggestions dmSuggestions = response.body();
                dmSuggestions.setDate(Util.getTodaysDate(Util.YYYYMMDD));
                String strDefinition = dmSuggestions.getDefinition();
                String strChallenge = Util.getRandomAlphabets(strDefinition);

                //Send event to whoever is listening
                EventBus.getDefault().post(strChallenge);

                //Store to Database
                suggestionsDao.insertOrReplace(dmSuggestions);
            }
    }

    @Override
    public void onFailure(Call<DMSuggestions> call, Throwable t) {

        //If API failed, fetch item from local DB | Only if the activity requested it
        List<DMSuggestions> dmSuggestions = suggestionsDao.loadAll();

        if (dmSuggestions != null && !dmSuggestions.isEmpty()) {
            String strDefinition = dmSuggestions.get(
                    Util.getRandomNumber(Integer.valueOf(String.valueOf(dmSuggestions.size())))
            ).getDefinition();
            String strChallenge = Util.getRandomAlphabets(strDefinition);

            //Send event to whoever is listening
            EventBus.getDefault().post(strChallenge);
        }
    }

    /**
     * Checks if today's challenge has been downloaded or not by querying the database for today's
     * challenge
     */
    private boolean hasTodaysChallenge() {
        //Make a query to check if today's word of the day has been downloaded or not
        DMSuggestions dmSuggestions = suggestionsDao.queryBuilder()
                .where(DMSuggestionsDao.Properties.Date.eq(
                        Util.getTodaysDate(Util.YYYYMMDD)
                ))
                .build()
                .unique();

        //Return true if DB returned something or not
        return dmSuggestions != null;
    }
}
