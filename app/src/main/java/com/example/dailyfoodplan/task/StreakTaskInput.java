package com.example.dailyfoodplan.task;

import com.example.dailyfoodplan.model.Day;
import com.example.dailyfoodplan.model.Food;
import com.example.dailyfoodplan.model.Tweak;

public class StreakTaskInput {
    private final Day startingDay;
    private final com.example.dailyfoodplan.RDA rda;

    public StreakTaskInput(final Day startingDay, final com.example.dailyfoodplan.RDA rda) {
        this.startingDay = startingDay;
        this.rda = rda;
    }

    Day getStartingDay() {
        return startingDay;
    }

    public Food getFood() {
        return (Food) rda;
    }

    public Tweak getTweak() {
        return (Tweak) rda;
    }
}
