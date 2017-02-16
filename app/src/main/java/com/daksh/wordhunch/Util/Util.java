package com.daksh.wordhunch.Util;

import android.support.annotation.NonNull;

public class Util {

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
}