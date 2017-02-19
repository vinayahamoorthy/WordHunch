package com.daksh.wordhunch.Rink;

import android.os.AsyncTask;

public class RinkScore extends AsyncTask<Object, Object, Integer> {

    /**
     * The word for which score needs to be calculated
     */
    private String strWord;

    /**
     * The score update listener that is executed
     */
    private OnScoreUpdateListener onScoreUpdateListener;

    /**
     * A method that accepts the recently entered word by the user
     * @param strWord The word for which score needs to be calculated
     */
    public void setWord(String strWord) {
        this.strWord = strWord;
    }

    /**
     * A method that accepts an interface that is executed when a new word score has been
     * computed and is to be delivered to the calling activity
     * @param onScoreUpdateListener the interface object
     */
    public void setScoreListener(OnScoreUpdateListener onScoreUpdateListener) {
        this.onScoreUpdateListener = onScoreUpdateListener;
    }

    @Override
    protected Integer doInBackground(Object... voids) {
        //Convert the word into an array of characters to iterate
        char[] characters = strWord.toCharArray();

        //The score card is always initialized by a zero since the RinkScore class
        //only counts the score made in the current submission | not cumulative
        int intScoreCard = 0;

        for (char character : characters) {
            switch (character) {
                case 'a': case 'A':
                    intScoreCard = intScoreCard + ScoreCard.A.getScore();
                    break;

                case 'b': case 'B':
                    intScoreCard = intScoreCard + ScoreCard.B.getScore();
                    break;

                case 'c': case 'C':
                    intScoreCard = intScoreCard + ScoreCard.C.getScore();
                    break;

                case 'd': case 'D':
                    intScoreCard = intScoreCard + ScoreCard.D.getScore();
                    break;

                case 'e': case 'E':
                    intScoreCard = intScoreCard + ScoreCard.E.getScore();
                    break;

                case 'f': case 'F':
                    intScoreCard = intScoreCard + ScoreCard.F.getScore();
                    break;

                case 'g': case 'G':
                    intScoreCard = intScoreCard + ScoreCard.G.getScore();
                    break;

                case 'h': case 'H':
                    intScoreCard = intScoreCard + ScoreCard.H.getScore();
                    break;

                case 'i': case 'I':
                    intScoreCard = intScoreCard + ScoreCard.I.getScore();
                    break;

                case 'j': case 'J':
                    intScoreCard = intScoreCard + ScoreCard.J.getScore();
                    break;

                case 'k': case 'K':
                    intScoreCard = intScoreCard + ScoreCard.K.getScore();
                    break;

                case 'l': case 'L':
                    intScoreCard = intScoreCard + ScoreCard.L.getScore();
                    break;

                case 'm': case 'M':
                    intScoreCard = intScoreCard + ScoreCard.M.getScore();
                    break;

                case 'n': case 'N':
                    intScoreCard = intScoreCard + ScoreCard.N.getScore();
                    break;

                case 'o': case 'O':
                    intScoreCard = intScoreCard + ScoreCard.O.getScore();
                    break;

                case 'p': case 'P':
                    intScoreCard = intScoreCard + ScoreCard.P.getScore();
                    break;

                case 'q': case 'Q':
                    intScoreCard = intScoreCard + ScoreCard.Q.getScore();
                    break;

                case 'r': case 'R':
                    intScoreCard = intScoreCard + ScoreCard.R.getScore();
                    break;

                case 's': case 'S':
                    intScoreCard = intScoreCard + ScoreCard.S.getScore();
                    break;

                case 't': case 'T':
                    intScoreCard = intScoreCard + ScoreCard.T.getScore();
                    break;

                case 'u': case 'U':
                    intScoreCard = intScoreCard + ScoreCard.U.getScore();
                    break;

                case 'v': case 'V':
                    intScoreCard = intScoreCard + ScoreCard.V.getScore();
                    break;

                case 'w': case 'W':
                    intScoreCard = intScoreCard + ScoreCard.W.getScore();
                    break;

                case 'x': case 'X':
                    intScoreCard = intScoreCard + ScoreCard.X.getScore();
                    break;

                case 'y': case 'Y':
                    intScoreCard = intScoreCard + ScoreCard.Y.getScore();
                    break;

                case 'z': case 'Z':
                    intScoreCard = intScoreCard + ScoreCard.Z.getScore();
                    break;
            }
        }
        return intScoreCard;
    }

    @Override
    protected void onPostExecute(Integer anInteger) {
        super.onPostExecute(anInteger);

        if(onScoreUpdateListener != null)
            onScoreUpdateListener.onScoreUpdated(anInteger);
    }
}