package com.daksh.wordhunch.Network.AutoComplete;

import com.daksh.wordhunch.Network.RetroFit;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
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
//        @GET("{dictCode}/search/didyoumean")
//        Call<DMSuggestions> getSuggestions(
//                @Path("dictCode") String strDictCode,
//                @Query("q") String strQuery,
//                @Query("entrynumber") int intEntryNumber,
//                @Query("page") int intPageNumber
//        );

        @GET("wordoftheday/preview/")
        Call<DMSuggestions> getWordOfTheDay(
                @Query("day") String strDate
        );
    }
}