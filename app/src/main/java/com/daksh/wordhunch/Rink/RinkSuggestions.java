package com.daksh.wordhunch.Rink;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.daksh.wordhunch.Network.AutoComplete.DMSuggestions;
import com.daksh.wordhunch.Network.AutoComplete.DMSuggestionsDao;
import com.daksh.wordhunch.Network.AutoComplete.OnSuggestionCompleteListener;
import com.daksh.wordhunch.Network.AutoComplete.RFAutocomplete;
import com.daksh.wordhunch.Util.DialogClass;
import com.daksh.wordhunch.Util.Util;
import com.daksh.wordhunch.WordHunch;
import com.firebase.jobdispatcher.JobService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RinkSuggestions implements Callback<DMSuggestions> {

    /**
     * The callback interface to be executed when response is received from the server
     */
    private OnSuggestionCompleteListener suggestionCompleteListener;

    private RinkSuggestions() {
        //Empty constructor with private modifier to objects may not be made without passing instance of
        //calling activity
    }

    /**
     * Constructor to accept a callback interface which is executed when the data is received
     * from the server
     * @param suggestionCompleteListener The interface object whose methods are executed
     *                                   to return the suggestion when returned from the service
     */
    public RinkSuggestions(@NonNull OnSuggestionCompleteListener suggestionCompleteListener) {
        this.suggestionCompleteListener = suggestionCompleteListener;
    }

    /**
     * A method that fetches word of the day from collins
     */
    public void getSuggestions() {
        //Get word list
        if(suggestionCompleteListener.getContext() != null)
            DialogClass.showBirdDialog((Activity) suggestionCompleteListener.getContext());

        RFAutocomplete.SuggestionsAPIInterface apiInterface = RFAutocomplete.getSuggestionsAPIInterface();
        Call<DMSuggestions> listCall = apiInterface.getWordOfTheDay(
                Util.getTodaysDate(Util.YYYYMMDD)
        );
        listCall.enqueue(RinkSuggestions.this);
    }

    @Override
    public void onResponse(Call<DMSuggestions> call, Response<DMSuggestions> response) {
        if(suggestionCompleteListener.getContext() != null)
            DialogClass.dismissBirdDialog((Activity) suggestionCompleteListener.getContext());

        if(response != null && response.isSuccessful())
            if(response.body() != null && !TextUtils.isEmpty(response.body().getDefinition())) {
                DMSuggestions dmSuggestions = response.body();
                String strDefinition = dmSuggestions.getDefinition();
                String strChallenge = Util.getRandomAlphabets(strDefinition);

                suggestionCompleteListener.onChallengeReceived(strChallenge);

                //Store to Database
                DMSuggestionsDao suggestionsDao = WordHunch.getDaoSession().getDMSuggestionsDao();
                suggestionsDao.insertOrReplace(dmSuggestions);
            }
    }

    @Override
    public void onFailure(Call<DMSuggestions> call, Throwable t) {

        //If API failed, fetch item from local DB | Only if the activity requested it
        if(suggestionCompleteListener.getContext() != null) {
            DMSuggestionsDao suggestionsDao = WordHunch.getDaoSession().getDMSuggestionsDao();
            Long lngTableCount = suggestionsDao.count();
            int intRandomEntry = Util.getRandomNumber(Integer.valueOf(String.valueOf(lngTableCount)));
            DMSuggestions dmSuggestions = suggestionsDao.loadByRowId(intRandomEntry);

            if (dmSuggestions != null) {
                String strDefinition = dmSuggestions.getDefinition();
                String strChallenge = Util.getRandomAlphabets(strDefinition);
                suggestionCompleteListener.onChallengeReceived(strChallenge);
            }
        }
    }
}
