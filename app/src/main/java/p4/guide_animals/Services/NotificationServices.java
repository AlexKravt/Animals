package p4.guide_animals.Services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import p4.guide_animals.MainActivity;
import p4.guide_animals.R;


public class NotificationServices extends IntentService {
    NotificationManager nm;
    final int TIME_SECONDS = 5;
    int TIME_COUNT = 0;
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;

    public NotificationServices() {
       super("NotificationServices");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            sendNotif();
        }
    }


    void sendNotif()
    {
        TIME_COUNT++;

        if(TIME_COUNT==TIME_SECONDS)
        {
            TIME_COUNT = 0;

        }
        //Перезапускаем по таймеру
        try {
            TimeUnit.SECONDS.sleep(1);
            sendNotif();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i("Time", "Count - " + TIME_COUNT);
      //  stopService(intentservices);
    }

    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        NotificationCompat.Builder mBuilder =  new NotificationCompat.Builder(this)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("Есть новое уведомление!")
                        // .setWhen(System.currentTimeMillis())
                        // .setAutoCancel(true)
                .setContentTitle("Количество заявок")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg)
                .setDefaults(Notification.DEFAULT_SOUND);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
