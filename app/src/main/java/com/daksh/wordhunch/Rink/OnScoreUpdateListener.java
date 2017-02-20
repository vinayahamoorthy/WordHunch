package com.daksh.wordhunch.Rink;

/**
 * A score update listener Passed on to the RinkScore class. The scoreUpdated method is called
 * when RinkScore activity finishes computing the score scored by the new word added to the list
 */
public interface OnScoreUpdateListener  {

    /**
     * The score updated method is executed when the score computing is done. The integer passed
     * on as parameter has the new word score only and not cumulative.
     * @param intWordScore The score achieved on the new word
     */
    void onScoreUpdated(Integer intWordScore);
}
