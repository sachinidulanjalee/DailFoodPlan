package com.example.dailyfoodplan.controller;

import com.example.dailyfoodplan.event.BackupCompleteEvent;
import com.example.dailyfoodplan.event.BaseEvent;
import com.example.dailyfoodplan.event.CalculateStreaksTaskCompleteEvent;
import com.example.dailyfoodplan.event.DisplayDateEvent;
import com.example.dailyfoodplan.event.FoodServingsChangedEvent;
import com.example.dailyfoodplan.event.LoadHistoryCompleteEvent;
import com.example.dailyfoodplan.event.RestoreCompleteEvent;
import com.example.dailyfoodplan.event.ShowExplodingStarAnimation;
import com.example.dailyfoodplan.event.TimeRangeSelectedEvent;
import com.example.dailyfoodplan.event.TimeScaleSelectedEvent;
import com.example.dailyfoodplan.event.TweakServingsChangedEvent;
import com.example.dailyfoodplan.event.WeightVisibilityChangedEvent;
import com.example.dailyfoodplan.model.Day;
import com.example.dailyfoodplan.model.Food;
import com.example.dailyfoodplan.model.Tweak;

import org.greenrobot.eventbus.EventBus;

public class Bus {
    public static void register(Object object) {
        final EventBus bus = EventBus.getDefault();
        if (!bus.isRegistered(object)) {
            bus.register(object);
        }
    }

    public static void unregister(Object object) {
        final EventBus bus = EventBus.getDefault();
        if (bus.isRegistered(object)) {
            bus.unregister(object);
        }
    }

    private static void post(BaseEvent event) {
        EventBus.getDefault().post(event);
    }

    public static void foodServingsChangedEvent(Day day, Food food) {
        post(new FoodServingsChangedEvent(day.getDateString(), food.getName(), com.example.dailyfoodplan.Common.isSupplement(food)));
    }

    public static void tweakServingsChangedEvent(Day day, Tweak tweak) {
        post(new TweakServingsChangedEvent(day.getDateString(), tweak.getName()));
    }

    public static void displayLatestDate() {
        post(new DisplayDateEvent(Day.getToday()));
    }

    public static void showExplodingStarAnimation() {
        post(new ShowExplodingStarAnimation());
    }

    public static void restoreCompleteEvent(final boolean success) {
        post(new RestoreCompleteEvent(success));
    }

    public static void backupCompleteEvent(final boolean success) {
        post(new BackupCompleteEvent(success));
    }

    public static void calculateStreaksComplete(final boolean success) {
        post(new CalculateStreaksTaskCompleteEvent(success));
    }

    public static void loadServingsHistoryCompleteEvent(final LoadHistoryCompleteEvent event) {
        post(event);
    }

    public static void timeScaleSelected(final int selectedTimeScale) {
        post(new TimeScaleSelectedEvent(selectedTimeScale));
    }

    public static void timeRangeSelectedEvent() {
        post(new TimeRangeSelectedEvent());
    }

    public static void weightVisibilityChanged() {
        post(new WeightVisibilityChangedEvent());
    }
}
