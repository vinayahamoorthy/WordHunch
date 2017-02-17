package com.daksh.wordhunch.Network.AutoComplete;

/**
 * The interface is passed as a callback Listener to RinkSuggestions class file. It's methods
 * are executed when RinkSuggestions activity file receives the word of the day and extracts a
 * word challenge for the user
 */
public interface OnSuggestionCompleteListener {

    /**
     * The method is executed when RinkSuggestion class receives the suggestion from collins dictionary
     * and a challenge has been formed.
     * @param strChallenge The challenge string which is presented to the user to make words
     */
    void onChallengeReceived(String strChallenge);
}
