package com.daksh.wordhunch.Rink;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.daksh.wordhunch.R;
import com.daksh.wordhunch.Util.SettingsUtil;
import com.daksh.wordhunch.WordHunch;

public class Vibrator {

    //A static Vibrator instance to implement singleton pattern
    private static Vibrator vibrator;
    //An object of the Android OS Vibrator service
    private android.os.Vibrator androidVibrator;

    /**
     * The getInstance() method to implement singleton pattern. All calls made to this class
     * go through getInstance() to retrieve context first
     * @return
     */
    public static Vibrator getInstance() {
        if(vibrator != null)
            return vibrator;
        else {
            vibrator = new Vibrator();
            return vibrator;
        }
    }

    /**
     * Default constructor which may only be called from within the class initializes the androidVibrator
     * object and service by requesting it from the system
     */
    private Vibrator() {
        //Cannot create instance | private modifier
        androidVibrator = (android.os.Vibrator) WordHunch.getContext().getSystemService(Context.VIBRATOR_SERVICE);
    }

    /**
     * A utility method to check if the android device has a vibrator or not
     * @return returns whether android device supports vibration or not
     */
    public boolean hasVibrator() {
        return androidVibrator.hasVibrator();
    }

    /**
     * A utility method to vibrate the phone for as long as the animation runs on the two input boxes
     * on the rink activity. This method is only used to inform the user that the entry inserted
     * is not valid.
     */
    public void vibrateInvalidInput() {
        if(SettingsUtil.isVibrationEnabled()) {
            Animation shake = AnimationUtils.loadAnimation(WordHunch.getContext(), R.anim.invalidinput_shake);
            androidVibrator.vibrate(shake.getDuration());
        }
    }
}
