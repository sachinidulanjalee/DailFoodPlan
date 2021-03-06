package com.example.dailyfoodplan.event;

class BaseTaskEvent extends BaseEvent {
    private final boolean success;

    BaseTaskEvent(final boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
