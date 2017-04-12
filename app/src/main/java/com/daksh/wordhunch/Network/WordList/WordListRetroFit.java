package com.daksh.wordhunch.Network.WordList;

import com.daksh.wordhunch.R;
import com.daksh.wordhunch.WordHunch;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WordListRetroFit implements com.daksh.wordhunch.Network.Retrofit {

    /**
     * API interface in singleton pattern
     */
    private static Retrofit retrofit = null;

    /**
     * A method to initialize various networking components of WordHunch. This method is only called
     * at application start up time to ensure resource intensive operations are carried out
     * in the background.
     * * okHTTP Client
     * * CollinsRetroFit using RetroFitBuilder
     */
    @Override
    public void onConfigure() {
//        // Initialize the logging interceptor to log all service requests
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);

        //Instantiate OkHttp library
        OkHttpClient.Builder httpClient = new OkHttpClient
                .Builder()
                .addInterceptor(loggingInterceptor);
//                .addInterceptor(WordListRetroFit.this);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        //Instantiate CollinsRetroFit Library
        retrofit = new Retrofit.Builder()
                //Set up Base URL
                .baseUrl(
                        WordHunch.getContext().getString(R.string.RetroFit_API_WordListURI)
                )
                //Set up OKHttP client as HTTP layer
                .client(httpClient.build())
                //Use GSON factory to parse server response
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    /**
     * A method which returns the CollinsRetroFit singleton instance of the CollinsRetroFit configuration
     * built.
     * @return Returns API Interface
     */
    @Override
    public Retrofit getRetrofit() {
        //return Retrofit
        return retrofit;
    }

    @Override
    public Class instanceOf() {
        return WordListRetroFit.class;
    }

//    @Override
//    public Response intercept(Chain chain) throws IOException {
//        Request request = chain.request();
//        Response response = chain.proceed(request);
//
//
//        if(EventBus.getDefault().hasSubscriberForEvent(WordListEvent.class))
//            EventBus.getDefault().post(new WordListEvent(response));
//
//        return response;
//    }
}