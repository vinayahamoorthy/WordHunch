package com.daksh.wordhunch.Network.AutoComplete;

import android.os.AsyncTask;

import com.daksh.wordhunch.WordHunch;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.util.List;

/**
 * The purge scheduler is executed in the background once a day, everyday. It iterates through the
 * list of words saved in the suggestions table and iterates over each word in the definition.
 * If any word is or has less than 2 characters, it is purged since WordHunch does not use
 * words with less than or equal to 2 characters.
 */
public class PurgeScheduler extends JobService {

    @Override
    public boolean onStartJob(JobParameters job) {
        //Create and execute an AsyncTask to carry out purge operations in the background
        new TaskPurge().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        //Return true if there is work remaining in the scheduler | Keep thread alive
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {

        //Return false if the Job needs to be retried.
        return false;
    }

    private class TaskPurge extends AsyncTask<Void, Void, Void>  {

        @Override
        protected Void doInBackground(Void... params) {
            //Get access to the table DAO
            DMSuggestionsDao suggestionsDao = WordHunch.getDaoSession().getDMSuggestionsDao();

            //Access only if the count is greater than zero | no point in proceeding if the table is
            //empty
            if(suggestionsDao.count() > 0) {
                //Load all rows
                List<DMSuggestions> list = suggestionsDao.loadAll();

                //Create a new string builder with default char limit
                StringBuilder stringBuilder = new StringBuilder();

                //iterate over each row
                for(DMSuggestions suggestion : list) {
                    //split each definition into an array of words
                    String[] definitions = suggestion.getDefinition().split(" ");
                    //iterate over each word of the definition
                    for(String strDefinition : definitions) {

                        //It is mandatory for the word to be longer than 2 characters.
                        if(strDefinition.toCharArray().length > 2) {

                            //Add the definition to the stringBuilder
                            stringBuilder.append(strDefinition);

                            //Append an empty space at the end so as to form a sentence
                            stringBuilder.append(" ");
                        }
                    }

                    //Replace existing definition with the newly minted one
                    suggestion.setDefinition(stringBuilder.toString());
                    //update in DB
                    suggestionsDao.insertOrReplace(suggestion);
                }
            }
            return null;
        }
    }
}