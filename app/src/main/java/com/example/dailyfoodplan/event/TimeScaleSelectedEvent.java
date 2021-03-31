package com.example.dailyfoodplan.event;

import com.example.dailyfoodplan.model.enums.TimeScale;

public class TimeScaleSelectedEvent extends BaseEvent {
    private int selectedTimeScale;

    public TimeScaleSelectedEvent(@TimeScale.Interface int selectedTimeScale) {
        this.selectedTimeScale = selectedTimeScale;
    }

    @TimeScale.Interface
    public int getSelectedTimeScale() {
        return selectedTimeScale;
    }
}
