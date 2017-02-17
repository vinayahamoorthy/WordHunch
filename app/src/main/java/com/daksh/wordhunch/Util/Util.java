package com.daksh.wordhunch.Util;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Util {

    /**
     * The YYYY MM DD format
     */
    public static final String YYYYMMDD = "yyyy-MM-dd";

    /**
     * A method to return a random alphabet using the math.Random function.
     * The Math.Random returns a double between 0.0 and 0.9. It is multiplied by 26 to set
     * max limit above code 34 keeping it as a minimum.
     * @return Returns a character which is depicted by the unicode returned by the math.random method
     */
    @NonNull
    public static Character getRandomAlphabet() {
        Integer integer = (int) (Math.random() * 26) + 34;
        return (char) ('B' + integer);
    }

    /**
     * A method that returns a random number from the range 0 - intEnd (The upper limit).
     * @param intEnd The upper limit to set | so the range from which numbers may be returned
     *               is 0 - intEnd
     * @return
     */
    @NonNull
    public static int getRandomNumber(@NonNull Integer intEnd) {
        return (int) (Math.random() * intEnd);
    }

    /**
     * Methods accepts a sentence as a parameter, splits up the words and returns two alphabets
     * from a word which form the chosen word
     * @param strSentence The sentence from which the alphabets will be extracted
     * @return Returns a string of two alphabets to be setup as a challenge for the user
     */
    @NonNull
    public static String getRandomAlphabets(@NonNull String strSentence) {
        if(!TextUtils.isEmpty(strSentence)) {
            String[] words = strSentence.split(" ");
            int intRandomWordLocation = getRandomNumber(words.length);
            String strRandomWord = words[intRandomWordLocation];
            return String.valueOf(strRandomWord.charAt(0)) + String.valueOf(strRandomWord.charAt(1));
        } else
            return strSentence;
    }

    /**
     * A method to return today's date in the format that was passed on to method.
     * Permissible formats may be from the ones defined above
     * @param strDateFormat The date format in which today's date is requested
     * @return Today's date in string
     */
    @NonNull
    public static String getTodaysDate(@NonNull String strDateFormat) {
        if(isDateFormat(strDateFormat)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(strDateFormat, Locale.getDefault());
            Calendar calendar = Calendar.getInstance();
            return simpleDateFormat.format(calendar.getTime());
        } else
            throw new RuntimeException();
    }

    /**
     * A method to verify if the provided date format is permissible or not
     * @param strDateFormat Date format to be tested
     * @return Returns  boolean value that tells if the passed date format was chosen
     * from the ones defined on the top of this file or not
     */
    private static boolean isDateFormat(@NonNull String strDateFormat) {
        switch (strDateFormat) {

            case YYYYMMDD:
                return true;

            default:
                return false;
        }
    }
}