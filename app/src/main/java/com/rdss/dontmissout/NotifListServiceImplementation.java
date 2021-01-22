package com.rdss.dontmissout;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.widget.Toast;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NotifListServiceImplementation extends NotificationListenerService {
    private Map<String, Set> notifMap = new HashMap<>();
    private Set<String> notifSet = new HashSet<>();
    private boolean notifPostedFlag = false;

    public NotifListServiceImplementation() {
        super();

//        NotificationListenerService.requestRebind();

    }

    public void manageNotifications(String packageName, String title, String text) {
        // in the backend keep timestamp of update
        // so we can sort the notification in webapp in desc order
        // once we push the notification from here to api
        // reset the Map and set data structures
        // will add a timeInterval to synchronization notifications after every n minutes.

        notifSet.add(title + "::" + text);
        notifMap.put(packageName, notifSet);
        if (!notifPostedFlag) {
            System.out.println(notifMap);
            // will add a syn call here
        }
//        else
//            notificationPostedFlag = true;
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Bundle extra = sbn.getNotification().extras;
        String packageName = sbn.getPackageName();
        String title = extra.getString("android.title");
        String text = String.valueOf(extra.getCharSequence("android.text"));
        manageNotifications(packageName, title, text);
        notifPostedFlag = !notifPostedFlag;
        Toast.makeText(this, packageName + " : " + title + " : " + text, Toast.LENGTH_SHORT).show();
    }

    public void tryReconnectService() {
        toggleNotificationListenerService();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ComponentName componentName =
                    new ComponentName(getApplicationContext(), NotifListServiceImplementation.class);

            //It say to Notification Manager RE-BIND your service to listen notifications again inmediatelly!
            requestRebind(componentName);
        }
    }


    private void toggleNotificationListenerService() {
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(this, NotifListServiceImplementation.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(new ComponentName(this, NotifListServiceImplementation.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn, RankingMap rankingMap) {
        super.onNotificationPosted(sbn, rankingMap);
        Bundle extra = sbn.getNotification().extras;
        String packageName = sbn.getPackageName();
        String title = extra.getString("android.title");
        String text = String.valueOf(extra.getCharSequence("android.text"));
        manageNotifications(packageName, title, text);
        notifPostedFlag = !notifPostedFlag;
        // Toast.makeText(this, packageName + " - " + title + " - " + text, Toast.LENGTH_SHORT).show();
        // push to local Database
        // check for repeated notifications and skip repeated ones
        // only push latest updated
        Toast.makeText(this, "ranking changed ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        Toast.makeText(this, "listener connected", Toast.LENGTH_SHORT).show();
        Intent foregroundServiceIntent = new Intent(this, ForegroundService.class);
        startForegroundService(foregroundServiceIntent);
        tryReconnectService();
    }

    @Override
    public void onListenerDisconnected() {
        super.onListenerDisconnected();

//        ComponentName componentName =
//                new ComponentName(getApplicationContext(), NotifListServiceImplementation.class);
//
//        requestRebind(componentName);
        Toast.makeText(this, "listener DisConnected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNotificationRankingUpdate(RankingMap rankingMap) {
        super.onNotificationRankingUpdate(rankingMap);
    }

    @Override
    public StatusBarNotification[] getActiveNotifications() {
        Toast.makeText(this, "getActiveNotifications", Toast.LENGTH_SHORT).show();
        return super.getActiveNotifications();
    }
}
