package p4.guide_animals.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String TYPE ="type";
    private static final int ACTION_START = 0;
    private static final int ACTION_STOP = 1;

    public NotificationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        int type =intent.getIntExtra(TYPE,ACTION_STOP);
        switch (type)
        {
            case ACTION_START:
                context.startService(new Intent(context,NotificationServices.class));
                    break;
            case ACTION_STOP:
                context.stopService(new Intent(context,NotificationServices.class));
                break;
        }
        // an Intent broadcast.
       // throw new UnsupportedOperationException("Not yet implemented");
    }
}
