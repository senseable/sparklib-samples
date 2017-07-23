package de.uni_freiburg.informatik.es.cigarettenotifier;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import eu.senseable.sparklib.Spark;

/**
 * This in a service, so it will be running as long as the notification is visible.
 *
 * Created by phil on 23.07.17.
 */

public class CigaretteNotification extends Service {

    private static final int NOTIFICATION_ID = 123;
    private Spark mSpark;

    /**
     *  called when the list of events has changed, in which case we update the
     *  currently displayed Notification.
     */
    private Spark.Callbacks mSparkCallbacks = new Spark.Callbacks.Stub() {
        public int mLastNumEvents = Integer.MAX_VALUE;

        @Override
        public void onEventsChanged(List<Spark.Event> events) {
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            /**
             * return if there are no events in the list
             */
            if (events.size() == 0) {
                mNotificationManager.cancel(NOTIFICATION_ID);
                return;
            }

            /**
             *  get the latest event
             */
            Resources res = getResources();
            long latest = events.get(events.size() - 1).beg.getTime();

            /**
             * select all events that have happened today
             */
            DateFormat ymd = new SimpleDateFormat("yyyyMMDD");
            String today = ymd.format(new Date());
            int num = 0;

            for (Spark.Event event : events)
                num += today.equals(ymd.format(event.beg)) ? 1 : 0;

            /**
             * was an event just added or removed
             */
            boolean newEvent = events.size() - mLastNumEvents > 0;
            mLastNumEvents = events.size();

            /**
             * return if there are no events today
             */
            if (num == 0) {
                mNotificationManager.cancel(NOTIFICATION_ID);
                return;
            }

            Notification notification = new NotificationCompat
                    .Builder(CigaretteNotification.this)
                    .setSmallIcon(R.drawable.ic_cig)
                    .setContentTitle(res.getString(R.string.title))
                    .setContentText(res.getQuantityString(R.plurals.cigToday, num, num))
                    .setWhen(latest)
                    .setShowWhen(true)
                    .setAutoCancel(true)
                    .setDefaults(newEvent ? Notification.DEFAULT_ALL : 0)
                    .build();

            mNotificationManager.notify(NOTIFICATION_ID, notification);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mSpark != null)  // make sure there is only a single connection!
            mSpark.close(this);

        mSpark = new Spark(this, mSparkCallbacks);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSpark.close(this);
    }
}
