package com.example.dailyfoodplan.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;

import com.example.dailyfoodplan.Args;
import com.example.dailyfoodplan.BuildConfig;
import com.example.dailyfoodplan.Common;
import com.example.dailyfoodplan.R;
import com.example.dailyfoodplan.adapter.DailyDozenPagerAdapter;
import com.example.dailyfoodplan.adapter.TweaksPagerAdapter;
import com.example.dailyfoodplan.controller.Bus;
import com.example.dailyfoodplan.controller.Prefs;
import com.example.dailyfoodplan.event.CalculateStreaksTaskCompleteEvent;
import com.example.dailyfoodplan.event.DisplayDateEvent;
import com.example.dailyfoodplan.model.DDServings;
import com.example.dailyfoodplan.model.Day;
import com.example.dailyfoodplan.task.CalculateStreaksTask;
import com.example.dailyfoodplan.util.DateUtil;
import com.example.dailyfoodplan.util.NotificationUtil;

import org.greenrobot.eventbus.Subscribe;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import hirondelle.date4j.DateTime;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    private static final String ALREADY_HANDLED_RESTORE_INTENT = "already_handled_restore_intent";

    @BindView(R.id.date_pager)
    protected ViewPager datePager;
    @BindView(R.id.date_pager_indicator)
    protected PagerTabStrip datePagerIndicator;

    private MenuItem menuToggleModes;

    private Handler dayChangeHandler;
    private Runnable dayChangeRunnable;

    private int daysSinceEpoch;

    private boolean alreadyHandledRestoreIntent;

    private boolean inDailyDozenMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_main);
        ButterKnife.bind(this);

        initDatePager();
        initDatePagerIndicator();

        calculateStreaksAfterDatabaseUpgradeToV2();

        handleIntentIfNecessary();

//        if (!Prefs.getInstance(this).userHasSeenOnboardingScreen()) {
//            final AppModeBottomSheet appModeBottomSheet = AppModeBottomSheet.newInstance();
//            appModeBottomSheet.setCancelable(false);
//            appModeBottomSheet.show(getSupportFragmentManager(), AppModeBottomSheet.TAG);
//        }
    }

    private void handleIntentIfNecessary() {
        final Intent intent = getIntent();

        if (intent != null) {
            final Bundle extras = intent.getExtras();

            if (extras != null) {
                if (extras.getBoolean(Args.OPEN_NOTIFICATION_SETTINGS, false)) {
                    startActivity(new Intent(this, DailyReminderSettingsActivity.class));
                }
            }
        }
    }

    private void calculateStreaksAfterDatabaseUpgradeToV2() {
        if (!Prefs.getInstance(this).streaksHaveBeenCalculatedAfterDatabaseUpgradeToV2()) {
            if (DDServings.isEmpty()) {
                Prefs.getInstance(this).setStreaksHaveBeenCalculatedAfterDatabaseUpgradeToV2();
            } else {
                new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setTitle(R.string.dialog_streaks_title)
                        .setMessage(R.string.dialog_streaks_message)
                        .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new CalculateStreaksTask(MainActivity.this).execute();
                            }
                        })
                        .create().show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bus.register(this);

        NotificationUtil.dismissUpdateReminderNotification(this);

        if (daysSinceEpoch < Day.getNumDaysSinceEpoch()) {
            initDatePager();
        }

        startDayChangeHandler();


    }

    @Override
    protected void onPause() {
        super.onPause();
        Bus.unregister(this);

        stopDayChangeHandler();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ALREADY_HANDLED_RESTORE_INTENT, alreadyHandledRestoreIntent);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        alreadyHandledRestoreIntent = savedInstanceState.getBoolean(ALREADY_HANDLED_RESTORE_INTENT);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        alreadyHandledRestoreIntent = false;


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        // Only show the debug menu option if the apk is a debug build
        menu.findItem(R.id.menu_debug).setVisible(BuildConfig.DEBUG);

        menuToggleModes = menu.findItem(R.id.menu_toggle_modes);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        toggleTweaksMenuItemVisibility();
        return super.onPrepareOptionsMenu(menu);
    }

    private void toggleTweaksMenuItemVisibility() {
        if (menuToggleModes != null) {
            menuToggleModes.setShowAsAction(
                    Prefs.getInstance(this).isAppModeDailyDozenOnly() ?
                            MenuItem.SHOW_AS_ACTION_NEVER :
                            MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_toggle_modes:
                inDailyDozenMode = !inDailyDozenMode;
                if (inDailyDozenMode) {
                    setTitle(R.string.app_name);
                    item.setTitle(R.string.twenty_one_tweaks);
                } else {
                    setTitle(R.string.twenty_one_tweaks);
                    item.setTitle(R.string.app_name);
                }
                initDatePager();
                return true;
            case R.id.menu_latest_videos:
                Common.openUrlInExternalBrowser(this, R.string.url_latest_videos);
                return true;

            case R.id.menu_daily_reminder_settings:
                startActivity(new Intent(this, DailyReminderSettingsActivity.class));

            case R.id.menu_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            case R.id.menu_debug:
                startActivityForResult(new Intent(this, DebugActivity.class), Args.DEBUG_SETTINGS_REQUEST);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Args.DEBUG_SETTINGS_REQUEST:
                // Always refresh the data shown when returning from the Debug Activity
                initDatePager();
                break;
            case Args.SELECTABLE_DATE_REQUEST:
                if (data != null && data.hasExtra(Args.DATE)) {
                    setDatePagerDate(DateUtil.convertDateToDateTime((Date) data.getSerializableExtra(Args.DATE)));
                }
                break;
        }
    }

    private void initDatePager() {
        final FragmentStatePagerAdapter pagerAdapter;
        if (inDailyDozenMode) {
            pagerAdapter = new DailyDozenPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        } else {
            pagerAdapter = new TweaksPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }
        datePager.setAdapter(pagerAdapter);
        daysSinceEpoch = pagerAdapter.getCount();

        // Go to today's date by default
        datePager.setCurrentItem(pagerAdapter.getCount(), false);
    }

    private void initDatePagerIndicator() {
        datePagerIndicator.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        datePagerIndicator.setBackgroundResource(R.color.colorPrimary);
        datePagerIndicator.setTabIndicatorColorResource(R.color.colorAccent);
        datePagerIndicator.setDrawFullUnderline(false);
    }


    @Subscribe
    public void onEvent(CalculateStreaksTaskCompleteEvent event) {
        if (event.isSuccess()) {
            Prefs.getInstance(this).setStreaksHaveBeenCalculatedAfterDatabaseUpgradeToV2();
            initDatePager();
        }
    }

    private void startDayChangeHandler() {
        if (dayChangeRunnable == null) {
            dayChangeRunnable = new Runnable() {
                @Override
                public void run() {
                    initDatePager();
                    startDayChangeHandler();
                }
            };
        }

        stopDayChangeHandler();

        dayChangeHandler = new Handler();
        dayChangeHandler.postDelayed(dayChangeRunnable, Day.getMillisUntilMidnight());
    }

    private void stopDayChangeHandler() {
        if (dayChangeHandler != null && dayChangeRunnable != null) {
            dayChangeHandler.removeCallbacks(dayChangeRunnable);
            dayChangeHandler = null;
        }
    }

    @Subscribe
    public void onEvent(DisplayDateEvent event) {
        setDatePagerDate(event.getDate());
    }

    private void setDatePagerDate(final DateTime dateTime) {
        if (dateTime != null) {
            Timber.d("Changing displayed date to %s", dateTime.toString());
            datePager.setCurrentItem(Day.getNumDaysSinceEpoch(dateTime));
        }
    }
}