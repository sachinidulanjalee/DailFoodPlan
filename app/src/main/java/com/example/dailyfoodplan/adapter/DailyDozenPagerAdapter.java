package com.example.dailyfoodplan.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.dailyfoodplan.fragment.DailyMealPlansFragment;
import com.example.dailyfoodplan.model.Day;

public class DailyDozenPagerAdapter extends FragmentStatePagerAdapter {
    private int numDaysSinceEpoch;

    public DailyDozenPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);

        this.numDaysSinceEpoch = Day.getNumDaysSinceEpoch();
    }

    @Override
    public Fragment getItem(int position) {
        return DailyMealPlansFragment.newInstance(Day.getByOffsetFromEpoch(position));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Day.getTabTitleForDay(position);
    }

    @Override
    public int getCount() {
        return numDaysSinceEpoch;
    }
}
