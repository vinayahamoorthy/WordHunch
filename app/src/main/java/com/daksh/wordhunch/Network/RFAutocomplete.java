package com.daksh.wordhunch.Network;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RFAutocomplete extends RetroFit {

    /**
     * API interface in singleton pattern
     */
    private static SuggestionsAPIInterface suggestionsAPIInterface = null;

    /**
     * A method which utilises RetroFit built from parent class and creates an implementation
     * defined by the passed interface.
     * @return Returns APIInterface related to Projects Module
     */
    public static SuggestionsAPIInterface getSuggestionsAPIInterface() {

        if(suggestionsAPIInterface == null) {
            Retrofit retrofit = getRetroFit();
            suggestionsAPIInterface = retrofit.create(SuggestionsAPIInterface.class);
            return suggestionsAPIInterface;
        } else
            return suggestionsAPIInterface;
    }

    public interface SuggestionsAPIInterface {
        @GET("/autocomplete/")
        Call<List<String>> getSuggestions(
                @Query("q") String strQuery
        );
    }
}