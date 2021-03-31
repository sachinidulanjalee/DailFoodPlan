package com.example.dailyfoodplan.event;

public class CalculateStreaksTaskCompleteEvent extends BaseTaskEvent {
    public CalculateStreaksTaskCompleteEvent(boolean success) {
        super(success);
    }
}
