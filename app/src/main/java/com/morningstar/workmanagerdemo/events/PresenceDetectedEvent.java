package com.morningstar.workmanagerdemo.events;

public class PresenceDetectedEvent {
    private boolean isPresent;

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }
}
