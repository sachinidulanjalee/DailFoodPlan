package com.example.dailyfoodplan.task.params;

import com.example.dailyfoodplan.model.enums.TimeScale;

public class LoadHistoryTaskParams {
    @TimeScale.Interface
    private int timeScale;

    private int selectedYear;
    private int selectedMonth;

    public LoadHistoryTaskParams(@TimeScale.Interface int timeScale, int selectedYear, int selectedMonth) {
        this.timeScale = timeScale;
        this.selectedYear = selectedYear;
        this.selectedMonth = selectedMonth;
    }

    @TimeScale.Interface
    public int getTimeScale() {
        return timeScale;
    }

    public int getSelectedYear() {
        return selectedYear;
    }

    public int getSelectedMonth() {
        return selectedMonth;
    }

}
