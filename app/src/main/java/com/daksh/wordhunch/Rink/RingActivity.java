package com.daksh.wordhunch.Rink;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.textservice.SentenceSuggestionsInfo;
import android.view.textservice.SpellCheckerSession;
import android.view.textservice.SuggestionsInfo;
import android.view.textservice.TextInfo;
import android.view.textservice.TextServicesManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daksh.wordhunch.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.github.krtkush.lineartimer.LinearTimer;
import io.github.krtkush.lineartimer.LinearTimerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RingActivity extends AppCompatActivity implements
        Callback<List<String>>, TextView.OnEditorActionListener,
        SpellCheckerSession.SpellCheckerSessionListener, TextWatcher, LinearTimer.TimerListener {

    //The field where the user inputs the words
    private EditText etUserInput;
    //Field which holds the first two alphabets for the user to build the word on
    private TextView txWord;
    //The RecyclerView which holds the words inserted by the user
    private RecyclerView rvUserInputs;
    //The adapter which builds the word list to be displayed on the RecyclerView
    private SuggestionsAdapter adapter;
    //A list of suggested words received from auto correct against which user inputs are matched
    private List<String> lsSuggestions = new ArrayList<>();
    //A spell checker session used to retrieve plausible words the user may enter and accepted
    private SpellCheckerSession spellCheckerSession;
    //A circular progress bar to keep track of time
    private LinearTimer linearTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring);
        //Bind views
        etUserInput = (EditText) findViewById(R.id.rinkInput);
        txWord = (TextView) findViewById(R.id.rinkWord);
        rvUserInputs = (RecyclerView) findViewById(R.id.rinkInputList);

        //Build the timer and start
        linearTimer = new LinearTimer.Builder()
                //Set the duration for the timer | 60 seconds into Millis
                .duration(60 * 1000)
                //Set the callback listeners
                .timerListener(RingActivity.this)
                //Set the CountDown / CountUp type
                .getCountUpdate(LinearTimer.COUNT_DOWN_TIMER, 1000)
                .build();
        linearTimer.startTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Force open the keyboard
        if(etUserInput != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInputFromInputMethod(etUserInput.getWindowToken(), InputMethodManager.SHOW_FORCED);

            //Set up word listener on the user input field
            etUserInput.setOnEditorActionListener(RingActivity.this);
            etUserInput.addTextChangedListener(RingActivity.this);

            etUserInput.setFilters(new InputFilter[] { new InputFilter.AllCaps()});
        }

        //Assign adapter to the recyclerView
        adapter = new SuggestionsAdapter();
        if(rvUserInputs != null)
            rvUserInputs.setAdapter(adapter);

        //Instantiate the spell checker
        TextServicesManager servicesManager = (TextServicesManager) getSystemService(Context.TEXT_SERVICES_MANAGER_SERVICE);
        spellCheckerSession = servicesManager.newSpellCheckerSession(null, Locale.UK, RingActivity.this, false);

//        //Get word list
//        RFAutocomplete.SuggestionsAPIInterface apiInterface = RFAutocomplete.getSuggestionsAPIInterface();
//        Call<List<String>> listCall = apiInterface.getSuggestions("PI");
//        listCall.enqueue(RingActivity.this);
    }

    @Override
    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
        if(response.isSuccessful())
            if(response.body() != null && !response.body().isEmpty())
                lsSuggestions = response.body();

        adapter.setItems(lsSuggestions);
    }

    @Override
    public void onFailure(Call<List<String>> call, Throwable t) {

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO) {
            String strWord = trimString(String.valueOf(txWord.getText()) + String.valueOf(etUserInput.getText()));
            if(lsSuggestions != null) {
                for(String strSuggestion : lsSuggestions)
                    if(strWord.equalsIgnoreCase(strSuggestion.trim())) {
                        //Add items to the adapter
                        adapter.addItem(strWord);

                        //Clear the user input field
                        etUserInput.setText("");

                        //Scroll the list down so user can see the latest entry
                        rvUserInputs.scrollToPosition(adapter.getItemCount());

                        return true;
                    }

                //Display error to user
                Toast.makeText(RingActivity.this, "Not a word", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(RingActivity.this, "Not a word", Toast.LENGTH_SHORT).show();

            return true;
        } else
            return false;
    }

    @Override
    public void onGetSuggestions(SuggestionsInfo[] results) {

    }

    @Override
    public void onGetSentenceSuggestions(SentenceSuggestionsInfo[] results) {
        for(SentenceSuggestionsInfo result : results) {
            int n = result.getSuggestionsCount();
            for(int i = 0 ; i < n ; i++){
                int m = result.getSuggestionsInfoAt(i).getSuggestionsCount();
                for(int k = 0 ; k < m ; k++) {
                    String strSuggestion = trimString(result.getSuggestionsInfoAt(i).getSuggestionAt(k));

                    // the word goes on the list only if it starts with the two letters given
                    //or if there are no spaces
                    if(strSuggestion.startsWith(trimString(String.valueOf(txWord.getText())))
                            || !strSuggestion.contains(" ")
                            || !lsSuggestions.contains(strSuggestion))
                        lsSuggestions.add(strSuggestion);
                }
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

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

    }

    /**
     * Trims the passed string and ensures the characters that make up the String are only
     * alphabetic. Specifically used to ensure no special characters or spaces are concatenated with
     * the string
     * @param aString The string to be trimmed down
     * @return A trimmed string which is only as long as the number of characters visible
     */
    private String trimString(String aString) {
        //Convert string to char array
        char[] characters = aString.toCharArray();
        //Create a string buffer to start appending chars | Size passed as 1 will grow as requireds
        StringBuilder stringBuffer = new StringBuilder(1);
        //Iterate the characters to check if all of them are alphabetic. Alphabetic characters are
        //appended to the string buffer and others are discarded
        for(char character : characters)
            if(Character.isAlphabetic(character))
                stringBuffer.append(character);

        //Return the new string retrieved
        return stringBuffer.toString();
    }

    @Override
    public void animationComplete() {

    }

    @Override
    public void timerTick(long l) {

    }
}
