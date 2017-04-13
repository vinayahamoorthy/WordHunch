package com.daksh.wordhunch.Rink;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.textservice.SpellCheckerSession;
import android.view.textservice.TextInfo;
import android.view.textservice.TextServicesManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daksh.wordhunch.Network.Collins.AutoComplete.DMWordList;
import com.daksh.wordhunch.Network.Collins.AutoComplete.DMWordListDao;
import com.daksh.wordhunch.R;
import com.daksh.wordhunch.Rink.Events.RequestChallengeEvent;
import com.daksh.wordhunch.Rink.Sounds.SoundManager;
import com.daksh.wordhunch.Util.DialogClass;
import com.daksh.wordhunch.Util.Util;
import com.daksh.wordhunch.WordHunch;
import com.google.android.gms.games.Games;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.krtkush.lineartimer.LinearTimer;
import io.github.krtkush.lineartimer.LinearTimerStates;
import io.github.krtkush.lineartimer.LinearTimerView;

public class RingActivity extends AppCompatActivity implements
        TextView.OnEditorActionListener,
        LinearTimer.TimerListener, OnScoreUpdateListener {

    //A string param used to send a boolean value to this activity that
    public static String PARAM_EXTRA_ISFIRST = "PARAM_EXTRA_ISFIRST";

    //The field where the user inputs the words
    @BindView(R.id.rinkInput) EditText etUserInput;
    //Field which holds the first two alphabets for the user to build the word on
    @BindView(R.id.rinkWord) TextView txWord;
    //The textview that shows the time left
    @BindView(R.id.countdownText) TextView countdownText;
    //A cover view that displays a replay button when the timer expires
    @BindView(R.id.cover) RelativeLayout rlVeil;
    //The RecyclerView which holds the words inserted by the user
    @BindView(R.id.rinkInputList) RecyclerView rvUserInputs;
    //The TextView that holds the score
    @BindView(R.id.rinkScore) TextView txRinkScore;
    //A circular progress bar to keep track of time
    @BindView(R.id.countdown) LinearTimerView linearTimerView;
    //A retry button displayed on the cover
//    @BindView(R.id.retry) ImageView retry;
    //The adapter which builds the word list to be displayed on the RecyclerView
    private SuggestionsAdapter adapter;
    //A list of suggested words received from auto correct against which user inputs are matched
//    private List<String> lsSuggestions = new ArrayList<>();
    //A linearTimer object which handles the configuration of the LinearTimerView
    private LinearTimer linearTimer;
    //The current score of the user at any given point
    private long intCurrentScore;
    //The new score won by a new word
    private long intNewScore;
    //An object of ScoreIncrease | child of the sound manager that is used to play a sound
    //when the score increases
    private SoundManager soundManager;
    //The WordList DAO handler which is used to communicate with the word list table
    private DMWordListDao wordListDao;

    /**
     * A tap listener on the replay button. Resets the game
     */
    @OnClick(R.id.retry)
    public void onRetryClick(View view) {
        //Set countdown timer to 0
        if(linearTimer.getState() == LinearTimerStates.FINISHED)
            linearTimer.resetTimer();
        //Clear the RecyclerView
        adapter.clearItems();
        //Enable editing on the input field and the word challenge
        etUserInput.setEnabled(true);
        txWord.setEnabled(true);
        //Ensure the input area is clear when starting a new game
        etUserInput.setText("");

        //Setup a new challenge
        DialogClass.showBirdDialog(RingActivity.this);
        EventBus.getDefault().post(new RequestChallengeEvent());

        //Get focus and open keyboard
        etUserInput.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInputFromInputMethod(etUserInput.getWindowToken(), InputMethodManager.SHOW_FORCED);

        //Fade away the veil
        rlVeil.setVisibility(View.GONE);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void challengeRequest(RequestChallengeEvent event) {
        //Get the WordListDao
        DMWordListDao wordListDao = WordHunch.getDaoSession().getDMWordListDao();
        //Get the number of items in the database and use it to find a random number
        Long lngRandomNumber = Util.getRandomNumber(wordListDao.count());
        DMWordList dmWordList = wordListDao.load(lngRandomNumber);
        String strChallenge = Util.getRandomAlphabets(dmWordList.getWord());
        //Test if the challenge returned matches the criteria set by getRandomAlphabets method or not
        //if not, null is returned and the DB is requested for a new word
        if (!TextUtils.isEmpty(strChallenge)){
            if (EventBus.getDefault().hasSubscriberForEvent(String.class))
                EventBus.getDefault().post(strChallenge);
        } else
            challengeRequest(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring);
        ButterKnife.bind(this);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        //Build the timer and start
        linearTimer = new LinearTimer.Builder()
                //Pass the view
                .linearTimerView(linearTimerView)
                //Set the duration for the timer | 90 seconds into Millis
                .duration(TimeUnit.SECONDS.toMillis(90))
                //Set the callback listeners
                .timerListener(RingActivity.this)
                //Set the CountDown / CountUp type
                .getCountUpdate(LinearTimer.COUNT_DOWN_TIMER, 1000)
                //Set the timer progression direction
                .progressDirection(LinearTimer.COUNTER_CLOCK_WISE_PROGRESSION)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Register this class to accept Events from the event bus
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Load DAO
        wordListDao = WordHunch.getDaoSession().getDMWordListDao();

        if (soundManager == null || soundManager.getSoundPool() == null)
            //initialize the sound manager via child classes that will be used to play sound files overtime
            soundManager = new SoundManager.Builder()
                    .setupScoreSounds()
                    .setupIncorrectEntry()
                    .setupTimerSounds()
                    .build();

        if (linearTimer.getState() != LinearTimerStates.ACTIVE) {

            //Set Challenge | Post an event to fetch suggestions
            DialogClass.showBirdDialog(RingActivity.this);
            EventBus.getDefault().post(new RequestChallengeEvent());

            //Force open the keyboard
            if (etUserInput != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInputFromInputMethod(etUserInput.getWindowToken(), InputMethodManager.SHOW_FORCED);

                //Set up word listener on the user input field
                etUserInput.setOnEditorActionListener(RingActivity.this);
                etUserInput.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            }

            //Assign adapter to the recyclerView
            adapter = new SuggestionsAdapter();
            if (rvUserInputs != null)
                rvUserInputs.setAdapter(adapter);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Release all sound resources with SoundPool
        if(soundManager != null)
            soundManager.release();

        //Unregister this class from the EventBus
        if(EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);

        //Destroy the LinearTimer
        linearTimer.pauseTimer();
        linearTimer.resetTimer();
    }

    /**
     * A utility method to inform user that the input string is not an acceptable type.
     * The method toggles the color of the input field, makes a sound and does a shake animation
     */
    private void rejectUserInput() {

        //Display error to user and play sound
        soundManager.getUserInputs().playRejectSound();

        //Retrieve animation and start
        Animation shake = AnimationUtils.loadAnimation(RingActivity.this, R.anim.invalidinput_shake);
        txWord.startAnimation(shake);
        etUserInput.startAnimation(shake);

        //Change input text color to Red to highlight an invalid input attempt
        setInputTextColor(R.color.redTextColorOne);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if(Vibrator.getInstance().hasVibrator())
                    Vibrator.getInstance().vibrateInvalidInput();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //Once the animation ends, change it back to it's orignal state
                setInputTextColor(R.color.blackTextColorOne);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //Empty Stub
            }
        });
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO) {
            if(!TextUtils.isEmpty(etUserInput.getText())) {
                String strWord = Util.trimString(String.valueOf(txWord.getText()) + String.valueOf(etUserInput.getText()));
                if (adapter.getItems() == null || (adapter.getItems() != null && !adapter.getItems().contains(strWord))) {
                    DMWordList word = wordListDao.queryBuilder()
                            .where(DMWordListDao.Properties.Word.eq(strWord.toLowerCase()))
                            .unique();
                    if (word != null && !TextUtils.isEmpty(word.getWord()) && strWord.equalsIgnoreCase(word.getWord())) {
                        //Compute score on a different thread and add to score
                        RinkScore rinkScore = new RinkScore();
                        rinkScore.setWord(strWord);
                        rinkScore.setScoreListener(RingActivity.this);
                        rinkScore.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                        //Add items to the adapter
                        adapter.addItem(strWord);

                        //Clear the user input field
                        etUserInput.setText("");

                        //Scroll the list down so user can see the latest entry
                        rvUserInputs.smoothScrollToPosition(adapter.getItemCount() - 1);

                        return true;
                    } else
                        rejectUserInput();
                } else
                    rejectUserInput();
            } else
                rejectUserInput();
            return true;
        } else
            return false;
    }

    /**
     * A helper method to toggle colors of input fields at the bottom of this activity.
     * @param intResid The resource ID of the color which the text fields are to be changed to
     */
    private void setInputTextColor(int intResid) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            txWord.setTextColor(getResources().getColor(intResid, getTheme()));
            etUserInput.setTextColor(getResources().getColor(intResid, getTheme()));
        } else {
            txWord.setTextColor(getResources().getColor(intResid));
            etUserInput.setTextColor(getResources().getColor(intResid));
        }
    }

    @Override
    public void animationComplete() {
        //Set countdown timer to 0
        countdownText.setText(String.valueOf(0));

        //Disable editing on the input field
        etUserInput.setEnabled(false);
        txWord.setEnabled(false);

        rlVeil.setVisibility(View.VISIBLE);
        //Submit score to Single Round High Score leader board
        Games.Leaderboards.submitScore(
                WordHunch.getGoogleApiClient(),
                getString(R.string.play_leaderboard_SingleRoundHighScore),
                intNewScore);

        //Extract the isFirst Timer boolean in the intent to specify if a round was completed for the
        //first time. Based on value, the FirstBlood achievement is unlocked
        boolean isFirstTimer = getIntent().getBooleanExtra(
                PARAM_EXTRA_ISFIRST,
                true
        );
        if(isFirstTimer)
            Games.Achievements.unlock(
                    WordHunch.getGoogleApiClient(),
                    getString(R.string.play_achievement_FirstBlood)
            );

        startActivityForResult(
                Games.Leaderboards.getLeaderboardIntent(
                        WordHunch.getGoogleApiClient(),
                        getString(R.string.play_leaderboard_SingleRoundHighScore)
                ),
                1);
    }

    @Override
    public void timerTick(long l) {
        //Set the elapsed time on the timer TextView
        countdownText.setText(String.valueOf(l / 1000));

        //Start playing the Timer End sound file if time is less than or equal to 5 seconds
        if(TimeUnit.MILLISECONDS.toSeconds(l) == 5)
            if(soundManager != null && soundManager.getSoundPool() != null)
                soundManager.getTimerSounds().playSound();
    }

    @Override
    public void onTimerReset() {
        //Empty Stub
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChallengeReceived(String strChallenge) {
        DialogClass.dismissBirdDialog(RingActivity.this);
        txWord.setText(strChallenge);
        //Start the timer
        if(linearTimer.getState() == LinearTimerStates.INITIALIZED)
            linearTimer.startTimer();
    }

    @Override
    public void onScoreUpdated(Integer intWordScore) {
        //Save the current score
        intCurrentScore = Integer.parseInt(String.valueOf(txRinkScore.getText()));
        //Calculate the new total score
        this.intNewScore = intWordScore + intCurrentScore;
        //Play voice to imitate climbing score
        soundManager.getScoreSounds().playSound();
        //Initiate a timer to score up the current score on the top right
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
                                  @Override
                                  public void run() {
                                      //Views may only be updated on UI threads, run runnable on UI thread
                                      runOnUiThread(new Runnable() {
                                          @Override
                                          public void run() {
                                              //If the current score is less than the new score, only then add one and update
                                              if(intCurrentScore < intNewScore)
                                                  txRinkScore.setText(String.valueOf(++intCurrentScore));
                                              else {
                                                  //A method to stop the streaming if the score stops increasing
                                                  soundManager.getScoreSounds().stopStream();
                                                  //Once timer has run its course, cancel and exit
                                                  cancel();
                                              }
                                          }
                                      });
                                  }
                              },
                0, //Execute timerTask immediately without delay
                20); //Recursively execute timerTask at an interim period of 20 millis
    }

    //RingActivity is used in associated with a builder pattern. This is done so as to ensure all
    //prerequisite parameters are sent to the activity that are required to ensure its proper
    //functioning. This ensures even when someone else than the primary developer works on this activity.
    //s/he will not have to go through layers of code to find which params are required
    //to instantiate this module.
    public static class Builder {

        //A context to the instantiating activity
        private Context context;
        //A boolean value that determines if the game is being played for the first time or not
        private Boolean isFirstTime;

        private Builder() {
            //Empty private constructor to disable instantiation
        }

        /**
         * The constructor to be called to instantiate the class.
         * @param context The context of the calling activity is required to manufacture the intent
         */
        public Builder(Context context) {
            this.context = context;
        }

        /**
         * Sets if the game is being played for the first time or not
         * @param isFirstTime A boolean value that determines if the game is being played for the first
         *                    time or not. Based on this value the FirstBlood(R.string.play_achievement_FirstBlood)
         *                    achievement may be unlocked or not.
         * @return returns an instance of this class
         */
        public Builder setFirstTime(Boolean isFirstTime) {
            this.isFirstTime = isFirstTime;
            return this;
        }

        /**
         * The build method executes and generates an intent. Returns the intent that may be used with
         * startActivity or startActivityForResult.
         *
         * @return Intent that may be used to start the RingActivity.
         */
        public Intent build() {
            Intent intent = new Intent(context, RingActivity.class);
            intent.putExtra(PARAM_EXTRA_ISFIRST, isFirstTime);
            return intent;
        }
    }
}