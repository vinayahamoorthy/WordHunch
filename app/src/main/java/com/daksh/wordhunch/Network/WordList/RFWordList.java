package com.daksh.wordhunch.Network.WordList;

import com.daksh.wordhunch.Network.RetrofitFactory;

import okhttp3.ResponseBody;
import okio.BufferedSink;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;

public class RFWordList {

    /**
     * API interface in singleton pattern
     */
    private static WordListInterface apiInterface = null;

    /**
     * A method which utilises CollinsRetroFit built from parent class and creates an implementation
     * defined by the passed interface.
     * @return Returns APIInterface related to Projects Module
     */
    public static WordListInterface getWordListAPIInterface() {

        if(apiInterface == null) {
            Retrofit retrofit = RetrofitFactory.getType(RetrofitFactory.Source.WordList).getRetrofit();
            apiInterface = retrofit.create(WordListInterface.class);
            return apiInterface;
        } else
            return apiInterface;
    }

    public interface WordListInterface {

        @GET("english-words/master/words2.txt")
        Call<ResponseBody> getWordList();
    }
}