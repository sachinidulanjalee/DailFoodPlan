package com.example.dailyfoodplan.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.dailyfoodplan.util.NotificationUtil;

/**
 * The purpose of the AlarmReceiver is to wake up and show the Update Reminder notification to the user
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationUtil.showUpdateReminderNotification(context, intent);
    }
}
