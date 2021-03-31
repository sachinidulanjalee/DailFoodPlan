package com.example.dailyfoodplan.event;

public class BackupCompleteEvent extends BaseTaskEvent {
    public BackupCompleteEvent(boolean success) {
        super(success);
    }
}
