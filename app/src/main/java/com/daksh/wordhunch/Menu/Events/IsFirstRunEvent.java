package com.daksh.wordhunch.Menu.Events;

public class IsFirstRunEvent {

    private boolean isFirstRun;

    public IsFirstRunEvent(boolean isFirstRun) {
        this.isFirstRun = isFirstRun;
    }

    public boolean isFirstRun() {
        return isFirstRun;
    }
}