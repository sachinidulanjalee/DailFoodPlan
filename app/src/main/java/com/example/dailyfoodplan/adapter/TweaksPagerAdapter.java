package com.example.dailyfoodplan.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.dailyfoodplan.fragment.TweaksFragment;
import com.example.dailyfoodplan.model.Day;

public class TweaksPagerAdapter extends FragmentStatePagerAdapter {
    private int numDaysSinceEpoch;

    public TweaksPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);

        numDaysSinceEpoch = Day.getNumDaysSinceEpoch();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return TweaksFragment.newInstance(Day.getByOffsetFromEpoch(position));
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return Day.getTabTitleForDay(position);
    }

    @Override
    public int getCount() {
        return numDaysSinceEpoch;
    }
}
