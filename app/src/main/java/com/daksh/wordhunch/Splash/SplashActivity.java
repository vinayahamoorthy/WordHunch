package com.daksh.wordhunch.Splash;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.daksh.wordhunch.Menu.ActivityMainMenu;
import com.daksh.wordhunch.Network.Collins.AutoComplete.DMWordList;
import com.daksh.wordhunch.Network.Collins.AutoComplete.DMWordListDao;
import com.daksh.wordhunch.Network.WordList.RFWordList;
import com.daksh.wordhunch.R;
import com.daksh.wordhunch.WordHunch;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity implements Callback<ResponseBody> {

    //The array list that stores the word list received from the internet
    private List<DMWordList> lsWordList = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        DMWordListDao wordListDao = WordHunch.getDaoSession().getDMWordListDao();
        if(wordListDao.count() <= 100) {
            //Execute a network request to download the word list from GitHub
            RFWordList.WordListInterface apiInterface = RFWordList.getWordListAPIInterface();
            Call<ResponseBody> call = apiInterface.getWordList();
            call.enqueue(this);
        } else {
            Intent intent = new ActivityMainMenu
                    .Builder(SplashActivity.this)
                    .build();
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        new TaskStoreResponse().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response.body());
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
//        If API failed, fetch item from local DB | Only if the activity requested it
//        List<DMWordList> dmSuggestions = suggestionsDao.loadAll();
//
//        if (dmSuggestions != null && !dmSuggestions.isEmpty()) {
//            String strDefinition = dmSuggestions.get(
//                    Util.getRandomNumber(Integer.valueOf(String.valueOf(dmSuggestions.size())))
//            ).getDefinition();
//            String strChallenge = Util.getRandomAlphabets(strDefinition);
//
//            Send event to whoever is listening
//            EventBus.getDefault().post(strChallenge);
//    }
    }

    private class TaskStoreResponse extends AsyncTask<ResponseBody, Void, Void> {

        @Override
        protected Void doInBackground(ResponseBody... params) {
            ResponseBody response = params[0];
            //Store to Database
            DMWordListDao wordListDao = WordHunch.getDaoSession().getDMWordListDao();
            //initialize the arrayList
            lsWordList = new ArrayList<>();
            //Convert the server response into an Input Stream
            InputStream inputStream = response.byteStream();
            //Instantiate a scanner to read the input stream line by line
            Scanner scanner = new Scanner(inputStream);
            //Iterate the scanner line by line as long as there is a next line to be read
            while (scanner.hasNextLine()) {
                //Read the next line
                String strWord = scanner.nextLine();

                lsWordList.add(new DMWordList(strWord));
                //Log the received entry
                Log.i(SplashActivity.class.getSimpleName(), strWord);
            }

            //Add the word to the list of words to be added to the database
            wordListDao.insertOrReplaceInTx(lsWordList);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent = new ActivityMainMenu
                    .Builder(SplashActivity.this)
                    .build();
            startActivity(intent);
            finish();
        }
    }
}