package com.example.dailyfoodplan;

import com.activeandroid.app.Application;
import com.example.dailyfoodplan.model.Food;
import com.example.dailyfoodplan.model.FoodInfo;
import com.example.dailyfoodplan.model.Tweak;
import com.example.dailyfoodplan.util.NotificationUtil;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import timber.log.Timber;

public class DailyPlanApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Iconify.with(new FontAwesomeModule());

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        ensureAllFoodsExistInDatabase();
        ensureAllTweaksExistInDatabase();

        FoodInfo.init(this);

        NotificationUtil.init(this);
    }

    private void ensureAllFoodsExistInDatabase() {
        Food.ensureAllFoodsExistInDatabase(
                getResources().getStringArray(R.array.food_names),
                getResources().getStringArray(R.array.food_id_names),
                getResources().getIntArray(R.array.food_quantities));
    }

    private void ensureAllTweaksExistInDatabase() {
        Tweak.ensureAllTweaksExistInDatabase(
                getResources().getStringArray(R.array.tweak_names),
                getResources().getStringArray(R.array.tweak_id_names),
                getResources().getIntArray(R.array.tweak_amounts));
    }
}
