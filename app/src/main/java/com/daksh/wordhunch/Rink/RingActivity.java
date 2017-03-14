package com.daksh.wordhunch.Rink;

import android.content.Context;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.textservice.SentenceSuggestionsInfo;
import android.view.textservice.SpellCheckerSession;
import android.view.textservice.SuggestionsInfo;
import android.view.textservice.TextInfo;
import android.view.textservice.TextServicesManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daksh.wordhunch.R;
import com.daksh.wordhunch.Rink.Events.RequestChallengeEvent;
import com.daksh.wordhunch.Rink.Sounds.SoundManager;
import com.daksh.wordhunch.Util.DialogClass;
import com.daksh.wordhunch.Util.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.github.krtkush.lineartimer.LinearTimer;
import io.github.krtkush.lineartimer.LinearTimerStates;
import io.github.krtkush.lineartimer.LinearTimerView;

public class RingActivity extends AppCompatActivity implements
        TextView.OnEditorActionListener,
        SpellCheckerSession.SpellCheckerSessionListener, TextWatcher,
        LinearTimer.TimerListener, OnScoreUpdateListener {

    //The field where the user inputs the words
    private EditText etUserInput;
    //Field which holds the first two alphabets for the user to build the word on
    private TextView txWord;
    //A cover view that displays a replay button when the timer expires
    private RelativeLayout rlViel;
    //The RecyclerView which holds the words inserted by the user
    private RecyclerView rvUserInputs;
    //The adapter which builds the word list to be displayed on the RecyclerView
    private SuggestionsAdapter adapter;
    //A list of suggested words received from auto correct against which user inputs are matched
    private List<String> lsSuggestions = new ArrayList<>();
    //A spell checker session used to retrieve plausible words the user may enter and accepted
    private SpellCheckerSession spellCheckerSession;
    //A linearTimer object which handles the configuration of the LinearTimerView
    private LinearTimer linearTimer;
    //The textview that shows the time left
    private TextView countdownText;
    //The TextView that holds the score
    private TextView txRinkScore;
    //The current score of the user at any given point
    private int intCurrentScore;
    //The new score won by a new word
    private int intNewScore;
    //An object of ScoreIncrease | child of the sound manager that is used to play a sound
    //when the score increases
    private SoundManager soundManager;
    //An instance of RinkSuggestions class to retrieve suggestions
    private RinkSuggestions rinkSuggestions;

    /**
     * A tap listener on the replay button. Resets the game
     */
    private View.OnClickListener replayListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
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
            EventBus.getDefault().post(new RequestChallengeEvent(RequestChallengeEvent.RequestMode.CHALLENGE_ALL));

            //Get focus and open keyboard
            etUserInput.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInputFromInputMethod(etUserInput.getWindowToken(), InputMethodManager.SHOW_FORCED);

            //Fade away the veil
            rlViel.setVisibility(View.GONE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        //Bind views
        etUserInput = (EditText) findViewById(R.id.rinkInput);
        txWord = (TextView) findViewById(R.id.rinkWord);
        rvUserInputs = (RecyclerView) findViewById(R.id.rinkInputList);
        countdownText = (TextView) findViewById(R.id.countdownText);
        txRinkScore = (TextView) findViewById(R.id.rinkScore);
        //A circular progress bar to keep track of time
        LinearTimerView timerView = (LinearTimerView) findViewById(R.id.countdown);
        rlViel = (RelativeLayout) findViewById(R.id.cover);

        //A retry button displayed on the cover
        ImageView retry = (ImageView) findViewById(R.id.retry);
        retry.setOnClickListener(replayListener);

        //instantiate the rink suggestions class to register Event listeners on the class
        rinkSuggestions = new RinkSuggestions();

        //Build the timer and start
        linearTimer = new LinearTimer.Builder()
                //Pass the view
                .linearTimerView(timerView)
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
        //Register receiver in the suggestions class
        rinkSuggestions.registerSubscriber();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(soundManager == null || soundManager.getSoundPool() == null)
            //initialize the sound manager via child classes that will be used to play sound files overtime
            soundManager = new SoundManager.Builder()
                    .setupScoreSounds()
                    .setupIncorrectEntry()
                    .build();

        if(linearTimer.getState() != LinearTimerStates.ACTIVE) {

            //Set Challenge | Post an event to fetch suggestions
            DialogClass.showBirdDialog(RingActivity.this);
            EventBus.getDefault().post(new RequestChallengeEvent(RequestChallengeEvent.RequestMode.CHALLENGE_ALL));

            //Force open the keyboard
            if (etUserInput != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInputFromInputMethod(etUserInput.getWindowToken(), InputMethodManager.SHOW_FORCED);

                //Set up word listener on the user input field
                etUserInput.setOnEditorActionListener(RingActivity.this);
                etUserInput.addTextChangedListener(RingActivity.this);

                etUserInput.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            }

            //Assign adapter to the recyclerView
            adapter = new SuggestionsAdapter();
            if (rvUserInputs != null)
                rvUserInputs.setAdapter(adapter);

            //Instantiate the spell checker
            TextServicesManager servicesManager = (TextServicesManager) getSystemService(Context.TEXT_SERVICES_MANAGER_SERVICE);
            spellCheckerSession = servicesManager.newSpellCheckerSession(null, Locale.UK, RingActivity.this, false);
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

        //Unregisters suggestions class from the subscriptions
        rinkSuggestions.unregisterSubscribe();
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
            String strWord = Util.trimString(String.valueOf(txWord.getText()) + String.valueOf(etUserInput.getText()));
            if(!TextUtils.isEmpty(etUserInput.getText()) && lsSuggestions != null)
                if(adapter.getItems() == null || (adapter.getItems() != null && !adapter.getItems().contains(strWord))) {
                    for(String strSuggestion : lsSuggestions)
                        if(strWord.equalsIgnoreCase(strSuggestion.trim())) {

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
                        }

                    rejectUserInput();
                } else
                    rejectUserInput();
            else
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
    public void onGetSuggestions(SuggestionsInfo[] results) {
        //Empty Stub
    }

    @Override
    public void onGetSentenceSuggestions(SentenceSuggestionsInfo[] results) {
        if(results != null)
            for(SentenceSuggestionsInfo result : results) {
                if(result != null) {
                    int n = result.getSuggestionsCount();
                    for (int i = 0; i < n; i++) {
                        int m = result.getSuggestionsInfoAt(i).getSuggestionsCount();
                        for (int k = 0; k < m; k++) {
                            String strSuggestion = Util.trimString(result.getSuggestionsInfoAt(i).getSuggestionAt(k));

                            // the word goes on the list only if it starts with the two letters given
                            //or if there are no spaces
                            if (strSuggestion.startsWith(Util.trimString(String.valueOf(txWord.getText())))
                                    || !strSuggestion.contains(" ")
                                    || !lsSuggestions.contains(strSuggestion))
                                lsSuggestions.add(strSuggestion);
                        }
                    }
                }
            }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //Empty Stub
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(spellCheckerSession != null)
            spellCheckerSession.getSentenceSuggestions(
                    new TextInfo[]{
                            new TextInfo(txWord.getText() + String.valueOf(s).toLowerCase())
                    },
                    10
            );
    }

    @Override
    public void afterTextChanged(Editable s) {
        //Empty Stub
    }

    @Override
    public void animationComplete() {
        //Set countdown timer to 0
        countdownText.setText(String.valueOf(0));

        //Disable editing on the input field
        etUserInput.setEnabled(false);
        txWord.setEnabled(false);

        rlViel.setVisibility(View.VISIBLE);
    }

    @Override
    public void timerTick(long l) {
        countdownText.setText(String.valueOf(l / 1000));
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
}