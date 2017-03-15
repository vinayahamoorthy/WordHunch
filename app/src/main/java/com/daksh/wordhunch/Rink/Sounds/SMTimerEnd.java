package com.daksh.wordhunch.Rink.Sounds;

import android.media.SoundPool;

import com.daksh.wordhunch.R;
import com.daksh.wordhunch.WordHunch;

public class SMTimerEnd {

    /**
     * the sound ID of the raw file se_timer_end which is returned by the soundPool.load() method.
     * The SoundID is issued at class initialization and may be used later to play or unload the
     * sound from memory
     */
    private int soundID;

    /**
     * The stream ID of the timer ending sound | This stream ID is used to control playback
     * when the file is being played
     */
    private int streamID;

    /**
     * The soundpool object is received from the SoundManager when SM classes are initialized one by one
     * depending on Scope passed on to SoundManager during it's initialization.
     */
    private SoundPool soundPool;

    /**
     * The default constructor used to load the sound file for an increasing counter.
     * it stores a the sound ID in the soundID object which is used to play the the sound using .play()
     * method.
     * @param soundPool A soundPool object used to load raw music files and extract a SoundID to be
     *                  used later to perform functions on the sound
     */
    SMTimerEnd(SoundPool soundPool) {
        this.soundPool = soundPool;
        soundID = soundPool.load(WordHunch.getContext(), R.raw.se_timer_end, 1);
    }

    /**
     * The method which tells SoundPool to play the sound associated with the SoundID
     * @throws IllegalStateException IllegalStateException is thrown when soundPool is null and
     *                               a request is made to play the sound. Without SoundPool, a sound cannot be played.
     *                               When this happens, an IllegalStateException is thrown to let the
     *                               user know SoundPool might not have been initialized properly.
     */
    public void playSound() throws IllegalStateException {
        if(soundPool != null)
            streamID = soundPool.play(soundID, 1f, 1f, 1, 0, 0.9f);
        else
            throw new IllegalStateException();
    }

    /**
     * A stop stream method which may be used to stop the sound being played at the given point
     */
    public void stopStream() {
        if(streamID != 0)
            soundPool.stop(streamID);
    }
}