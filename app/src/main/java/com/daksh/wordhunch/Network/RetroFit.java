package com.daksh.wordhunch.Network;

import com.daksh.wordhunch.R;
import com.daksh.wordhunch.WordHunch;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroFit {

    /**
     * API interface in singleton pattern
     */
    private static Retrofit retrofit = null;

    /**
     * A method to initialize various networking components of Lean4J SDK. This method is only called
     * from Lean4J at application start up time to ensure resource intensive operations are carried out
     * in the background.
     * * okHTTP Client
     * * RetroFit using RetroFitBuilder
     */
    public static void initializeRetroFit() {
        // Initialize the logging interceptor to log all service requests
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //Instantiate OkHttp library
        OkHttpClient.Builder httpClient = new OkHttpClient
                .Builder()
                .addInterceptor(new AuthKeyInterceptor())
                .addInterceptor(loggingInterceptor);

        //Instantiate RetroFit Library
        retrofit = new Retrofit.Builder()
                //Set up Base URL
                .baseUrl(
                        WordHunch.getContext().getString(R.string.RetroFit_API_URI)
                                + WordHunch.getContext().getString(R.string.RetroFit_API_Version)
                                + WordHunch.getContext().getString(R.string.RetroFit_API_Dict)
                )
                //Set up OKHttP client as HTTP layer
                .client(httpClient.build())
                //Use GSON factory to parse server response
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * A method which returns the RetroFit singleton instance of the RetroFit configuration
     * built.
     * @return Returns API Interface
     */
    public static Retrofit getRetroFit() {
        //return Retrofit
        return retrofit;
    }

    /**
     * A static class which implements Interceptor to intercept and add Authorization
     * header to the service request. If AccessTokens do no exist, service request intercepted
     * is returned in it's original format.
     */
    private static class AuthKeyInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();

            //Add access key to the headers
            Headers headers = originalRequest.headers().newBuilder()
                    .add("accessKey", WordHunch.getContext().getString(R.string.API_Key_Collins))
                    .build();

            return chain.proceed(originalRequest.newBuilder().headers(headers).build());
        }
    }
}