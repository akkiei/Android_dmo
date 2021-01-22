package com.rdss.dontmissout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = this.findViewById(R.id.postNotif);
        textView = this.findViewById(R.id.notifText);
        NotificationAccess();
        // Intent foregroundServiceIntent = new Intent(this, ForegroundService.class);
        // startForegroundService(foregroundServiceIntent);
        // I changed the seq of events here
        // earlier I was calling ForegroundService.class here and not calling NLService at all.
        // now Im calling NL-service first and that service calls the foreground service which shows the notif forever.
        // this is to test the restarting prob I have faced where NLService doesn't work as expected.
        Intent intentNotif = new Intent(this, NotifListServiceImplementation.class);
        startService(intentNotif);

    }

    public void AccessibilityAccess() {
//        String packageName = "com.example";
//        String className = "$packageName.service.MyService";
//        String string = "enabled_accessibility_services";
//        String cmd = "settings put secure $string $packageName/$className";
//        InstrumentationRegistry.getInstrumentation()
//                .getUiAutomation(UiAutomation.FLAG_DONT_SUPPRESS_ACCESSIBILITY_SERVICES)
//                .executeShellCommand(cmd)
//                .close();
//        TimeUnit.SECONDS.sleep(3);
//        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
//        startActivity(intent);
    }

    public void NotificationAccess() {

        if (!Settings.Secure.getString(this.getContentResolver(), "enabled_notification_listeners").contains(getApplicationContext().getPackageName())) {
            Toast.makeText(this, "Enable Notification Access.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(intent);
        }
    }

    public void postNotificationClicked(View v) {

        if (v.getId() == R.id.postNotif) {
            NotificationHelper notificationHelper = new NotificationHelper(this);
            notificationHelper.createNotification();
        }
    }

}





