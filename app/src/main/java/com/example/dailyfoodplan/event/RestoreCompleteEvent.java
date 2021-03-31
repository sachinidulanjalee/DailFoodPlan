package com.example.dailyfoodplan.event;

public class RestoreCompleteEvent extends BaseTaskEvent {
    public RestoreCompleteEvent(final boolean success) {
        super(success);
    }
}
