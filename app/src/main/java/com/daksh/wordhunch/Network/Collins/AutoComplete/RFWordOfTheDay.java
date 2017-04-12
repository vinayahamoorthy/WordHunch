package com.daksh.wordhunch.Network.Collins.AutoComplete;

import com.daksh.wordhunch.Network.RetrofitFactory;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RFWordOfTheDay {

    /**
     * API interface in singleton pattern
     */
    private static WordOfTheDayAPIInterface apiInterface = null;

    /**
     * A method which utilises CollinsRetroFit built from parent class and creates an implementation
     * defined by the passed interface.
     * @return Returns APIInterface related to Projects Module
     */
    public static WordOfTheDayAPIInterface getSuggestionsAPIInterface() {

        if(apiInterface == null) {
            Retrofit retrofit = RetrofitFactory.getType(RetrofitFactory.Source.Collins).getRetrofit();
            apiInterface = retrofit.create(WordOfTheDayAPIInterface.class);
            return apiInterface;
        } else
            return apiInterface;
    }

    public interface WordOfTheDayAPIInterface {

        @GET("wordoftheday/preview/")
        Call<DMWordList> getWordOfTheDay(
                @Query("day") String strDate
        );
    }
}