package com.daksh.wordhunch.Rink;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.daksh.wordhunch.Network.AutoComplete.DMSuggestions;
import com.daksh.wordhunch.Network.AutoComplete.OnSuggestionCompleteListener;
import com.daksh.wordhunch.Network.AutoComplete.RFAutocomplete;
import com.daksh.wordhunch.Util.DialogClass;
import com.daksh.wordhunch.Util.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RinkSuggestions implements Callback<DMSuggestions> {

    /**
     * Context of the calling activity
     */
    private RingActivity ringActivity;

    private RinkSuggestions() {
        //Empty constructor with private modifier to objects may not be made without passing instance of
        //calling activity
    }
    /**
     * Constructor to accept the calling activity's context
     * @param activity
     */
    RinkSuggestions(@NonNull RingActivity activity) {
        ringActivity = activity;
    }

    /**
     * A method that fetches word of the day from collins
     */
    void getSuggestions() {
        //Get word list
        DialogClass.showBirdDialog(ringActivity);
        RFAutocomplete.SuggestionsAPIInterface apiInterface = RFAutocomplete.getSuggestionsAPIInterface();
        Call<DMSuggestions> listCall = apiInterface.getWordOfTheDay(
                Util.getTodaysDate(Util.YYYYMMDD)
        );
        listCall.enqueue(RinkSuggestions.this);
    }

    @Override
    public void onResponse(Call<DMSuggestions> call, Response<DMSuggestions> response) {
        DialogClass.dismissBirdDialog(ringActivity);
        if(response != null && response.isSuccessful())
            if(response.body() != null && !TextUtils.isEmpty(response.body().getDefinition())) {
                DMSuggestions dmSuggestions = response.body();
                String strDefinition = dmSuggestions.getDefinition();
                String strChallenge = Util.getRandomAlphabets(strDefinition);
                OnSuggestionCompleteListener onSuggestionCompleteListener = ringActivity;
                onSuggestionCompleteListener.onChallengeReceived(strChallenge);
            }
    }

    @Override
    public void onFailure(Call<DMSuggestions> call, Throwable t) {

    }
}
