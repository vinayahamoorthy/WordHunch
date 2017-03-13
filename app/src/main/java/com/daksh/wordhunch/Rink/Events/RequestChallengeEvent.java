package com.daksh.wordhunch.Rink.Events;

public class RequestChallengeEvent {

    /**
     * A request mode received from the caller when the class is instantiated.
     * It specifies to the observer the time of request being made.
     */
    private RequestMode requestMode;

    private RequestChallengeEvent() {
        //Empty constructor with private modifier to ensure instances may not be made
    }

    //A constructor to initialize and accept the requestMode for this particular event
    public RequestChallengeEvent(RequestMode requestMode) {
        this.requestMode = requestMode;
    }

    /**
     * A method to return the type of challenge requested from the event
     * @return One of the variables inside the RequestMode enum
     */
    public RequestMode getChallenge() {
        return requestMode;
    }

    /**
     * A request mode enum that is sent to the RequestChallengeEvent class whenever it is initialized.
     * It has three types of variables defined in it.
     */
    public enum RequestMode {

        /**
         * The challenge All variable defines that both, the network call and DB access may be used to
         * fetch a new challenge.
         */
        CHALLENGE_ALL,

        /**
         * The challenge network variable defines that only the network call may be used to fetch
         * a new challenge
         */
        CHALLENGE_NETWORK,

        /**
         * The challenge network variable defines that only the storage may be accessed to fetch
         * a new challenge now
         */
        CHALLENGE_STORAGE
    }
}