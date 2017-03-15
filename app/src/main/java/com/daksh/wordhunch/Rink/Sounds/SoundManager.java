package com.daksh.wordhunch.Rink.Sounds;

import android.annotation.TargetApi;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.annotation.NonNull;

public class SoundManager {

    /**
     * A soundPool object that is used to play gamefied sounds in the background
     */
    private static SoundPool soundPool;

    /**
     * An object to the ScoreIncrease class | Used to play score increasing sounds
     */
    private SMScoreIncrease smScoreIncrease;

    /**
     * The user entries SoundManager auxiliary file that is used to play sounds related to user entries
     */
    private SMUserEntries smUserEntries;

    /**
     * The TimerEnd SoundManager auxiliary file that is used to play sounds related to timers
     */
    private SMTimerEnd smTimerEnd;

    /**
     * The soundManager Constructor that calls the appropriate initialization method based on
     * the version of SDK running on the user's device.
     */
    private SoundManager() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            initializeOnKitkat();
        else
            initializeOnLollipop();
    }

    /**
     * The method returns the smScoreIncrease object which is used to play whatever soundFiles are associated
     * with the object
     * @return Returns An SMScoreIncrease object that may be used to access soundFiles associated with
     *                 the object
     */
    @NonNull
    public SMScoreIncrease getScoreSounds() {
        return smScoreIncrease;
    }

    /**
     * The method returns the smUserEntries object which is used to play whatever soundFiles
     * are associated with the object
     * Ex : Incorrect user entry | Accept user entry
     * @return Returns An smUserEntries object that may be used to access soundFiles associated with
     *                 the object
     */
    @NonNull
    public SMUserEntries getUserInputs() {
        return smUserEntries;
    }

    /**
     * The method returns the smTimerEnd object which is used to play whatever soundFiles
     * are associated with the object
     * Ex : Music when the timer is about to end - 5 or less seconds remaining
     * @return Returns An smTimerEnd object that may be used to access soundFiles associated with
     *                 the object
     */
    @NonNull
    public SMTimerEnd getTimerSounds() {
        return smTimerEnd;
    }

    /**
     * A method that returns the sound pool | Probably won't be used, kept for god measure
     * @return Returns the sound pool being used to play the gamified sounds
     */
    public SoundPool getSoundPool() {
        return soundPool;
    }

    /**
     * The method initializes the SoundPool for active use by all classes
     * extending the SoundManager class. Initialization is done automatically and
     * need not be carried out by the developer
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initializeOnLollipop() {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .setMaxStreams(10)
                .build();
    }

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void initializeOnKitkat() {
        soundPool = new SoundPool(
                10,
                AudioManager.STREAM_MUSIC,
                0
        );
    }

    /**
     * A method to release all resources gathered up by soundpool; This is to ensure no memory or
     * resources are held up by dead modules of the app;
     * SoundPool cannot be used once called;
     */
    public void release() {
        soundPool.release();
    }


    public static class Builder {

        /**
         * An object to the SoundManager which is built when .build() method is called
         */
        private SoundManager soundManager;

        /**
         * A soundStores array that is used to keep track of which sound APIs are requested
         * by the user at any given time
         */
        private SoundStore[] soundStores = new SoundStore[3];

        public Builder() {
            //Empty Constructor
        }

        /**
         * A method to setup the sound resources to be used when the score increases. The sound
         * file for this particular function is a 5 second long climbing/rolling type sound that is
         * started when the score counter starts increasing and stopped when the counter stops.
         * @return Builder Returns the builder class for the builder implementation.
         */
        public Builder setupScoreSounds() {
            if(soundStores != null)
                for(int intCounter = 0 ; intCounter < soundStores.length ; intCounter++)
                    if(soundStores[intCounter] == SoundStore.ScoreIncrease)
                        return this;
                    else if(soundStores[intCounter] == null) {
                        //If the program execution reaches null, soundStores does not have SoundStore.ScoreIncrease
                        //Hence, we add it to the soundStores object
                        soundStores[intCounter] = SoundStore.ScoreIncrease;
                        break;
                    }

            return this;
        }

        /**
         * A method to setup the sound resources to be used when the timer is running out of time.
         * The sound file for this particular function is a 4 second long music file that is
         * started when the timer has only 5 seconds left on the board.
         * @return Builder Returns the builder class for the builder implementation.
         */
        public Builder setupTimerSounds() {
            if(soundStores != null)
                for(int intCounter = 0 ; intCounter < soundStores.length ; intCounter++)
                    if(soundStores[intCounter] == SoundStore.TimerSounds)
                        return this;
                    else if(soundStores[intCounter] == null) {
                        //If the program execution reaches null, soundStores does not have SoundStore.TimerSounds
                        //Hence, we add it to the soundStores object
                        soundStores[intCounter] = SoundStore.TimerSounds;
                        break;
                    }

            return this;
        }

        /**
         * A method to setup the sound resources to be used when a user entry is rejected. The sound
         * file for this particular function is a 1 second long obstruction type sound that is
         * played when the user taps on 'submit/enter/go' but the entry is not valid
         * @return Builder Returns the builder class for the builder implementation.
         */
        public Builder setupIncorrectEntry() {
            if(soundStores != null)
                for(int intCounter = 0 ; intCounter < soundStores.length ; intCounter++)
                    if(soundStores[intCounter] == SoundStore.RejectEntry)
                        return this;
                    else if(soundStores[intCounter] == null) {
                        //If the program execution reaches null, soundStores does not have SoundStore.ScoreIncrease
                        //Hence, we add it to the soundStores object
                        soundStores[intCounter] = SoundStore.RejectEntry;
                        break;
                    }

            return this;
        }

        /**
         * The build method is executed to build all types of sounds requested by the user and the
         * sound manager class. It is used to assign stream IDs / Sound IDs to the files
         * which are in turn used later to perform operations on the playback.
         * @return Returns the SoundManager to the user
         */
        public SoundManager build() {
            //Build the SoundManager
            soundManager = new SoundManager();

            //Iterate through the soundStores array to check which SM APIs need to be initialized
            for(SoundStore soundStore : soundStores) {

                //Test if SoundStore contains ScoreIncrease API
                if (soundStore != null && soundStore.equals(SoundStore.ScoreIncrease))
                    //initialize
                    soundManager.smScoreIncrease = new SMScoreIncrease(soundPool);

                //Test if SoundStore contains UserEntries API
                if (soundStore != null && soundStore.equals(SoundStore.ScoreIncrease))
                    //initialize
                    soundManager.smUserEntries = new SMUserEntries(soundPool);

                //Test if SoundStore contains TimerEnd API
                if (soundStore != null && soundStore.equals(SoundStore.TimerSounds))
                    //initialize
                    soundManager.smTimerEnd = new SMTimerEnd(soundPool);
            }
            //Return the soundManager to the user so the only point of contact to play sounds is the SoundManager
            return soundManager;
        }
    }
}
