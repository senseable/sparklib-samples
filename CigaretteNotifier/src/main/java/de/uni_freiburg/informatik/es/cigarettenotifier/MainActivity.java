package de.uni_freiburg.informatik.es.cigarettenotifier;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import eu.senseable.sparklib.Spark;

/**
 *  The sole purpose of this Activity (currently) is to get around the security restriction
 *  of Android, which requires any application to have presented an Activity to the user before
 *  any Service can be started.
 *
 * Created by phil on 22.07.17.
 */

public class MainActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = new Intent();
        i.setClass(this, CigaretteNotification.class);
        startService(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        finish();
    }
}
