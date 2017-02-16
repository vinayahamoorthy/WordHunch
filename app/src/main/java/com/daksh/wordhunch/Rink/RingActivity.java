package com.daksh.wordhunch.Rink;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daksh.wordhunch.Network.RFAutocomplete;
import com.daksh.wordhunch.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RingActivity extends AppCompatActivity implements Callback<List<String>>, TextView.OnEditorActionListener {

    private EditText etUserInput;
    private TextView txWord;
    private RecyclerView rvUserInputs;
    private SuggestionsAdapter adapter;
    private List<String> lsSuggestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring);
        //Bind views
        etUserInput = (EditText) findViewById(R.id.rinkInput);
        txWord = (TextView) findViewById(R.id.rinkWord);
        rvUserInputs = (RecyclerView) findViewById(R.id.rinkInputList);
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

            etUserInput.setFilters(new InputFilter[] { new InputFilter.AllCaps()});
        }

        //Assign adapter to the recyclerView
        adapter = new SuggestionsAdapter();
        if(rvUserInputs != null)
            rvUserInputs.setAdapter(adapter);

        //Get word list
        RFAutocomplete.SuggestionsAPIInterface apiInterface = RFAutocomplete.getSuggestionsAPIInterface();
        Call<List<String>> listCall = apiInterface.getSuggestions("PI");
        listCall.enqueue(RingActivity.this);
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
            String strWord = txWord.getText().toString().toLowerCase() + String.valueOf(etUserInput.getText()).toLowerCase();
            if(lsSuggestions != null && lsSuggestions.contains(strWord)) {
                //Add items to the adapter
                adapter.addItem(strWord);

                //Clear the user input field
                etUserInput.setText("");

                rvUserInputs.scrollToPosition();
            } else
                Toast.makeText(RingActivity.this, "Not a word", Toast.LENGTH_SHORT).show();

            return true;
        } else
            return false;
    }
}
