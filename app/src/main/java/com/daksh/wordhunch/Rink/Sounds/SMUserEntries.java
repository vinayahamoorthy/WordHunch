package com.daksh.wordhunch.Rink.Sounds;

import android.media.SoundPool;

import com.daksh.wordhunch.R;
import com.daksh.wordhunch.WordHunch;

public class SMUserEntries {

    /**
     * the sound ID of the raw file sound_incorrect_entry which is returned by the soundPool.load() method.
     * The SoundID is issued at class initialization and may be used later to play or unload the
     * sound from memory
     */
    private int rejectSoundID;

    /**
     * The stream ID of the entry delined sound | This stream ID is used to control playback
     * when the file is being played
     */
    private int rejectStreamID;

    /**
     * The soundpool object is received from the SoundManager when SM classes are initialized one by one
     * depending on Scope passed on to SoundManager during it's initialization.
     */
    private SoundPool soundPool;

    /**
     * The default constructor used to load the sound file for an entry being declined.
     * it stores a the sound ID in the soundID object which is used to play the the sound using .play()
     * method.
     * @param soundPool A soundPool object used to load raw music files and extract a SoundID to be
     *                  used later to perform functions on the sound
     */
    public SMUserEntries(SoundPool soundPool) {
        this.soundPool = soundPool;
        rejectSoundID = soundPool.load(WordHunch.getContext(), R.raw.sound_incorrect_entry, 1);
    }

    /**
     * The method which tells SoundPool to play the sound associated with the SoundID
     * @throws IllegalStateException IllegalStateException is thrown when soundPool is null and
     *                               a request is made to play the sound. Without SoundPool, a sound cannot be played.
     *                               When this happens, an IllegalStateException is thrown to let the
     *                               user know SoundPool might not have been initialized properly.
     */
    public void playRejectSound() throws IllegalStateException {
        if(soundPool != null)
            rejectStreamID = soundPool.play(rejectSoundID, 1, 1, 1, 0, 1.0f);
        else
            throw new IllegalStateException();
    }

    /**
     * A stop stream method which may be used to stop the sound being played at the given point
     */
    public void stopRejectStream() {
        if(rejectStreamID != 0)
            soundPool.stop(rejectStreamID);
    }
}
