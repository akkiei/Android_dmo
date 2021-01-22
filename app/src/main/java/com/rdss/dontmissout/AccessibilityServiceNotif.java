package com.rdss.dontmissout;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Notification;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

public class AccessibilityServiceNotif extends android.accessibilityservice.AccessibilityService {
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        try {
            Toast.makeText(this, "onAccessibilityEvent", Toast.LENGTH_SHORT).show();
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            Parcelable data = event.getParcelableData();

            if (data != null) {
                Notification notification = (Notification) data;

                RemoteViews remoteView =   notification.bigContentView;

                ViewGroup localView = (ViewGroup) inflater.inflate(remoteView.getLayoutId(), null);

//                remoteView.reapply(getApplicationContext(), localView);

                Resources resources = null;

                PackageManager pkm = getPackageManager();

                try {
                    resources = pkm.getResourcesForApplication("com.rdss.dontmissout");
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                if (resources == null)
                    return;

                int TITLE = resources.getIdentifier("android:id/title", null, null);

                int INBOX = resources.getIdentifier("android:id/big_text", null, null);

                int TEXT = resources.getIdentifier("android:id/text", null, null);

                String packagename = String.valueOf(event.getPackageName());

                TextView title = (TextView) localView.findViewById(TITLE);

                TextView inbox = (TextView) localView.findViewById(INBOX);

                TextView text = (TextView) localView.findViewById(TEXT);

                Log.d("NOTIFICATION Package : ", packagename);

                Log.d("NOTIFICATION Title : ", title.getText().toString());

                Log.d("NOTIFICATION You have got x messages : ", text.getText().toString());

                Log.d("NOTIFICATION inbox : ", inbox.getText().toString());
            }
        } catch (Exception e) {
            Log.e("onAccessibilityEvent ERROR", e.toString());
        }
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        try {
            Toast.makeText(this, "onServiceConnected", Toast.LENGTH_SHORT).show();

            AccessibilityServiceInfo info = new AccessibilityServiceInfo();

            info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;

            info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;

            info.notificationTimeout = 100;

            setServiceInfo(info);
        } catch (Exception e) {
            Log.d("ERROR onServiceConnected", e.toString());
        }
    }

    @Override
    public void onInterrupt() {

    }
}
